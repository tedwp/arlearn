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
package org.celstec.arlearn2.android.list;

import java.util.ArrayList;

import org.celstec.arlearn2.android.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class GameListAdapter extends ArrayAdapter<GenericListRecord> {
	
	private String CLASSNAME = this.getClass().getName();

	private ArrayList<GenericListRecord> games;
	private ListitemClickInterface callback;
	

	public GameListAdapter(Context context, int textViewResourceId, ArrayList<GenericListRecord> theGames) {
		super(context, textViewResourceId, theGames);
		this.games = theGames;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(listLineResourceId(), null);
		}
		GenericListRecord record = games.get(position);
		if (record != null) {
			v = record.getView(v);			
		}
		
		// Click on row		
		v.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {

				GameListRecord glr = (GameListRecord)games.get(position);
				glr.setAction(GameListRecord.GAME_ACTION_EDIT);
				callback.onListItemClick(v, position, glr);	
				
				return false;
			}
		});
			

		
		v.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View vg) {
				Log.d(CLASSNAME, "Clicked row position "+position);
				GameListRecord glr = (GameListRecord)games.get(position);
				glr.setAction(GameListRecord.GAME_ACTION_GENERALITEMS);
				callback.onListItemClick(vg, position, glr);
				
			}
		});


		// Click on delete image
		ImageView ivDelete = (ImageView) v.findViewById(R.id.delete_icon);
		ivDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View ivDelete) {
				Log.d(CLASSNAME, "Clicked delete button position "+position);
				GameListRecord glr = (GameListRecord)games.get(position);
				glr.setAction(GameListRecord.GAME_ACTION_DELETE);
				callback.onListItemClick(ivDelete, position, glr);				
				
			}
		});		
		
		
		// Click on edit general item
//		ImageView ivGenItem = (ImageView) v.findViewById(R.id.edit_gitem_icon);
//		ivGenItem.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View ivGenItem) {
//				Log.d(CLASSNAME, "Clicked edit general item button position "+position);
//				GameListRecord glr = (GameListRecord)games.get(position);
//				glr.setAction(GameListRecord.GAME_ACTION_GENERALITEMS);
//				callback.onListItemClick(ivGenItem, position, glr);				
//				
//			}
//		});

		

		
		return v;
	}
	public void setOnListItemClickCallback(ListitemClickInterface callback) {
		this.callback = callback;
	}

	protected int listLineResourceId() {
		return R.layout.list_game_line;
	}

}
