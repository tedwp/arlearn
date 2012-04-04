package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class ProgressRecordJDO extends RunClass {


	@Persistent
    private Long generalItemId;
	
	@Persistent
    private String generalItemType;
	
	@Persistent
    private String action;
	
	@Persistent
    private String scope;
	
	@Persistent
    private String userEmail;
	
	@Persistent
    private String teamId;
	
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
	
}
