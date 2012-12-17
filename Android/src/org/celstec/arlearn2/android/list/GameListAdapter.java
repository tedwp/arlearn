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
		v.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(CLASSNAME, "Clicked row position "+position);
				GameListRecord glr = (GameListRecord)games.get(position);
				glr.setAction(GameListRecord.GAME_ACTION_EDIT);
				callback.onListItemClick(v, position, glr);
				
			}
		});


		// Click on edit runs image
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
		ImageView ivGenItem = (ImageView) v.findViewById(R.id.edit_gitem_icon);
		ivGenItem.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View ivGenItem) {
				Log.d(CLASSNAME, "Clicked edit general item button position "+position);
				GameListRecord glr = (GameListRecord)games.get(position);
				glr.setAction(GameListRecord.GAME_ACTION_GENERALITEMS);
				callback.onListItemClick(ivGenItem, position, glr);				
				
			}
		});

		
		// Click on edit runs image
		ImageView ivTeams = (ImageView) v.findViewById(R.id.runs_icon);
		ivTeams.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View ivTeams) {
				Log.d(CLASSNAME, "Clicked edit users button position "+position);
				GameListRecord glr = (GameListRecord)games.get(position);
				glr.setAction(GameListRecord.GAME_ACTION_RUNS);
				callback.onListItemClick(ivTeams, position, glr);				
				
			}
		});
		

		
		return v;
	}
	public void setOnListItemClickCallback(ListitemClickInterface callback) {
		this.callback = callback;
	}

	protected int listLineResourceId() {
		return R.layout.list_game_line;
	}

}
