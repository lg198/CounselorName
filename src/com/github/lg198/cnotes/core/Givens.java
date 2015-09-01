//Author: Layne Gustafson
//Date created: Jan 25, 2015
package com.github.lg198.cnotes.core;

import java.util.Properties;

public class Givens {

    private static Properties props = null;

    private static void load() {
        props = new Properties();
        try {
            props.load(Givens.class.getResourceAsStream("/com/github/lg198/cnotes/res/givens.properties"));
        } catch (Exception e) {
            System.exit(1);
        }
    }

    public static void check() {
        if (props == null) {
            load();
        }
    }

    public static String name() {
        check();
        return props.getProperty("name");
    }

    public static String version() {
        check();
        return props.getProperty("version");
    }

    public static String fullName() {
        //check(); ALREADY CHECKS
        return name() + " v" + version();
    }

}
