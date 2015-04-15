//Author: Layne Gustafson
//Date created: Jan 23, 2015
package com.github.lg198.cnotes.core;

import java.io.File;
import java.sql.SQLException;

import com.alee.utils.SystemUtils;
import com.github.lg198.cnotes.database.DatabaseManager;

public class CounselorNotesMain {

    public static File folder;

    public static void main(String[] args) throws Exception {
        if (SystemUtils.isWindows()) {
            String appdata = System.getenv("APPDATA");
            if (appdata == null) {
                appdata = System.getProperty("user.home");
            }
            folder = new File(appdata, "CounselorNotes");
        } else if (SystemUtils.isMac()) {
            folder = new File(System.getProperty("user.home") + "/Library/Application Support/CounselorNotes");
        } else {
            folder = new File(System.getProperty("user.home") + ".CounselorNotes");
        }
        if (!folder.exists()) {
            folder.mkdirs();
        }
        DatabaseManager.init("org.sqlite.JDBC", "jdbc:sqlite:" + folder.getPath() + File.separator + "cndb.db");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    DatabaseManager.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static File getPasswordFile() {
        return new File(folder, "profile.glub");
    }

}
