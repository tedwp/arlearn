package org.celstec.arlearn2.android.db.beans;

import java.io.Serializable;

public class Bean  implements Serializable {
public static final int RUNNOTFOUND = 1;
	
	private String error;
	private Integer errorCode;
	
	private Long timestamp;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	
	public boolean runNotFound() {
		if (errorCode == null) return false;
		return errorCode == RUNNOTFOUND;
	}
}
