package org.celstec.arlearn2.android.util;

import java.io.File;
import java.io.IOException;

import org.celstec.arlearn2.android.Constants;

import android.os.Environment;

public class MediaFolders {
	
	public static File getIncommingFilesDir(){
		File sdcard = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath());
		File cacheDirFile = new File(sdcard, Constants.CACHE_DIR);
		if (!cacheDirFile.exists()) cacheDirFile.mkdir();
		File incommingDir = new File(cacheDirFile, Constants.INCOMMING);
		if (!incommingDir.exists()) incommingDir.mkdir();
		return incommingDir;
	}
	
	public static File getOutgoingFilesDir(){
		File sdcard = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath());
		File cacheDirFile = new File(sdcard, Constants.CACHE_DIR);
		if (!cacheDirFile.exists()) cacheDirFile.mkdir();
		File incommingDir = new File(cacheDirFile, Constants.OUTGOING);
		if (!incommingDir.exists()) incommingDir.mkdir();
		return incommingDir;
	}
	
	public static File createOutgoingJpgFile(){
		File outFolder = getOutgoingFilesDir();
		try {
			return File.createTempFile("image", ".jpg", outFolder);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static File createOutgoingAmrFile(){
		File outFolder = getOutgoingFilesDir();
		try {
			return File.createTempFile("recording", ".amr", outFolder);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
