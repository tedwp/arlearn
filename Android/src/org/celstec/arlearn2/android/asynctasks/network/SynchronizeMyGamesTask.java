package org.celstec.arlearn2.android.asynctasks.network;

import org.celstec.arlearn2.android.activities.ListGamesActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.broadcast.GenericReceiver;
import org.celstec.arlearn2.android.broadcast.NetworkSwitcher;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.beans.game.GameAccess;
import org.celstec.arlearn2.beans.game.GameAccessList;
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

				GamesList gl = new GamesList();
				GameAccessList gal = null;

				gal = GameClient.getGameClient().getGamesAccess(PropertiesAdapter.getInstance(ctx).getAuthToken(), 0l);
				System.out.println("Num of games: ["+ gal.getGameAccess().size() + "]");

				if (gal.getError() == null) {

					if (gal.getGameAccess().size() > 0) {

						for (GameAccess ga : gal.getGameAccess()) {
							System.out.println("Gameid: "+ga.getGameId());

							gl.addGame(GameClient.getGameClient().getGame(PropertiesAdapter.getInstance(ctx).getAuthToken(), ga.getGameId()));

						}

						GameDelegator.getInstance().saveServerGamesToAndroidDb(ctx, gl, gal);
						//PropertiesAdapter.getInstance(ctx).setMyGameLastSynchronizationDate(gl.getServerTime());
						ActivityUpdater.updateActivities(ctx, ListGamesActivity.class.getCanonicalName());

					}

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
