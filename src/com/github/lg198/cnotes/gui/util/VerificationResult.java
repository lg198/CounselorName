//Author: Layne Gustafson
//Date created: Jan 27, 2015
package com.github.lg198.cnotes.gui.util;

public class VerificationResult {
	
	private boolean verify;
	private String message;
	
	public VerificationResult(boolean v, String m) {
		verify = v;
		message = m;
	}
	
	public VerificationResult(boolean v) {
		this(v, "");
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean verify() {
		return verify;
	}

}
