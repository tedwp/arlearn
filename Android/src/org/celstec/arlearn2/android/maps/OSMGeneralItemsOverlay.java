package org.celstec.arlearn2.android.maps;

import org.celstec.arlearn2.android.activities.GIActivitySelector;
import org.celstec.arlearn2.android.activities.OsmMapViewActivity;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;

public class OSMGeneralItemsOverlay extends ItemizedOverlay<OSMOverlayItem>{

	
	private GeneralItem[] gis = new GeneralItem[0]; 
	private OSMOverlayItem[] overlayItems = new OSMOverlayItem[0]; 
	private OsmMapViewActivity ctx;

	public OSMGeneralItemsOverlay(Drawable defaultMarker, OsmMapViewActivity ctx) {
		super(defaultMarker, new DefaultResourceProxyImpl(ctx));
		this.ctx = ctx;
	}

	@Override
	public boolean onSnapToItem(int arg0, int arg1, Point arg2, IMapView arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected OSMOverlayItem createItem(int i) {
		overlayItems[i] = new OSMOverlayItem(gis[i], ctx);
		return overlayItems[i];
	}
	
//	public boolean onSingleTapConfirmed(android.view.MotionEvent e,
//            MapView mapView) {
//		GIActivitySelector.startActivity(ctx, gis[index]);
//		return true;
//	}
	
	
	public boolean onSingleTapUp(android.view.MotionEvent e,
            MapView mapView) {
		return false;
	}
	
//	@Override
//	protected boolean onTap() {
//		GIActivitySelector.startActivity(ctx, gis[index]);
////		gis[index].startActivity(ctx);
//		return true;
//	}
	
	@Override
	public int size() {
		return gis.length;
	}
	
//	public void syncItems(Context ctx) {
//		DBAdapter db = new DBAdapter(ctx);
//		db.openForRead();
//		//TODO read messages with location from cache
//
////		gis = (GeneralItem[]) ((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).queryWithLocation(this.ctx.getRunId());
//		overlayItems = new OSMOverlayItem[gis.length];
//		db.close();
//		populate();
//	}

	protected static android.graphics.drawable.Drawable boundCenterBottom(android.graphics.drawable.Drawable balloon){
		return GenericItemsOverlay.boundCenterBottom(balloon);
	}

	
	 @Override
	public void draw(Canvas canvas, org.osmdroid.views.MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		super.draw(canvas, mapView, shadow);
	        // go through all OverlayItems and draw title for each of them
	        for (OSMOverlayItem item: overlayItems)
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
