/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
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
