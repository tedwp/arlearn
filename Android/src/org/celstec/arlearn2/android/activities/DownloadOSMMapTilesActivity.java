package org.celstec.arlearn2.android.activities;

import java.util.HashMap;
import java.util.List;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.osmPackager.RegionDownloader;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.MapRegion;
import org.celstec.arlearn2.beans.run.Run;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class DownloadOSMMapTilesActivity extends Activity {

	Button buttonStartProgress;
	ProgressBar progressBar;
	private long runId;
	private List<MapRegion> regions;
	private HashMap<String, MapRegionDownloader> ongoingDownloads = new HashMap<String, MapRegionDownloader>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.osm_downloader);
		runId = getIntent().getLongExtra("runId", 0);
		
		DBAdapter db = new DBAdapter(this);
		db.openForRead();
		try { 
			Run r = (Run) db.getRunAdapter().queryById(""+runId);
			Game g = (Game) db.getGameAdapter().queryById(r.getGameId());
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
		progressBar.setProgress(0);

		buttonStartProgress.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ongoingDownloads.get(regions.toString()) == null) {
					MapRegionDownloader mrd = new MapRegionDownloader();
					mrd.execute();
					buttonStartProgress.setClickable(false);
					ongoingDownloads.put(regions.toString(), mrd);
				}
				
				
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MapRegionDownloader mrd = ongoingDownloads.get(regions.toString()); 
		if (mrd != null) {
			mrd.setProgressBar(progressBar);
		}
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

		@Override
		protected Void doInBackground(Void... params) {
			RegionDownloader rd = new RegionDownloader("/mnt/sdcard/osmdroid/tiles/Mapnik");
			for (MapRegion mr: regions) {
				Log.i("GAME", "adding region"+mr );
				rd.addRegion(mr);
			}
			rd.downloadAllRegions();
			int remaining;
			do {
				SystemClock.sleep(200);
				remaining =rd.getDownloadCount();	
				if (remaining > progressBar.getMax()) progressBar.setMax(remaining);
				publishProgress(progressBar.getMax()-remaining);
			} while (remaining != 0); 
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressBar.setProgress(values[0]);
		}

	}

}
