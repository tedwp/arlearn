package org.celstec.arlearn2.android.activities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.celstec.arlearn2.client.GameClient;
import org.celstec.arlearn2.client.GeneralItemClient;
import org.celstec.arlearn2.client.GenericClient;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class UploadGeneralItemActivity extends GeneralActivity {

	private String CLASSNAME = this.getClass().getName();

	private GeneralItem generalItem;
	private String fileName;
	private Uri uriFile;

	private static final String MIME_TYPE_AUDIO = "audio/3gpp";
	private static final String MIME_TYPE_VIDEO = "video/x-motion-jpeg";

	private static final int OPERATION_SUCCESS = 0;
	private static final int OPERATION_FAILED = 1;

	// private String itemName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle extras = getIntent().getExtras();

		generalItem = (GeneralItem) extras.get("generalItem");
		fileName = (String) extras.get("filename");
		String sPath = "file:///sdcard/" + fileName;
		uriFile = Uri.parse(sPath);

		if (!uploadGeneralItemDB()) {
			// Could not upload GeneralItem in DB
			Log.e(CLASSNAME, "Could not upload GeneralItem in DB");
		} else {

			UploadGeneralItemActivity.this.finish();

		}

	}

	protected String getMimeType(Uri uri, ContentResolver cr) {
		return cr.getType(uri);

	}

	protected InputStream getInputStream(Uri uri, ContentResolver cr) {

		InputStream is = null;

		try {
			is = cr.openInputStream(uri);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return is;

	}

	
	private boolean uploadGeneralItemDB() {

		// TODO path should also be a parameter

		Log.d(CLASSNAME, "Upload blob: " + uriFile.toString());

		// Get URL to upload
		String uploadUrl = requestKey();
		if (uploadUrl == null) {
			return false;
		}

		// Upload blob
		if (publishData(uploadUrl) == OPERATION_SUCCESS) {
			// Create general item

			if (generalItem.getType().equals(Constants.GI_TYPE_AUDIO_OBJECT)) {
				AudioObject o = (AudioObject) generalItem;
				o.setAudioUrl(uploadUrl);
			} else if (generalItem.getType().equals(Constants.GI_TYPE_VIDEO_OBJECT)) {
				VideoObject o = (VideoObject) generalItem;
				o.setVideoUrl(uploadUrl);
			}
			GeneralItem g = new GeneralItem();
			PropertiesAdapter pa = new PropertiesAdapter(this);

// Commented by btb			
//			g = GeneralItemClient.getGeneralItemClient().createGeneralItem(pa.getFusionAuthToken(), generalItem);
//
//			if (g.getErrorCode() != null) {
//				if (g.getErrorCode() == GameClient.ERROR_DESERIALIZING) {
//					Log.e(CLASSNAME, "Exception deserializing audio object item " + generalItem.getName() + ".");
//					return false;
//				}
//			}
			return true;

		} else {
			return false;
		}


	}




	public boolean isGenItemActivity() {
		return false;
	}	
	
// Commented by btb	
//	private URL getURL() {
//
//		URL url = null;
//
//		try {
//
//			url = new URL(GenericClient.urlPrefix + "/uploadServiceWithUrl");
//			if (GenericClient.urlPrefix.endsWith("/")) {
//				url = new URL(GenericClient.urlPrefix + "uploadServiceWithUrl");
//			}
//
//			// url = new URL(GenericClient.urlPrefix + "/uploadUserContent/"+fileName+"?account=arlearn5");
//			// if (GenericClient.urlPrefix.endsWith("/")) {
//			// url = new URL(GenericClient.urlPrefix + "uploadUserContent/" +fileName+"?account=arlearn5");
//			// }
//
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return url;
//	}

	/**
	 * Request external URL for the blob
	 * It includes the key of the blob
	 * 
	 * @return
	 */
	private String requestKey() {
		try {
			PropertiesAdapter pa = new PropertiesAdapter(this);						
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
			conn.setRequestProperty("Authorization", "GoogleLogin auth=" + pa.getFusionAuthToken());

			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

			// // TODO
			// writer.write("gameId=86030&account=arlearn5&fileName=" +
			// fileName);
			writer.write("runId="+pa.getCurrentRunId()+"&account="+pa.getUsername()+"&fileName=" + fileName);
			// This is for userConten writer.write("&name="+uriFile.toString());
			// writer.write("runId=" + runId + "&account=" + account +
			// "&fileName=" + fileName);
			writer.close();
			StringBuffer answer = new StringBuffer();
			
			// Read returned key
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				answer.append(line);
			}

			reader.close();
			
			Log.d(CLASSNAME, "Key obtained from server [" + answer.toString() + "].");

			return answer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Log.d(CLASSNAME, "Could not read key from server.");
		
		return null;
	}

	/**
	 * Publish file into appengine repository 
	 * Uploads file into FileJDO
	 * 
	 * @param urlString
	 * @return
	 */
	private int publishData(String urlString) {
		int iResult = OPERATION_SUCCESS;
		try {

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext httpContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(urlString);

			CustomMultipartEntity multipartContent = new CustomMultipartEntity();

			InputStream is = this.getContentResolver().openInputStream(uriFile);
			multipartContent.addPart("file", new InputStreamBody(is, getMimeType(), fileName));

			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			System.out.println("Response: " + response.toString());
			httpPost.getEntity();

		} catch (Exception e) {
			iResult = OPERATION_FAILED;

			if (e != null) {
				e.printStackTrace();
			}
		}
		return iResult;
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
				lastUpdateActivities = System.currentTimeMillis();
			}
		}

		public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;
			System.out.println("transferred " + this.transferred);

		}

	}

	/**
	 * Returns mime type for current general item
	 * 
	 * @return String
	 */
	private String getMimeType() {
		if (generalItem.getType().equals(Constants.GI_TYPE_AUDIO_OBJECT)) {
			return UploadGeneralItemActivity.MIME_TYPE_AUDIO;
		} else if (generalItem.getType().equals(Constants.GI_TYPE_VIDEO_OBJECT)) {
			return UploadGeneralItemActivity.MIME_TYPE_VIDEO;
		}
		return "";

	}
}
