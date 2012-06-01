package org.celstec.arlearn2.gwt.client.control;

import org.celstec.arlearn2.gwt.client.network.JsonCallback;

public class Game {

	static Game instance;
	
	private Game() {
		
	}
	
	public static Game getInstance() {
		if (instance == null) instance = new Game();
		return instance;
	}
	
//	public void createGame(String title, String creator, final JsonCallback jcb) {
//		GameClient.createGame(title, creator, jcb);
//	}
}
