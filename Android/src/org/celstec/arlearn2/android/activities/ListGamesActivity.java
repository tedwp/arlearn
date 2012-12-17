package org.celstec.arlearn2.android.activities;

import java.util.ArrayList;
import java.util.Iterator;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.cache.GameCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GameAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.list.GameListAdapter;
import org.celstec.arlearn2.android.list.GameListRecord;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.ListitemClickInterface;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.client.GameClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class ListGamesActivity extends GeneralActivity implements ListitemClickInterface {
	
	private String CLASSNAME = this.getClass().getName();

	private Game[] vGames = null;
	private long lSelectedGameId = 0L;
	ArrayList<String> lGameTitles;
	private GameListAdapter adapter;
	private String sOwner = "arlearn5";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		lGameTitles = new ArrayList<String>();		
		setContentView(R.layout.listgamescreen);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		syncronizeLocalDatabaseFromServer(this);
		renderGamesList();
		
	}
	
	@Override
	public void onBroadcastMessage(Bundle bundle, boolean render) {
		super.onBroadcastMessage(bundle, render);
		if (render) {
			renderGamesList();
		}
	}
	
	protected void syncronizeLocalDatabaseFromServer(Context ctx) {
		
		DBAdapter db = new DBAdapter(ctx);
		db.openForWrite();
		try { 
//			GamesList gl = GameCache.getInstance().getGame(0);
			GamesList gl = GameClient.getGameClient().getGames(PropertiesAdapter.getInstance(ctx).getFusionAuthToken());
			if (gl.getError() == null) {

//TOD check this
//				db.getGameAdapter().emptyTable();
				
				Iterator<Game> it = gl.getGames().iterator();
				while (it.hasNext()) {
					Game game = it.next();
					db.getGameAdapter().insertGame(game);
					Log.e("Synchronizing games:", game.getTitle() + ".");
				}
			}
		} catch (ARLearnException ae){
			if (ae.invalidCredentials()) {
			}
			
		} catch (Exception e) {
			Log.e("exception", e.getMessage(), e);
			
		} finally {
			db.close();
		}				
	}

	


	private void renderGamesList() {
				
		ArrayList<GenericListRecord> alGenericListRecord = new ArrayList<GenericListRecord>();
		ListView listView = (ListView) findViewById(R.id.listGames);
	
		adapter = new GameListAdapter(this, R.layout.listgamescreen, alGenericListRecord);
		adapter.setOnListItemClickCallback(this);
		
			
		DBAdapter db = new DBAdapter(this);
		db.openForRead();

		vGames = (Game[]) ((GameAdapter) db.table(DBAdapter.GAME_ADAPTER)).queryByOwner(sOwner);
		
		if (vGames != null) {
			if (vGames.length > 0){
				for (int j = 0; j < vGames.length; j++) {
					GameListRecord r = new GameListRecord(vGames[j]);
					lGameTitles.add(vGames[j].getTitle());
					Log.d(CLASSNAME, "Game ["+j+"] "+vGames[j].getTitle());
					adapter.add(r);
				}				
			}
			
		}
		listView.setAdapter(adapter);

		db.close();

	}

	@Override
	public void onListItemClick(View v, int position, GenericListRecord genericListRecord) {
			
		Intent intent = null;				
		GameListRecord glr = (GameListRecord) genericListRecord;
		lSelectedGameId = vGames[position].getGameId();
		
//		switch (glr.getAction()) {
//		case 0:
//			// Delete game
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setMessage("Are you sure you to delete game "+vGames[position].getTitle()+"?")
//			       .setCancelable(false)
//			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//			           public void onClick(DialogInterface dialog, int id) {
//			        	   
//			        	   	Log.d(CLASSNAME, "Clicked yes "+lSelectedGameId);
//			        	   	Intent in = new Intent(ListGamesActivity.this, DeleteGameActivity.class);
//			    			in.putExtra("selectedGameId", lSelectedGameId);
//			    			ListGamesActivity.this.startActivity(in);
//			           }
//			       })
//			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
//			           public void onClick(DialogInterface dialog, int id) {
//			        	   
//			        	   	Log.d(CLASSNAME, "Clicked NO "+lSelectedGameId);
//			                dialog.cancel();
//			           }
//			       });
//			AlertDialog alert = builder.create();
//			alert.show();
//									
//			break;
//		case 1:
//			// Edit game
//			Log.d(CLASSNAME, "Clicked edit game "+position);
//			intent = new Intent(ListGamesActivity.this, EditGameActivity.class);
//			intent.putExtra("existingGames", lGameTitles);
//			intent.putExtra("selectedGame", vGames[position]);
//			ListGamesActivity.this.startActivity(intent);			
//			break;
//		case 2:
//			// Show runs
//			Log.d(CLASSNAME, "Clicked show runs "+position);
//			intent = new Intent(ListGamesActivity.this, ListRunsActivity.class);
//			intent.putExtra("selectedGame", vGames[position]);
//			ListGamesActivity.this.startActivity(intent);			
//			break;
//		case 3:
//			// Show General Items
//			Log.d(CLASSNAME, "Clicked show g items "+position);
//			intent = new Intent(ListGamesActivity.this, ListGIActivity.class);
//			intent.putExtra("selectedGame", vGames[position]);
//			ListGamesActivity.this.startActivity(intent);			
//			break;
//
//		default:
//			break;
//		}
		
	}	

	public void onButtonNewGameClick(View v) {
//		Intent intent = new Intent(ListGamesActivity.this, NewGameActivity.class);
//		intent.putExtra("existingGames", vGames);
//		ListGamesActivity.this.startActivity(intent);			
	}	


	public boolean isGenItemActivity() {
		return false;
	}

	public boolean showStatusLed() {
		return true;
	}

	@Override
	public boolean setOnLongClickListener(View v, int position, GenericListRecord messageListRecord) {
		return false;
	}

}
