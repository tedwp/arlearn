package org.celstec.arlearn2.android.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.celstec.arlearn2.beans.game.Game;

public class GameCache extends GenericCache {

	private static GameCache instance;

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
	
	public Set<Game> getGames(String account) {
		if (myGames.get(account) == null) return new TreeSet<Game>();
		TreeSet<Game> result =  new TreeSet<Game>();
		Iterator<Long> it = myGames.get(account).keySet().iterator();
		while (it.hasNext()) {
			result.add(myGames.get(account).get(it.next()));
		}
		return result;
	}

	public void deleteGame(Long gameId) {
		synchronized (gameMap) {
			gameMap.remove(gameId);
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
