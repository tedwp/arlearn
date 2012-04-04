package org.celstec.arlearn2.beans;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@XmlRootElement
@XmlType(name="Response", namespace="http://celstec.org/arlearn2")
public class Response extends RunBean{
	
	public static final String TABLE_NAME = "Response"; 
	
	private String generalItemId;
	private String userEmail;
	private String responseValue;
	private String responseItemId;
	private Long timestamp;
    private Boolean revoked;

	
	public String getGeneralItemId() {
		return generalItemId;
	}
	public void setGeneralItemId(String generalItemId) {
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
	
	
	public String getFormattedResponseValue() {
		String result = "";
		try {
			JSONObject json = new JSONObject(getResponseValue());
			if (json.has("answer"))result += "answer: " + json.get("answer") + "<br>";
			if (json.has("isCorrect"))result += "isCorrect: " + json.get("isCorrect") + "<br>";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
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
	
	
}
