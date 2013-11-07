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
import java.util.HashMap;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.genItemActivities.AudiorecorderActivity;
import org.celstec.arlearn2.android.genItemActivities.PictureItemActivity;
import org.celstec.arlearn2.android.genItemActivities.VideorecorderActivity;
import org.celstec.arlearn2.android.list.GeneralItemListAdapter;
import org.celstec.arlearn2.android.list.GeneralItemListRecord;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.ListitemClickInterface;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.VideoObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class GameItemsActivity extends Activity implements ListitemClickInterface{

	private String CLASSNAME = this.getClass().getName();

	private GeneralItemListAdapter itemsListAdapter;
	private Game selectedGame = null;
	private GeneralItem selectedGeneralItem = null;
	
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_items_tab);
        
		final Bundle extras = getIntent().getExtras();

		if (extras != null) {
			selectedGame = (Game) extras.get("selectedGame");
		}
        
    }	
	

	@Override
	protected void onResume() {
		super.onResume();
		GeneralItemsDelegator.getInstance().synchronizeGeneralItemsWithServer(this, selectedGame.getGameId());

		renderGeneralItemsList();
		
	}	

	/**
	 * Load data from database
	 */
	private void renderGeneralItemsList() {
		ArrayList<GenericListRecord> alGenericListRecord = new ArrayList<GenericListRecord>();
		ListView listView = (ListView) findViewById(R.id.listGeneralItemsTab);

		itemsListAdapter = new GeneralItemListAdapter(this, R.layout.game_items_tab, alGenericListRecord);
		itemsListAdapter.setOnListItemClickCallback(this);
		
		HashMap<Long, GeneralItem> hmGeneralItems = new HashMap<Long, GeneralItem>();	
		hmGeneralItems = GeneralItemsCache.getInstance().getGeneralItemsWithGameId(selectedGame.getGameId());
		
		if(hmGeneralItems != null){
						
			for (GeneralItem gi : hmGeneralItems.values()) {
				GeneralItemListRecord r = new GeneralItemListRecord(gi);
				itemsListAdapter.add(r);								
			}
		}		
		
//		
//		AudioObject gi1 = new AudioObject();
//		gi1.setDescription("Audio item desc 1");
//		gi1.setId(000101l);
//		gi1.setDeleted(false);
//		gi1.setGameId(0l);
//		gi1.setName("Audio item name 1");
//		gi1.setType(Constants.GI_TYPE_AUDIO_OBJECT);
//		
//		VideoObject gi2 = new VideoObject();
//		gi2.setDescription("Video item desc 1");
//		gi2.setId(000102l);
//		gi2.setDeleted(false);
//		gi2.setGameId(0l);
//		gi2.setName("Video item name 1");
//		gi2.setType(Constants.GI_TYPE_VIDEO_OBJECT);
//
//		NarratorItem gi3 = new NarratorItem();
//		gi3.setDescription("Narrator item desc 1");
//		gi3.setId(000103l);
//		gi3.setDeleted(false);
//		gi3.setGameId(0l);
//		gi3.setName("Narrator item name 1");
//		gi3.setType(Constants.GI_TYPE_NARRATOR_ITEM);
//		
//
//		MultipleChoiceTest gi4 = new MultipleChoiceTest();
//		gi4.setDescription("Multiple choice item desc 1");
//		gi4.setId(000104l);
//		gi4.setDeleted(false);
//		gi4.setGameId(0l);
//		gi4.setName("Multiple choid item name 1");
//		gi4.setType(Constants.GI_TYPE_MULTIPLE_CHOICE);
//		
//		
//		
//		GeneralItemListRecord r1 = new GeneralItemListRecord(gi1);
//		GeneralItemListRecord r2 = new GeneralItemListRecord(gi2);
//		GeneralItemListRecord r3 = new GeneralItemListRecord(gi3);
//		GeneralItemListRecord r4 = new GeneralItemListRecord(gi4);
//		
//		
//		itemsListAdapter.add(r1);
//		itemsListAdapter.add(r2);
//		itemsListAdapter.add(r3);
//		itemsListAdapter.add(r4);

		
		listView.setAdapter(itemsListAdapter);
	}

	
	
	@Override
	public void onListItemClick(View v, int position, GenericListRecord genericListRecord) {

		Intent intent = null;
		GeneralItemListRecord glr = (GeneralItemListRecord) genericListRecord;
		selectedGeneralItem = glr.getGeneralItem();
		// intent = getIntent(glr.getAction(), glr.getGeneralItemType(),
		// vGeneralItems[position]);

		switch (glr.getAction()) {
		case 0:
			// Delete generalItem
			Log.d(CLASSNAME, "Clicked delete generalItem " + position);			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you to delete "+selectedGeneralItem.getName()+"?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   GeneralItemsDelegator.getInstance().deleteGeneralItem(GameItemsActivity.this, selectedGeneralItem.getId(), selectedGeneralItem.getGameId());
			        	   //GameItemsActivity.this.onResume();
			        	   //Toast.makeText(getApplicationContext(), "lucas", Toast.LENGTH_SHORT).show();	        	   
			        	   // TODO refresh list

			        	   
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
			// Edit generalItem
			Log.d(CLASSNAME, "Clicked edit generalItem " + position);
			
			if(selectedGeneralItem.getType().equals(Constants.GI_TYPE_PICTURE)){
				intent = new Intent(GameItemsActivity.this, PictureItemActivity.class);
			}else if(selectedGeneralItem.getType().equals(Constants.GI_TYPE_VIDEO_OBJECT)){
				intent = new Intent(GameItemsActivity.this, VideorecorderActivity.class);
			}else if(selectedGeneralItem.getType().equals(Constants.GI_TYPE_AUDIO_OBJECT)){
				intent = new Intent(GameItemsActivity.this, AudiorecorderActivity.class);
			}else if(selectedGeneralItem.getType().equals(Constants.GI_TYPE_MULTIPLE_CHOICE)){
				intent = new Intent(GameItemsActivity.this, MultipleChoiceItemActivity.class);				
			}else{
				intent = new Intent(GameItemsActivity.this, NewNarratorItemActivity.class);
			}
			
			
			
			intent.putExtra("generalItem", glr.getGeneralItem());
			intent.putExtra("action", NewNarratorItemActivity.NI_ACTION_EDIT);
			GameItemsActivity.this.startActivity(intent);

			break;

		default:
			break;
		}

	}


	public void onButtonSearchGeneralItemClick(View v) {
		
		Intent intent = null;
		intent = new Intent(GameItemsActivity.this, SearchGeneralItemActivity.class);
		intent.putExtra("selectedGameId", selectedGame.getGameId());
		GameItemsActivity.this.startActivity(intent);					
		
		
	}

	
	public void onButtonNewGeneralItemClick(View v) {

		final CharSequence[] csArrayElems = { "Narrator item", "Photo xxx", "Video object", "Audio object", "Multiple choice test", "Single choice test", "Youtube Object", "Scan tag"  };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose an item");
		builder.setSingleChoiceItems(csArrayElems, -1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int iCheckBoxIndex) {
				Log.d(CLASSNAME, "Clicked item " + iCheckBoxIndex);
				// Toast.makeText(getApplicationContext(),
				// csArrayElems[iCheckBoxIndex], Toast.LENGTH_SHORT).show();

				// finish();
				Intent intent = null;
				switch (iCheckBoxIndex) {
				case 0:
					// Narrator item			
					intent = new Intent(GameItemsActivity.this, NewNarratorItemActivity.class);
					NarratorItem n = new NarratorItem();
					n.setGameId(selectedGame.getGameId());
					intent.putExtra("generalItem", n);
					intent.putExtra("action", NewNarratorItemActivity.NI_ACTION_CREATE);
					GameItemsActivity.this.startActivity(intent);
					break;
				case 1:
					// Photo item
					intent = new Intent(GameItemsActivity.this, PictureItemActivity.class);
					NarratorItem pi = new NarratorItem();
					pi.setGameId(selectedGame.getGameId());
					intent.putExtra("generalItem", pi);
					GameItemsActivity.this.startActivity(intent);
					break;
				case 2:
					// VideoObject
					intent = new Intent(GameItemsActivity.this, VideorecorderActivity.class);
					VideoObject v = new VideoObject();
					v.setGameId(selectedGame.getGameId());
					intent.putExtra("generalItem", v);
					GameItemsActivity.this.startActivity(intent);
					break;
				case 3:
					// Audio
					intent = new Intent(GameItemsActivity.this, AudiorecorderActivity.class);
					AudioObject a = new AudioObject();
					a.setGameId(selectedGame.getGameId());
					intent.putExtra("generalItem", a);
					GameItemsActivity.this.startActivity(intent);
					break;
				case 4:
					// MultipleChoiceTest item
					intent = new Intent(GameItemsActivity.this, MultipleChoiceItemActivity.class);
					MultipleChoiceTest m = new MultipleChoiceTest();
					m.setGameId(selectedGame.getGameId());
					intent.putExtra("generalItem", m);
					GameItemsActivity.this.startActivity(intent);
					break;					
				default:
// Commented by btb					
//					intent = new Intent(GameItemsActivity.this, NarratorItemActivity.class);
//					intent.putExtra("genItemType", csArrayElems[iCheckBoxIndex]);
//					intent.putExtra("selectedGame", selectedGame);
//					GameItemsActivity.this.startActivity(intent);
					break;
				}

				dialog.dismiss();

			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	public boolean isGenItemActivity() {
		return false;
	}

	public boolean showStatusLed() {
		return true;
	}

	@Override
	public boolean setOnLongClickListener(View v, int position,
			GenericListRecord messageListRecord) {
		// TODO Auto-generated method stub
		return false;
	}

    
    
    
}
