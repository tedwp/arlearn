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

public class SynchronizeGamesTask extends GenericTask implements NetworkTask {

	private Context ctx;

	public SynchronizeGamesTask(Context ctx) {
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

}
