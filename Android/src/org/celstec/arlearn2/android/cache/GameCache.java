package org.celstec.arlearn2.android.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.game.Game;

public class GameCache extends GenericCache {

	private static GameCache instance;

	private HashMap<Long, Game> gameMap = new HashMap<Long, Game>();
	private HashMap<String, HashSet<Game>> myGames = new HashMap<String, HashSet<Game>>();

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
				myGames.put(g.getOwner(), new HashSet<Game>());
			myGames.get(g.getOwner()).add(g);
		}
	}
	
	public List<Game> getGames(String account) {
		if (myGames.get(account) == null) return new ArrayList<Game>();
		ArrayList<Game> result =  new ArrayList<Game>();
		Iterator<Game> it = myGames.get(account).iterator();
		while (it.hasNext()) {
			result.add(it.next());
		}
		return result;
	}

	public void deleteGame(Long gameId) {
		synchronized (gameMap) {
			gameMap.remove(gameId);
		}
		synchronized (myGames) {
			for (String account: myGames.keySet()) {
				Iterator<Game>it = myGames.get(account).iterator();
				while (it.hasNext()) {
					if (it.next().getGameId().equals(gameId)) {
						it.remove();
					}
				}
			}
		}
		
	}
}
