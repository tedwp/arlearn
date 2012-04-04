package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.delegators.RunDelegator;

import com.google.gdata.util.AuthenticationException;

public class DeleteRuns extends GenericBean {

	private Long gameId;
	private String userEmail;

	public DeleteRuns(){
		super();
	}
	
	public DeleteRuns(String token, Long gameId, String userEmail) {
		super(token);
		this.gameId = gameId;
		this.userEmail = userEmail;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Override
	public void run() {
		RunDelegator rd;
		try {
			rd = new RunDelegator("auth=" + getToken());
			rd.deleteRuns(getGameId(),getUserEmail());
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		
		
	}

}
