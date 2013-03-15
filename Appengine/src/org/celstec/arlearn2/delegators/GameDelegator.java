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
package org.celstec.arlearn2.delegators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.celstec.arlearn2.beans.dependencies.ActionDependency;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.beans.game.MapRegion;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.notification.GameModification;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.cache.MyGamesCache;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.jdo.manager.GameManager;
import org.celstec.arlearn2.jdo.manager.GeneralItemManager;
import org.celstec.arlearn2.jdo.manager.UserManager;
import org.celstec.arlearn2.tasks.beans.DeleteGeneralItems;
import org.celstec.arlearn2.tasks.beans.DeleteProgressDefinitions;
import org.celstec.arlearn2.tasks.beans.DeleteRuns;
import org.celstec.arlearn2.tasks.beans.DeleteScoreDefinitions;
import org.celstec.arlearn2.tasks.beans.GameSearchIndex;
import org.celstec.arlearn2.tasks.beans.NotifyRunsFromGame;
import org.codehaus.jettison.json.JSONException;

import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;
import com.google.gdata.util.AuthenticationException;
import com.sun.jersey.server.impl.monitoring.GlassFishMonitoringInitializer;

public class GameDelegator extends GoogleDelegator {

	private static final Logger logger = Logger.getLogger(GameDelegator.class.getName());

	public GameDelegator(String authtoken) throws AuthenticationException {
		super(authtoken);
	}

	public GameDelegator(GoogleDelegator gd) {
		super(gd);
	}
	
	public GameDelegator() {
		super();
	}

	public GamesList getGames(Long from, Long until) {
		GamesList gl = new GamesList();
		String myAccount = UserLoggedInManager.getUser(authToken);
		if (myAccount == null) {
			gl.setError("login to retrieve your list of games");
			return gl;
		}
//		List<Game> list = MyGamesCache.getInstance().getGameList(null, null, myAccount, null, null);
//		List<Game> list = GameManager.getGames(myAccount, from, until);
//		if (list == null) {
//			list = GameManager.getGames(null, null, myAccount, null, null);
//			MyGamesCache.getInstance().putGameList(list, null, null, myAccount, null, null);
//		}
		gl.setGames(GameManager.getGames(myAccount, from, until));
		return gl;
	}
	
	public GamesList getParticipateGames() {
		GamesList gl = new GamesList();
		UsersDelegator qu = new UsersDelegator(this);
		RunDelegator rd = new RunDelegator(this);
		String myAccount = qu.getCurrentUserAccount();
		Iterator<User> it = UserManager.getUserList(null, myAccount, null, null).iterator();
		HashSet<Long> addGames = new HashSet<Long>();
		while (it.hasNext()) {
			User user = (User) it.next();
			Run r = rd.getRun(user.getRunId());
			if (r != null) {
				r.setDeleted(user.getDeleted());
				if (!r.getDeleted()) {
					if (r.getGame() != null && !addGames.contains(r.getGameId())) {
						gl.addGame(r.getGame());
						addGames.add(r.getGameId());
					}
				}
			} else {
				logger.severe("following run does not exist" + user.getRunId());

			}
		}
		gl.setServerTime(System.currentTimeMillis());
		return gl;
	}
	
	public GamesList getParticipateGames(Long from, Long until) {
		if (from !=null && until !=null &&from.longValue() >= until.longValue()) {
			return new GamesList();
		}
		UsersDelegator qu = new UsersDelegator(this);
		String myAccount = qu.getCurrentUserAccount();
		Iterator<User> it = UserManager.getUserListGameParticipate(myAccount, from, until).iterator();
		GamesList gl = new GamesList();
		while (it.hasNext()) {
			User user = (User) it.next();
			if (user.getGameId() != null ) {
				Game g = getGameWithoutAccount(user.getGameId());
				if (g!= null) {
					gl.addGame(g);
				} else {
					logger.severe("following game does not exist"+user.getGameId());	
				}
			}
		}
		gl.setServerTime(System.currentTimeMillis());
		
		return gl;
		
	}

	public Game getGame(Long gameId) {
		String myAccount = UserLoggedInManager.getUser(authToken);
		if (myAccount == null) {
			Game game = new Game();
			game.setGameId(gameId);
			game.setError("login to retrieve game");
			return game;
		}
		List<Game> list = MyGamesCache.getInstance().getGameList(gameId, null, myAccount, null, null);
		if (list == null) {
			list = GameManager.getGames(gameId, null, myAccount, null, null);
			MyGamesCache.getInstance().putGameList(list, gameId, null, myAccount, null, null);
		}
		if (list.isEmpty()) {
			Game game = new Game();
			game.setGameId(gameId);
			game.setError("game does not exist");
			return game;
		}
		return list.get(0);
	}
	
	public Game getGameWithoutAccount(Long gameId) {
		List<Game> list = MyGamesCache.getInstance().getGameList(gameId, null, null, null, null);
		if (list == null) {
			list = GameManager.getGames(gameId, null, null, null, null);
			if (!list.isEmpty()) MyGamesCache.getInstance().putGameList(list, gameId, null, null, null, null);
		}
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	public Game getUnOwnedGame(Long gameId) {
		List<Game> list = MyGamesCache.getInstance().getGameList(gameId, null, null, null, null);
		if (list == null) {
			list = GameManager.getGames(gameId, null, null, null, null);
			MyGamesCache.getInstance().putGameList(list, gameId, null, null, null, null);
		}
		if (list.isEmpty()) {
			Game game = new Game();
			game.setGameId(gameId);
			game.setError("game does not exist");
			return game;
		}
		return list.get(0);
	}

	public Game getNotOwnedGame(Long gameId) {
		List<Game> list = GameManager.getGames(gameId, null, null, null, null);
		if (list.isEmpty()) {
			Game game = new Game();
			game.setGameId(gameId);
			game.setError("game does not exist");
			return game;
		}
		return list.get(0);
	}
	
	public Game createGame(Game game, int modificationType) {
		return createGame(game, modificationType, true);
	}
	
	public Game createGame(Game game, int modificationType, boolean notify) {
		UsersDelegator qu = new UsersDelegator(this);
		String myAccount = qu.getCurrentUserAccount();
		if (myAccount == null) {
			game.setError("login to create a game");
			return game;
		}
//		game.setGameId(GameManager.addGame(game.getTitle(), myAccount, game.getCreator(), game.getFeedUrl(), game.getGameId(), game.getConfig()));
		game.setGameId(GameManager.addGame(game, myAccount));
		MyGamesCache.getInstance().removeGameList(null, null, myAccount, null, null);
		MyGamesCache.getInstance().removeGameList(game.getGameId(), null, myAccount, null, null);
		MyGamesCache.getInstance().removeGameList(game.getGameId(), null, null, null, null);
		
		GameModification gm = new GameModification();
		gm.setModificationType(modificationType);
		gm.setGame(game);
		if (notify) ChannelNotificator.getInstance().notify(myAccount, gm);
		
		(new NotifyRunsFromGame(authToken, game.getGameId(), null, modificationType)).scheduleTask();

		return game;
	}

	public Game deleteGame(Long gameIdentifier) {
		UsersDelegator qu = new UsersDelegator(this);
		String myAccount = qu.getCurrentUserAccount();
		Game g = getGame(gameIdentifier);
		if (g.getError() != null)
			return g;
		if (!g.getOwner().equals(myAccount)) {
			Game game = new Game();
			game.setError("You are not the owner of this game");
			return game;
		}
		GameManager.deleteGame(gameIdentifier);
		MyGamesCache.getInstance().removeGameList(null, null, myAccount, null, null);
		MyGamesCache.getInstance().removeGameList(gameIdentifier, null, myAccount, null, null);
		(new DeleteRuns(authToken, gameIdentifier, myAccount)).scheduleTask();
		(new DeleteProgressDefinitions(authToken, gameIdentifier)).scheduleTask();
		(new DeleteScoreDefinitions(authToken, gameIdentifier)).scheduleTask();
		(new DeleteGeneralItems(authToken, gameIdentifier)).scheduleTask();

		GameModification gm = new GameModification();
		gm.setModificationType(GameModification.DELETED);
		gm.setGame(g);
		ChannelNotificator.getInstance().notify(myAccount, gm);
		
		return g;
	}

	public Game createRole(Long gameIdentifier, String roleString) {
		Game g = getGame(gameIdentifier);
		if (g.getError() != null)
			return g;
		if (g.getConfig() == null)
			g.setConfig(new Config());
		Config c = g.getConfig();
		if (c.getRoles() == null)
			c.setRoles(new ArrayList<String>());
		c.getRoles().add(roleString);
		createGame(g, GameModification.ALTERED);
		return g;

	}
	
	public Game setWithMap(Long gameIdentifier, boolean value) {
		Game g = getGame(gameIdentifier);
		if (g.getError() != null)
			return g;
		if (g.getConfig() == null)
			g.setConfig(new Config());
		Config c = g.getConfig();
		if (c.getRoles() == null)
			c.setRoles(new ArrayList<String>());
		
		c.setMapAvailable(value);
		
		createGame(g, GameModification.ALTERED);
		return g;

	}
	
	public Game setMapType(Long gameIdentifier, int type) {
		Game g = getGame(gameIdentifier);
		if (g.getError() != null)
			return g;
		if (g.getConfig() == null)
			g.setConfig(new Config());
		Config c = g.getConfig();
		if (c.getRoles() == null)
			c.setRoles(new ArrayList<String>());
		
		c.setMapType(type);
		
		createGame(g, GameModification.ALTERED);
		return g;

	}
	
	public Game setSharing(Long gameIdentifier, Integer sharingType) {
		Game g = getGame(gameIdentifier);
		if (g.getError() != null)
			return g;
		if (!g.getSharing().equals(sharingType)) {
			g.setSharing(sharingType);
			createGame(g, GameModification.ALTERED);
		}
		new GameSearchIndex(g.getTitle(), g.getCreator(), sharingType, g.getGameId()).scheduleTask();
		return g;
	}
	
	public Game setRegions(Long gameIdentifier, List<MapRegion> regions) {
		Game g = getGame(gameIdentifier);
		if (g.getError() != null)
			return g;
		if (g.getConfig() == null)
			g.setConfig(new Config());
		Config c = g.getConfig();
		if (c.getRoles() == null)
			c.setRoles(new ArrayList<String>());
		
		c.setMapRegions(regions);
		
		createGame(g, GameModification.ALTERED);
		return g;

	}

	public Game addManualTrigger(Long gameIdentifier, String generalItem) {
		Game g = getGame(gameIdentifier);
		GeneralItem gi;
		try {
			gi = (GeneralItem) JsonBeanDeserializer.deserialize(generalItem);
		} catch (JSONException e) {
			g.setError(e.getMessage());
			return g;
		}
		if (g.getError() != null)
			return g;
		if (g.getConfig() == null)
			g.setConfig(new Config());
		Config c = g.getConfig();
		if (c.getManualItems() == null)
			c.setManualItems(new ArrayList<GeneralItem>());
		
			c.getManualItems().add(gi);
		
		createGame(g, GameModification.ALTERED);
		GeneralItemDelegator gd = new GeneralItemDelegator(this);
		GeneralItem itemFromDb = gd.getGeneralItemForGame(gameIdentifier, gi.getId());
		if (itemFromDb.getDependsOn() == null) {
			ActionDependency ad = new ActionDependency();
			ad.setAction("manual-"+gi.getId());
			itemFromDb.setDependsOn(ad);
			gd.createGeneralItem(itemFromDb);
		}
		return g;

	}

	public Game removeTrigger(Long gameIdentifier, long itemIdentifier) {
		Game g = getGame(gameIdentifier);
		if (g.getError() != null)
			return g;
		if (g.getConfig() == null)
			g.setConfig(new Config());
		Config c = g.getConfig();
		if (c.getManualItems() == null)
			c.setManualItems(new ArrayList<GeneralItem>());
		for (Iterator<GeneralItem> iter = c.getManualItems().iterator(); iter.hasNext();) {
			GeneralItem gi = iter.next();
			if (gi.getId() == itemIdentifier) {
				iter.remove();
			}

		}
		createGame(g, GameModification.ALTERED);
		return g;

	}
	
	public  void updateAllGames() {
		int i = 0;
//		for (Game game: GameManager.getGames(null, null, null, null, null)) {
//			if (game.getLastModificationDate() == null) {
//				GameManager.addGame(game.getTitle(), game.getOwner(), game.getCreator(), game.getFeedUrl(), game.getGameId(), game.getConfig());
//				i++;
//				if (i> 10) return;
//			}
//		}
//		GeneralItemVisibilityManager.updateAll();
//		GeneralItemManager.updateAll();
		UserManager.updateAll(this);
//		RunManager.updateAll();
//		for (GeneralItemVisibility vis: GeneralItemVisibilityManager.getVisibleItems(null, null)) {
//			
//		}
		
	}

	public GamesList search(String searchQuery) {
		try {
		    Results<ScoredDocument> results = getIndex().search(searchQuery);
		    GamesList resultsList = new GamesList();
		    for (ScoredDocument document : results) {
		    	Game g =new Game();
		    	g.setTitle(document.getFields("title").iterator().next().getText());
		    	g.setCreator(document.getFields("author").iterator().next().getText());
		    	g.setGameId(document.getFields("gameId").iterator().next().getNumber().longValue());
		        // Use the document for display.
		    	resultsList.addGame(g);
		    }
		    return resultsList;
		} catch (SearchException e) {
		    if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
		        // retry
		    }
		}
		return null;
	}

	public Index getIndex() {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("game_index").build();
		return SearchServiceFactory.getSearchService().getIndex(indexSpec);
	}
}
