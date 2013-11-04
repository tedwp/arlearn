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
package org.celstec.arlearn2.android.maps;

import java.util.HashMap;
import java.util.Iterator;

import org.celstec.arlearn2.beans.run.LocationUpdate;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class UsersOverlay extends ItemizedOverlay {
	
	private HashMap<String, OverlayItem> overlayItems = new HashMap<String, OverlayItem>();
	private String[] keys = new String [0];
	
	public UsersOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		markerHeight = ((BitmapDrawable) defaultMarker).getBitmap().getHeight();
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return overlayItems.get(keys[i]);
	}

	@Override
	public int size() {
		return overlayItems.size();
	}

	public void updateLocation(LocationUpdate bean) {
		//TODO define user
		String user = "arlearn1";
		overlayItems.put(user, new OverlayItem( new GeoPoint((int) (bean.getLat() * 1E6), (int) (bean.getLng() * 1E6)), user, ""));
		Iterator it = overlayItems.keySet().iterator();
		keys = new String[size()];
		int i =0;
		while (it.hasNext()) {
			keys[i++] = (String) it.next();
			
		}
        setLastFocusedIndex(-1);
		populate();
	}

	 @Override
	    public void draw(android.graphics.Canvas canvas, MapView mapView,
	            boolean shadow)
	    {
	        super.draw(canvas, mapView, shadow);

	        // go through all OverlayItems and draw title for each of them
	        for (String gitem: keys)
	        {
	            /* Converts latitude & longitude of this overlay item to coordinates on screen.
	             * As we have called boundCenterBottom() in constructor, so these coordinates
	             * will be of the bottom center position of the displayed marker.
	             */
	        	OverlayItem item = overlayItems.get(gitem);
	            GeoPoint point = item.getPoint();
	            Point markerBottomCenterCoords = new Point();
	            mapView.getProjection().toPixels(point, markerBottomCenterCoords);

	            /* Find the width and height of the title*/
	            TextPaint paintText = new TextPaint();
	            Paint paintRect = new Paint();

	            Rect rect = new Rect();
	            paintText.setTextSize(FONT_SIZE);
	            paintText.getTextBounds(item.getTitle(), 0, item.getTitle().length(), rect);

	            rect.inset(-TITLE_MARGIN, -TITLE_MARGIN);
	            rect.offsetTo(markerBottomCenterCoords.x - rect.width()/2, markerBottomCenterCoords.y - markerHeight - rect.height());

	            paintText.setTextAlign(Paint.Align.CENTER);
	            paintText.setTextSize(FONT_SIZE);
	            paintText.setARGB(255, 255, 255, 255);
	            paintRect.setARGB(130, 0, 0, 0);

	            canvas.drawRoundRect( new RectF(rect), 2, 2, paintRect);
	            canvas.drawText(item.getTitle(), rect.left + rect.width() / 2,
	                    rect.bottom - TITLE_MARGIN, paintText);
	        }
	    }
	    private int markerHeight;

	 private static final int FONT_SIZE = 12;
	    private static final int TITLE_MARGIN = 3;
}
