package org.celstec.arlearn2.android.broadcast.task;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemVisibility;
import org.celstec.arlearn2.android.delegators.VisibleGeneralItemsDelegator;
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
		VisibleGeneralItemsDelegator.getInstance().checkRoleAndMakeItemVisible(db, runId, gi);
		
		
		//TODO remove
//		if (GeneralItemDependencyHandler.itemMatchesPlayersRole(db, runId, gi)) {
//			boolean wasVisible = GeneralItemVisibilityCache.getInstance().isVisible(runId, gi.getId());
//			Long appearAt = gi.getVisibleAt();
//			if (appearAt == null || appearAt == -1) {
//				appearAt = System.currentTimeMillis();
//			}
////			GeneralItemDependencyHandler depHandler= new GeneralItemDependencyHandler();
//			if (!wasVisible) {
//				db.getGeneralItemVisibility().setVisibilityStatus(gi.getId(), runId, appearAt, GeneralItemVisibility.VISIBLE);
//				ForceUpdateTask.scheduleEvent(db.getContext(), runId, false, null);
//
////				depHandler.broadcastTroughIntent(gi, db.getContext(), runId);
//			}
////			depHandler.addTaskToQueue(db.getContext());
//		}
		

	}

}
