package org.celstec.arlearn2.android.delegators.game;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTaskHandler;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GameAccess;
import org.celstec.arlearn2.client.GameClient;

import android.content.Context;
import android.os.Message;
import android.widget.Toast;

public class CreateGameTask implements NetworkTask {

	private Context ctx;
	
	private Game game;
	private GameAccess gameAccess = null;
	
	public CreateGameTask(Context ctx, Game game) {
		this.ctx = ctx;
		this.game = game;
	}
	
	public CreateGameTask(Context ctx, Game game, GameAccess gameAccess) {
		this.ctx = ctx;
		this.game = game;
		this.gameAccess = gameAccess;
	}
	

	public void addTaskToQueue(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();

		Message m = Message.obtain(nwHandler);
		m.obj = this;
		m.what = NetworkTaskHandler.GAME_CREATE;
		m.sendToTarget();
	}
	
	@Override
	public void execute() {

		if(gameAccess != null){
			// TOFIX Game g = GameClient.getGameClient().createGame(PropertiesAdapter.getInstance(ctx).getAuthToken(), game, gameAccess);
			Game g = GameClient.getGameClient().createGame(PropertiesAdapter.getInstance(ctx).getAuthToken(), game);
			if (g.getErrorCode() != null) {
					Toast.makeText(ctx, "update/creation of this game failed", Toast.LENGTH_LONG).show(); //TODO i18n
			}			
		}else{
			Game g = GameClient.getGameClient().createGame(PropertiesAdapter.getInstance(ctx).getAuthToken(), game);
			if (g.getErrorCode() != null) {
					Toast.makeText(ctx, "update/creation of this game failed", Toast.LENGTH_LONG).show(); //TODO i18n
			}			
		}			
	}


}
