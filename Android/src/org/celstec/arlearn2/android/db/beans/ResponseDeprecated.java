package org.celstec.arlearn2.android.db.beans;

import java.io.Serializable;

public class ResponseDeprecated  implements Serializable{

	private String userEmail;
	private String generalItemId;
	private String responseValue;
	private Long runId;
	private Long timestamp;
	private String error;
    private Boolean revoked;

	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getGeneralItemId() {
		return generalItemId;
	}
	public void setGeneralItemId(Long generalItemId) {
		this.generalItemId = ""+generalItemId;
	}
	public String getResponseValue() {
		return responseValue;
	}
	public void setResponseValue(String responseValue) {
		this.responseValue = responseValue;
	}
	public Long getRunId() {
		return runId;
	}
	public void setRunId(Long runId) {
		this.runId = runId;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Boolean getRevoked() {
		return revoked;
	}
	public void setRevoked(Boolean revoked) {
		this.revoked = revoked;
	}	
}
