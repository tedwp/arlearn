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

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.broadcast.NetworkSwitcher;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.client.ActionClient;

import android.content.Context;
import android.os.Message;

public class PublishActionTask implements NetworkTask {

	private Action action;
	private Context ctx;

	public PublishActionTask(Context ctx, Action action) {
		this.action = action;
		this.ctx = ctx;
	}

	public void addTaskToQueue(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		Message m = Message.obtain(nwHandler);
		m.obj = this;
		m.what = NetworkTaskHandler.PUBLISH_ACTION;
		m.sendToTarget();
	}

	@Override
	public void execute() {
		if (!NetworkSwitcher.isOnline(ctx)) {
			ActionsDelegator.getInstance().replicationFailed(ctx, action);
		} else {
			ActionClient ac = ActionClient.getActionClient();
			Action result = ac.publishAction(PropertiesAdapter.getInstance(ctx).getAuthToken(), action);
			result.setTime(action.getTime());
			if (result.getError() == null) {
				ActionsDelegator.getInstance().confirmReplicated(ctx, result);
			} else {
				if (result.getError() != null && "User not found".equals(result.getError())) {
					// DBAdapter.getAdapter(ctx).getMyActions().confirmReplicated(result);
					// this is not elegant... but its mean that the user was
					// deleted, so don't try to sync in future
					ActionsDelegator.getInstance().confirmReplicated(ctx, action);
				} else if (result.getError() != null && "exception null".equals(result.getError())) {
					// no network
					ActionsDelegator.getInstance().replicationFailed(ctx, action);

				}

				
			}
		}
	}
}
