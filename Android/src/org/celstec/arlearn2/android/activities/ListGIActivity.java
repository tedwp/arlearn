package org.celstec.arlearn2.android.activities;

import java.util.ArrayList;
import java.util.HashMap;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.cache.GameCache;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.genItemActivities.AudiorecorderActivity;
import org.celstec.arlearn2.android.genItemActivities.VideorecorderActivity;
import org.celstec.arlearn2.android.list.GameListAdapter;
import org.celstec.arlearn2.android.list.GameListRecord;
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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class ListGIActivity extends GeneralActivity implements ListitemClickInterface {

	private String CLASSNAME = this.getClass().getName();

	private GeneralItemListAdapter adapter;
	private Game selectedGame = null;
	private GeneralItem selectedGeneralItem = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle extras = getIntent().getExtras();

		if (extras != null) {
			selectedGame = (Game) extras.get("selectedGame");
		}

		setContentView(R.layout.listgeneralitemscreen);
	}

	@Override
	protected void onResume() {
		super.onResume();
		GeneralItemsDelegator.getInstance().fetchMyGeneralItemsFromServer(selectedGame.getGameId(), this);
		
		renderGeneralItemsList();
		
	}

	@Override
	public void onBroadcastMessage(Bundle bundle, boolean render) {
		super.onBroadcastMessage(bundle, render);
		if (render) {
			renderGeneralItemsList();
		}
	}	


	private void renderGeneralItemsList() {
		ArrayList<GenericListRecord> alGenericListRecord = new ArrayList<GenericListRecord>();
		ListView listView = (ListView) findViewById(R.id.listGeneralItems);

		adapter = new GeneralItemListAdapter(this, R.layout.listgeneralitemscreen, alGenericListRecord);
		adapter.setOnListItemClickCallback(this);
		
		HashMap<Long, GeneralItem> hmGeneralItems = new HashMap<Long, GeneralItem>();	
		hmGeneralItems = GeneralItemsCache.getInstance().getGeneralItemsWithGameId(selectedGame.getGameId());
		
		if(hmGeneralItems != null){
						
			// TODO Order list by description
			for (GeneralItem gi : hmGeneralItems.values()) {
				GeneralItemListRecord r = new GeneralItemListRecord(gi);
				adapter.add(r);								
			}
		}
		listView.setAdapter(adapter);
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
			        	   GeneralItemsDelegator.getInstance().deleteGeneralItem(ListGIActivity.this, selectedGeneralItem.getId(), selectedGeneralItem.getGameId());
			        	   //ListGIActivity.this.onResume();
			        	   //Toast.makeText(getApplicationContext(), "lucas", Toast.LENGTH_SHORT).show();
			        	   Log.e(CLASSNAME, "REFRESCAZOOOOOOOOO");
			        	   
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
			intent = new Intent(ListGIActivity.this, NewNarratorItemActivity.class);
			intent.putExtra("generalItem", glr.getGeneralItem());
			intent.putExtra("action", NewNarratorItemActivity.NI_ACTION_EDIT);
			ListGIActivity.this.startActivity(intent);

			break;

		default:
			break;
		}

	}



	public void onButtonNewGeneralItemClick(View v) {

		final CharSequence[] csArrayElems = { "Narrator item", "Multiple choice question", "Record video", "Youtube video", "Record audio" };

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
					intent = new Intent(ListGIActivity.this, NewNarratorItemActivity.class);
					NarratorItem n = new NarratorItem();
					n.setGameId(selectedGame.getGameId());
					intent.putExtra("generalItem", n);
					intent.putExtra("action", NewNarratorItemActivity.NI_ACTION_CREATE);
					ListGIActivity.this.startActivity(intent);
					break;
				case 1:
					// MultipleChoiceTest item
					intent = new Intent(ListGIActivity.this, MultipleChoiceItemActivity.class);
					MultipleChoiceTest m = new MultipleChoiceTest();
					m.setGameId(selectedGame.getGameId());
					intent.putExtra("generalItem", m);
					ListGIActivity.this.startActivity(intent);
					break;
				case 2:
					// VideoObject
					intent = new Intent(ListGIActivity.this, VideorecorderActivity.class);
					VideoObject v = new VideoObject();
					v.setGameId(selectedGame.getGameId());
					intent.putExtra("generalItem", v);
					ListGIActivity.this.startActivity(intent);
					break;
				case 3:
					// Youtube - TO BE DELETED
					
//					intent = new Intent(ListGIActivity.this, NarratorItemActivity.class);
//					intent.putExtra("genItemType", csArrayElems[iCheckBoxIndex]);
//					intent.putExtra("selectedGame", selectedGame);
//					ListGIActivity.this.startActivity(intent);
					
					break;
				case 4:
					// Audio
					intent = new Intent(ListGIActivity.this, AudiorecorderActivity.class);
					AudioObject a = new AudioObject();
					a.setGameId(selectedGame.getGameId());
					intent.putExtra("generalItem", a);
					ListGIActivity.this.startActivity(intent);
					break;
				default:
// Commented by btb					
//					intent = new Intent(ListGIActivity.this, NarratorItemActivity.class);
//					intent.putExtra("genItemType", csArrayElems[iCheckBoxIndex]);
//					intent.putExtra("selectedGame", selectedGame);
//					ListGIActivity.this.startActivity(intent);
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
