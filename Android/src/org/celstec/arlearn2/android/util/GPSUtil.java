package org.celstec.arlearn2.android.util;

public class GPSUtil {
	public final static int METERS = 1;
	public final static int KILOMETERS = 2;

	public final static int MILES = 3;
	public final static int NAUTICAL = 4;

	public static double distance(double lat1, double lon1, double lat2, double lon2,
			int unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		switch (unit) {
		case METERS:
			dist = dist * 1.609344 * 1000;
			break;
		case KILOMETERS:
			dist = dist * 1.609344;
			break;
		case NAUTICAL:
			dist = dist * 0.8684;
			break;
		default:
			break;
		}

		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

}

