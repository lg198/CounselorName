package com.github.lg198.cnotes.util;


public class SystemUtil {

    public enum OS {
        WINDOWS,
        MAC,
        LINUX
    }

    private static final OS os;

    static {
        String name = System.getProperty("os.name").toUpperCase();
        if (name.contains("WIN")) {
            os = OS.WINDOWS;
        } else if (name.contains("MAC")) {
            os = OS.MAC;
        } else {
            os = OS.LINUX;
        }
    }

    public static boolean isWindows() {
        return os == OS.WINDOWS;
    }

    public static boolean isMac() {
        return os == OS.MAC;
    }
}
