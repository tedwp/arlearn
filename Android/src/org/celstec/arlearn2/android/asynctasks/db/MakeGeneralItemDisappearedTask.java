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

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemVisibility;
import org.celstec.arlearn2.android.delegators.GeneralItemVisibilityDelegator;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

public class MakeGeneralItemDisappearedTask implements  DBAdapter.DatabaseTask {
	
	private GeneralItem gi;
	private Long runId;

	public GeneralItem getGi() {
		return gi;
	}

	public void setGi(GeneralItem gi) {
		this.gi = gi;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	@Override
	public void execute(DBAdapter db) {
//			GeneralItemDependencyHandler depHandler= new GeneralItemDependencyHandler();
			Long disAt = gi.getDisappearAt();
			if (disAt == null || disAt == -1) {
				disAt = System.currentTimeMillis();
			}
			GeneralItemVisibilityDelegator.getInstance().makeItemVisible(db, gi.getId(), runId, disAt, GeneralItemVisibility.NO_LONGER_VISIBLE);

			ActivityUpdater.updateActivities(db.getContext(), 
					ListMessagesActivity.class.getCanonicalName(), 
					MapViewActivity.class.getCanonicalName(), 
					ListMapItemsActivity.class.getCanonicalName(), 
					NarratorItemActivity.class.getCanonicalName());
	}

}
