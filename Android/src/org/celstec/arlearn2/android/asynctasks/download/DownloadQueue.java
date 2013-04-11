package org.celstec.arlearn2.android.asynctasks.download;

import java.util.concurrent.CountDownLatch;


import android.os.Looper;
import android.util.Log;

public class DownloadQueue {
	private static CountDownLatch startLatch;
	private static DownloadTaskHandler networkTaskHandler;

	private static Thread thread;
	
	

	public static DownloadTaskHandler getNetworkTaskHandler() {
		if (networkTaskHandler != null) return networkTaskHandler;
		getThread();
    	return getNetworkTaskHandler();
    }
	
	 public static DownloadTaskHandler getThread() {
			startLatch = new CountDownLatch(1);
	    	if (thread == null) {
	    		thread = new DownloadUploadTaskThread();
	    		thread.start();
	    	} else {
	    		startLatch.countDown();
	    	}
	    	try {
				startLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	return networkTaskHandler;
	    	
	    }
	
	 static class DownloadUploadTaskThread extends Thread {
		 @Override
			public void run() {
				try {
					Looper.prepare();
					networkTaskHandler = new DownloadTaskHandler();
					startLatch.countDown();
					Looper.loop();
				} catch (Throwable t) {
					Log.e("database", "network thread halted", t);
				}
			}
		 
	 }
}
