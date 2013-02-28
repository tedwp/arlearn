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

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class OSMOverlayItem extends OverlayItem{

	
	private GeneralItem gi;
	private Context ctx;

	public OSMOverlayItem(GeneralItem gi, Context ctx){
		super(gi.getName(), "", new org.osmdroid.util.GeoPoint(gi.getLat(), gi.getLng()));
		this.gi = gi;
		this.ctx = ctx;
	}
	
	
	@Override
	public Drawable getMarker(int stateBitset) {
		int icon =ListMapItemsActivity.getIcon(gi);
		if (icon != 0) {
			Drawable drawable = GenericItemsOverlay.boundCenterBottom(ctx.getResources().getDrawable(icon));
			return drawable; //TODO 
		}
		
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
