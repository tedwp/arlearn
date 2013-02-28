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
import org.celstec.arlearn2.android.delegators.RunDelegator;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.client.RunClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.Context;
import android.os.Message;

public class SynchronizeRunsTask extends GenericTask implements NetworkTask {

	private Context ctx;

	public SynchronizeRunsTask(Context ctx) {
		this.ctx = ctx;
	}

	public void run(Context ctx) {
		this.ctx = ctx;
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		if (!nwHandler.hasMessages(NetworkTaskHandler.SYNC_RUNS)) {
			Message m = Message.obtain(nwHandler);
			m.obj = this;
			m.what = NetworkTaskHandler.SYNC_RUNS;
			m.sendToTarget();
		}
	}

	@Override
	public void execute() {
		if (!NetworkSwitcher.isOnline(ctx)) {
			runAfterTasks(ctx);
			return;
		} else {
			try {
				RunList rl = null;
				Long lastDate = PropertiesAdapter.getInstance(ctx).getRunLastSynchronizationDate() - 120000;
				if (lastDate <= 0) {
					rl = RunClient.getRunClient().getRunsParticipate(PropertiesAdapter.getInstance(ctx).getFusionAuthToken());
				} else {
					rl = RunClient.getRunClient().getRunsParticipate(PropertiesAdapter.getInstance(ctx).getFusionAuthToken(), lastDate);
				}
				PropertiesAdapter.getInstance(ctx).setRunLastSynchronizationDate(rl.getServerTime());
				RunDelegator.getInstance().saveServerRunsToAndroidDb(ctx, rl);
			} catch (ARLearnException ae) {
				if (ae.invalidCredentials()) {
					GenericReceiver.setStatusToLogout(ctx);
				}

			}
		}
	}

}
