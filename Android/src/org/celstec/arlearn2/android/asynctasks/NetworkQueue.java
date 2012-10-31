package org.celstec.arlearn2.android.asynctasks;

import java.util.concurrent.CountDownLatch;

import org.celstec.arlearn2.android.asynctasks.network.NetworkTaskHandler;
import org.celstec.arlearn2.android.db.DBAdapter.DatabaseHandler;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

public class NetworkQueue {

	private static CountDownLatch startLatch;
	private static NetworkTaskHandler networkTaskHandler;

	private static Thread thread;
	
	

	public static NetworkTaskHandler getNetworkTaskHandler() {
		if (networkTaskHandler != null) return networkTaskHandler;
		getThread();
    	return getNetworkTaskHandler();
    }
	
	 public static NetworkTaskHandler getThread() {
			startLatch = new CountDownLatch(1);
	    	if (thread == null) {
	    		thread = new NeworkTaskThread();
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
	
	 static class NeworkTaskThread extends Thread {
		 @Override
			public void run() {
				try {
					Looper.prepare();
					networkTaskHandler = new NetworkTaskHandler();
					startLatch.countDown();
					Looper.loop();
				} catch (Throwable t) {
					Log.e("database", "network thread halted", t);
				}
			}
		 
	 }
}
