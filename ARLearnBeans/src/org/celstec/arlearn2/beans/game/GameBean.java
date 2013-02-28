/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
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

