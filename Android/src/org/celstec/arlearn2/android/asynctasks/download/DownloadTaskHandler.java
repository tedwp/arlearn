package org.celstec.arlearn2.android.asynctasks.download;

import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DownloadTaskHandler extends Handler {
    public static final int SYNC_USER_MEDIA = 7;

    public static final int SYNC_UPLOAD_MEDIA = 11;


	@Override
	public void handleMessage(Message message) {
		Log.i("DownloadTask", "start "+System.currentTimeMillis());
		NetworkTask task = (NetworkTask) message.obj;
		long time = System.currentTimeMillis();
		try {
			task.execute();
		} catch (Exception e) {
			Log.e("exception", "in databasehandler", e);
		}
		Log.i("DownloadTask", "end "+task.getClass().getCanonicalName()+" "+ (System.currentTimeMillis()-time));
	}

}
