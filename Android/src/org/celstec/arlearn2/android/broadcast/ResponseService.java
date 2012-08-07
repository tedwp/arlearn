package org.celstec.arlearn2.android.broadcast;

import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MyResponses;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.client.ResponseClient;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ResponseService extends IntentService {

	public ResponseService() {
		super("Response item service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Response bean = (Response) extras.getSerializable("bean");
			if (bean != null) {
				process(this, bean);
				updateActivities(this, NarratorItemActivity.class.getCanonicalName());

			}
		}
		syncronize(this);
	}

	private void process(final Context context, final Response resp) {
		new Thread(new Runnable() {
			public void run() {
				DBAdapter db = new DBAdapter(context);
				db.openForWrite();
				boolean inserted = db.table(DBAdapter.MYRESPONSES_ADAPTER).insert(resp);
				db.close();
			}
		}).start();
	}

	private void syncronize(Context ctx) {
		DBAdapter db = new DBAdapter(ctx);
		db.openForWrite();
		Response responses[] = (Response[]) ((MyResponses) db.table(DBAdapter.MYRESPONSES_ADAPTER)).queryRevoked();
		publishResponse(db, responses, PropertiesAdapter.getInstance(ctx));
		db.close();
	}

	private void publishResponse(DBAdapter db, final Response[] responses, PropertiesAdapter pa) {
		for (Response r : responses) {
			try {
				Response result = ResponseClient.getResponseClient().publishAction(pa.getFusionAuthToken(), r);

				MyResponses dbAdapter = ((MyResponses) db.table(DBAdapter.MYRESPONSES_ADAPTER));
				if (result.getError() == null) {
					dbAdapter.confirmReplicated(result);
				}
			} catch (Exception e) {
				Log.e("exception", e.getMessage(), e);
			}
		}
	}

	protected void updateActivities(Context ctx, String... activities) {
		Intent updateIntent = new Intent();
		updateIntent.setAction("org.celstec.arlearn.updateActivities");
		for (int i = 0; i < activities.length; i++) {
			updateIntent.putExtra(activities[i], true);
		}
		ctx.sendBroadcast(updateIntent);
	}
}
