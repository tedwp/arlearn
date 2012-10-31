package org.celstec.arlearn2.android.broadcast.task;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemVisibility;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.service.GeneralItemDependencyHandler;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

public class MakeGeneralItemVisibleTask implements  DBAdapter.DatabaseTask {
	
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
		if (GeneralItemDependencyHandler.itemMatchesPlayersRole(db, runId, gi)) {
			db.getGeneralItemVisibility().setVisibilityStatus(gi.getId(), runId, System.currentTimeMillis(), GeneralItemVisibility.VISIBLE);
																									
		}
		(new GeneralItemDependencyHandler(db.getContext())).checkDependencies(db);
		ActivityUpdater.updateActivities(db.getContext(), 
				ListMessagesActivity.class.getCanonicalName(), 
				MapViewActivity.class.getCanonicalName(), 
				ListMapItemsActivity.class.getCanonicalName(), 
				NarratorItemActivity.class.getCanonicalName());
	}

}
