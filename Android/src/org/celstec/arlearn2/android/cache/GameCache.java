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
package org.celstec.arlearn2.android.cache;

import java.util.HashMap;

import org.celstec.arlearn2.beans.game.Game;

public class GameCache extends GenericCache {

	private static GameCache instance;

	private HashMap<Long, Game> gameMap = new HashMap<Long, Game>();

	private GameCache() {
	}

	public void empty() {
		gameMap = new HashMap<Long, Game>();
	}

		
	public static GameCache getInstance() {
		if (instance == null) {
			instance = new GameCache();
		}
		return instance;
	}
	
	public Game getGame(long gameId) {
		if (gameMap == null) return null;
		return gameMap.get(gameId);
	}
	
	public void putGame(long gameId, Game game) {
		synchronized (gameMap) {
			gameMap.put(gameId, game);
		}
	}

	public void putGame(Game g) {
		putGame(g.getGameId(), g);
		
	}
}
