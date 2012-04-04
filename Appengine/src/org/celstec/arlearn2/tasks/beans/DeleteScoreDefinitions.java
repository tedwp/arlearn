package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.delegators.ScoreDefinitionDelegator;

import com.google.gdata.util.AuthenticationException;

public class DeleteScoreDefinitions extends GenericBean {
	private Long gameId;

	public DeleteScoreDefinitions() {
		super();
	}

	public DeleteScoreDefinitions(String token, Long gameId) {
		super(token);
		this.gameId = gameId;
	}

	@Override
	public void run() {
		ScoreDefinitionDelegator rd;
		try {
			rd = new ScoreDefinitionDelegator("auth=" + getToken());
			rd.deleteScoreDefinition(getGameId());
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
