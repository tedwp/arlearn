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
package org.celstec.arlearn2.client;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GameAccessList;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.network.ConnectionFactory;

public class GameClient extends GenericClient{

	private static GameClient instance;
	private static final int ERROR_DESERIALIZING = 1;

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
	
	public GamesList getGames(String token,Long from) {
		return (GamesList)  executeGet(getUrlPrefix()+"?from="+from, token, GamesList.class);
	}
	
	public GamesList getGamesParticipate(String token) {
		return (GamesList)  executeGet(getUrlPrefix()+"/participate", token, GamesList.class);
	}
	
	public GamesList getGamesParticipate(String token, Long from) {
		return (GamesList)  executeGet(getUrlPrefix()+"/participate?from="+from, token, GamesList.class);
	}

    public GameAccessList getGameAccessList(String token, Long gameId) {
        return (GameAccessList) executeGet(getUrlPrefix()+"/access/gameId/"+gameId, token, GameAccessList.class);
    }
	
	public Game getGame(String token, Long gameId) {
		return (Game)  executeGet(getUrlPrefix()+"/gameId/"+gameId, token, Game.class);
	}
	
	public Game createGame(String token, Game game) {
		HttpResponse response = ConnectionFactory.getConnection().executePOST(getUrlPrefix(), token, "application/json", toJson(game), "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(response.getEntity(), "utf-8");
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
	
	public GamesList search( String token, String query) {
		return (GamesList) executePost(getUrlPrefix()+"/search", token, query, GamesList.class);
	}

}
