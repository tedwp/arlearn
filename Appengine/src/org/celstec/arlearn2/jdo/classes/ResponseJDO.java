package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class ResponseJDO extends RunClass{

	@Persistent
    private Long generalItemId;
	
	@Persistent
    private String userEmail;
	
	@Persistent
    private Text responseValue;
	
	@Persistent
    private Long timeStamp;
	
	@Persistent
    private boolean revoked;

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
		return responseValue.getValue();
	}

	public void setResponseValue(String responseValue) {
		this.responseValue = new Text(responseValue);
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public boolean isRevoked() {
		return revoked;
	}

	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

	public void setResponseValue(Text responseValue) {
		this.responseValue = responseValue;
	}
	
	
}
