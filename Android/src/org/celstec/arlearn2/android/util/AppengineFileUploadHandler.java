package org.celstec.arlearn2.android.util;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.client.GenericClient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class AppengineFileUploadHandler {

	private final static int READY_TO_OPEN = 1;
	private final static int END = 2;
	private BufferedInputStream fStream;
	private Context ctx;
	private Uri uri;
	private String fileName;
	private String remoteFilePath;
	private int status;
	private String mimeType;
	private String token;
	private Long runId;
	private String account;
	private boolean endWithError;
	private int amountofKbs = 1;
	final static String end = "\r\n";
	final static String twoHyphens = "--";
	final static String boundary = "END_OF_PART";

	byte[] buffer = new byte[1024];

	public AppengineFileUploadHandler(Context ctx, Uri uri, String mimetype, String token, Long runId, String account) {
		try {
			this.mimeType = mimetype;
			this.token = token;
			this.account = account;
			this.runId = runId;
			this.fileName = uri.getLastPathSegment();
			this.ctx = ctx;
			this.uri = uri;
			fStream = new BufferedInputStream(ctx.getContentResolver().openInputStream(uri));
			DBAdapter db = new DBAdapter(ctx);
			db.openForWrite();
			MediaCache mc = ((MediaCache) db.table(DBAdapter.MEDIA_CACHE));
			mc.registerTotalAmountofBytes(uri, fStream.available());
			db.close();
			status = READY_TO_OPEN;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			status = END;
			endWithError = true;
		} catch (IOException e) {
			e.printStackTrace();
			status = END;
			endWithError = true;
		}
	}

	public boolean endedwithError() {
		return endWithError;
	}

	public void startUpload() {
		try {
			while (status != END) {
				long start = System.currentTimeMillis();
				publishData(false);
				long stop = System.currentTimeMillis();
				if ((stop - start)>10000 && amountofKbs != 1) amountofKbs /= 2;
				if ((stop - start)<4000 ) amountofKbs *= 2;
			}
			if (!endWithError) {
				publishData(true);
			}

			fStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("MS", e.getMessage(), e);
			endWithError = true;
		}
	}

	private void publishData(boolean last) throws IOException {

		URL url = new URL(GenericClient.urlPrefix + "/uploadServiceIncremental");
		if (GenericClient.urlPrefix.endsWith("/")) {
			url = new URL(GenericClient.urlPrefix + "uploadServiceIncremental");
		}
		
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");

		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("GData-Version", "1.2");
		conn.setRequestProperty("Authorization", "GoogleLogin auth=" + token);
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);


		final DataOutputStream ds = new DataOutputStream(conn.getOutputStream());
		setAttribute("runId", ""+runId, ds);
		setAttribute("account", account, ds);
		setAttribute("last", ""+last, ds);
		
		if (remoteFilePath != null) {
			setAttribute("serverPath", remoteFilePath, ds);
		}

		ds.writeBytes(twoHyphens + boundary + end);
		ds.writeBytes("Content-Disposition: form-data; name=\"files\"; filename=\"" + fileName + "\"" + end + "Content-Type: " + mimeType + end);
		ds.writeBytes(end);

		// final CountDownLatch handlerInitLatch = new CountDownLatch(1);
		Log.i("MS", "1 before thread " );
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {

		try {
			Log.i("2 MS", "amountofkb "+amountofKbs );
			Log.i("3 MS", "available " + fStream.available());
			int length =0;
			for (int i = 0; i < amountofKbs && length != -1; i++) {
				length = fStream.read(buffer);
				
				if (length != -1){
					ds.write(buffer, 0, length);
					
				}
					
				

			}

			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			
			DBAdapter db = new DBAdapter(ctx);
			db.openForWrite();
			MediaCache mc = ((MediaCache) db.table(DBAdapter.MEDIA_CACHE));
			mc.registerBytesAvailable(uri, fStream.available());
			db.close();
			updateActivities(ctx, NarratorItemActivity.class.getCanonicalName());
			
		} catch (IOException e) {
			Log.e("MS", e.getMessage(), e);
			e.printStackTrace();
			endWithError = true;
			status = END;

		}
		ds.flush();
		ds.close();
		remoteFilePath = slurp(conn.getInputStream());
		if (fStream.available() == 0)
			status = END;
	}
	private void setAttribute(String name, String value, final DataOutputStream ds) throws IOException{
		ds.writeBytes(twoHyphens + boundary + end);
		ds.writeBytes("Content-Disposition: form-data; name=\""+name+"\"" + end + end + value + end);
	}


	public static String slurp(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
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
