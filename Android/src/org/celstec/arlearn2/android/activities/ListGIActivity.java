package org.celstec.arlearn2.android.activities;

import java.util.ArrayList;
import java.util.Iterator;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemGameAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.genItemActivities.AudiorecorderActivity;
import org.celstec.arlearn2.android.genItemActivities.VideorecorderActivity;
import org.celstec.arlearn2.android.list.GeneralItemListAdapter;
import org.celstec.arlearn2.android.list.GeneralItemListRecord;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.ListitemClickInterface;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.celstec.arlearn2.client.GeneralItemClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class ListGIActivity extends GeneralActivity implements ListitemClickInterface {

	private String CLASSNAME = this.getClass().getName();

	private GeneralItem[] vGeneralItems = null;
	private GeneralItemListAdapter adapter;
	private Game selectedGame = null;

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
		syncronizeLocalDatabaseFromServer(this);
		doLocalDBQueryAndRender();
	}

// Commented by btb	
//	@Override
//	public void onBroadcastMessage(Bundle bundle) {
//		Log.e("BROADCAST", "list general items broadcast ");
//
//		super.onBroadcastMessage(bundle, false);
//		doLocalDBQueryAndRender();
//
//	}

	protected void syncronizeLocalDatabaseFromServer(Context ctx) {

		DBAdapter db = new DBAdapter(ctx);
		db.openForWrite();

		try {
			// Take into account that this query is retrieving all GIs (also with deleted = true)
			GeneralItemList gl = GeneralItemClient.getGeneralItemClient().getGameGeneralItems(PropertiesAdapter.getInstance(ctx).getFusionAuthToken(), selectedGame.getGameId());
			// Commented by btb
			//GeneralItemList gl = GeneralItemClient.getGeneralItemClient().getGeneralItemsByGameId(PropertiesAdapter.getInstance(ctx).getFusionAuthToken(), selectedGame.getGameId());
			if (gl.getError() == null) {

				db.getGeneralItemGameAdapter().emptyTable();
				Iterator<GeneralItem> it = gl.getGeneralItems().iterator();
				while (it.hasNext()) {
					GeneralItem g = it.next();
					if (!g.getDeleted()){
						db.getGeneralItemGameAdapter().insert(g);
						Log.d("Synchronizing db lite with DB.  general item:", g.getId() + ".");						
					}
				}

			}
		} catch (ARLearnException ae) {
			if (ae.invalidCredentials()) {
			}

		} catch (Exception e) {
			Log.e(CLASSNAME, "exception"+e.getMessage(), e);

		} finally {
			db.close();
		}
	}

	private void doLocalDBQueryAndRender() {

		if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
			this.finish();
		} else {
			Long runId = getMenuHandler().getPropertiesAdapter().getCurrentRunId();
			if (runId != null) {
				DBAdapter db = new DBAdapter(this);
				db.openForRead();

				vGeneralItems = (GeneralItem[]) ((GeneralItemGameAdapter) db.table(DBAdapter.GENERALITEM_GAME_ADAPTER)).queryByGameId(selectedGame.getGameId());
				// Log.d(CLASSNAME,
				// "Elems en estructura despues de consultar bd: " +
				// vGeneralItems.length);

				db.close();
				renderGeneralItemsList();
			}
		}

	}

	private void renderGeneralItemsList() {
		ArrayList<GenericListRecord> alGenericListRecord = new ArrayList<GenericListRecord>();
		ListView listView = (ListView) findViewById(R.id.listGeneralItems);

		adapter = new GeneralItemListAdapter(this, R.layout.listgeneralitemscreen, alGenericListRecord);
		adapter.setOnListItemClickCallback(this);
		listView.setAdapter(adapter);

		for (int j = 0; (j < vGeneralItems.length) && (vGeneralItems[j] != null); j++) {
			GeneralItemListRecord r = new GeneralItemListRecord(vGeneralItems[j]);
			adapter.add(r);
		}

	}

	@Override
	public void onListItemClick(View v, int position, GenericListRecord genericListRecord) {

		Intent intent = null;
		GeneralItemListRecord glr = (GeneralItemListRecord) genericListRecord;
		// intent = getIntent(glr.getAction(), glr.getGeneralItemType(),
		// vGeneralItems[position]);

		switch (glr.getAction()) {
		case 0:
			// Delete generalItem
			Log.d(CLASSNAME, "Clicked delete generalItem " + position);
// Commented by btb			
//			intent = new Intent(ListGIActivity.this, DeleteGeneralItemActivity.class);
//			intent.putExtra("generalItem", vGeneralItems[position]);
//			intent.putExtra("action", glr.getAction());
//			ListGIActivity.this.startActivity(intent);

			break;
		case 1:
			// Edit generalItem
			Log.d(CLASSNAME, "Clicked edit generalItem " + position);
			
// Commented by btb			
//			intent = new Intent(ListGIActivity.this, NarratorItemActivity.class);
//			intent.putExtra("generalItem", vGeneralItems[position]);
//			intent.putExtra("action", NarratorItemActivity.NI_ACTION_EDIT);
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
// Commented by btb					
//					intent = new Intent(ListGIActivity.this, NarratorItemActivity.class);
//					NarratorItem n = new NarratorItem();
//					n.setGameId(selectedGame.getGameId());
//					intent.putExtra("generalItem", n);
//					intent.putExtra("action", NarratorItemActivity.NI_ACTION_CREATE);
//					ListGIActivity.this.startActivity(intent);
					break;
				case 1:
					// MultipleChoiceTest item
// Commented by btb					
//					intent = new Intent(ListGIActivity.this, MultipleChoiceItemActivity.class);
//					MultipleChoiceTest m = new MultipleChoiceTest();
//					m.setGameId(selectedGame.getGameId());
//					intent.putExtra("generalItem", m);
//					ListGIActivity.this.startActivity(intent);
					break;
				case 2:
					// VideoObject
// Commented by btb					
//					intent = new Intent(ListGIActivity.this, VideorecorderActivity.class);
//					VideoObject v = new VideoObject();
//					v.setGameId(selectedGame.getGameId());
//					intent.putExtra("generalItem", v);
//					ListGIActivity.this.startActivity(intent);
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
// Commented by btb					
//					intent = new Intent(ListGIActivity.this, AudiorecorderActivity.class);
//					AudioObject a = new AudioObject();
//					a.setGameId(selectedGame.getGameId());
//					intent.putExtra("generalItem", a);
//					ListGIActivity.this.startActivity(intent);
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
