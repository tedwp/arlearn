package org.celstec.arlearn2.android.osmPackager;

import java.io.File;
import java.util.Vector;

import org.celstec.arlearn2.beans.game.MapRegion;

public class RegionDownloader  {

	private Vector<MapRegion> regions; //array of regions
	
	public RegionDownloader() {
		regions = new Vector();
	}
	
	public void addRegion(MapRegion region) {
		regions.add(region);

	}
	public void deleteRegion(MapRegion region) {
		regions.remove(region);
	}
	
	private void downloadRegion(MapRegion region, String folderName,int zoom, DownloadManager dm) {			
		final OSMTileInfo upperLeft = getMapTileFromCoordinates(region.getLatUp(), region.getLngLeft(), zoom);
        final OSMTileInfo lowerRight = getMapTileFromCoordinates(region.getLatDown(), region.getLngRight(), zoom);
        for(int y = upperLeft.y; y <= lowerRight.y; y++){
            for(int x = upperLeft.x; x <= lowerRight.x; x++){
            	dm.add(new OSMTileInfo(x,y,zoom));
            }
        }
	}
	
	private void downloadRegionZoomMinMax(MapRegion region, String folderName, DownloadManager dm)
	{			
		for(int i = region.getMinZoom(); i<= region.getMaxZoom(); i++) downloadRegion(region, folderName, i, dm);
		
	}
	
	public void downloadAllRegions(String folderName) {
		for (MapRegion r : regions) {
			DownloadManager dm = createDownloadManagerObj (folderName);
			downloadRegionZoomMinMax(r, folderName, dm);
		}
	}
	
	public final DownloadManager createDownloadManagerObj (String folderName){
	int pThreadCount = 2;
    System.out.println(getMapTileFromCoordinates(50, 6, 5));
    String pFileAppendix = "";
    String pBaseURL = "http://tile.openstreetmap.org/%d/%d/%d.png";
    final String pTempBaseURL = folderName
            + File.separator + "%d"
            + File.separator + "%d"
            + File.separator + "%d"
            + pBaseURL.substring(pBaseURL.lastIndexOf('.'))
            + pFileAppendix
            .replace(File.separator + File.separator, File.separator);
    //System.out.println("variable = "+pTempBaseURL);
    
    final DownloadManager dm = new DownloadManager(pBaseURL, pTempBaseURL, pThreadCount);
    return dm;
	}
	
	
	
	public static OSMTileInfo getMapTileFromCoordinates(final double aLat, final double aLon, final int zoom) {
	final int y = (int) Math.floor((1 - Math.log(Math.tan(aLat * Math.PI / 180) + 1 / Math.cos(aLat * Math.PI / 180)) / Math.PI) / 2 * (1 << zoom));
	final int x = (int) Math.floor((aLon + 180) / 360 * (1 << zoom));

	return new OSMTileInfo(x, y, zoom);
	}
		
}
	


