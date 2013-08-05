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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GIActivitySelector;
import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.cache.ActionCache;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.DBAdapter.DatabaseHandler;
import org.celstec.arlearn2.android.db.GeneralItemVisibility;
import org.celstec.arlearn2.android.db.GeneralItemVisibility.VisibilityEvent;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GeneralItemVisibilityDelegator;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.delegators.VisibleGeneralItemsDelegator;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Action;
import org.json.JSONArray;

import android.media.SoundPool.OnLoadCompleteListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Message;
import android.os.Vibrator;

public class GeneralItemDependencyHandler extends GenericTask implements  DatabaseTask {

//	private Context ctx;
	
	public GeneralItemDependencyHandler() {
	}
	
	public void addTaskToQueue(Context ctx) {
		DatabaseHandler dbHandler = DBAdapter.getDatabaseThread(ctx);
		if (!dbHandler.hasMessages(DBAdapter.DEPENDENCIES_MESSAGE)) {
			Message m = Message.obtain(dbHandler);
			m.obj = this;
			m.what = DBAdapter.DEPENDENCIES_MESSAGE;
			dbHandler.sendMessageDelayed(m, 500);
		}
	}
	
	@Override
	public void execute(DBAdapter db) {
		
		checkDependencies(db);
		ActivityUpdater.updateActivities(db.getContext(), 
				ListMessagesActivity.class.getCanonicalName(), 
				MapViewActivity.class.getCanonicalName(), 
				ListMapItemsActivity.class.getCanonicalName(), 
				NarratorItemActivity.class.getCanonicalName());
	}
	
	

	public void checkDependencies(DBAdapter db) {
		checkDependencies(db, (new PropertiesAdapter(db.getContext())).getCurrentRunId());
	}

	public void checkDependencies(DBAdapter db, long runId) {
//		beep = (new PropertiesAdapter(db.getContext())).getCurrentRunId() == runId;
		List<Action> actions = ActionCache.getInstance().getActions(runId);
		if (actions != null) {
			retrieveUserRoles(db, runId);
			processItemsNotYetInitialised(db, runId);
			processItemsNotYetVisible(db, runId, actions);
			processItemsNotYetDisappeared(db, runId, actions);
		}
	}

	public void processItemsNotYetInitialised(DBAdapter db, final long runId) {
		VisibilityEvent[] events = db.getGeneralItemVisibility().query(runId, GeneralItemVisibility.NOT_INITIALISED);
//		TreeSet<GeneralItem> items = GeneralItemVisibilityCache.getInstance().getAllNotInitializedItems(runId);
		for (VisibilityEvent e: events) {
			GeneralItem gi = GeneralItemsDelegator.getInstance().getGeneralItem(e.itemId);
			if (!gi.isDeleted() && itemMatchesPlayersRole(db, runId, gi)) {
				GeneralItemVisibilityDelegator.getInstance().makeItemVisible(db, gi.getId(), runId, 0, GeneralItemVisibility.NOT_YET_VISIBLE);

			}
		}
//		if (items != null)
//			for (GeneralItem gi : items) {
//				if (itemMatchesPlayersRole(db, runId, gi)) {
//					Dependency dep = gi.getDependsOn();
//					DBAdapter.getAdapter(db.getContext()).getGeneralItemVisibility().setVisibilityStatus(gi.getId(), runId, 0, GeneralItemVisibility.NOT_YET_VISIBLE);
//				}
//			}
	}

	private static boolean containsRole(JSONArray userRoles, String role) {
		try {
			for (int i = 0; i < userRoles.length(); i++) {

				if (userRoles.get(i).equals(role))
					return true;
			}
		} catch (org.json.JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	private HashMap<String, String[]> accountRolesMap = new HashMap<String, String[]>();
	
	private void retrieveUserRoles(DBAdapter db, long runId) {
		String userRoles = db.getRunAdapter().queryRoles(runId);
		if (userRoles != null && !"".equals(userRoles)) {
			try {
				boolean playerHasRequiredRole = false;
				JSONArray userRolesJson = new JSONArray(userRoles);
				String[] currentUserRoles = new String[userRolesJson.length()];
				for (int i =0; i< currentUserRoles.length; i++) {
					currentUserRoles[i] = userRolesJson.getString(i);
				}
				;
				accountRolesMap.put(PropertiesAdapter.getInstance(db.getContext()).getFullId(), currentUserRoles);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
	}
	
	public static boolean itemMatchesPlayersRole(DBAdapter db, long runId, GeneralItem gi) {
		if (gi.getRoles() != null && !gi.getRoles().isEmpty()) {
			String userRoles = db.getRunAdapter().queryRoles(runId);
			if (userRoles != null && !"".equals(userRoles)) {
				try {
					boolean playerHasRequiredRole = false;
					JSONArray userRolesJson = new JSONArray(userRoles);
					for (String giRole : gi.getRoles()) {
						playerHasRequiredRole |= containsRole(userRolesJson, giRole);
					}
					return playerHasRequiredRole;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} 
		return true;
	}

	public void processItemsNotYetVisible(final DBAdapter db, final long runId, final List<Action> actions) {
		VisibilityEvent[] events = db.getGeneralItemVisibility().query(runId, GeneralItemVisibility.NOT_YET_VISIBLE);
		for (VisibilityEvent e: events) {
			GeneralItem generalItem = GeneralItemsDelegator.getInstance().getGeneralItem(e.itemId);
			if (generalItem != null && !generalItem.isDeleted()) {
				Dependency dep = generalItem.getDependsOn();
				long satisfiedAt = -1; //item not yet visible
				if (dep != null) {
					satisfiedAt = dep.satisfiedAt(actions, accountRolesMap); 
				} else {
					satisfiedAt = 0; //item supposed to visible from time 0
				}
				if (satisfiedAt != -1) { //if update is necessary
					GeneralItemVisibilityDelegator.getInstance().makeItemVisible(db, generalItem.getId(), runId, satisfiedAt, GeneralItemVisibility.VISIBLE);
				}
			}
		}
		events = db.getGeneralItemVisibility().query(runId, GeneralItemVisibility.VISIBLE, System.currentTimeMillis()); 
		for (VisibilityEvent e: events) {
			GeneralItem generalItem = GeneralItemsDelegator.getInstance().getGeneralItem(e.itemId);
			if (generalItem != null && !generalItem.isDeleted()) {
				Dependency dep = generalItem.getDependsOn();
				long satisfiedAt = dep.satisfiedAt(actions, accountRolesMap); 
				
				if (satisfiedAt < e.satisfiedAt) { 
					GeneralItemVisibilityDelegator.getInstance().makeItemVisible(db, generalItem.getId(), runId, satisfiedAt, GeneralItemVisibility.VISIBLE);
				}
			}
		}
	}

	public void processItemsNotYetDisappeared(final DBAdapter db, final long runId, final List<Action> actions) {
		VisibilityEvent[] visibleEvents = db.getGeneralItemVisibility().query(runId, GeneralItemVisibility.VISIBLE);
		VisibilityEvent[] noLongerVisibleEvents = db.getGeneralItemVisibility().query(runId, GeneralItemVisibility.NO_LONGER_VISIBLE);

//		Long[] itemIds = db.getGeneralItemVisibility().query(runId, GeneralItemVisibility.VISIBLE);
//		Long[] nolongVis = db.getGeneralItemVisibility().query(runId, GeneralItemVisibility.NO_LONGER_VISIBLE);
		ArrayList<VisibilityEvent> visible = new ArrayList<VisibilityEvent>();
		for (VisibilityEvent visEvent: visibleEvents) {
			visible.add(visEvent);
		}
		for (VisibilityEvent novisLong: noLongerVisibleEvents) {
			visible.remove(novisLong);
		}
		for (VisibilityEvent e: visible) {
//			if (!DBAdapter.getDatabaseThread(db.getContext()).hasMessages((int) itemId.longValue())) { //TODO make this safer
				final GeneralItem generalItem = GeneralItemsCache.getInstance().getGeneralItems(e.itemId); 
				if (generalItem == null || generalItem.isDeleted())
					return;
				Dependency dep = generalItem.getDisappearOn();
				long satisfiedAtTemp = -1;
				if (dep != null) {
					satisfiedAtTemp = dep.satisfiedAt(actions, accountRolesMap);
					final long satisfiedAt = satisfiedAtTemp;
					if (satisfiedAt != -1) {
						GeneralItemVisibilityDelegator.getInstance().makeItemVisible(db, e.itemId, runId, satisfiedAt, GeneralItemVisibility.NO_LONGER_VISIBLE);

//						ForceUpdateTask.scheduleEvent(db.getContext(), runId, false, null);
					}
				}
//			}
		}
	}
	
	

	

	@Override
	protected void run(Context ctx) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = this;
		m.sendToTarget();
		
	}

	
}
