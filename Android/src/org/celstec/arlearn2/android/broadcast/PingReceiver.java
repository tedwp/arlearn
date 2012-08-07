package org.celstec.arlearn2.android.broadcast;

import org.celstec.arlearn2.android.activities.NfcScanOnDemandActivity;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.beans.notification.Ping;
import org.celstec.arlearn2.client.ChannelClient;
import org.codehaus.jettison.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
							ChannelClient.getChannelClient().pong(0, "", bean.getTo(), bean.getFrom(), Ping.PING, "", bean.getTimestamp());
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

	private void dbQuery(final Context context, Ping bean) {
		DBAdapter db = new DBAdapter(context);
		db.openForRead();
		JSONObject allTables = db.getGenericJsonAdapter().queryAll(bean.getPayload());
		db.close();
		ChannelClient.getChannelClient().pong(0, "", bean.getTo(), bean.getFrom(),Ping.DB_QUERY, allTables.toString(), bean.getTimestamp());
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
