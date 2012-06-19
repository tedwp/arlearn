package org.celstec.arlearn2.android.service;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.notificationbeans.GeneralItem;
import org.celstec.arlearn2.android.db.notificationbeans.LocationUpdate;
import org.celstec.arlearn2.android.db.notificationbeans.NotificationBean;
import org.celstec.arlearn2.android.db.notificationbeans.UpdateScore;
import org.celstec.arlearn2.android.sync.GameSyncroniser;
import org.celstec.arlearn2.android.sync.GeneralItemsSyncroniser;
import org.celstec.arlearn2.android.sync.MediaCacheSyncroniser;
import org.celstec.arlearn2.android.sync.MyActionsSyncronizer;
import org.celstec.arlearn2.android.sync.MyLocationSyncronizer;
import org.celstec.arlearn2.android.sync.MyResponseSyncronizer;
import org.celstec.arlearn2.android.sync.RunSyncroniser;
import org.celstec.arlearn2.android.sync.ScoreSyncroniser;
import org.celstec.arlearn2.beans.Version;
import org.celstec.arlearn2.client.GenericClient;
import org.celstec.arlearn2.client.VersionClient;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class BackgroundService extends IntentService {

	private boolean continueRunning = true;
	private LocationManager locMgr;
	RunSyncroniser run;
	GameSyncroniser game;
	GeneralItemsSyncroniser gis;
	MyLocationSyncronizer loc;
	MyResponseSyncronizer resp;
	MyActionsSyncronizer act;
	MediaCacheSyncroniser mcs;
	ScoreSyncroniser scs;

	DependencyChecker depCheck;
	
	public BackgroundService() {
		super("BackgroundService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (locMgr != null)
			locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.TIME_BETWEEN_GPS_UPDATES_UNSENSITIVE, Constants.DISTANCE_GPS_LISTENER_IN_METERES_UNSENSITIVE, unSensitiveListener);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (locMgr != null)
			locMgr.removeUpdates(unSensitiveListener);
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent.getExtras() != null && intent.getExtras().getBoolean("exit")) {
			continueRunning = false;
			Toast.makeText(this, "stopping background services", Toast.LENGTH_SHORT).show();
		} else if (intent.getExtras() != null && intent.getExtras().getBoolean("depCheck")){
			if (depCheck != null)
				depCheck.run();
		} else{
			if (gis != null)
				gis.run();
//			resetTimes();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	protected void resetTimes() {
		if (run != null) run.resetDelay();
		if (game != null) game.resetDelay();
		if (gis != null) {
			gis.resetDelay();
			gis.run();
		}
//		if (loc != null) loc.resetDelay();
		if (resp != null) resp.resetDelay();
		if (act != null) act.resetDelay();
		if (mcs != null) mcs.resetDelay();
		if (depCheck != null) depCheck.resetDelay();
		if (scs != null) scs.resetDelay();
	}

	protected void onHandleIntent(Intent intent) {
		checkServerUrl();
//		run = new RunSyncroniser(this);
		game = new GameSyncroniser(this);
//		gis = new GeneralItemsSyncroniser(this);
		resp = new MyResponseSyncronizer(this);
		act = new MyActionsSyncronizer(this);
		mcs = new MediaCacheSyncroniser(this);
		depCheck = new DependencyChecker(this);
		scs = new ScoreSyncroniser(this);
		int time = 0;
		while (continueRunning) {
			// Log.i("service", "is still running");
			synchronized (this) {
				try {
//					if (run.timeToExecute(time))
//						run.run();
					if (game.timeToExecute(time))
						game.run();
//					if (gis.timeToExecute(time))
//						gis.run();
//					if (loc.timeToExecute(time))
//						loc.run();
					if (resp.timeToExecute(time))
						resp.run();
					if (act.timeToExecute(time))
						act.run();
					if (mcs.timeToExecute(time))
						mcs.run();
					if (depCheck.timeToExecute(time))
						depCheck.run();
					if (scs.timeToExecute(time))
						scs.run();
					time += 1;
					wait(40000);
				} catch (InterruptedException e) {
					Log.e("interrupt", e.getMessage(), e);
				}
			}
		}
	}
	
	private void checkServerUrl() {
		try {
			PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			int versionNumber = pinfo.versionCode;
//			Version v = VersionClient.getVersionClient().getVersionInfo(versionNumber);
//			if (v != null) {
//				GenericClient.setUrlPrefix(v.getServiceUrl());
//				System.out.println(v.getServiceUrl());
//
//			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	LocationListener unSensitiveListener = new LocationListener() {
		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}

		public void onLocationChanged(Location location) {
			DBAdapter db = new DBAdapter(BackgroundService.this);
			db.openForWrite();
			org.celstec.arlearn2.beans.run.Location locBean = new org.celstec.arlearn2.beans.run.Location();
			PropertiesAdapter pa = new PropertiesAdapter(BackgroundService.this);
			String account = pa.getUsername();
			if (pa != null) {
				locBean.setAccount(pa.getUsername());
				locBean.setLat(location.getLatitude());
				locBean.setLng(location.getLongitude());
				locBean.setTimestamp(location.getTime());
				db.table(DBAdapter.MYLOCATIONS_ADAPTER).insert(locBean);
			}
			db.close();
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

}
