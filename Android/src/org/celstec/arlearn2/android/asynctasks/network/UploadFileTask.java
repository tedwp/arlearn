package org.celstec.arlearn2.android.asynctasks.network;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.util.AppengineFileUploadHandler.CountingOutputStream;
import org.celstec.arlearn2.client.GenericClient;

import android.content.Context;
import android.net.Uri;

public class UploadFileTask implements NetworkTask {

	public String mimeType;
	public String token;
	public Long runId;
	public String account;
	public String fileName;
	public Uri uri;
	public Context ctx;
	public String mcItemId;

	private boolean endWithError;
	private long totalSize;


	@Override
	public void execute() {
		DBAdapter.getAdapter(ctx).getMediaCache().setReplicationStatus(mcItemId, MediaCache.REP_STATUS_SYNCING);
		String uploadUrl = requestExternalUrl();
		if (uploadUrl == null) {
			endWithError = true;
			return;
		}
		publishData(uploadUrl);
		if (!endWithError) {
			DBAdapter.getAdapter(ctx).getMediaCache().setReplicationStatus(mcItemId, MediaCache.REP_STATUS_DONE);
		} else {
			DBAdapter.getAdapter(ctx).getMediaCache().setReplicationStatus(mcItemId, MediaCache.REP_STATUS_TODO);
		}
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

			InputStream is = ctx.getContentResolver().openInputStream(uri);
			multipartContent.addPart("uploaded_file", new InputStreamBody(is, mimeType, fileName));
			totalSize = is.available();

			DBAdapter.getAdapter(ctx).getMediaCache().registerTotalAmountofBytes(uri, (int) totalSize);

			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			httpPost.getEntity();

			DBAdapter.getAdapter(ctx).getMediaCache().registerBytesAvailable(uri, 0);
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
				DBAdapter.getAdapter(ctx).getMediaCache().registerBytesAvailable(uri, (int) (totalSize - transferred));
				lastUpdateActivities = System.currentTimeMillis();
			}
		}

		public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;
			System.out.println("transferred " + this.transferred);

		}

	}
}
