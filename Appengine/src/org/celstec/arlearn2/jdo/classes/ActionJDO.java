package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class ActionJDO  extends RunClass{
	
	@Persistent
    private Long generalItemId;
	
	@Persistent
    private String generalItemType;
	
	@Persistent
    private String userEmail;
	
	@Persistent
    private String action;
	
	@Persistent
    private Long time;

	public Long getGeneralItemId() {
		return generalItemId;
	}

	public void setGeneralItemId(Long generalItemId) {
		this.generalItemId = generalItemId;
	}

	public String getGeneralItemType() {
		return generalItemType;
	}

	public void setGeneralItemType(String generalItemType) {
		this.generalItemType = generalItemType;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
	
	
}
