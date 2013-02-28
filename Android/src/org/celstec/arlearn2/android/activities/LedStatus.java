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
package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.service.ChannelAPINotificationService;

import android.app.Activity;
import android.widget.ImageView;

public class LedStatus {

	public static void updateStatus(Activity ctx){
		ImageView iv = (ImageView) ctx.findViewById(R.id.statusLed);
		switch (PropertiesAdapter.getInstance(ctx).getStatus()) {
		case ChannelAPINotificationService.ONLINE_STATUS:
			iv.setImageResource(R.drawable.status_icon_green);
			break;

		default:
			iv.setImageResource(R.drawable.status_icon_red);
			break;
		}
	}
}
