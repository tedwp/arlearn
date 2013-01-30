package org.celstec.arlearn2.android.asynctasks.db;

import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.delegators.VisibleGeneralItemsDelegator;
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
	}

}
