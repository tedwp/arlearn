package org.celstec.arlearn2.beans.game;

import org.celstec.arlearn2.beans.Bean;

public class GameBean extends Bean{

	private Long gameId;

	private Boolean deleted;
	
	private Long lastModificationDate;
	
	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Long getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Long lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	@Override
	public boolean equals(Object obj) {
		GameBean other = (GameBean ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getDeleted(), other.getDeleted()) && 
			nullSafeEquals(getLastModificationDate(), other.getLastModificationDate()) && 
			nullSafeEquals(getGameId(), other.getGameId()); 

	}
}

