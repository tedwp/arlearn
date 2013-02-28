/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.beans;

import java.io.Serializable;

import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.codehaus.jettison.json.JSONObject;

@SuppressWarnings("serial")
public class Bean  implements Serializable {

	/*
	 * Error code, indicates run was not found
	 */
	public static final int RUNNOTFOUND = 1;
	public static final int INVALID_CREDENTIALS = 2;
	 
	private String type;
	private String error;
	private Integer errorCode;
	private Long timestamp;

	public Bean() {
		setType(getClass().getName());
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
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
//		JsonBeanSerialiser jbs = new JsonBeanSerialiser(this);
//		return jbs.serialiseToJson().toString();
		JSONObject json = JsonBeanSerialiser.serialiseToJson(this);
		if (json == null) {
			System.err.println("json is null for this bean "+getClass());
			return null;
		}
		return json.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		Bean other = (Bean ) obj;
		if (obj == null) return false;
		return nullSafeEquals(getError(), other.getError()) && 
				nullSafeEquals(getType(), other.getType()) &&
				 nullSafeEquals(getErrorCode(), other.getErrorCode()) && 
						 nullSafeEquals(getTimestamp(), other.getTimestamp()); 

	}
	
	protected boolean nullSafeEquals(Object o1, Object o2) {
		if (o2 == null || o1 == o2) return true;
		if (o1 == null) return false;
		if (o1.getClass() != o2.getClass()) return false;
		return o1.equals(o2);
	}

}
