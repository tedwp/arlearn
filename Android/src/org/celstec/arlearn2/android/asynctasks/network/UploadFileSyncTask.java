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
package org.celstec.arlearn2.android.asynctasks.network;

import java.io.BufferedReader;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.ListRunsParticipateActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.download.DownloadQueue;
import org.celstec.arlearn2.android.asynctasks.download.DownloadTaskHandler;

import org.celstec.arlearn2.android.cache.MediaUploadCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCacheUpload;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.client.GenericClient;

import android.content.Context;
import android.os.Message;

public class UploadFileSyncTask extends GenericTask implements NetworkTask {
	
	private MediaCacheUpload.UploadItem uploadItem;
	private boolean endWithError;

	private Context ctx;

	@Override
	public void run(Context ctx) {
		this.ctx =ctx;
		Message m = Message.obtain(DownloadQueue.getNetworkTaskHandler());
		m.what = DownloadTaskHandler.SYNC_USER_MEDIA;
		m.obj = this;
		m.sendToTarget();
		
	}

	@Override
	public void execute() {
		LoadItemFromDatabase loadItem = new LoadItemFromDatabase();
		
		loadItem.run(ctx);
	}
	
	private class UploadItem extends GenericTask implements NetworkTask {
		
		@Override
		public void run(Context ctx) {
			Message m = Message.obtain(DownloadQueue.getNetworkTaskHandler());
			m.what = DownloadTaskHandler.SYNC_USER_MEDIA;
			m.obj = UploadItem.this;
			m.sendToTarget();
		}
		
		@Override
		public void execute() {
			String uploadUrl = requestExternalUrl();
//            uploadUrl = uploadUrl.replace("streetlearn.appspot.com", "192.168.1.8:9999");
			if (uploadUrl == null) {
				endWithError = true;
				return;
			}
			publishData(uploadUrl);
			MediaUploadCache.getInstance(uploadItem.getRunId()).put(uploadItem.buildRemotePath(), MediaCacheUpload.REP_STATUS_SYNCING);
			if (ctx == null) {
                System.out.println("problem");
            }   else {
                ActivityUpdater.updateActivities(ctx, NarratorItemActivity.class.getCanonicalName());
            }


			WriteStatusToDatabase statusWrite = new WriteStatusToDatabase();
			
			if (!endWithError) {
				uploadItem.setReplicated(MediaCacheUpload.REP_STATUS_DONE);
				statusWrite.setStatus(MediaCacheUpload.REP_STATUS_DONE);
				statusWrite.taskToRunAfterExecute(new UploadFileSyncTask());
			} else {
				uploadItem.setReplicated(MediaCacheUpload.REP_STATUS_TODO);
				statusWrite.setStatus(MediaCacheUpload.REP_STATUS_TODO);
			}
			statusWrite.run(ctx);
			UploadFileSyncTask.this.runAfterTasks(ctx);
		}
	}
	
//	private void startUpload() {
//		String uploadUrl = requestExternalUrl();
//		if (uploadUrl == null) {
//			endWithError = true;
//			return;
//		}
//		publishData(uploadUrl);
//		MediaUploadCache.getInstance(uploadItem.getRunId()).put(uploadItem.buildRemotePath(), MediaCacheUpload.REP_STATUS_SYNCING);
//		ActivityUpdater.updateActivities(ctx, NarratorItemActivity.class.getCanonicalName());
//
//		WriteStatusToDatabase statusWrite = new WriteStatusToDatabase();
//		
//		if (!endWithError) {
//			uploadItem.setReplicated(MediaCacheUpload.REP_STATUS_DONE);
//			statusWrite.setStatus(MediaCacheUpload.REP_STATUS_DONE);
//			statusWrite.taskToRunAfterExecute(new UploadFileSyncTask());
//		} else {
//			uploadItem.setReplicated(MediaCacheUpload.REP_STATUS_TODO);
//			statusWrite.setStatus(MediaCacheUpload.REP_STATUS_TODO);
//		}
//		statusWrite.run(ctx);
//		runAfterTasks(ctx);
//	}
	
	private class LoadItemFromDatabase extends GenericTask implements DatabaseTask {

		@Override
		public void execute(DBAdapter db) {
			uploadItem = db.getMediaCacheUpload().getNextItemToUpload();
			if (uploadItem != null) {
//				startUpload();
				new UploadItem().run(ctx);
			}
		}

		@Override
		protected void run(Context ctx) {
			Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
			m.obj = this;
			m.sendToTarget();
		}
	}
	
	private class WriteStatusToDatabase extends GenericTask implements DatabaseTask {

		private int status;
		
		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		@Override
		public void execute(DBAdapter db) {
			db.getMediaCacheUpload().writeUploadStatus(uploadItem, getStatus());
			runAfterTasks(db.getContext());
		}

		@Override
		protected void run(Context ctx) {
			Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
			m.obj = this;
			m.sendToTarget();
		}
	
	}
	
	
	private String getToken() {
		return PropertiesAdapter.getInstance(ctx).getAuthToken();
	}
	
	private String addToParameters(String parameters, String key, String value) {
		if (!"".equals(parameters)) {
			parameters += "&";
		}
		parameters+= key+"="+value;
		return parameters;
	}
	
	private String requestExternalUrl() {
		try {
			URL url = new URL(GenericClient.urlPrefix + "/uploadServiceWithUrl");
			if (GenericClient.urlPrefix.endsWith("/")) {
				url = new URL(GenericClient.urlPrefix + "uploadServiceWithUrl");
			}

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("GData-Version", "1.2");
			conn.setRequestProperty("Authorization", "GoogleLogin auth=" + getToken());

			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

			// write parameters
			
			String parameters = "";
			parameters = addToParameters(parameters, "runId", ""+uploadItem.getRunId());
			parameters = addToParameters(parameters, "account", ""+uploadItem.getUserId());
			parameters = addToParameters(parameters, "fileName", ""+uploadItem.getUri().getLastPathSegment());
			System.out.println("parameters "+parameters);
			
//			writer.write("runId=" + runId + "&account=" + account + "&fileName=" + fileName);
			writer.write(parameters);
			writer.close();
			StringBuffer answer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				answer.append(line);
			}

			reader.close();

			return answer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void publishData(String urlString) {
		try {
			URL url = new URL(urlString);

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext httpContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(urlString);

			CustomMultipartEntity multipartContent = new CustomMultipartEntity();

			InputStream is = ctx.getContentResolver().openInputStream(uploadItem.getUri());
			String mimeType = "application/octet-stream";
			if (uploadItem.getMimetype() !=null ) mimeType = uploadItem.getMimetype();
			multipartContent.addPart("uploaded_file", new InputStreamBody(is, mimeType, uploadItem.getUri().getLastPathSegment()));
			int totalSize = is.available();


			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			httpPost.getEntity();

//			DBAdapter.getAdapter(ctx).getMediaCache().registerBytesAvailable(uri, 0);
		} catch (Exception e) {
			endWithError = true;
			if (e != null) {
				e.printStackTrace();
			}
		}
	}
	
	public class CustomMultipartEntity extends MultipartEntity {

		@Override
		public void writeTo(final OutputStream outstream) throws IOException {
			super.writeTo(new CountingOutputStream(outstream));
		}
	}

	public class CountingOutputStream extends FilterOutputStream {

		private long transferred;
		private long lastUpdateActivities = System.currentTimeMillis();

		public CountingOutputStream(final OutputStream out) {
			super(out);
			this.transferred = 0;
		}

		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			this.transferred += len;
			if ((System.currentTimeMillis() - lastUpdateActivities) > 1000) {
//				DBAdapter.getAdapter(ctx).getMediaCache().registerBytesAvailable(uri, (int) (totalSize - transferred));
				lastUpdateActivities = System.currentTimeMillis();
			}
		}

		public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;
		}

	}
}
