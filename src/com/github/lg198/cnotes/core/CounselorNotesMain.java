//Author: Layne Gustafson
//Date created: Jan 23, 2015
package com.github.lg198.cnotes.core;

import com.github.lg198.cnotes.database.DatabaseManager;
import com.github.lg198.cnotes.util.SystemUtil;

import java.io.File;
import java.sql.SQLException;

public class CounselorNotesMain {

    public static File folder;

    public static void main(String[] args) throws Exception {
        if (SystemUtil.isWindows()) {
            String appdata = System.getenv("APPDATA");
            if (appdata == null) {
                appdata = System.getProperty("user.home");
            }
            folder = new File(appdata, "CounselorNotes");
        } else if (SystemUtil.isMac()) {
            folder = new File(System.getProperty("user.home") + "/Library/Application Support/CounselorNotes");
        } else {
            folder = new File(System.getProperty("user.home") + "/.config/CounselorNotes");
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

        Settings.load();

    }

    /*public static File getPasswordFile() {
        return new File(folder, "profile.glub");
    }*/
    public static File getSettingsFile() { return new File(folder, "settings.properties"); }

}
