package org.celstec.arlearn2.android.delegators.game;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTaskHandler;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.client.GameClient;

import android.content.Context;
import android.os.Message;
import android.util.Log;

public class CreateGameTask implements NetworkTask {

	private Context ctx;

	private String gameTitle;
	private String author;
	private Boolean withMap = true;
	
	public CreateGameTask(Context ctx) {
		this.ctx = ctx;
	}

	public void addTaskToQueue(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();

		Message m = Message.obtain(nwHandler);
		m.obj = this;
		m.what = NetworkTaskHandler.CREATE_GAME;
		m.sendToTarget();
	}
	
	public String getGameTitle() {
		return gameTitle;
	}

	public void setGameTitle(String game) {
		this.gameTitle = gameTitle;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Boolean getWithMap() {
		return withMap;
	}

	public void setWithMap(Boolean withMap) {
		this.withMap = withMap;
	}

	@Override
	public void execute() {
		Game newGame = new Game();
		newGame.setCreator(getAuthor());
		newGame.setTitle(getGameTitle());

		Config newConfig = new Config();
		newConfig.setMapAvailable(getWithMap());
		newGame.setConfig(newConfig);
		
		

		Game g = GameClient.getGameClient().createGame(PropertiesAdapter.getInstance(ctx).getFusionAuthToken(), newGame);
		if (g.getErrorCode() != null) {
			if (g.getErrorCode() == 0) { //TODO reactivate the following code
				//TODO
			}
		}
	}


}
