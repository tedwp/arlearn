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


public class DownloadTiles {

	private final DownloadManager dm;
	private final double aLatUp; // latitude up
	private final double aLonLeft; // longitude left
	private final double aLatDown; // latitude down
	private final double aLonRight; // longitude right
	
	
	// ===========================================================
	// Constructors
	// ===========================================================

	
	public DownloadTiles(String folderName, final double aLatUp, final double aLonLeft, final double aLatDown, final double aLonRight) {
		this.dm = this.createDownloadManagerObj (folderName);
		this.aLatUp = aLatUp;
		this.aLonLeft = aLonLeft;
		this.aLatDown = aLatDown;
		this.aLonRight = aLonRight;
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	
	public final DownloadManager createDownloadManagerObj (String folderName){
		int pThreadCount = 2;
	    System.out.println(DownloadTiles.getMapTileFromCoordinates(50, 6, 5));
	    String pFileAppendix = "";
	    String pBaseURL = "http://tile.openstreetmap.org/%d/%d/%d.png";
	    final String pTempBaseURL = folderName
	            + File.separator + "%d"
	            + File.separator + "%d"
	            + File.separator + "%d"
	            + pBaseURL.substring(pBaseURL.lastIndexOf('.'))
	            + pFileAppendix
	            .replace(File.separator + File.separator, File.separator);
	    
	    final DownloadManager dm = new DownloadManager(pBaseURL, pTempBaseURL, pThreadCount);
	    return dm;
		}
	public void getTile(int zoom)
	{			
		final OSMTileInfo upperLeft = getMapTileFromCoordinates(aLatUp, aLonLeft, zoom);
        final OSMTileInfo lowerRight = getMapTileFromCoordinates(aLatDown, aLonRight, zoom);
        for(int y = upperLeft.y; y <= lowerRight.y; y++){
        	System.out.println("y "+y+" "+upperLeft.x);
            dm.add(new OSMTileInfo(upperLeft.x,y,zoom));
        }
	}// end getTile
	
	public void getTilesFromZoomMinMax(int zoomMin, int zoomMax)
	{			
		for (int i = zoomMin; i<=zoomMax; i++)this.getTile(i);
		try {
			dm.waitEmpty();
			System.out.println(" done.");
		} catch (final InterruptedException e) {
	    e.printStackTrace();
		}


		try {
			System.out.print("Awaiting termination of all threads ...");
			dm.waitFinished();
			System.out.println(" done.");
		} catch (final InterruptedException e) {
	    e.printStackTrace();
	}
        
	}// end getTilesFromZoomMinMax
	
	
	public static OSMTileInfo getMapTileFromCoordinates(final double aLat, final double aLon, final int zoom) {
	final int y = (int) Math.floor((1 - Math.log(Math.tan(aLat * Math.PI / 180) + 1 / Math.cos(aLat * Math.PI / 180)) / Math.PI) / 2 * (1 << zoom));
	final int x = (int) Math.floor((aLon + 180) / 360 * (1 << zoom));

	return new OSMTileInfo(x, y, zoom);
	}
	

}
