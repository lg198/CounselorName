//Author: Layne Gustafson
//Date created: Jan 23, 2015
package com.github.lg198.cnotes.database;

import com.github.lg198.cnotes.bean.Note;
import com.github.lg198.cnotes.bean.Student;
import com.github.lg198.cnotes.bean.field.CustomField;
import com.github.lg198.cnotes.bean.field.CustomFieldType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseManager {

    private static Connection connection = null;

    public static void init(String driver, String uri)
            throws DatabaseNotFoundException, DatabaseConnectionException,
            DatabaseInitException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new DatabaseNotFoundException("The driver '" + driver
                    + "' was not found on the classpath!", e);
        }

        try {
            connection = DriverManager.getConnection(uri);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(
                    "Failed to connect to database!", e);
        }

        try {
            for (String stat : parseScript("create_tables")) {
                connection.createStatement().execute(stat);
            }
        } catch (SQLException e) {
            throw new DatabaseInitException(e);
        }
    }

    public static void close() throws SQLException {
        connection.close();
    }

    public static String[] parseScript(String s) {
        try {
            String script = "/com/github/lg198/cnotes/res/script/" + s + ".sql";
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    DatabaseManager.class.getResourceAsStream(script)));
            List<String> lines = new ArrayList<String>();
            String currentQuery = "";
            for (String line; (line = br.readLine()) != null; ) {
                if (!line.startsWith("#SEP")) {
                    currentQuery += line;
                } else {
                    lines.add(currentQuery);
                    currentQuery = "";
                }
            }
            return lines.toArray(new String[0]);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load script!");
        }
    }


    public static List<Student> searchStudents(String query)
            throws SQLException {
        if (query.isEmpty()) {
            return new ArrayList<Student>();
        }
        ResultSet rs = connection
                .createStatement()
                .executeQuery(
                        "SELECT * FROM student WHERE UPPER(firstname || \" \" || lastname) LIKE UPPER('%"
                                + query + "%')");
        List<Student> students = new ArrayList<Student>();
        while (rs.next()) {
            Student s = new Student(rs.getString("id"),
                    rs.getString("firstname"), rs.getString("lastname"));
            students.add(s);
        }
        return students;
    }

    public static ResultSet getAllStudents() throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM student");
        return rs;
    }

    public static Student addStudent(String fn, String ln) throws SQLException {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        PreparedStatement statement = prep("INSERT INTO student (id, firstname, lastname) values (?, ?, ?)");
        statement.setString(1, id);
        statement.setString(2, fn);
        statement.setString(3, ln);
        statement.execute();
        return new Student(id, fn, ln);
    }

    public static void updateStudent(Student s) throws SQLException {
        connection.createStatement().execute(
                "UPDATE student SET firstname=\"" + s.getFirstName()
                        + "\", lastname=\"" + s.getLastName() + "\" WHERE id LIKE \"" + s.getId() + "\"");
    }

    public static void deleteStudent(Student s) throws SQLException {
        connection.createStatement().execute(
                "DELETE FROM student WHERE id=\"" + s.getId() + "\"");
    }

    public static boolean loadNames(Reader r) {
        try {
            BufferedReader br = new BufferedReader(r);
            Pattern p = Pattern.compile("\\s*([^\\s]+)\\s+([^\\s]+)\\s*");
            for (String s; (s = br.readLine()) != null; ) {
                Matcher m = p.matcher(s.trim());
                if (m.matches()) {
                    String ln = m.group(1);
                    String fn = m.group(2);
                    addStudent(fn, ln);
                }
            }
            br.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<CustomField> getCustomFields(Student s) throws SQLException {
        List<CustomField> fields = new ArrayList<CustomField>();
        ResultSet rs = execq("SELECT * FROM fieldDef");
        while (rs.next()) {
            ResultSet rs1 = execq("SELECT * FROM customField WHERE fieldId = \"" + rs.getString("id")
                    + "\" AND student = \"" + s.getId() + "\"");
            if (rs1.next()) {
                fields.add(new CustomField(rs.getString("id"), rs.getString("name"),
                        rs1.getString("fieldValue"), CustomFieldType.valueOf(rs.getString("fieldType"))));
            } else {
                CustomFieldType cft = CustomFieldType.valueOf(rs.getString("fieldType"));
                String dv = cft.getPresentableDefault(rs.getString("defaultValue"));
                fields.add(new CustomField(rs.getString("id"), rs.getString("name"),
                        dv, cft));
                execf("INSERT INTO customField VALUES (\"%s\", \"%s\", \"%s\")", rs.getString("id"), s.getId(), dv);
            }
            rs1.close();
        }
        rs.close();
        return fields;
    }

    public static CustomField getDefaultCustomField(String id) throws SQLException {
        PreparedStatement ps = prep("SELECT * FROM fieldDef WHERE id = ?");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            CustomField f = new CustomField(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("defaultValue"),
                    CustomFieldType.valueOf(rs.getString("fieldType"))
            );
            return f;
        } else {
            throw new SQLException("Nonexistent id " + id);
        }
    }

    public static String getDefaultCustomFieldValue(String id) throws SQLException {
        ResultSet rs = execq("SELECT * FROM fieldDef WHERE id = \"" + id + "\"");
        rs.next();
        return rs.getString("defaultValue");
    }

    public static void setDefaultCustfomFieldValue(String id, String value) throws SQLException {
        PreparedStatement statement = prep("INSERT or REPLACE INTO fieldDef (id, defaultValue, fieldType, name) VALUES (?, ?, (SELECT fieldType FROM fieldDef WHERE id = ?), (SELECT name FROM fieldDef WHERE id = ?))");
        statement.setString(1, id);
        statement.setString(2, value);
        statement.setString(3, id);
        statement.setString(4, id);
        statement.execute();
    }

    public static List<CustomField> getDefaultCustomFields() throws SQLException {
        List<CustomField> fields = new ArrayList<>();
        ResultSet rs = execq("SELECT * FROM fieldDef");
        while (rs.next()) {
            CustomFieldType cft = CustomFieldType.valueOf(rs.getString("fieldType"));
            String dv = cft.getPresentableDefault(rs.getString("defaultValue"));
            fields.add(new CustomField(rs.getString("id"), rs.getString("name"),
                    dv, cft));
        }
        return fields;
    }

    public static int countCustomFields() throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery("SELECT COUNT(*) AS rcount FROM fieldDef");
        return rs.getInt("rcount");
    }

    public static void defineCustomField(String name, String defaultValue, CustomFieldType type) throws SQLException {
        String fid = name.replaceAll("\\s", "_").toLowerCase();
        PreparedStatement statement = prep("INSERT INTO fieldDef VALUES (?, ?, ?, ?)");
        statement.setString(1, fid);
        statement.setString(2, name);
        statement.setString(3, type.name());
        statement.setString(4, defaultValue);
        statement.execute();
    }

    public static void updateCustomField(Student s, CustomField cf) throws SQLException {
        PreparedStatement statement = prep("INSERT or REPLACE INTO customField VALUES (?, ?, ?)");
        statement.setString(1, cf.getId());
        statement.setString(2, s.getId());
        statement.setString(3, cf.getValue());
        statement.execute();

        /*ResultSet rs = execq("SELECT COUNT(*) AS rcount FROM customField WHERE student = \"" + s.getId() + "\" AND fieldId = \"" + cf.getId() + "\"");
        if (rs.getInt("rcount") == 0) {
            execf("INSERT INTO customField VALUES (\"%s\", \"%s\", \"%s\")", cf.getId(), s.getId(), cf.getValue());
        } else {
            execf("UPDATE customField SET fieldValue = \"%s\" WHERE student = \"%s\" AND fieldId = \"%s\"", cf.getValue(), s.getId(), cf.getId());
        }*/
    }

    public static void updateNote(Note n) throws SQLException {
        PreparedStatement statement = prep("INSERT or REPLACE INTO note VALUES (?, ?, ?, ?, ?)");
        statement.setString(1, n.getId());
        statement.setString(2, n.getStudentId());
        statement.setString(3, n.getDateString());
        statement.setString(4, n.getContents());
        statement.setString(5, n.getTitle());
        statement.execute();
    }

    public static List<Note> getNotes(Student s) throws SQLException {
        PreparedStatement statement = prep("SELECT * FROM note WHERE student = ?");
        statement.setString(1, s.getId());
        ResultSet rs = statement.executeQuery();

        List<Note> notes = new ArrayList<>();
        while (rs.next()) {
            notes.add(new Note(
                    rs.getString("id"),
                    rs.getString("student"),
                    rs.getString("createTime"),
                    rs.getString("note")
            ));
        }
        return notes;
    }

    public static Properties getProfileFields() throws SQLException {
        PreparedStatement statement = prep("SELECT * FROM profile");
        ResultSet rs = statement.executeQuery();

        Properties props = new Properties();
        while (rs.next()) {
            props.setProperty(rs.getString("id"), rs.getString("val"));
        }
        return props;
    }

    public static boolean hasProfileField(String fid) throws SQLException {
        PreparedStatement statement = prep("SELECT * FROM profile WHERE id=?");
        statement.setString(1, fid);
        ResultSet rs = statement.executeQuery();
        return rs.next();
    }

    public static void setProfileField(String id, String val) throws SQLException {
        PreparedStatement statement = prep("INSERT INTO profile VALUES (?, ?)");
        statement.setString(1, id);
        statement.setString(2, val);
        statement.execute();
    }

    public static PreparedStatement prep(String s) throws SQLException {
        return connection.prepareStatement(s);
    }

    public static boolean execf(String s, Object... o) throws SQLException {
        return connection.createStatement().execute(String.format(s, o));
    }

    public static ResultSet execq(String s) throws SQLException {
        return connection.createStatement().executeQuery(s);
    }

    public static class DatabaseNotFoundException extends Exception {

        public DatabaseNotFoundException(String s, Exception e) {
            super(s, e);
        }
    }

    public static class DatabaseConnectionException extends Exception {

        public DatabaseConnectionException(String s, Exception e) {
            super(s, e);
        }
    }

    public static class DatabaseInitException extends Exception {

        public DatabaseInitException(Exception e) {
            super(e);
        }
    }

}
