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
import org.celstec.arlearn2.android.activities.ListOpenRunsActivity.QueryRuns;
import org.celstec.arlearn2.android.broadcast.NetworkSwitcher;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.list.GeneralItemListAdapter;
import org.celstec.arlearn2.android.list.GeneralItemListRecord;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.ListitemClickInterface;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.celstec.arlearn2.client.GeneralItemClient;
import org.celstec.arlearn2.client.RunClient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//public class SearchGeneralItemActivity extends GeneralActivity implements ListitemClickInterface {
public class SearchGeneralItemActivity extends GeneralActivity {

	private String CLASSNAME = this.getClass().getName();

	private GeneralItemList gil;
//	private GeneralItem newGeneralItem;
	private GeneralItem selectedGeneralItem;
	private long lGameId = 0L;
	private Context ctx;

	
    private ListView lv;
    private ArrayAdapter<String> arrayAdapter;
    private EditText inputSearch;	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchlistgeneralitemscreen);
		ctx = this;

		lGameId = getIntent().getExtras().getLong("selectedGameId");
		
        lv = (ListView) findViewById(R.id.listGeneralItems);
        lv.setOnItemClickListener(new OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView parent, View view,int position, long _id){
        		
        		
        		GeneralItem g = gil.getGeneralItems().get(position);
        		
        		new QueryGeneralItem().execute(g.getId());
        		
        		
        		
        		//GeneralItem gi = GeneralItemsDelegator.getInstance().getGeneralItem(g.getId());
        		
        		
//        		NarratorItem ni = new NarratorItem();
//        		
//        		ni.setName(g.getName());
//        		ni.setDescription("Test description "+g.getName());
//        		ni.setType(Constants.GI_TYPE_NARRATOR_ITEM);
//        		ni.setGameId(lGameId);
//        		ni.setScope("user");
//        		ni.setAutoLaunch(false);
//        		ni.setRichText("");
//    			OpenQuestion oq = new OpenQuestion();
//    			oq.setWithAudio(false);
//    			oq.setWithPicture(true);
//    			oq.setWithVideo(false);
//    			ni.setOpenQuestion(oq);
//    			
//    			newGeneralItem = (GeneralItem) ni;
  			
    			Log.d(CLASSNAME, "Clicked delete generalItem " + position);			
    			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
    			builder.setMessage("Are you sure to reuse mOER '"+g.getName()+"' to Liege'?")
    			       .setCancelable(false)
    			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    			           public void onClick(DialogInterface dialog, int id) {   
    			        	   
    			        	   selectedGeneralItem.setGameId(lGameId);
    			        	   selectedGeneralItem.setId(0l); // TODO check with Stefaan whether general items are created in server when id is 0
    						
    			        	   GeneralItemsDelegator.getInstance().createGeneralItem(ctx, selectedGeneralItem);
    			        	   SearchGeneralItemActivity.this.finish();

    			        	   
    			           }
    			       })
    			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
    			           public void onClick(DialogInterface dialog, int id) {
    			                dialog.cancel();
    			           }
    			       });
    			AlertDialog alert = builder.create();
    			alert.show();        		
        		
        		
        		
        		
        		
        		

        	}
        }); 
        
        inputSearch = (EditText) findViewById(R.id.inputSearch);
		
        inputSearch.addTextChangedListener(new TextWatcher() {
        	 
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            	SearchGeneralItemActivity.this.arrayAdapter.getFilter().filter(cs);
            }
         
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
         
            }
         
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }

        });		

	}
	
	
	public class QueryGeneralItem extends AsyncTask<Object, GeneralItem, Void> {

		@Override
		protected Void doInBackground(Object... arg0) {
			try {
				if (NetworkSwitcher.isOnline(SearchGeneralItemActivity.this)) {
					// TODO pending to create this method from Stefaan
					GeneralItem g = GeneralItemClient.getGeneralItemClient().getGeneralItem(getMenuHandler().getPropertiesAdapter().getFusionAuthToken(), (Long)arg0[0]);
					publishProgress(g);
				} else {
					publishProgress();
				}
			} catch (Exception e) {
				publishProgress();

			}
			return null;
		}

		@Override
		protected void onProgressUpdate(GeneralItem...theGeneralItem) {
			if (theGeneralItem.length == 0) {
				Toast.makeText(SearchGeneralItemActivity.this, getString(R.string.networkUnavailable), Toast.LENGTH_LONG).show();
			} else {
				selectedGeneralItem = theGeneralItem[0];
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}	
	

	@Override
	protected void onResume() {
		super.onResume();
		GameDelegator.getInstance().synchronizeMyGamesWithServer(this);
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
		

		GeneralItemsDelegator.getInstance().getGeneralItems(this, "");

		if (GeneralItemsDelegator.getInstance().getGISearchList() != null){
			
			gil = GeneralItemsDelegator.getInstance().getGISearchList();
			
	        String[] products = new String[gil.getGeneralItems().size()];
	        int i = 0;
			for (GeneralItem gi : gil.getGeneralItems()) {
				products[i] = gi.getName();
				i++;
			}
	        
	 

	 
	        // Adding items to listview
	        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.search_item_name, products);
	        lv.setAdapter(arrayAdapter); 		
				
		
			
		}
		
	}
	

	
	
	/**
	 * This list will replace the previous one when GeneralItems retrieved are
	 * including information about type, description ...
	 * 
	 */
//	private void renderGeneralItemsListAux() {
//
//		GeneralItemsDelegator.getInstance().getGeneralItems(this, "");
//
//		if (GeneralItemsDelegator.getInstance().getGISearchList() != null){
//			
//			ArrayList<GenericListRecord> alGenericListRecord = new ArrayList<GenericListRecord>();
//			ListView listView = (ListView) findViewById(R.id.listGeneralItems);
//
//			adapter = new GeneralItemListAdapter(this, R.layout.searchlistgeneralitemscreen, alGenericListRecord);
//			adapter.setOnListItemClickCallback(this);
//
//			
//			gil = GeneralItemsDelegator.getInstance().getGISearchList();
//			
//			for (GeneralItem gi : gil.getGeneralItems()) {
//				GeneralItemListRecord r = new GeneralItemListRecord(gi);
//				adapter.add(r);								
//			}
//			listView.setAdapter(adapter);			
//			
//	        
//		}
//
//          
//        
//	}
//		
//	
//
//	@Override
//	public void onListItemClick(View v, int position,
//			GenericListRecord messageListRecord) {
//		
//		System.out.println("Clicked on postion"+position);
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public boolean setOnLongClickListener(View v, int position,
//			GenericListRecord messageListRecord) {
//		System.out.println("Clicked on postion"+position);
//		// TODO Auto-generated method stub
//		return false;
//	}
	
	
	
	@Override
	public boolean isGenItemActivity() {
		// TODO Auto-generated method stub
		return false;
	}

}
