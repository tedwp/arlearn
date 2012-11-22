package org.celstec.arlearn2.android.asynctasks.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NetworkTaskHandler extends Handler {
    public static final int SYNC_GENERALITEMS = 1;
    public static final int SYNC_ACTIONS = 2;
    public static final int PUBLISH_ACTION = 3;
    public static final int SYNC_GAMES = 4;
    public static final int SYNC_RUNS = 5;
    public static final int SYNC_GI_MEDIA = 6;
    public static final int SYNC_USER_MEDIA = 7;


	@Override
	public void handleMessage(Message message) {
		Log.i("NWTask", "start "+System.currentTimeMillis());
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
