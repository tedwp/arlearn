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
package org.celstec.arlearn2.android.delegators;

import org.celstec.arlearn2.android.asynctasks.db.GeneralItemDependencyHandler;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemVisibility;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

@Deprecated // does not seem to be used
public class VisibleGeneralItemsDelegator {
	
//	private static VisibleGeneralItemsDelegator instance;
//	
//	private VisibleGeneralItemsDelegator() {
//		
//	}
//	
//	public static VisibleGeneralItemsDelegator getInstance() {
//		if (instance == null) {
//			instance = new VisibleGeneralItemsDelegator();
//		}
//		return instance;
//	}
//	
//	public void checkRoleAndMakeItemVisible(DBAdapter db, long runId, GeneralItem gi) {
//		if (GeneralItemDependencyHandler.itemMatchesPlayersRole(db, runId, gi)) {
//			makeItemVisible(db, runId, gi);
//		}
//	}
//	
//	public void makeItemVisible(DBAdapter db, long runId, GeneralItem gi) {
//		Long appearAt = gi.getVisibleAt();
//		if (appearAt == null || appearAt == -1) {
//			appearAt = System.currentTimeMillis();
//		}
//		makeItemVisible(db, runId, gi, appearAt);
//
//	}
//	
//	public void makeItemVisible(DBAdapter db, long runId, GeneralItem gi, long appearAt) {
////		if (!GeneralItemVisibilityCache.getInstance().isVisible(runId, gi.getId())) {
//		//if ("323001*309001".equals(getKey(runId, itemId))) {
////		if (runId == 323001l && gi.getId().equals(319001l) ) {
////			System.out.println("break");
////		}
//		if (!db.getGeneralItemVisibility().isVisible(gi.getId(), runId)) {
//			db.getGeneralItemVisibility().setVisibilityStatus(gi.getId(), runId, appearAt, GeneralItemVisibility.VISIBLE);
//			
//			long satisfiedAtDelta = System.currentTimeMillis() - appearAt;
//			if (satisfiedAtDelta > 0 && satisfiedAtDelta < 30000) {
////				Gene	ralItemDependencyHandler.broadcastTroughIntent(gi, db.getContext(), runId);
//			} else if (satisfiedAtDelta < 0) {
////				ForceUpdateTask.scheduleEvent(db.getContext(), runId, false, null);
//			}
//		}
//	}
	
	
	


}
