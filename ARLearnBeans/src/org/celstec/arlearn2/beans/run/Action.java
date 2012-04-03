package org.celstec.arlearn2.beans.run;


public class Action extends RunBean{

//	public static final String TABLE_NAME = "Action"; 

	private Long generalItemId;
	private String generalItemType;
	private String userEmail;
	private Long time;
	private String action;
	
	public Action() {
		
	}

	public Long getGeneralItemId() {
		return generalItemId;
	}

	public void setGeneralItemId(Long generalItemId) {
		this.generalItemId = generalItemId;
	}

	public String getGeneralItemType() {
		return generalItemType;
	}

	public void setGeneralItemType(String generalItemtype) {
		this.generalItemType = generalItemtype;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}

