package org.celstec.arlearn2.client;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.network.ConnectionFactory;

public class GameClient extends GenericClient{

	private static GameClient instance;

	private GameClient() {
		super("/myGames");
	}
	
	public static GameClient getGameClient() {
		if (instance == null) {
			instance = new GameClient();
		}
		return instance;
	}
	
	public GamesList getGames(String token) {
		return (GamesList)  executeGet(getUrlPrefix(), token, GamesList.class);
	}
	
	public Game createGame(String token, Game game) {
		HttpResponse response = ConnectionFactory.getConnection().executePOST(getUrlPrefix(), token, "application/json", toJson(game), "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(response.getEntity());
			return (Game) jsonDeserialise(entry, Game.class);
		} catch (Exception e) {
			e.printStackTrace();
			Game g = new Game();
			g.setError("exception "+e.getMessage());
			return g;
		}
	}
	
	public Game delete(String token, long gameId) {
		return (Game) executeDelete(getUrlPrefix()+"/gameId/"+gameId, token, Game.class);
	}

}
