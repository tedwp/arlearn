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

import java.util.List;

import org.celstec.arlearn2.android.activities.OsmMapViewActivity;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.OverlayItem;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;

public class OsmGeneralItemizedIconOverlay2 extends ItemizedIconOverlay<OverlayItem> {

	private OsmMapViewActivity ctx;

	public OsmGeneralItemizedIconOverlay2(final OsmMapViewActivity ctx, final List<OverlayItem> aList, OnItemGestureListener<OverlayItem> gestureListener) {
		 super(ctx, aList, gestureListener);
		 this.ctx  = ctx;
	}

	public void setGeneralItemList(GeneralItem[] gis) {
		removeAllItems();
		for (int i = 0; i < gis.length; i++) {
			addItem(new OSMOverlayItem(gis[i], ctx));

		}
		
	}

	protected static android.graphics.drawable.Drawable boundCenterBottom(android.graphics.drawable.Drawable balloon) {
		return GenericItemsOverlay.boundCenterBottom(balloon);
	}


 @Override
public void draw(Canvas canvas, org.osmdroid.views.MapView mapView, boolean shadow) {
	// TODO Auto-generated method stub
	super.draw(canvas, mapView, shadow);
        // go through all OverlayItems and draw title for each of them
	for (int i = 0; i< size(); i++){
		OSMOverlayItem item = (OSMOverlayItem) getItem(i);
            GeoPoint point = item.getPoint();
            Point markerBottomCenterCoords = new Point();
            mapView.getProjection().toPixels(point, markerBottomCenterCoords);

            /* Find the width and height of the title*/
            TextPaint paintText = new TextPaint();
            Paint paintRect = new Paint();

            Rect rect = new Rect();
            paintText.setTextSize(FONT_SIZE);
            int itemTitleLength = 0;
            String itemTitle = "";
            if (item.getTitle() != null) itemTitleLength = item.getTitle().length(); 
            if (item.getTitle() != null) itemTitle = item.getTitle(); 
            paintText.getTextBounds(itemTitle, 0, itemTitleLength, rect);

            rect.inset(-TITLE_MARGIN, -TITLE_MARGIN);
            int markerHeight = item.getMarkerHeight() /2;
//            rect.offsetTo(markerBottomCenterCoords.x - rect.width()/2, markerBottomCenterCoords.y + markerHeight ); //- rect.height()
            rect.offsetTo(markerBottomCenterCoords.x - rect.width()/2, markerBottomCenterCoords.y - 5 ); //- rect.height()

            paintText.setTextAlign(Paint.Align.CENTER);
            paintText.setTextSize(FONT_SIZE);
            paintText.setARGB(255, 255, 255, 255);
            paintRect.setARGB(130, 0, 0, 0);

            canvas.drawRoundRect( new RectF(rect), 2, 2, paintRect);
            canvas.drawText(itemTitle, rect.left + rect.width() / 2,
                    rect.bottom - TITLE_MARGIN, paintText);
        }
    }

 private static final int FONT_SIZE = 12;
    private static final int TITLE_MARGIN = 3;
}
