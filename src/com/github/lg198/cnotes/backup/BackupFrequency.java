package com.github.lg198.cnotes.backup;

public enum BackupFrequency {

    EVERY_HOUR,
    EVERY_12_HOURS,
    EVERY_DAY,
    EVERY_OTHER_DAY,
    EVERY_5_DAYS,
    EVERY_WEEK,
    EVERY_MONTH;

    public static int defaultFrequency() {
        return EVERY_DAY.ordinal();
    }

}
