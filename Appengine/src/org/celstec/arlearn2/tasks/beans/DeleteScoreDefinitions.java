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
