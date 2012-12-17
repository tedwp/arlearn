package org.celstec.arlearn2.android.delegators;

import org.celstec.arlearn2.android.broadcast.task.ForceUpdateTask;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemVisibility;
import org.celstec.arlearn2.android.service.GeneralItemDependencyHandler;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

public class VisibleGeneralItemsDelegator {
	
	private static VisibleGeneralItemsDelegator instance;
	
	private VisibleGeneralItemsDelegator() {
		
	}
	
	public static VisibleGeneralItemsDelegator getInstance() {
		if (instance == null) {
			instance = new VisibleGeneralItemsDelegator();
		}
		return instance;
	}
	
	public void checkRoleAndMakeItemVisible(DBAdapter db, long runId, GeneralItem gi) {
		if (GeneralItemDependencyHandler.itemMatchesPlayersRole(db, runId, gi)) {
			makeItemVisible(db, runId, gi);
		}
	}
	
	public void makeItemVisible(DBAdapter db, long runId, GeneralItem gi) {
		Long appearAt = gi.getVisibleAt();
		if (appearAt == null || appearAt == -1) {
			appearAt = System.currentTimeMillis();
		}
		makeItemVisible(db, runId, gi, appearAt);

	}
	
	public void makeItemVisible(DBAdapter db, long runId, GeneralItem gi, long appearAt) {
		if (!GeneralItemVisibilityCache.getInstance().isVisible(runId, gi.getId())) {
//			if (!db.getGeneralItemVisibility().isVisible(itemId, runId)) {
			db.getGeneralItemVisibility().setVisibilityStatus(gi.getId(), runId, appearAt, GeneralItemVisibility.VISIBLE);
			long satisfiedAtDelta = System.currentTimeMillis() - appearAt;
			if (satisfiedAtDelta > 0 && satisfiedAtDelta < 30000) {
				GeneralItemDependencyHandler.broadcastTroughIntent(gi, db.getContext(), runId);
			} else if (satisfiedAtDelta < 0) {
				ForceUpdateTask.scheduleEvent(db.getContext(), runId, false, null);
			}
		}
	}
	
	
	


}
