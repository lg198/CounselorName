//Author: Layne Gustafson
//Date created: Jan 23, 2015
package com.github.lg198.cnotes.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.lg198.cnotes.bean.Student;
import com.github.lg198.cnotes.bean.field.CustomField;
import com.github.lg198.cnotes.bean.field.CustomFieldType;

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
            for (String line = ""; (line = br.readLine()) != null;) {
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

    public static Student getStudentFromName(String name) throws SQLException {
        name = name.replaceAll("\\s+", "").toUpperCase();
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM student WHERE UPPER(firstname || lastname) LIKE '" + name + "'");
        if (rs.next()) {
            return new Student(rs.getString("id"), rs.getString("firstname"), rs.getString("lastname"));
        }
        
        throw new SQLException("No student exists for name '" + name + "'!");
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
        String id = UUID.randomUUID().toString();
        connection
                .createStatement()
                .execute(
                        String.format(
                                "INSERT INTO student (id, firstname, lastname) values (\"%s\", \"%s\", \"%s\")",
                                id, fn, ln));
        return new Student(id, fn, ln);
    }

    public static void updateStudent(Student s) throws SQLException {
        connection.createStatement().execute(
                "UPDATE student SET firstname=\"" + s.getFirstName()
                + "\", lastname=\"" + s.getLastName() + "\"");
    }

    public static void deleteStudent(Student s) throws SQLException {
        connection.createStatement().execute(
                "DELETE FROM student WHERE id=\"" + s.getId() + "\"");
    }

    public static boolean loadNames(Reader r) {
        try {
            BufferedReader br = new BufferedReader(r);
            Pattern p = Pattern.compile("([^\\s]+)\\s+([^\\s]+)");
            for (String s = ""; (s = br.readLine()) != null;) {
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
                fields.add(new CustomField(rs.getString("id"), rs.getString("name"),
                        "", CustomFieldType.valueOf(rs.getString("fieldType"))));
                execf("INSERT INTO customField VALUES (\"%s\", \"%s\", \"%s\")", rs.getString("id"), s.getId(), " ");
            }
            rs1.close();
        }
        rs.close();
        return fields;
    }

    public static int countCustomFields() throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery("SELECT COUNT(*) AS rcount FROM fieldDef");
        return rs.getInt("rcount");
    }

    public static boolean customFieldExists(String name) throws SQLException {
        return countCustomFields() != 0;
    }

    public static void createCustomField(String name, CustomFieldType type) throws SQLException {
        String fid = name.replaceAll("\\s", "_").toLowerCase();
        execf("INSERT INTO fieldDef VALUES (\"%s\", \"%s\", \"%s\")", fid, name, type.name());
    }

    public static void updateCustomField(Student s, CustomField cf) throws SQLException {
        ResultSet rs = execq("SELECT COUNT(*) AS rcount FROM customField WHERE student = \"" + s.getId() + "\"");
        if (rs.getInt("rcount") == 0) {
            execf("INSERT INTO customField VALUES (\"%s\", \"%s\", \"%s\")", cf.getId(), s.getId(), cf.getValue());
        } else {
            execf("UPDATE customField SET fieldValue = \"%s\" WHERE student = \"%s\" AND fieldId = \"%s\"", cf.getValue(), s.getId(), cf.getId());
        }
    }

    public static boolean exec(String s) throws SQLException {
        return connection.createStatement().execute(s);
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
