package org.celstec.arlearn2.beans.game;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import org.celstec.arlearn2.beans.Bean;

public class GamesList extends Bean implements Serializable{
	
	public static String gamesType = "org.celstec.arlearn2.beans.game.Game";
	
	private List<Game> games = new Vector();

	public GamesList() {
		
	}
	
	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	public void addGame(Game game) {
		games.add(game);
	}
	
}
