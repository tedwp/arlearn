package org.celstec.arlearn2.android.sync;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.DataOutputStream;
import java.io.FileInputStream;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.beans.MediaCacheItem;
import org.celstec.arlearn2.client.GenericClient;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class MediaCacheSyncroniser extends GenericSyncroniser {

	final static String end = "\r\n";
	final static String twoHyphens = "--";
	final static String boundary = "END_OF_PART";

	private static MediaCacheSyncroniser instance = null;

	public static MediaCacheSyncroniser getInstance() {
		return instance;
	}

	public MediaCacheSyncroniser(Context ctx) {
		super(ctx);
		instance = this;
	}

	public void runAuthenticated() {
//		boolean increase = updateCache();
		boolean increase = true;
		if (increase) {
			increaseDelay();
		} else {
			resetDelay();
		}
	}

//	private boolean updateCache() {
//		DBAdapter db = new DBAdapter(ctx);
//		db.openForWrite();
//		MediaCache mc = ((MediaCache) db.table(DBAdapter.MEDIA_CACHE));
//		MediaCacheItem mci = null;
//		boolean succesful = false;
//		boolean returnvalue = true;
//		do {
//			mci = mc.getNextUnsyncedItem();
//			succesful = false;
//			if (mci != null && mci.isIncomming()) {
//				try {
//					String localPath = downloadFile(mci.getRemoteFile());
//					mc.updateLocalPath(mci.getItemId(), localPath);
//					succesful = true;
//					returnvalue = false;
//				} catch (FileNotFoundException fnf) {
//					mc.updateLocalPath(mci.getItemId(), null);
//				}
//			}
//			if (mci != null && !mci.isIncomming()) {
//				File localFile = new File(mci.getLocalFile());
//				succesful = publishData(mci.getRunId(), mci.getAccount(), localFile, mci.getToken(), mci.getMimetype());
//				if (succesful) {
//					mc.updateRemotePath(mci.getItemId(), mci.buildRemotePath(mci.getUri()));
//					returnvalue = false;;
//				}
//			}
//		} while (mci != null && succesful);
//		db.close();
//		return returnvalue;
//	}

	private boolean publishData(Long runId, String account, File file, String token, String mimeType) {

		try {
			URL url = new URL(GenericClient.urlPrefix + "/uploadService");

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

			DataOutputStream ds = new DataOutputStream(conn.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; name=\"runId\"" + end + end + runId + end);

			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; name=\"account\"" + end + end + account + end);

			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; name=\"files\"; filename=\"" + file.getName() + "\"" + end + "Content-Type: " + mimeType + end);
			ds.writeBytes(end);

			FileInputStream fStream = new FileInputStream(file);
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;

			while ((length = fStream.read(buffer)) != -1) {
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			ds.close();

			conn.getInputStream();
			return true;
		} catch (IOException e) {
			Log.e("exception", e.getMessage(), e);
		}
		return false;
	}

	private String downloadFile(String url) throws FileNotFoundException {
		try {
			URL myFileUrl = new URL(url);
			File outputFile = urlToCacheFile(url);
			HttpURLConnection.setFollowRedirects(false); // new
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			// if (url.contains("sites.google.com") && token !=null) {
			// conn.setRequestProperty("Authorization","GoogleLogin auth=" +
			// token);
			// }
			if (conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
				String newUrl = conn.getHeaderField("location");
				conn.disconnect();
				return downloadFile(newUrl);
			}
			conn.connect();

			InputStream is = conn.getInputStream();
			// File cacheDir = getCacheDir();

			FileOutputStream fos = new FileOutputStream(outputFile);
			int len1;
			byte[] buffer = new byte[1024];
			while ((len1 = is.read(buffer)) > 0) {
				fos.write(buffer, 0, len1);
			}
			fos.flush();
			fos.close();
			is.close();
			return outputFile.getAbsolutePath();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			Log.e("error while retrieve media item - addToCache", e.getMessage(), e);
		}
		return null;
	}

	private File getCacheDir() {
		File sdcard = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		File cacheDirFile = new File(sdcard, Constants.CACHE_DIR);
		if (!cacheDirFile.exists())
			cacheDirFile.mkdir();
		File incommingDir = new File(cacheDirFile, Constants.INCOMMING);
		if (!incommingDir.exists())
			incommingDir.mkdir();
		return incommingDir;
	}

	private File urlToCacheFile(String url) {
		String urlRet = url.hashCode() + getURLSuffix(url);
		if (urlRet.contains("?"))
			urlRet = urlRet.substring(0, urlRet.indexOf("?"));
		return new File(getCacheDir(), urlRet);

	}

	private String getURLSuffix(String url) {
		String suffix = "";
		int index = url.lastIndexOf("/");
		if (index != -1) {
			suffix = url.substring(index, url.length());
		} else {
			return "";
		}
		if (suffix.contains("/") || suffix.contains("\\")) {
			return "";
		}
		return suffix;
	}
	
	public void increaseDelay() {
		if (delay < 60) {
			delay *= 2;
		}
	}
}
