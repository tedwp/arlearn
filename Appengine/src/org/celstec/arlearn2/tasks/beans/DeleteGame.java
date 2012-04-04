package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.delegators.GameDelegator;

import com.google.gdata.util.AuthenticationException;

public class DeleteGame extends GenericBean {
	
	
	public DeleteGame(String token) {
		super(token);
	}

	private Long gameId;

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	@Override
	public void run() {
		GameDelegator cg;
		try {
			cg = new GameDelegator(getToken());
			cg.deleteGame(getGameId());
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
	}

}
