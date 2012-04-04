package org.celstec.arlearn2.genericBeans;

public class Action {
	private String GeneralItemId;
	private String UserEmail;
	private Long time;
	private String action;

	public Action() {
		
	}

	public String getGeneralItemId() {
		return GeneralItemId;
	}

	public void setGeneralItemId(String generalItemId) {
		GeneralItemId = generalItemId;
	}

	public String getUserEmail() {
		return UserEmail;
	}

	public void setUserEmail(String userEmail) {
		UserEmail = userEmail;
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
