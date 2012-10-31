package org.celstec.arlearn2.android.maps;

import java.util.TreeSet;

import org.celstec.arlearn2.android.activities.GIActivitySelector;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
//import org.celstec.arlearn2.genericBeans.GeneralItem;
//import org.celstec.arlearn2.genericBeans.Run;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class GenericItemsOverlay extends ItemizedOverlay {
	
	private GeneralItem[] gis = new GeneralItem[0]; 
	private GenericItemOverlayItem[] overlayItems = new GenericItemOverlayItem[0]; 
	private MapViewActivity ctx;

	public GenericItemsOverlay(Drawable defaultMarker, MapViewActivity ctx) {
		super(boundCenterBottom(defaultMarker));
		this.ctx = ctx;
		populate();
	}

	protected OverlayItem createItem(int i) {
		GeoPoint point = new GeoPoint((int) (gis[i].getLat() * 1E6), (int) (gis[i].getLng() * 1E6));
		overlayItems[i] = new GenericItemOverlayItem(gis[i], ctx);
		return overlayItems[i];
	}

	public int size() {
		return gis.length;
	}
	
	public void syncItems(Context ctx, long runId) {
		TreeSet<GeneralItem> gil = GeneralItemVisibilityCache.getInstance().getAllVisibleLocations(runId, ctx);
		if (gil != null) {
			gis = gil.toArray(new GeneralItem[] {});
		} 
		overlayItems = new GenericItemOverlayItem[gis.length];
		populate();
	}
	
	@Override
	protected boolean onTap(int index) {
		GIActivitySelector.startActivity(ctx, gis[index]);
		return true;
	}
	
	protected static android.graphics.drawable.Drawable boundCenterBottom(android.graphics.drawable.Drawable balloon){
		return ItemizedOverlay.boundCenter(balloon);
	}

	 @Override
	    public void draw(android.graphics.Canvas canvas, MapView mapView,
	            boolean shadow)
	    {
	        super.draw(canvas, mapView, shadow);

	        // go through all OverlayItems and draw title for each of them
	        for (GenericItemOverlayItem item: overlayItems)
	        {
	            /* Converts latitude & longitude of this overlay item to coordinates on screen.
	             * As we have called boundCenterBottom() in constructor, so these coordinates
	             * will be of the bottom center position of the displayed marker.
	             */
//	        	OverlayItem item = overlayItems.get(gitem);
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
//	            rect.offsetTo(markerBottomCenterCoords.x - rect.width()/2, markerBottomCenterCoords.y + markerHeight ); //- rect.height()
	            rect.offsetTo(markerBottomCenterCoords.x - rect.width()/2, markerBottomCenterCoords.y + rect.height() ); //- rect.height()

	            paintText.setTextAlign(Paint.Align.CENTER);
	            paintText.setTextSize(FONT_SIZE);
	            paintText.setARGB(255, 255, 255, 255);
	            paintRect.setARGB(130, 0, 0, 0);

	            canvas.drawRoundRect( new RectF(rect), 2, 2, paintRect);
	            canvas.drawText(itemTitle, rect.left + rect.width() / 2,
	                    rect.bottom - TITLE_MARGIN, paintText);
	        }
	    }
//	    private int markerHeight;

	 private static final int FONT_SIZE = 12;
	    private static final int TITLE_MARGIN = 3;
	
}
