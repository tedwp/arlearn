package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.delegators.ProgressDefinitionDelegator;

import com.google.gdata.util.AuthenticationException;

public class DeleteProgressDefinitions extends GenericBean {

	private Long gameId;

	public DeleteProgressDefinitions() {
		super();
	}

	public DeleteProgressDefinitions(String token, Long gameId) {
		super(token);
		this.gameId = gameId;
	}

	@Override
	public void run() {
		ProgressDefinitionDelegator rd;
		try {
			rd = new ProgressDefinitionDelegator("auth=" + getToken());
			rd.deleteProgressDefinition(getGameId());
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}

	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

}
