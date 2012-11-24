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
				DBAdapter.getAdapter(this).getMyResponses().publishResponse(bean);
				
			}
		} else {
			DBAdapter.getAdapter(this).getMyResponses().syncResponses();
		}
		
	}
}
