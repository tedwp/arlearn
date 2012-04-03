package org.celstec.arlearn2.beans;

import java.io.Serializable;

import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;

public class Bean  implements Serializable {

	/*
	 * Error code, indicates run was not found
	 */
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
	
	public String toString() {
		JsonBeanSerialiser jbs = new JsonBeanSerialiser(this);
		return jbs.serialiseToJson().toString();
	}
}
