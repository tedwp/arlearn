package org.celstec.arlearn2.android.asynctasks.db;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemVisibility;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.service.GeneralItemDependencyHandler;
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
			db.getGeneralItemVisibility().setVisibilityStatus(gi.getId(), runId, disAt, GeneralItemVisibility.NO_LONGER_VISIBLE);
			ActivityUpdater.updateActivities(db.getContext(), 
					ListMessagesActivity.class.getCanonicalName(), 
					MapViewActivity.class.getCanonicalName(), 
					ListMapItemsActivity.class.getCanonicalName(), 
					NarratorItemActivity.class.getCanonicalName());
	}

}
