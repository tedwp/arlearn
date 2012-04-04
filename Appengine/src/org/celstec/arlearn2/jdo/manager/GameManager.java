package org.celstec.arlearn2.jdo.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.GameJDO;
import org.codehaus.jettison.json.JSONException;

public class GameManager {

	private static final String params[] = new String[] { "id", "creatorEmail", "owner", "feedUrl", "title" };
	private static final String paramsNames[] = new String[] { "gameParam", "creatorEmailParam", "ownerEmailParam", "feedUrlParam", "titleParam" };
	private static final String types[] = new String[] { "Long", "String", "String", "String", "String" };

	public static Long addGame(String title, String owner, String creatorEmail, String feedUrl, Long gameId, Config config) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		GameJDO gameJdo = new GameJDO();
		gameJdo.setGameId(gameId);
		gameJdo.setCreatorEmail(creatorEmail);
		gameJdo.setOwner(owner);
		gameJdo.setFeedUrl(feedUrl);
		gameJdo.setTitle(title);
		if (config != null)  {
			gameJdo.setConfig(config.toString());
		}
		try {
			GameJDO persistentGame = pm.makePersistent(gameJdo);
			return persistentGame.getGameId();

		} finally {
			pm.close();
		}
	}

	public static List<Game> getGames(Long gameId, String creatorEmail, String owner, String feedUrl, String title) {
		ArrayList<Game> returnGames = new ArrayList<Game>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Iterator<GameJDO> it = getGames(pm, gameId, creatorEmail, owner, feedUrl, title).iterator();
			while (it.hasNext()) {
				returnGames.add(toBean((GameJDO) it.next()));
			}
			return returnGames;
		} finally {
			pm.close();
		}

	}

	private static List<GameJDO> getGames(PersistenceManager pm, Long gameId, String creatorEmail, String owner, String feedUrl, String title) {
		Query query = pm.newQuery(GameJDO.class);
		Object args[] = { gameId, creatorEmail, owner, feedUrl, title };
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return ((List<GameJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args)));
	}

	public static void deleteGame(Long gameIdentifier) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<GameJDO> gamesToDelete = getGames(pm, gameIdentifier, null, null, null, null);
			pm.deletePersistentAll(gamesToDelete);
		} finally {
			pm.close();
		}
	}

	private static Game toBean(GameJDO jdo) {
		if (jdo == null)
			return null;
		Game game = new Game();
		game.setCreator(jdo.getCreatorEmail());
		game.setTitle(jdo.getTitle());
		game.setFeedUrl(jdo.getFeedUrl());
		game.setGameId(jdo.getGameId());
		game.setOwner(jdo.getOwner());
		if (jdo.getConfig() != null && !"".equals(jdo.getConfig())) {
			JsonBeanDeserializer jbd;
			try {
				jbd = new JsonBeanDeserializer(jdo.getConfig());
				Config config = (Config) jbd.deserialize(Config.class);
				game.setConfig(config);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return game;
	}
}
