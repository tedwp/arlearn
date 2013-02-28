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
package org.celstec.arlearn2.android.broadcast;

import org.celstec.arlearn2.android.activities.NfcScanOnDemandActivity;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.service.LocationService;
import org.celstec.arlearn2.beans.notification.Ping;
import org.celstec.arlearn2.beans.run.Location;
import org.celstec.arlearn2.client.ChannelClient;
import org.codehaus.jettison.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

public class PingReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			final Ping bean = (Ping) extras.getSerializable("bean");
			if (bean != null) {
				new Thread(new Runnable() {
					public void run() {
						if (bean.getRequestType() == null) {
							android.location.Location loc = LocationService.getBestLocation(context);
							Location locBean = new Location();
							if (loc != null) {
								locBean.setLat(loc.getLatitude());
								locBean.setLng(loc.getLongitude());
								locBean.setAccuracy(loc.getAccuracy());
							}
							ChannelClient.getChannelClient().pong(0, "", bean.getTo(), bean.getFrom(), Ping.PING, locBean.toString(), bean.getTimestamp());
						} else {
							processSwitchRequest(context, bean);
						}
					}
				}).start();

			}
		}
	}
	
	private void processSwitchRequest(final Context context, Ping bean) {
		switch (bean.getRequestType()) {
		case Ping.DB_QUERY:
			dbQuery(context, bean);
			break;
		case Ping.READ_NFC:
			dbNfcRead(context, bean);
			break;
		default:
			break;
		}
		
	}
	
	private void dbNfcRead(Context ctx, Ping bean) {
		Intent i = null;
		updateActivities(ctx, NfcScanOnDemandActivity.class.getCanonicalName());
		i = new Intent(new Intent(ctx, NfcScanOnDemandActivity.class));
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("to", bean.getTo());
		i.putExtra("from", bean.getFrom());
		i.putExtra("timestamp", bean.getTimestamp());
		ctx.startActivity(i);
		
	}

	private void dbQuery(final Context context, final Ping bean) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(context));
		m.obj = new DBAdapter.DatabaseTask() {

			@Override
			public void execute(DBAdapter db) {
				JSONObject allTables = db.getGenericJsonAdapter().queryAll(bean.getPayload());		
				ChannelClient.getChannelClient().pong(0, "", bean.getTo(), bean.getFrom(),Ping.DB_QUERY, allTables.toString(), bean.getTimestamp());		
			}
			
		};
		m.sendToTarget();
	}
	
	protected void updateActivities(Context ctx, String... activities) {
		Intent updateIntent = new Intent();
		updateIntent.setAction("org.celstec.arlearn.updateActivities");
		for (int i = 0; i < activities.length; i++) {
			updateIntent.putExtra(activities[i], true);
			updateIntent.putExtra(NfcScanOnDemandActivity.NFC_SCAN_SHUTDOWN, true);
		}
		ctx.sendBroadcast(updateIntent);
	}

}
