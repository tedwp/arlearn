package org.celstec.arlearn2.android.broadcast;

import java.util.Iterator;

import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.osmPackager.RegionDownloader;
import org.celstec.arlearn2.android.util.MediaFolders;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.beans.game.MapRegion;
import org.celstec.arlearn2.client.GameClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class GameReceiver extends GenericReceiver {

	public static String action = "org.celstec.arlearn2.beans.notification.GameModification";

	@Override
	public void onReceive(final Context context, final Intent intent) {
		new Thread(new Runnable() {
			public void run() {
				syncronizeGames(context);
				updateActivities(context);
			}
		}).start();
	}

	protected void syncronizeGames(Context ctx) {
		DBAdapter db = new DBAdapter(ctx);
		db.openForWrite();
		try { 
			GamesList gl = GameClient.getGameClient().getGames(PropertiesAdapter.getInstance(ctx).getFusionAuthToken());
			if (gl.getError() == null) {
				Iterator<Game> it = gl.getGames().iterator();
				while (it.hasNext()) {
					Game game = it.next();
					db.getGameAdapter().insertGame(game);
//					checkOsmTiles(game);
				}
			}
		} catch (ARLearnException ae){
			if (ae.invalidCredentials()) {
				setStatusToLogout(ctx);
			}
			
		} catch (Exception e) {
			Log.e("exception", e.getMessage(), e);
			
		} finally {
			db.close();
		}				
	}
	
	private void checkOsmTiles(Game game) {
		if (game.getConfig() == null) return;
		if (game.getConfig().getMapRegions() == null) return;
		RegionDownloader rd = new RegionDownloader();
		for (MapRegion mr: game.getConfig().getMapRegions()) {
			rd.addRegion(mr);
		}
		rd.downloadAllRegions("/mnt/sdcard/osmdroid/tiles/MapquestOSM");
		
		
	}
}
