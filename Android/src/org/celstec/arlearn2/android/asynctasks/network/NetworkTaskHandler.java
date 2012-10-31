package org.celstec.arlearn2.android.asynctasks.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NetworkTaskHandler extends Handler {

	@Override
	public void handleMessage(Message message) {
		NetworkTask task = (NetworkTask) message.obj;
		long time = System.currentTimeMillis();
		try {
			task.execute();
		} catch (Exception e) {
			Log.e("exception", "in databasehandler", e);
		}
		Log.i("NWTask", "end "+task.getClass().getCanonicalName()+" "+ (System.currentTimeMillis()-time));
	}

}
