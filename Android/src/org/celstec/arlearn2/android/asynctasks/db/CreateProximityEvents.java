package org.celstec.arlearn2.android.asynctasks.db;

import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.broadcast.ProximityIntentReceiver;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.ProximityEventRegistry;
import org.celstec.arlearn2.android.db.ProximityEventRegistry.ProximityEvent;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.dependencies.ProximityDependency;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Message;

public class CreateProximityEvents extends GenericTask implements  DatabaseTask {

	private static final long defaultExpireTime = 7200000l;
	
	private Context ctx;
	private long runId;
	private long gameId;
	
	public CreateProximityEvents(Context ctx, long runId, long gameId) {
		this.ctx = ctx;
		this.runId = runId;
		this.gameId = gameId;
	}
	
	@Override
	public void execute(DBAdapter db) {
		for (GeneralItem item :GeneralItemsCache.getInstance().getGeneralItemsWithGameId(gameId).values()) {
			if (item.getDependsOn() != null) {
				Dependency dep = item.getDependsOn();
				if (dep instanceof ProximityDependency) {
					System.out.println("todo deal with "+dep);
					ProximityDependency proxDep = (ProximityDependency) dep;
					addProximityAlert(db, proxDep.getLat(), proxDep.getLng(), proxDep.getRadius());
				}
			}
		}
		
	}
	
	 private void addProximityAlert(DBAdapter db, double latitude, double longitude, int radius) {
		 ProximityEventRegistry.ProximityEvent[] pes = db.getProximityEventRegistry().getProximityEvent(latitude, longitude, radius, runId);
		 boolean updateNecessary = false;
		 ProximityEvent pe = new ProximityEventRegistry.ProximityEvent();
		 if (pes.length == 0) {
			 updateNecessary = true;
			 pe.lat =latitude;
			 pe.lng = longitude;
			 pe.runId = runId;
			 pe.radius = radius;
			 pe.expires = System.currentTimeMillis() + defaultExpireTime;
			 db.getProximityEventRegistry().createProximityEvent(pe);
		 } else {
			 if (pes[0].expires < System.currentTimeMillis()) {
				 pe = pes[0];
				 pe.expires = System.currentTimeMillis() + defaultExpireTime;
				 updateNecessary = true;
				 db.getProximityEventRegistry().createProximityEvent(pe);

			 }
		 }
		 if (updateNecessary) {
	         Intent intent = new Intent(ctx, ProximityIntentReceiver.class);
	         intent.putExtra(ProximityIntentReceiver.ID, pe.id);
	         PendingIntent proximityIntent = PendingIntent.getBroadcast(ctx, (int) pe.id , intent, 0);
	         LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

	         locationManager.removeProximityAlert(proximityIntent);
	         locationManager.addProximityAlert(latitude, longitude, radius, pe.expires - System.currentTimeMillis(), proximityIntent );
			 
		 }

//         IntentFilter filter = new IntentFilter(proxIntentAction); 
//         ctx.registerReceiver(new ProximityIntentReceiver(), filter);

     }

	@Override
	protected void run(Context ctx) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = this;
		m.sendToTarget();
	}

}
