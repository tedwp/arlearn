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
 * Contributors: Bernardo Tabuenca
 ******************************************************************************/
package org.celstec.arlearn2.android.activities;

import java.util.ArrayList;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.cache.GameCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.android.list.GameListAdapter;
import org.celstec.arlearn2.android.list.GameListRecord;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.ListitemClickInterface;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GameAccess;

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
		this.setTitle("Mobile Authoring Tool: Your games");
	}

	@Override
	protected void onResume() {
		super.onResume();
		GameDelegator.getInstance().synchronizeMyGamesWithServer(this);
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
				
		vGames = GameCache.getInstance().getGames().toArray(new Game[]{});
				
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

//			intent = new Intent(ListGamesActivity.this, EditGameActivity.class);
//			intent.putExtra("selectedGame", vGames[position]);
//			ListGamesActivity.this.startActivity(intent);
			break;			

		case 2:
			// Show runs
//			intent = new Intent(ListGamesActivity.this, ListRunsActivity.class);
//			intent.putExtra("selectedGame", vGames[position]);
//			ListGamesActivity.this.startActivity(intent);			
			break;
		case 3:

			
			// View game
			Game gAux = vGames[position];
			GameAccess gaAux = new GameAccess();
			gaAux = GameCache.getInstance().getGameAccess(gAux.getGameId());
			
			
			intent = new Intent(ListGamesActivity.this, GameTabActivity.class);
			intent.putExtra("selectedAction", Constants.AUTHORING_ACTION_EDIT);
			intent.putExtra("selectedGame", gAux);
			intent.putExtra("selectedGameAccess", gaAux);
			ListGamesActivity.this.startActivity(intent);			
			break;

		default:
			break;
		}
		
	}	

	public void onButtonNewGameClick(View v) {
		Intent intent = new Intent(ListGamesActivity.this, GameTabActivity.class);
		intent.putExtra("selectedAction", Constants.AUTHORING_ACTION_CREATE);
		ListGamesActivity.this.startActivity(intent);			
	}
	
	
	public void onReload(View v) {
	
		GameDelegator.getInstance().synchronizeMyGamesWithServer(this);
		renderGamesList();
		
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
