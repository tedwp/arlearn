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
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.client.GeneralItemClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.Context;
import android.os.Message;

public class SynchronizeGeneralItemsTask extends GenericTask implements NetworkTask {

	private Long gameId;
	private Context ctx;

	@Override
	public void execute() {
		if (NetworkSwitcher.isOnline(ctx)) {
			PropertiesAdapter pa = PropertiesAdapter.getInstance(ctx);
			Long lastDate = PropertiesAdapter.getInstance(ctx).getGeneralItemLastSynchronizationDate(gameId) - 120000;

			try {
				GeneralItemList gl = null;
				if (lastDate <= 0) {
					gl = GeneralItemClient.getGeneralItemClient().getGameGeneralItems(pa.getFusionAuthToken(), gameId);

				} else {
					gl = GeneralItemClient.getGeneralItemClient().getGameGeneralItems(pa.getFusionAuthToken(), gameId, lastDate);
				}
				final GeneralItemList finalGl = gl;
				if (gl.getError() != null || gl.getErrorCode() != null) {
					return;
				}
				PropertiesAdapter.getInstance(ctx).setGeneralItemsLastSynchronizationDate(gl.getServerTime(), gameId);

				Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
				m.obj = new DBAdapter.DatabaseTask() {

					@Override
					public void execute(DBAdapter db) {
						Iterator<GeneralItem> it = finalGl.getGeneralItems().iterator();
						while (it.hasNext()) {
							GeneralItem item = it.next();
							if (item != null) {
								generalItemToDb(db, item);
							}
						}
						GeneralItemsCache.getInstance().put(db.getGeneralItemAdapter().queryByGameId(gameId));
						runAfterTasks(ctx);
					}

					protected void generalItemToDb(DBAdapter db, GeneralItem item) {
						boolean newInsert = db.getGeneralItemAdapter().insert(item);
						if (newInsert) {
							db.getGeneralItemVisibility().deleteGeneralItem(item.getId());
						}
						GeneralItemsDelegator.getInstance().createDownloadTasks(db.getContext(), item);
					}
				};
				m.sendToTarget();
			} catch (ARLearnException ae) {
				ae.printStackTrace();
			}
		} else {
			runAfterTasks(ctx);
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

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

}
