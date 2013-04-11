package org.celstec.arlearn2.android.db;

import org.celstec.arlearn2.android.broadcast.ProximityIntentReceiver;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;

public class ProximityEventRegistry extends GenericDbTable {

	public ProximityEventRegistry(DBAdapter db) {
		super(db);
	}

	public static final String PROXIMITY_EVENT_TABLE = "proximityEventRegistry";
	public static final String ID = "id";
	public static final String LAT = "lat";
	public static final String LNG = "lng";
	public static final String RADIUS = "radius";
	public static final String RUNID = "runId";

	public static final String EXPIRES = "expires";

	@Override
	public String createStatement() {
		return "create table " + PROXIMITY_EVENT_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LAT + " long, " + LNG + " long, " + RADIUS + " long , " + RUNID + " long ," + EXPIRES + " long);";
	}

	@Override
	protected String getTableName() {
		return PROXIMITY_EVENT_TABLE;
	}

	public void createProximityEvent(ProximityEvent pe) {
		ProximityEvent[] existingEvent = getProximityEvent(pe.lat, pe.lng, pe.radius, pe.runId);
		ContentValues initialValues = new ContentValues();
		initialValues.put(EXPIRES, pe.expires);
		if (existingEvent.length == 0) {
			initialValues.put(LAT, ((long)(pe.lat*1000000)));
			initialValues.put(LNG, ((long)(pe.lng*1000000)));
			initialValues.put(RUNID, pe.runId);
			initialValues.put(RADIUS, pe.radius);
			pe.id = db.getSQLiteDb().insert(getTableName(), null, initialValues);
		} else {
			db.getSQLiteDb().update(getTableName(), initialValues, LAT + " = ? and " + LNG + " = ? and " + RADIUS + " = ? and " + RUNID + " = ?", new String[] { "" +  ((long)(pe.lat*1000000)), "" + ((long)(pe.lng*1000000)), "" + pe.radius, "" + pe.runId });
		}
	}

	public ProximityEvent[] getProximityEvent(double lat, double lng, long radius, long runId) {
		Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), new String[] { ID, EXPIRES }, LAT + " = ? and " + LNG + " = ? and " + RADIUS + " = ? and " + RUNID + " = ?", new String[] { "" + ((long)(lat*1000000)), "" + ((long)(lng*1000000)), "" + radius, "" + runId }, null, null, null, null);
		ProximityEvent[] result = new ProximityEvent[mCursor.getCount()];
		int i = 0;
		while (mCursor.moveToNext()) {
			ProximityEvent pe = new ProximityEvent();
			pe.id = mCursor.getLong(0);
			pe.expires = mCursor.getLong(1);
			pe.lat = lat;
			pe.lng = lng;
			pe.radius = radius;
			pe.runId = runId;
			result[i++] = pe;
		}
		mCursor.close();
		return result;
	}
	
	public ProximityEvent getProximityEventById(long id) {
		Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), new String[] { ID, EXPIRES, LAT, LNG, RADIUS, RUNID }, ID + " = ?", new String[] { "" + id }, null, null, null, null);
		try {
		if (mCursor.moveToNext()) {
			ProximityEvent pe = new ProximityEvent();
			pe.id = mCursor.getLong(0);
			pe.expires = mCursor.getLong(1);
			pe.setLat(mCursor.getLong(2));
			pe.setLng(mCursor.getLong(3));
			pe.radius = mCursor.getLong(4);
			pe.runId = mCursor.getLong(5);
			return pe;
		}
		}finally {
			mCursor.close();	
		}
		
		return null;
	}

	private ProximityEvent[] getProximityEvent(long runId) {
		Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), new String[] { ID, EXPIRES }, RUNID + " = ?", new String[] { "" + runId }, null, null, null, null);
		ProximityEvent[] result = new ProximityEvent[mCursor.getCount()];
		int i = 0;
		while (mCursor.moveToNext()) {
			ProximityEvent pe = new ProximityEvent();
			pe.id = mCursor.getLong(0);
			pe.expires = mCursor.getLong(1);
			pe.runId = runId;
			result[i++] = pe;
		}
		mCursor.close();
		return result;
	}

	public int deleteRun(long runId) {
		Intent intent = new Intent(db.getContext(), ProximityIntentReceiver.class);
		LocationManager locationManager = (LocationManager) db.getContext().getSystemService(Context.LOCATION_SERVICE);
		for (ProximityEvent event : getProximityEvent(runId)) {
			PendingIntent proximityIntent = PendingIntent.getBroadcast(db.getContext(), (int) event.id, intent, 0);
			locationManager.removeProximityAlert(proximityIntent);
		}
		return db.getSQLiteDb().delete(getTableName(), RUNID + " = " + runId, null);

	}

	public static class ProximityEvent {
		public long id;
		public double lat;
		public double lng;
		public long radius;
		public long runId;
		public long expires;
		
		public void setLat(long lat) {
			this.lat = ((double)lat)/1000000;
		}
		
		public void setLng(long lng) {
			this.lng = ((double)lng)/1000000;
		}
		
		
	}

}
