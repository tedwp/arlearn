package org.celstec.arlearn2.android.asynctasks.network;

import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.broadcast.GenericReceiver;
import org.celstec.arlearn2.android.broadcast.NetworkSwitcher;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.client.GameClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.Context;
import android.os.Message;
import android.util.Log;

public class SynchronizeMyGamesTask extends GenericTask implements NetworkTask {

	private Context ctx;

	public SynchronizeMyGamesTask(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	public void execute() {
		synchronizeGames();
		runAfterTasks(ctx);
	}

	public void run(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		if (!nwHandler.hasMessages(NetworkTaskHandler.SYNC_GAMES)) {
			Message m = Message.obtain(nwHandler);
			m.obj = this;
			m.what = NetworkTaskHandler.SYNC_GAMES;
			m.sendToTarget();
		}
	}

	private void synchronizeGames() {
		if (!NetworkSwitcher.isOnline(ctx)) {
			return;
		} else {
			try {
				GamesList gl = null;
				Long lastDate = PropertiesAdapter.getInstance(ctx).getMyGameLastSynchronizationDate() - 120000;
				gl = GameClient.getGameClient().getGames(PropertiesAdapter.getInstance(ctx).getAuthToken(), lastDate);
				if (gl.getError() == null) {
					GameDelegator.getInstance().saveServerGamesToAndroidDb(ctx, gl);
					PropertiesAdapter.getInstance(ctx).setMyGameLastSynchronizationDate(gl.getServerTime());
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

}
