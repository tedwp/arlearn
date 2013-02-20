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

public class GeneralItemListAdapter extends ArrayAdapter<GenericListRecord> {
	
	private String CLASSNAME = this.getClass().getName();	

	private ArrayList<GenericListRecord> genItems;
	private ListitemClickInterface callback;
	

	public GeneralItemListAdapter(Context context, int textViewResourceId, ArrayList<GenericListRecord> theGenItems) {
		super(context, textViewResourceId, theGenItems);
		this.genItems = theGenItems;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(listLineResourceId(), null);
		}
		GenericListRecord record = genItems.get(position);
		if (record != null) {
			v = record.getView(v);			
		}
		
		// Click on row		
		v.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(CLASSNAME, "Clicked row position "+position);
				GeneralItemListRecord glr = (GeneralItemListRecord)genItems.get(position);
				glr.setAction(GeneralItemListRecord.GI_ACTION_EDIT);
				callback.onListItemClick(v, position, glr);
				
			}
		});

		
		
		// Click on delete runs image
		ImageView ivDelete = (ImageView) v.findViewById(R.id.delete_icon);
		ivDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View ivDelete) {
				Log.d(CLASSNAME, "Clicked delete button position "+position);
				GeneralItemListRecord glr = (GeneralItemListRecord)genItems.get(position);
				glr.setAction(GeneralItemListRecord.GI_ACTION_DELETE);
				callback.onListItemClick(ivDelete, position, glr);				
				
			}
		});		

		
		return v;
	}
	public void setOnListItemClickCallback(ListitemClickInterface callback) {
		this.callback = callback;
	}

	protected int listLineResourceId() {
		return R.layout.list_genitem_line;
	}

}
