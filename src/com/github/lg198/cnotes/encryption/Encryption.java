//Author: Layne Gustafson
//Date created: Jan 24, 2015
package com.github.lg198.cnotes.encryption;

import com.github.lg198.cnotes.database.DatabaseManager;
import org.jasypt.util.password.BasicPasswordEncryptor;

import java.sql.SQLException;
import java.util.Properties;

public class Encryption {

    public static final String PASS_KEY = "passhash";

    private static Encryption INSTANCE;

    public static void init() throws SQLException {
        if (INSTANCE != null) {
            throw new IllegalStateException("Encryption is already initialized!");
        }

        INSTANCE = new Encryption(DatabaseManager.getProfileFields());
    }

    public static void init(String pass) throws SQLException {
        if (INSTANCE != null) {
            throw new IllegalStateException("Encryption is already initialized!");
        }

        BasicPasswordEncryptor enc = new BasicPasswordEncryptor();
        DatabaseManager.setProfileField(PASS_KEY, enc.encryptPassword(pass));

        init();
    }


    public static Encryption get() {
        return INSTANCE;
    }

    private Properties props;

    private Encryption(Properties p) {
        props = p;
    }

    public boolean checkPassword(String pass) {
        BasicPasswordEncryptor enc = new BasicPasswordEncryptor();
        return enc.checkPassword(pass, props.getProperty(PASS_KEY));
    }


}
