package org.celstec.arlearn2.android.asynctasks.db;

import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.ProximityEventRegistry;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;

import android.content.Context;
import android.os.Message;

public class ProcessProximityAlert extends GenericTask implements DatabaseTask {

	private long uniqueId;
	private Context ctx;

	@Override
	public void execute(DBAdapter db) {
		ProximityEventRegistry.ProximityEvent pe = db.getProximityEventRegistry().getProximityEventById(uniqueId);
		System.out.println("recording this as an action" + pe.id);
		if (pe != null) {
			PropertiesAdapter pa = new PropertiesAdapter(ctx);
			ActionsDelegator.getInstance().publishAction(ctx, "geo:" + pe.lat + ":" + pe.lng + ":" + pe.radius, pe.runId, pa.getUsername());
		}
	}

	@Override
	public void run(Context ctx) {
		this.ctx = ctx;
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = this;
		m.sendToTarget();
	}

	public long getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(long uniqueId) {
		this.uniqueId = uniqueId;
	}

}
