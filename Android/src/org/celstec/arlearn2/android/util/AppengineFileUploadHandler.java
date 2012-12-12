package org.celstec.arlearn2.android.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.client.GenericClient;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


@Deprecated
public class AppengineFileUploadHandler {

	private final static int READY_TO_OPEN = 1;
	private final static int END = 2;
//	private BufferedInputStream fStream;
	protected Context ctx;
	private Uri uri;
	private String fileName;
	private String remoteFilePath;
//	private int status;
	private String mimeType;
	private String token;
	private Long runId;
	private String account;
	private boolean endWithError;
	private int amountofKbs = 1;
	final static String end = "\r\n";
	final static String twoHyphens = "--";
	final static String boundary = "END_OF_PART";
	private long totalSize;

	byte[] buffer = new byte[1024];
	

	public AppengineFileUploadHandler(Context ctx, Uri uri, String mimetype, String token, Long runId, String account) {
			this.mimeType = mimetype;
			this.token = token;
			this.account = account;
			this.runId = runId;
			this.fileName = uri.getLastPathSegment();
			this.ctx = ctx;
			this.uri = uri;
	}

	public boolean endedwithError() {
		return endWithError;
	}

	public void startUpload() {
		String uploadUrl = requestExternalUrl();
		if (uploadUrl == null) {
			endWithError = true;
			return;
		}
		publishData(uploadUrl);
		
		
	}
	
	private  String requestExternalUrl() {
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
			conn.setRequestProperty("Authorization", "GoogleLogin auth=" + token);

			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

			// write parameters
			writer.write("runId=" + runId + "&account=" + account + "&fileName=" + fileName);
			writer.close();
			StringBuffer answer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				answer.append(line);
			}

			reader.close();

			// Output the response
			return answer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public void startUploadOld() {
//		try {
//			while (status != END) {
//				long start = System.currentTimeMillis();
//				publishData(false);
//				long stop = System.currentTimeMillis();
//				if ((stop - start)>10000 && amountofKbs != 1) amountofKbs /= 2;
//				if ((stop - start)<4000 ) amountofKbs *= 2;
//			}
//			if (!endWithError) {
//				publishData(true);
//			}
//
//			fStream.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Log.e("MS", e.getMessage(), e);
//			endWithError = true;
//		}
//	}

	private void publishData (String urlString) {
		try {
			URL url = new URL(urlString);

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext httpContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(urlString);
			
			CustomMultipartEntity multipartContent = new CustomMultipartEntity();

			// We use FileBody to transfer an image 

			InputStream is = ctx.getContentResolver().openInputStream(uri);
			multipartContent.addPart("uploaded_file", new InputStreamBody(is, mimeType, fileName));
			totalSize = is.available();
			
			DBAdapter db = new DBAdapter(ctx);
			db.openForWrite();
			MediaCache mc = ((MediaCache) db.table(DBAdapter.MEDIA_CACHE));
			mc.registerTotalAmountofBytes(uri, (int)  totalSize);
			db.close();
			
			httpPost.setEntity(multipartContent);

			HttpResponse response = httpClient.execute(httpPost, httpContext);
			httpPost.getEntity();
			
			db.openForWrite();
			mc.registerBytesAvailable(uri, 0);
			db.close();
			updateActivities(ctx, NarratorItemActivity.class.getCanonicalName());
			
		} catch (Exception e) {
			endWithError = true;
			if (e != null) {
				e.printStackTrace();
			}
		}
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
	
	public class CustomMultipartEntity extends MultipartEntity {
		
		@Override
		public void writeTo(final OutputStream outstream) throws IOException
		{
			super.writeTo(new CountingOutputStream(outstream));
		}
	}
	
	public  class CountingOutputStream extends FilterOutputStream
	{
 
		private long transferred;
		private long lastUpdateActivities = System.currentTimeMillis();

 
		public CountingOutputStream(final OutputStream out)
		{
			super(out);
//			this.listener = listener;
			this.transferred = 0;
		}
 
		public void write(byte[] b, int off, int len) throws IOException
		{
			out.write(b, off, len);
			this.transferred += len;
//			this.listener.transferred(this.transferred);
		
	            
	            if ((System.currentTimeMillis() - lastUpdateActivities) > 1000 ) {
	            	System.out.println("transferred "+this.transferred);
	            	DBAdapter db = new DBAdapter(ctx);
					db.openForWrite();
					MediaCache mc = ((MediaCache) db.table(DBAdapter.MEDIA_CACHE));
					mc.registerBytesAvailable(uri, (int) (totalSize-transferred));
					db.close();
					updateActivities(ctx, NarratorItemActivity.class.getCanonicalName());
					lastUpdateActivities = System.currentTimeMillis();
	            }
			}
		
		public void write(int b) throws IOException
		{
			out.write(b);
			this.transferred++;
//			this.listener.transferred(this.transferred);
			System.out.println("transferred "+this.transferred);

		}
		
	}
}
