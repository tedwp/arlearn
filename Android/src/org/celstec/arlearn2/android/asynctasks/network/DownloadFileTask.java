package org.celstec.arlearn2.android.asynctasks.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class DownloadFileTask implements NetworkTask{

	public String url;
	public Context ctx;
	public String itemId;
	
	@Override
	public void execute() {
		try {
			String localPath = downloadFile();
		} catch (FileNotFoundException fnf) {
			
			DBAdapter.getAdapter(ctx).getMediaCache().setReplicationStatus(itemId, MediaCache.REP_STATUS_TODO);
			DBAdapter.getAdapter(ctx).getMediaCache().updateLocalPath(itemId, null);
		}
	}
	
	private String downloadFile() throws FileNotFoundException {
		try {
			URL myFileUrl = new URL(url);
			File outputFile = urlToCacheFile(url);
			HttpURLConnection.setFollowRedirects(false); // new
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			if (conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
				url = conn.getHeaderField("location");
				conn.disconnect();
				return downloadFile();
			}
			conn.connect();

			
			InputStream is = conn.getInputStream();
			// File cacheDir = getCacheDir();
			String cLength = conn.getHeaderField("Content-Length");
			int contentLength = 0;

			if (cLength != null) {
				 contentLength = Integer.parseInt(cLength);
				 DBAdapter.getAdapter(ctx).getMediaCache().registerTotalAmountofBytes(url, contentLength);
			}
			
			FileOutputStream fos = new FileOutputStream(outputFile);
			int len1;
			long byteCounter = 0;
			byte[] buffer = new byte[1024];
			long startTime= System.currentTimeMillis();
			while ((len1 = is.read(buffer)) > 0) {
				fos.write(buffer, 0, len1);
				byteCounter +=1024;
				if ((startTime + 1500)<System.currentTimeMillis()) {
					startTime = System.currentTimeMillis();
					DBAdapter.getAdapter(ctx).getMediaCache().registerBytesAvailable(url, contentLength-byteCounter);
				}
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
	
	private File urlToCacheFile(String url) {
		String urlRet = url.hashCode() + getURLSuffix(url);
		if (urlRet.contains("?"))
			urlRet = urlRet.substring(0, urlRet.indexOf("?"));
		return new File(getCacheDir2(), urlRet);

	}
	
	private File getCacheDir2() {
		File sdcard = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		File cacheDirFile = new File(sdcard, Constants.CACHE_DIR);
		if (!cacheDirFile.exists())
			cacheDirFile.mkdir();
		File incommingDir = new File(cacheDirFile, Constants.INCOMMING);
		if (!incommingDir.exists())
			incommingDir.mkdir();
		return incommingDir;
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

}
