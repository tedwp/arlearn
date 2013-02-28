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

import java.util.Iterator;

import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.broadcast.NetworkSwitcher;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;

import org.celstec.arlearn2.beans.run.GeneralItemVisibility;
import org.celstec.arlearn2.beans.run.GeneralItemVisibilityList;
import org.celstec.arlearn2.client.GeneralItemVisibilityClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.Context;
import android.os.Message;
import android.util.Log;

public class SynchronizeGeneralItemVisiblityTask extends GenericTask implements NetworkTask {

	private Long runId;
	private Context ctx;

	@Override
	public void execute() {
		if (!NetworkSwitcher.isOnline(ctx)) {
			
//			Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
//			m.obj = new DBAdapter.DatabaseTask() {
//
//				@Override
//				public void execute(DBAdapter db) {
//					db.getGeneralItemVisibility().query(runId, org.celstec.arlearn2.android.db.GeneralItemVisibility.VISIBLE);
//					db.getGeneralItemVisibility().query(runId, org.celstec.arlearn2.android.db.GeneralItemVisibility.NO_LONGER_VISIBLE);
//				}
//			};
//			m.sendToTarget();
			runAfterTasks(ctx);
			return;
		} else {
			PropertiesAdapter pa = PropertiesAdapter.getInstance(ctx);
			Long lastDate = PropertiesAdapter.getInstance(ctx).getGeneralItemsVisibilityLastSynchronizationDate(runId);
			try {
				GeneralItemVisibilityList viList = GeneralItemVisibilityClient.getGeneralItemClient().getGeneralItemVisibilities(pa.getFusionAuthToken(), runId, lastDate);
				final GeneralItemVisibilityList finalViList = viList;
				if (viList.getErrorCode() != null) {
					return;
				}
				PropertiesAdapter.getInstance(ctx).setGeneralItemsVisibilityLastSynchronizationDate(finalViList.getServerTime(), runId);

				Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
				m.obj = new DBAdapter.DatabaseTask() {

					@Override
					public void execute(DBAdapter db) {
						Iterator<GeneralItemVisibility> it = finalViList.getGeneralItemsVisibility().iterator();
						while (it.hasNext()) {
							GeneralItemVisibility visItem = it.next();
							if (visItem != null) {
								db.getGeneralItemVisibility().setVisibilityStatus(visItem.getGeneralItemId(), getRunId(), visItem.getTimeStamp(), visItem.getStatus());
								// generalItemToDb(db, item);
							}
						}
						runAfterTasks(ctx);

					}
				};
				m.sendToTarget();
			} catch (ARLearnException ae) {
				ae.printStackTrace();
			}
		}
	}

	@Override
	public void run(Context ctx) {
		this.ctx = ctx;
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		Message m = Message.obtain(nwHandler);
		m.obj = this;
		m.sendToTarget();
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

}
