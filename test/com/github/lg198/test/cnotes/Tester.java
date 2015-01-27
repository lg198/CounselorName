//Author: Layne Gustafson
//Date created: Jan 24, 2015
package com.github.lg198.test.cnotes;

import javax.xml.bind.DatatypeConverter;

import com.alee.laf.WebLookAndFeel;
import com.github.lg198.cnotes.encryption.Encryption;
import com.github.lg198.cnotes.gui.GuiMain;

public class Tester {

	public static void main(String[] args) throws Exception {
		WebLookAndFeel.install();
		WebLookAndFeel.setDecorateFrames(false);
		new GuiMain();
	}
	
	public static void checkPass() throws Exception {
		String pass = "12345";
		String salts = "09BFFA45";
		byte[] salt = DatatypeConverter.parseHexBinary(salts);
		
		byte[] hashed = Encryption.saltedHash(pass, salt);
		System.out.println(DatatypeConverter.printHexBinary(hashed));
		
		String toEncrypt = "Happy Birthday, Santa!";
		String correct = "54FFDED0B5A39284";
		
		String res = DatatypeConverter.printHexBinary(Encryption.saltedHash(toEncrypt, hashed));
		System.out.println(res.equals(correct));
	}

}
