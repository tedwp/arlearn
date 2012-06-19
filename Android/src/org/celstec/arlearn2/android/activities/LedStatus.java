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
