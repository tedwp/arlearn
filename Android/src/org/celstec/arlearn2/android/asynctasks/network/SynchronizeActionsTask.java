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
import org.celstec.arlearn2.android.broadcast.NetworkSwitcher;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.beans.run.ActionList;
import org.celstec.arlearn2.client.ActionClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.Context;
import android.os.Message;

public class SynchronizeActionsTask extends GenericTask implements NetworkTask {

	private Context ctx;
	private Long runId;

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	@Override
	public void execute() {
		if (!NetworkSwitcher.isOnline(ctx)) {
			runAfterTasks(ctx);
			return;
		} else {
			if (runId == null)
				setRunId();
			if (runId != null && runId > 0) {
				syncronizeActions();
			}
		}
	}

	private void setRunId() {
		PropertiesAdapter pa = PropertiesAdapter.getInstance(ctx);
		runId = pa.getCurrentRunId();
		syncronizeActions();
	}

	private void syncronizeActions() {
		final PropertiesAdapter pa = PropertiesAdapter.getInstance(ctx);
		try {
			if (runId == null)
				return;
			final ActionList al = ActionClient.getActionClient().getRunActions(pa.getAuthToken(), runId);
			ActionsDelegator.getInstance().saveServerActionsToAndroidDb(ctx, al, pa.getFullId());
		} catch (ARLearnException ae) {
			ae.printStackTrace();
		}

	}

	@Override
	public void run(Context ctx) {
		this.ctx = ctx;
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		if (!nwHandler.hasMessages(NetworkTaskHandler.SYNC_ACTIONS)) {
			Message m = Message.obtain(nwHandler);
			m.obj = this;
			m.what = NetworkTaskHandler.SYNC_ACTIONS;
			m.sendToTarget();
		}

	}
}
