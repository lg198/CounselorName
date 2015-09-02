package com.github.lg198.cnotes.core;

import com.github.lg198.cnotes.backup.BackupFrequency;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Settings {

    private static Properties props = new Properties();

    public static void load() {
        if (!CounselorNotesMain.getSettingsFile().exists()) {
            loadDefaults();
            return;
        }
        try {
            props.load(new FileReader(CounselorNotesMain.getSettingsFile()));
        } catch (IOException e) {
            System.err.println("Could not load settings. Falling back on defaults!");
            loadDefaults();
        }
    }

    private static void save(String k, String v) {
        props.setProperty(k, v);
        try {
            props.store(new FileWriter(CounselorNotesMain.getSettingsFile()), "CounselorNotes Settings file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadDefaults() {
        props.setProperty("backup-frequency", BackupFrequency.EVERY_DAY.name());
    }

    public static BackupFrequency getBackupFrequency() {
        return BackupFrequency.valueOf(props.getProperty("backup-frequency"));
    }

    public static void setBackupFrequency(BackupFrequency bf) {
        save("backup-frequency", bf.name());
    }
}
