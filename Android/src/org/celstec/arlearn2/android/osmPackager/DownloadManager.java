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
package org.celstec.arlearn2.android.osmPackager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.osmdroid.tileprovider.util.StreamUtils;

public class DownloadManager {
	
	private final ExecutorService mThreadPool;

	private final Queue<OSMTileInfo> mQueue = new LinkedBlockingQueue<OSMTileInfo>();

	private final String mBaseURL;
	private final String mDestinationURL;

	
	public DownloadManager(final String pBaseURL, final String pDestinationURL, final int mThreads) {
		this.mBaseURL = pBaseURL;
		this.mDestinationURL = pDestinationURL;
		this.mThreadPool = Executors.newFixedThreadPool(mThreads);
	}

	public int getRemaining() {
		return mQueue.size();
	}
	
	public synchronized void add(final OSMTileInfo pTileInfo){
		this.mQueue.add(pTileInfo);
		spawnNewThread();
	}

	private synchronized OSMTileInfo getNext(){
		final OSMTileInfo tile = this.mQueue.poll();

		final int remaining = this.mQueue.size();

		this.notify();
		return tile;
	}

	public synchronized void waitEmpty() throws InterruptedException {
		while(this.mQueue.size() > 0){
			this.wait();
		}
	}

	public void waitFinished() throws InterruptedException {
		waitEmpty();
		this.mThreadPool.shutdown();
		this.mThreadPool.awaitTermination(6, TimeUnit.SECONDS);
	}

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private void spawnNewThread() {
		this.mThreadPool.execute(new DownloadRunner());
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================


	private class DownloadRunner implements Runnable {

		private OSMTileInfo mTileInfo;
		private File mDestinationFile;

		public DownloadRunner() {
		}

		private void init(final OSMTileInfo pTileInfo) {
			this.mTileInfo = pTileInfo;
			/* Create destination file. */
			try {
				final String filename = String.format(DownloadManager.this.mDestinationURL, this.mTileInfo.zoom, this.mTileInfo.x, this.mTileInfo.y);
				this.mDestinationFile = new File(filename);
				final File parent = this.mDestinationFile.getParentFile();
				parent.mkdirs();
			} catch (java.lang.NullPointerException npe) {
				npe.printStackTrace();
			}
		}

		
		public void run() {
			InputStream in = null;
			OutputStream out = null;
			final OSMTileInfo pTileInfo = DownloadManager.this.getNext(); 
			init(pTileInfo);

			if (mDestinationFile == null || mDestinationFile.exists()) {
				return; // TODO issue 70 - make this an option
			}

			final String finalURL = String.format(DownloadManager.this.mBaseURL, this.mTileInfo.zoom, this.mTileInfo.x, this.mTileInfo.y);

			try {
				in = new BufferedInputStream(new URL(finalURL).openStream(), StreamUtils.IO_BUFFER_SIZE);

				final FileOutputStream fileOut = new FileOutputStream(this.mDestinationFile);
				out = new BufferedOutputStream(fileOut, StreamUtils.IO_BUFFER_SIZE);

				StreamUtils.copy(in, out);

				out.flush();
			} catch (final Exception e) {
				System.err.println("Error downloading: '" + this.mTileInfo + "' from URL: " + finalURL + " : " + e);
				DownloadManager.this.add(this.mTileInfo); // try again later
//				mQueue.remove(pTileInfo);
			} finally {
				StreamUtils.closeStream(in);
				StreamUtils.closeStream(out);
			}
		}
	}
}

