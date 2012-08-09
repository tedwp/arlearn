package org.celstec.arlearn2.delegators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.celstec.arlearn2.beans.dependencies.ActionDependency;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.cache.MyGamesCache;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.jdo.manager.GameManager;
import org.celstec.arlearn2.tasks.beans.DeleteGeneralItems;
import org.celstec.arlearn2.tasks.beans.DeleteProgressDefinitions;
import org.celstec.arlearn2.tasks.beans.DeleteRuns;
import org.celstec.arlearn2.tasks.beans.DeleteScoreDefinitions;
import org.codehaus.jettison.json.JSONException;

import com.google.gdata.util.AuthenticationException;

public class GameDelegator extends GoogleDelegator {

	private static final Logger logger = Logger.getLogger(GameDelegator.class.getName());

	public GameDelegator(String authtoken) throws AuthenticationException {
		super(authtoken);
	}

	public GameDelegator(GoogleDelegator gd) {
		super(gd);
	}

	public GamesList getGames() {
		GamesList gl = new GamesList();
		String myAccount = UserLoggedInManager.getUser(authToken);
		if (myAccount == null) {
			gl.setError("login to retrieve your list of games");
			return gl;
		}
		List<Game> list = MyGamesCache.getInstance().getGameList(null, null, myAccount, null, null);
		if (list == null) {
			list = GameManager.getGames(null, null, myAccount, null, null);
			MyGamesCache.getInstance().putGameList(list, null, null, myAccount, null, null);
		}
		gl.setGames(list);
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

	public Game createGame(Game game) {
		UsersDelegator qu = new UsersDelegator(this);
		String myAccount = qu.getCurrentUserAccount();
		if (myAccount == null) {
			game.setError("login to create a game");
			return game;
		}
		game.setGameId(GameManager.addGame(game.getTitle(), myAccount, game.getCreator(), game.getFeedUrl(), game.getGameId(), game.getConfig()));
		MyGamesCache.getInstance().removeGameList(null, null, myAccount, null, null);
		MyGamesCache.getInstance().removeGameList(game.getGameId(), null, myAccount, null, null);
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

		// Queue queue = QueueFactory.getDefaultQueue();
		// queue.add(TaskOptions.Builder.withUrl("/asyncTask")
		// .param(AsyncTasksServlet.TASK, "" + AsyncTasksServlet.DELETE_RUNS)
		// .param(AsyncTasksServlet.AUTH, authToken)
		// .param(AsyncTasksServlet.GAMEID, ""+gameIdentifier)
		// .param(AsyncTasksServlet.EMAIL, myAccount));
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
		createGame(g);
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
		
		createGame(g);
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
		createGame(g);
		return g;

	}
}
