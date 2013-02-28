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

import java.util.TreeSet;

import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.activities.ViewAnswerActivity;
import org.celstec.arlearn2.android.cache.ResponseCache;
import org.celstec.arlearn2.beans.run.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class ResponsesOverlay extends ItemizedOverlay {

	private Response[] responses = new Response[0];
	private MapViewActivity ctx;

	public ResponsesOverlay(Drawable defaultMarker, MapViewActivity ctx) {
		super(defaultMarker);
		this.ctx = ctx;
		populate();
	}

	public void syncItems(Context ctx, long runId) {
		TreeSet<Response> ts = ResponseCache.getInstance().getLocatedResponses(runId, ctx);
		if (ts == null) return;
		responses = ts.toArray(new Response[]{});
		setLastFocusedIndex(-1);
		populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		JSONObject json;
		try {
			json = new JSONObject(responses[i].getResponseValue());
			double lat = 0;
			double lng = 0;
			if (json.has("lat")) lat = json.getDouble("lat");
			if (json.has("lng")) lng = json.getDouble("lng");
			GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
			OverlayItem item = new OverlayItem(point, "annotation", "");
			return item;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int size() {
		return responses.length;
	}

	protected boolean onTap(int index) {
		Intent i = new Intent(ctx, ViewAnswerActivity.class);
		i.putExtra("response", responses[index]);
		ctx.startActivity(i);
		return true;
	}
	
}
