//Author: Layne Gustafson
//Date created: Jan 24, 2015
package com.github.lg198.cnotes.encryption;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

public class Encryption {

	private static Encryption INSTANCE;
        
        public static void init(File f) throws IOException {
            if (INSTANCE != null) {
                throw new IllegalStateException("Encryption is already initialized!");
            }
            
            Properties p = new Properties();
            p.load(new FileReader(f));
            
            INSTANCE = new Encryption(p);
        }
        
        public static void init(String password, File f) throws IOException {
            if (INSTANCE != null) {
                throw new IllegalStateException("Encryption is already initialized!");
            }
            
            Properties p = new Properties();
            
            BasicPasswordEncryptor enc = new BasicPasswordEncryptor();
            String es = enc.encryptPassword(password);
            p.setProperty("pass", es);
            p.store(new FileWriter(f), "");
            
            INSTANCE = new Encryption(p);
        }
        
        public static Encryption get() {
            return INSTANCE;
        }
        
        private Properties props;
        private BasicTextEncryptor tenc = new BasicTextEncryptor();
        private String dbpass;
        
        private Encryption(Properties p) {
            props = p;
        }
        
        public boolean checkPassword(String pass) {
            BasicPasswordEncryptor enc = new BasicPasswordEncryptor();
            return enc.checkPassword(pass, props.getProperty("pass"));
        }
        
        public void initDatabaseEncryption(String pass) {
            tenc.setPassword(pass);
        }
        
        public String enc(String s) {
            if (dbpass == null) {
                throw new IllegalStateException("Cannot encrypt- not initialized!");
            }
            
            return tenc.encrypt(s);
        }
        
        public String dec(String s) {
            if (dbpass == null) {
                throw new IllegalStateException("Cannot decrypt- not initialized!");
            }
            
            return tenc.decrypt(s);
        }
        
        

}
