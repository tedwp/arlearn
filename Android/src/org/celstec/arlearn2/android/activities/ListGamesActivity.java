package org.celstec.arlearn2.android.activities;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.cache.GameCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.android.list.GameListAdapter;
import org.celstec.arlearn2.android.list.GameListRecord;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.ListitemClickInterface;
import org.celstec.arlearn2.beans.game.Game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ListGamesActivity extends GeneralActivity implements ListitemClickInterface {
	
	Game [] vGames = null;
	ArrayList<String> lGameTitles;
	private GameListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		lGameTitles = new ArrayList<String>();		
		setContentView(R.layout.listgamescreen);
	}

	@Override
	protected void onResume() {
		super.onResume();
		GameDelegator.getInstance().synchronizeGamesWithServer(this);
		renderGamesList();
		
	}
	
	@Override
	public void onBroadcastMessage(Bundle bundle, boolean render) {
		super.onBroadcastMessage(bundle, render);
		if (render) {
			renderGamesList();
		}
	}
	
	private void renderGamesList() {
				
		ArrayList<GenericListRecord> alGenericListRecord = new ArrayList<GenericListRecord>();
		ListView listView = (ListView) findViewById(R.id.listGames);
	
		adapter = new GameListAdapter(this, R.layout.listgamescreen, alGenericListRecord);
		adapter.setOnListItemClickCallback(this);
		
		vGames = GameCache.getInstance().getGames(PropertiesAdapter.getInstance(this).getUsername()).toArray(new Game[]{});
		
		if (vGames != null) {
			if (vGames.length != 0){
				for (Game game: vGames) {
					GameListRecord r = new GameListRecord(game);
					lGameTitles.add(game.getTitle());
					adapter.add(r);
				}				
			}
		}
		listView.setAdapter(adapter);
	}

	@Override
	public void onListItemClick(View v, int position, GenericListRecord genericListRecord) {
			
		Intent intent = null;				
		GameListRecord glr = (GameListRecord) genericListRecord;
		final Game selectedGame = vGames[position];
		long lSelectedGameId = selectedGame.getGameId();
		
		switch (glr.getAction()) {
		case 0:
			// Delete game
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you to delete game "+selectedGame.getTitle()+"?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   GameDelegator.getInstance().deleteGame(ListGamesActivity.this, selectedGame.getGameId());
			           }
			       })
			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
									
			break;
		case 1:
			// Edit game
			intent = new Intent(ListGamesActivity.this, EditGameActivity.class);
			intent.putExtra("selectedGame", vGames[position]);
			ListGamesActivity.this.startActivity(intent);			
			break;
		case 2:
			// Show runs
// Commented by btb			
//			intent = new Intent(ListGamesActivity.this, ListRunsActivity.class);
//			intent.putExtra("selectedGame", vGames[position]);
//			ListGamesActivity.this.startActivity(intent);			
			break;
		case 3:
			// Show General Items
			intent = new Intent(ListGamesActivity.this, ListGIActivity.class);
			intent.putExtra("selectedGame", vGames[position]);
			ListGamesActivity.this.startActivity(intent);			
			break;

		default:
			break;
		}
		
	}	

	public void onButtonNewGameClick(View v) {
		Intent intent = new Intent(ListGamesActivity.this, NewGameActivity.class);
		ListGamesActivity.this.startActivity(intent);			
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