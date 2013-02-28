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
