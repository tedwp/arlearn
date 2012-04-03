package org.celstec.arlearn2.beans.run;

public class VisibleItem extends RunBean {

	private Long generalItemId;
	private String email;
    private String teamId;
    
    public VisibleItem() {}

	public Long getGeneralItemId() {
		return generalItemId;
	}

	public void setGeneralItemId(Long generalItemId) {
		this.generalItemId = generalItemId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
    
    
}
