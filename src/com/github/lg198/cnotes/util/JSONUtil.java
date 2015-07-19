package com.github.lg198.cnotes.util;

import com.github.lg198.cnotes.database.DatabaseManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JSONUtil {

    public static void exportStudentsToFile(File f) throws IOException, SQLException {
        ResultSet rs = DatabaseManager.getAllStudents();
        FileWriter fw = new FileWriter(f);
        PrintWriter pw = new PrintWriter(fw);
        pw.println("[");
        if (rs.next()) {
            pw.println("\t{");
            pw.print("\t\t\"id\": ");
            pw.println('"' + rs.getString("id") + "\",");
            pw.print("\t\t\"firstname\": ");
            pw.println('"' + rs.getString("firstname") + "\",");
            pw.print("\t\t\"lastname\": ");
            pw.println('"' + rs.getString("lastname") + "\"");
            pw.print("\t}");
        }
        while (rs.next()) {
            pw.println(",");
            pw.println("\t{");
            pw.print("\t\t\"id\": ");
            pw.println('"' + rs.getString("id") + "\",");
            pw.print("\t\t\"firstname\": ");
            pw.println('"' + rs.getString("firstname") + "\",");
            pw.print("\t\t\"lastname\": ");
            pw.println('"' + rs.getString("lastname") + "\"");
            pw.print("\t}");
        }
        pw.println();
        pw.println("]");
        pw.close();
    }

}
