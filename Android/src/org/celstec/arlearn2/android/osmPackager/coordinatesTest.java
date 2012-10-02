package org.celstec.arlearn2.android.osmPackager;

import android.text.GetChars;

public class coordinatesTest {
	
	public static void main(String[] args) {
//		System.out.println((1<<1));
//		System.out.println((1<<2));
//		System.out.println((1<<3));
//		System.out.println((1<<4));
//		System.out.println((1<<5));
//		System.out.println((1<<6));
//		System.out.println((1<<7));
//		System.out.println((1<<8));
//		
//		System.out.println((3<<1));
//		System.out.println((3<<2));
//		System.out.println((3<<3));
//		System.out.println((3<<4));
//		System.out.println((3<<5));
//		System.out.println((3<<6));
		
		for (int i = -90; i <= 90; i+=10) {
			System.out.println(" .. "+getMapTileFromCoordinates(i, 5, 8).y);	
		}
//		System.out.println("DD "+getLon(33807, 16));
		
//		System.out.println(" .. "+getMapTileFromCoordinates(50, 168.75, 8).x);
	}
	
	public static double getLon(int x, int zoom) {
		double divis = ((double)x)/(1<<zoom);
		return divis*360 -180;
	}

	public static OSMTileInfo getMapTileFromCoordinates(final double aLat, final double aLon, final int zoom) {
		System.out.println("- "+aLat+" "+(1 - Math.log(
						Math.tan(aLat * Math.PI / 180) + 1 / Math.cos(aLat * Math.PI / 180)
						) / Math.PI) / 2 * (1 << zoom));
		System.out.println("* "+aLat  / 180);
		final int y = (int) Math.floor(
				(1 - Math.log(
						Math.tan(aLat * Math.PI / 180) + 1 / Math.cos(aLat * Math.PI / 180)
						) / Math.PI) / 2 * (1 << zoom)
				
				);
		final int x = (int) Math.floor((aLon + 180) / 360 * (1 << zoom));
//		System.out.println(" "+aLon+" "+(aLon + 180) / 360);

		return new OSMTileInfo(x, y, zoom);
	}
}
