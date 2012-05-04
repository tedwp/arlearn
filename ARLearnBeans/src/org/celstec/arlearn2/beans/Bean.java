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
	
	@Override
	public boolean equals(Object obj) {
		Bean other = (Bean ) obj;
		if (obj == null) return false;
		return nullSafeEquals(getError(), other.getError()) && 
				 nullSafeEquals(getErrorCode(), other.getErrorCode()) && 
						 nullSafeEquals(getTimestamp(), other.getTimestamp()); 

	}
	
	protected boolean nullSafeEquals(Object o1, Object o2) {
		if (o2 == null || o1 == o2) return true;
		if (o1 == null) return false;
		return o1.equals(o2);
	}

}
