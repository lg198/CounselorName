//Author: Layne Gustafson
//Date created: Jan 24, 2015
package com.github.lg198.cnotes.encryption;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Encryption {

	public static byte[] saltedHash(String pass, byte[] salt) throws EncryptionException {
		char[] pchars = pass.toCharArray();
		int iterations = 1000;
		int bytes = 8;

		PBEKeySpec spec = new PBEKeySpec(pchars, salt, iterations, bytes * 8);
		SecretKeyFactory skf;
		try {
			skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException(e);
		} catch (InvalidKeySpecException e) {
			throw new EncryptionException(e);
		}
		
	}
	
	public static class EncryptionException extends Exception {
		public EncryptionException(Exception e) {
			super(e);
		}
	}

}
