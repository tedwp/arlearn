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
package org.celstec.arlearn2.android.asynctasks.db;

import java.util.TreeSet;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemVisibility;
import org.celstec.arlearn2.android.db.GeneralItemVisibility.VisibilityEvent;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;

import android.content.Context;
import android.os.Message;

public class ForceUpdateTask implements DBAdapter.DatabaseTask {

	private Context ctx;
	private long runId;
	private boolean update;
	private TreeSet<GeneralItemVisibility.VisibilityEvent> futureEvents;

	@Override
	public void execute(DBAdapter db) {
		if (futureEvents != null) {
			GeneralItemVisibility.VisibilityEvent nextVisibleEvent = null;
			do {
				GeneralItemVisibility.VisibilityEvent event = futureEvents.first();
				if (event.satisfiedAt < System.currentTimeMillis() && event.status == GeneralItemVisibility.VISIBLE) {
					nextVisibleEvent = event;
				}
				futureEvents.remove(event);
			} while (nextVisibleEvent == null && !futureEvents.isEmpty());
			if (nextVisibleEvent != null) {
				GeneralItemDependencyHandler.broadcastTroughIntent(GeneralItemsCache.getInstance().getGeneralItems(nextVisibleEvent.itemId), ctx, runId);
				update = false;
			}
			
		}
		if (update) {
			ActivityUpdater.updateActivities(ctx, 
					ListMessagesActivity.class.getCanonicalName(), 
					MapViewActivity.class.getCanonicalName(), 
					ListMapItemsActivity.class.getCanonicalName(),
					NarratorItemActivity.class.getCanonicalName());
			

		}
		TreeSet<GeneralItemVisibility.VisibilityEvent>  events = db.getGeneralItemVisibility().getNextEvent(db, runId);
		if (!events.isEmpty()) {
			scheduleEvent(ctx, runId, true, events);
		}
		
		

	}
	
	public static void scheduleEvent(Context ctx, long runId, boolean update, TreeSet<GeneralItemVisibility.VisibilityEvent>  events) {
		ForceUpdateTask fut = new ForceUpdateTask();
		fut.ctx= ctx;
		fut.runId = runId;
		fut.update = update;
		fut.futureEvents = events;
		Message m = Message.obtain();
		m.what = DBAdapter.FORCE_UPDATE_MESSAGE;
		m.obj = fut;
		if ( events != null && !events.isEmpty()){
			long delay = (events.first().satisfiedAt- System.currentTimeMillis()  );
			if (delay < 500) {
				DBAdapter.getDatabaseThread(ctx).sendMessage(m);
			} else {
				DBAdapter.getDatabaseThread(ctx).sendMessageDelayed(m, delay)	;
			}
			
		} else {
			DBAdapter.getDatabaseThread(ctx).removeMessages(DBAdapter.FORCE_UPDATE_MESSAGE);
			DBAdapter.getDatabaseThread(ctx).sendMessage(m);
		}
		
	}
}
