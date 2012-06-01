package org.celstec.arlearn2.beans.game;

import org.celstec.arlearn2.beans.Bean;

public class GameBean extends Bean{

	private Long gameId;

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	
	@Override
	public boolean equals(Object obj) {
		GameBean other = (GameBean ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getGameId(), other.getGameId()); 

	}
}

