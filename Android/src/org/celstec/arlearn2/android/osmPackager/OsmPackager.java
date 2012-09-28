package org.celstec.arlearn2.android.osmPackager;
import java.io.File;

public class OsmPackager {
	
	
	//String  = "http://tile.openstreetmap.org/%d/%d/%d.png";
	public OsmPackager(double north, double east, double south, double west,
			String pBaseURL, File targetZipFile) {

	}

	public static OSMTileInfo getMapTileFromCoordinates(final double aLat,
			final double aLon, final int zoom) {
		final int y = (int) Math.floor((1 - Math.log(Math.tan(aLat * Math.PI
				/ 180)
				+ 1 / Math.cos(aLat * Math.PI / 180))
				/ Math.PI)
				/ 2 * (1 << zoom));
		final int x = (int) Math.floor((aLon + 180) / 360 * (1 << zoom));

		return new OSMTileInfo(x, y, zoom);
	}
}
