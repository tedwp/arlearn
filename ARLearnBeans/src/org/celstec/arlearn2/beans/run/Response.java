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
package org.celstec.arlearn2.beans.run;


public class Response extends RunBean implements Comparable<Response>{
	
	
	private Long generalItemId;
	private String userEmail;
	private String responseValue;
	private String responseItemId;
	private Long timestamp;
    private Boolean revoked;
    
    public Response() {
    	
    }

	public Long getGeneralItemId() {
		return generalItemId;
	}

	public void setGeneralItemId(Long generalItemId) {
		this.generalItemId = generalItemId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getResponseValue() {
		return responseValue;
	}

	public void setResponseValue(String responseValue) {
		this.responseValue = responseValue;
	}

	public String getResponseItemId() {
		return responseItemId;
	}

	public void setResponseItemId(String responseItemId) {
		this.responseItemId = responseItemId;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean getRevoked() {
		return revoked;
	}

	public void setRevoked(Boolean revoked) {
		this.revoked = revoked;
	}
    
	@Override
	public boolean equals(Object obj) {
		Response other = (Response ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getGeneralItemId(), other.getGeneralItemId()) && 
			nullSafeEquals(getUserEmail(), other.getUserEmail()) && 
			nullSafeEquals(getResponseValue(), other.getResponseValue()) && 
			nullSafeEquals(getResponseItemId(), other.getResponseItemId()) && 
			nullSafeEquals(getTimestamp(), other.getTimestamp()) && 
			nullSafeEquals(getRevoked(), other.getRevoked()); 
	}

	@Override
	public int compareTo(Response other) {
		if (getTimestamp() == null) setTimestamp(0l);
		if (other.getTimestamp() == null) other.setTimestamp(0l);
		return (int) (getTimestamp() - other.getTimestamp());
	}
}
