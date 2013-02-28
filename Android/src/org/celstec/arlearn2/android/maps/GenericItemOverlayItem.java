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

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;


public class GenericItemOverlayItem extends OverlayItem {

	private GeneralItem gi;
	private Context ctx;

	public GenericItemOverlayItem(GeneralItem gi, Context ctx){
		super(new GeoPoint((int) (gi.getLat() * 1E6), (int) (gi.getLng() * 1E6)), gi.getName(), ""); //TODO move geopoint to other location
		this.gi = gi;
		this.ctx = ctx;
	}
	
	public GenericItemOverlayItem(GeoPoint point, java.lang.String title, java.lang.String snippet) {
		super(point, title, snippet);
	}

	@Override
	public Drawable getMarker(int stateBitset) {
		int icon =ListMapItemsActivity.getIcon(gi);
//		int icon = gi.getIcon();
		if (icon != 0) return GenericItemsOverlay.boundCenterBottom(ctx.getResources().getDrawable(icon)); 
//		if (gi.getType().equals("MultipleChoiceTest")) {
//			return R.drawable.multiple_choice));
//		} else 	if (gi.getType().equals("OpenQuestion")) {
//			return GenericItemsOverlay.boundCenterBottom(ctx.getResources().getDrawable(R.drawable.speechbubble_green));
//		} else 	if (gi.getType().equals("AudioObject")) {
//			return GenericItemsOverlay.boundCenterBottom(ctx.getResources().getDrawable(R.drawable.audio_icon));
//		}
		return null;
	}
	
	public int getMarkerHeight() {
		BitmapDrawable bd = (BitmapDrawable) getMarker(0);
		if (bd != null) {
			return ((BitmapDrawable) getMarker(0)).getBitmap().getHeight();
		}
		return 0;
	}
	
	

}
