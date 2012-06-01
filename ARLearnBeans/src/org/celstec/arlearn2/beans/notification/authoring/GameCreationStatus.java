package org.celstec.arlearn2.beans.notification.authoring;

import org.celstec.arlearn2.beans.Bean;

public class GameCreationStatus extends Bean{

	public final static int GAME_CREATED = 0;
	public final static int IDENTIFIERS_UPDATED = 1;
	public final static int PROCESSED_MANUAL_ITEMS = 2;
	
	private Long gameId;
	private Integer status;
	
	public Long getGameId() {
		return gameId;
	}
	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Override
	public boolean equals(Object obj) {
		GameCreationStatus other = (GameCreationStatus ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getGameId(), other.getGameId()) && 
			nullSafeEquals(getStatus(), other.getStatus()); 

	}

}
