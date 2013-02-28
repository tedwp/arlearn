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
package org.celstec.arlearn2.android.activities;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.GenericMessageListAdapter;
import org.celstec.arlearn2.android.osmPackager.RegionDownloader;
import org.celstec.arlearn2.android.util.MediaFolders;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.MapRegion;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.client.RunClient;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class DownloadOSMMapTilesActivity extends Activity {

	Button buttonStartProgress;
	ProgressBar progressBar;
	private long runId;
	private long gameId;
//	private List<MapRegion> regions;
	private List<MapRegion> regions = new ArrayList<MapRegion>();
	private static HashMap<String, MapRegionDownloader> ongoingDownloads; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.osm_downloader);
		runId = getIntent().getLongExtra("runId", 0);
		
		DBAdapter db = new DBAdapter(this);
		db.openForRead();
		try { 
			Run r = (Run) db.getRunAdapter().queryById(""+runId);
			gameId = r.getGameId();
			Game g = (Game) db.getGameAdapter().queryById(gameId);
			if (g.getConfig() != null) {
					regions = g.getConfig().getMapRegions();	
			}
			
		} catch (Exception e) {
			Log.e("exception", e.getMessage(), e);
			
		} finally {
			db.close();
		}				
		buttonStartProgress = (Button) findViewById(R.id.startprogress);
		progressBar = (ProgressBar) findViewById(R.id.progressbar_Horizontal);
		progressBar.setVisibility(View.GONE);

		progressBar.setProgress(0);

		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (ongoingDownloads == null) ongoingDownloads = new HashMap<String, MapRegionDownloader>();
		if (ongoingDownloads.get(regions.toString()) == null) {
			buttonStartProgress.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (ongoingDownloads.get(regions.toString()) == null) {
						progressBar.setVisibility(View.VISIBLE);
						MapRegionDownloader mrd = new MapRegionDownloader();
						mrd.setProgressBar(progressBar);
						mrd.execute();
						buttonStartProgress.setClickable(false);
						ongoingDownloads.put(regions.toString(), mrd);
					}
				}
			});
			} else {
				ongoingDownloads.get(regions.toString()).setProgressBar(progressBar);
				progressBar.setVisibility(View.VISIBLE);
			}
		
		renderDownloadedMaps();
	}
	
//	@Override
//	public void onSaveInstanceState(Bundle savedInstanceState) {
//		super.onSaveInstanceState(savedInstanceState);
//		savedInstanceState.putSerializable("ongoingDownloads", ongoingDownloads);
//	}
//	
//	@Override
//	public void onRestoreInstanceState(Bundle savedInstanceState) {
//	  super.onRestoreInstanceState(savedInstanceState);
//	  ongoingDownloads = (HashMap<String, MapRegionDownloader>) savedInstanceState.getSerializable("ongoingDownloads"); 
//	}
	
	private String getFileSize(File file) {
		if (file.length() < 1024)
			return file.length() + " bytes";
		long length = file.length() / 1024;
		if (length < 1024)
			return length + " kB";
		length = length / 1024;
		if (length < 1024)
			return length + " MB";
		length = length / 1024;
		return length + " GB";

	}
	
	private void renderDownloadedMaps() {
		ArrayList<GenericListRecord> fileList = new ArrayList<GenericListRecord>();
		final File[] files = getFileList();
		for (File file: files) {
			GenericListRecord r = new GenericListRecord();
			r.setImageResourceId(R.drawable.map_icon);
			r.setMessageHeader(file.getName());
			r.setMessageDetail(getFileSize(file));
			fileList.add(r);
		}
		
		LinearLayout listView = (LinearLayout) findViewById(R.id.downloadedMapsList); 
		GenericMessageListAdapter adapter = new GenericMessageListAdapter(this,R.layout.osm_downloader, fileList);
		listView.removeAllViews();
		for (int i = 0 ; i< adapter.getCount(); i++) {
			View v = adapter.getView(i, null, listView);
			listView.addView(v);
			final int id = i;
			v.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(DownloadOSMMapTilesActivity.this);
					builder.setMessage(getString(R.string.deleteMap)+" "+files[id].getName()).setCancelable(false);
					
					builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
					builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id1) {
							files[id].delete();				
							dialog.cancel();
							renderDownloadedMaps();
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
			});
		}
	}
	
	private File getOsmDroidFolder() {
		return new File("/mnt/sdcard/osmdroid/");
	}
	
	private File[] getFileList() {
		return getOsmDroidFolder().listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isFile() && f.getName().endsWith(".zip");
			}
		});
	}

	public class MapRegionDownloader extends AsyncTask<Void, Integer, Void> {

		int myProgress;
		
		ProgressBar progressBar;

		
		public void setProgressBar(ProgressBar pBar) {
			progressBar = pBar;
		}

		@Override
		protected void onPostExecute(Void result) {
			buttonStartProgress.setClickable(true);
		}

		@Override
		protected void onPreExecute() {
			myProgress = 0;
		}

		protected Void doInBackground(Void... params) {
			File folder = new File("/mnt/sdcard/"+System.currentTimeMillis());
			folder.mkdir();
			File folderNew = new File(folder, "Mapnik");
			folderNew.mkdir();
			try {
			RegionDownloader rd = new RegionDownloader(folderNew.getAbsolutePath());
			for (MapRegion mr: regions) {
				rd.addRegion(mr);
			}
			rd.downloadAllRegions();
				int remaining;
				do {
					SystemClock.sleep(200);
					remaining = rd.getDownloadCount();
					if (remaining > progressBar.getMax())
						progressBar.setMax(remaining);
					publishProgress(progressBar.getMax() - remaining);
				} while (remaining != 0);
				RegionDownloader.zipFolderToFile(new File("/mnt/sdcard/osmdroid/" + gameId + ".zip"), folderNew);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				FileUtils.deleteDirectory(folder);
			} catch (IOException e) {
				e.printStackTrace();
			}
			publishProgress(-1);
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if (values[0]==-1) {
				progressBar.setVisibility(View.GONE);
				renderDownloadedMaps();
			} else {
				progressBar.setProgress(values[0]);	
			}
			
		}

	}

}
