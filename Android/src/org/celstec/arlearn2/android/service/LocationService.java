package org.celstec.arlearn2.android.service;

import java.math.BigInteger;
import java.util.List;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.LocationUpdateConfig;
import org.celstec.arlearn2.beans.run.LocationUpdate;
import org.celstec.arlearn2.client.LocationClient;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationService extends IntentService {
	private static final int TWO_MINUTES = 1000 * 60 * 2;

	public LocationService() {
		super("LocationService");
	}

	private LocationManager locMgr;

	@Override
	public void onCreate() {
		super.onCreate();
		locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);

	}

	LocationListener unSensitiveListener = new LocationListener() {
		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}

		public void onLocationChanged(Location location) {
			System.out.println("location changed " + location.getLatitude()
					+ " " + location.getLongitude());
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	private boolean continueRunning = true;
	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getExtras() != null && intent.getExtras().getSerializable("bean") != null) {
			Config config = (Config) intent.getExtras().getSerializable("bean");
//			List<LocationUpdateConfig> list = config.getLocationUpdates();
//			long gcd = list.get(0).getTimeBetweenUpdates();
//			for (int i =1; i< list.size();i++){
//				gcd = gcdThing(gcd, list.get(i).getTimeBetweenUpdates());
//			}
			while (continueRunning) {
				// Log.i("service", "is still running");
				synchronized (this) {
					Location location = getBestLocation(this);
//							locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (location != null) {
						LocationUpdate locUpdate = new LocationUpdate();
						locUpdate.setLat(location.getLatitude());
						locUpdate.setLng(location.getLongitude());
						LocationClient.getLocationClient().postLocation(PropertiesAdapter.getInstance(this).getFusionAuthToken(),PropertiesAdapter.getInstance(this).getCurrentRunId(), locUpdate);
					}
					try {
						wait(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
//			if (locMgr != null) {
//				locMgr.removeUpdates(unSensitiveListener);
//				locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//						gcd,
//						Constants.DISTANCE_GPS_LISTENER_IN_METERES_UNSENSITIVE,
//						unSensitiveListener);
//			}
		}
		

	}
	
	public static Location getBestLocation(Context ctx) {
		Location loc = ((LocationManager) ctx.getSystemService(LOCATION_SERVICE)).getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location loc2 = ((LocationManager) ctx.getSystemService(LOCATION_SERVICE)).getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (loc == null) {
			loc = loc2;
		} else {
			if (loc2 != null) if (isBetterLocation(loc2, loc)) loc= loc2;
			
		}
		return loc;
	}
	
	private static  boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}
	
	private static boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
	
	private long gcdThing(long a, long b) {
	    BigInteger b1 = BigInteger.valueOf(a);
	    BigInteger b2 = BigInteger.valueOf(b);
	    return b1.gcd(b2).longValue();
	}

}
