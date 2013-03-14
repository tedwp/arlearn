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

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.list.GeneralItemListAdapter;
import org.celstec.arlearn2.android.list.GeneralItemListRecord;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class SearchGeneralItemActivity extends GeneralActivity {

	private String CLASSNAME = this.getClass().getName();

	private GeneralItemListAdapter adapter;
	private long lGameId = 0L;
	
    private ListView lv;
    private ArrayAdapter<String> arrayAdapter;
    private EditText inputSearch;	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchlistgeneralitemscreen);

		lGameId = getIntent().getExtras().getLong("selectedGameId");
		
		renderGeneralItemsList();
		
//        // Listview Data
//        String products[] = {"Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense", "HTC Sensation XE",
//                                "iPhone 4S", "Samsung Galaxy Note 800",
//                                "Samsung Galaxy S3", "MacBook Air", "Mac Mini", "MacBook Pro"};
//        
//        
//        
//        
//        
//        
//        
// 
//        lv = (ListView) findViewById(R.id.listGeneralItems);
//        inputSearch = (EditText) findViewById(R.id.inputSearch);
// 
//        // Adding items to listview
//        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.item_name, products);
//        lv.setAdapter(arrayAdapter); 		
//			
//        inputSearch.addTextChangedListener(new TextWatcher() {
//        	 
//            @Override
//            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//                // When user changed the Text
//            	SearchGeneralItemActivity.this.arrayAdapter.getFilter().filter(cs);
//            }
//         
//            @Override
//            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//                    int arg3) {
//                // TODO Auto-generated method stub
//         
//            }
//         
//            @Override
//            public void afterTextChanged(Editable arg0) {
//                // TODO Auto-generated method stub
//            }
//
//        });		

	}

	
	
	private void renderGeneralItemsList() {

		
		HashMap<Long, GeneralItem> hmGeneralItems = new HashMap<Long, GeneralItem>();	
		hmGeneralItems = GeneralItemsCache.getInstance().getGeneralItemsButGameId(lGameId);
        String[] products = new String[hmGeneralItems.values().size()];
        int i = 0;
		for (GeneralItem gi : hmGeneralItems.values()) {
			products[i] = gi.getName();
			i++;
		}
        
 
        lv = (ListView) findViewById(R.id.listGeneralItems);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
 
        // Adding items to listview
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.item_name, products);
        lv.setAdapter(arrayAdapter); 		
			
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
		
	
	
	
	
	@Override
	public boolean isGenItemActivity() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	


}
