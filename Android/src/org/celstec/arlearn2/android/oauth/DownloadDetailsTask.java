package org.celstec.arlearn2.android.oauth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.broadcast.NetworkSwitcher;
import org.celstec.arlearn2.android.cache.GenericCache;
import org.celstec.arlearn2.android.cache.MediaGeneralItemCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.client.AccountClient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class DownloadDetailsTask extends AsyncTask<Void, String, Void> {

	private Context ctx;

	public DownloadDetailsTask(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			if (NetworkSwitcher.isOnline(ctx)) {
//				Account ac = AccountClient.getAccountClient().accountDetails(PropertiesAdapter.getInstance(ctx).getAuthToken());
				DownloadDetailsTask.downloadAndStoreAccountDetails(ctx);
			} else {
				publishProgress(ctx.getString(R.string.networkUnavailable));
			}
		} catch (Exception e) {
			PropertiesAdapter.getInstance(ctx).disAuthenticate();
			publishProgress("downloading account details failed");
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(String... voids) {

		Toast.makeText(ctx, voids[0], Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
	}

	public static void downloadAndStoreAccountDetails(Context ctx) {
		Account ac = AccountClient.getAccountClient().accountDetails(PropertiesAdapter.getInstance(ctx).getAuthToken());
		
		String oldAddress = User.normalizeEmail(PropertiesAdapter.getInstance(ctx).getFullId());
		String newAddress = User.normalizeEmail(ac.getFullId());
		if (oldAddress != null && !oldAddress.equals(newAddress)) {
			Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
			m.obj = new DBAdapter.DatabaseTask() {
				@Override
				public void execute(DBAdapter db) {
					db.eraseAllData();
					GenericCache.emptyAllCaches();
				}
			};
			m.sendToTarget();
		}
		PropertiesAdapter.getInstance(ctx).setFullId(newAddress);
		PropertiesAdapter.getInstance(ctx).setFullName(ac.getName());
		PropertiesAdapter.getInstance(ctx).setParticipateGameLastSynchronizationDate(0l);
		PropertiesAdapter.getInstance(ctx).setMyGameLastSynchronizationDate(0l);
		PropertiesAdapter.getInstance(ctx).setRunLastSynchronizationDate(0l);
		PropertiesAdapter.getInstance(ctx).setAccountLevel(ac.getAccountLevel());
		String picture = ac.getPicture();
		if (picture != null &&picture.startsWith("http://")) {
			downloadPicture(picture, ctx);
		}
	}
	
	private static void downloadPicture(String url, Context ctx) {
		
		try {

			URL myFileUrl = new URL(url);
			File sdcard = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
			File cacheDirFile = new File(sdcard, Constants.CACHE_DIR);
			if (!cacheDirFile.exists())
				cacheDirFile.mkdir();
			File incommingDir = new File(cacheDirFile, Constants.INCOMMING);
			File outputFile = new File(cacheDirFile, "profileImage");
			HttpURLConnection.setFollowRedirects(false); // new
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			if (conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
				url = conn.getHeaderField("location");
				conn.disconnect();
				downloadPicture(url, ctx);
			}
			conn.connect();

			InputStream is = conn.getInputStream();
			
			

			FileOutputStream fos = new FileOutputStream(outputFile);
			int len1;
			long byteCounter = 0;
			byte[] buffer = new byte[1024];
			long startTime = System.currentTimeMillis();
			while ((len1 = is.read(buffer)) > 0) {
				fos.write(buffer, 0, len1);
			}
			fos.flush();
			fos.close();
			is.close();
			PropertiesAdapter.getInstance(ctx).setPicture(outputFile.getAbsolutePath());
			System.out.println("file storeed as "+outputFile.getAbsolutePath());
		} catch (FileNotFoundException e) {
			PropertiesAdapter.getInstance(ctx).setPicture(null);
		} catch (Exception e) {
			Log.e("error while retrieve media item - addToCache", e.getMessage(), e);
		}
		
	}

}