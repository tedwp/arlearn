package org.celstec.arlearn2.android.db.notificationbeans;

import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.sync.GameSyncroniser;
import org.celstec.arlearn2.android.sync.GeneralItemsSyncroniser;
import org.celstec.arlearn2.android.sync.RunSyncroniser;

import android.content.Context;

public class Run extends NotificationBean {

	private Boolean created;
	private Boolean deleted;
	private String action;
	private Long runId;
	
	public Boolean getCreated() {
		return created;
	}
	public void setCreated(Boolean created) {
		this.created = created;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Long getRunId() {
		return runId;
	}
	public boolean requiresBroadcast() {
		return true;
	}
	
	public void setRunId(Long runId) {
		this.runId = runId;
	}
	
	public void run(Context ctx) {
		if (getDeleted() != null && getDeleted()) {
			deleteRunFromDatabase(ctx);
		}
		if (getCreated() != null && getCreated()) {
			readRunFromServer(ctx);
		}
	}
	private void readRunFromServer(Context ctx) {
		RunSyncroniser rs = new RunSyncroniser(ctx);
		rs.runAuthenticated();
		GameSyncroniser gs = new GameSyncroniser(ctx);
		gs.runAuthenticated();
	}
	
	private void deleteRunFromDatabase(Context ctx) {
		DBAdapter db = new DBAdapter(ctx);
		db.openForWrite();
		db.table(DBAdapter.RUN_ADAPTER).delete(getRunId());
		db.close();	
		PropertiesAdapter pa = new PropertiesAdapter(ctx);
		pa.setRunStart(getRunId(), 0);
		pa.setTotalScore(0);
		if (pa.getCurrentRunId() == getRunId()) pa.setCurrentRunId(0l);

	}
	
}
