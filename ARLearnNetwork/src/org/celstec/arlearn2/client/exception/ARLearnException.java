package org.celstec.arlearn2.client.exception;

import org.celstec.arlearn2.beans.Bean;

public class ARLearnException extends RuntimeException {

	public static final int RUNNOTFOUND = 1;
	public static final int NOT_AUTHENTICATED = 2;

	
	private int code;
	
	public ARLearnException(int code) {
		this.code = code;
	}
	
	public boolean invalidCredentials() {
		return this.code == Bean.INVALID_CREDENTIALS;
	}
}
