package org.celstec.arlearn2.android.asynctasks.network;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.broadcast.GenericReceiver;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.client.GameClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.Context;
import android.os.Message;
import android.util.Log;

public class SynchronizeGamesTask implements NetworkTask {

	private Context ctx;

	public SynchronizeGamesTask(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	public void execute() {
		synchronizeGames();
	}

	public void addTaskToQueue(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		if (!nwHandler.hasMessages(NetworkTaskHandler.SYNC_GAMES)) {
			Message m = Message.obtain(nwHandler);
			m.obj = this;
			m.what = NetworkTaskHandler.SYNC_GAMES;
			m.sendToTarget();
		}
	}

	private void synchronizeGames() {
		try {
			GamesList gl = null;
			Long lastDate = PropertiesAdapter.getInstance(ctx).getGameLastSynchronizationDate() - 120000;
			if (lastDate <= 0) {
				gl = GameClient.getGameClient().getGamesParticipate(PropertiesAdapter.getInstance(ctx).getFusionAuthToken());
			} else {
				gl = GameClient.getGameClient().getGamesParticipate(PropertiesAdapter.getInstance(ctx).getFusionAuthToken(), lastDate);
			}
			if (gl.getError() == null) {
				GameDelegator.getInstance().saveServerGamesToAndroidDb(ctx, gl);
				PropertiesAdapter.getInstance(ctx).setGameLastSynchronizationDate(gl.getServerTime());
			}
		} catch (ARLearnException ae) {
			if (ae.invalidCredentials()) {
				GenericReceiver.setStatusToLogout(ctx);
			}
		} catch (Exception e) {
			Log.e("exception", e.getMessage(), e);

		}
	}

}
