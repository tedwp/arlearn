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
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GameAccess;

public class GameCache extends GenericCache {

	private static GameCache instance;

	private HashMap<Long, GameAccess> gameAccessMap = new HashMap<Long, GameAccess>();
	private HashMap<Long, Game> gameMap = new HashMap<Long, Game>();
	private HashMap<String, HashMap<Long, Game>> myGames = new HashMap<String, HashMap<Long, Game>>();

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
		if (g.getOwner() != null) {
			if (!myGames.containsKey(g.getOwner()))
				myGames.put(g.getOwner(), new HashMap<Long, Game>());
			myGames.get(g.getOwner()).put(g.getGameId(), g);
		}
	}
	
	public void putGameAccess(GameAccess g) {
		synchronized (gameAccessMap) {
			gameAccessMap.put(g.getGameId(),g);
		}
	}	
	
	public Set<Game> getGames(String account) {
		if (myGames.get(account) == null) return new TreeSet<Game>();
		TreeSet<Game> result =  new TreeSet<Game>();
		Iterator<Long> it = myGames.get(account).keySet().iterator();
		while (it.hasNext()) {
			result.add(myGames.get(account).get(it.next()));
		}
		return result;
	}
	
	public Set<Game> getGames() {
		
		TreeSet<Game> result =  new TreeSet<Game>();
		Iterator<Entry<Long, Game>> it = gameMap.entrySet().iterator();
		while (it.hasNext()) {
			result.add(it.next().getValue());
		}
		return result;
		
	} 
	
	public GameAccess getGameAccess(Long gameId){
		return gameAccessMap.get(gameId);
	}
	


	public void deleteGame(Long gameId) {
		synchronized (gameMap) {
			gameMap.remove(gameId);
			gameAccessMap.remove(gameId);
		}
		synchronized (myGames) {
			for (String account: myGames.keySet()) {
				if (myGames.get(account).containsKey(gameId)) {
					myGames.get(account).remove(gameId);
				}
			}
		}
		
	}
}
