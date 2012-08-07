package org.celstec.arlearn2.android.list;

import java.util.ArrayList;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.ViewAnswerActivity;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GenericMessageListAdapter extends ArrayAdapter<GenericListRecord> {

	private ArrayList<GenericListRecord> messages;
	private ListitemClickInterface callback;
	

	public GenericMessageListAdapter(Context context, int textViewResourceId, ArrayList<GenericListRecord> messages) {
		super(context, textViewResourceId, messages);
		this.messages = messages;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(listLineResourceId(), null);
		}
		GenericListRecord record = messages.get(position);
		if (record != null) {
			v = record.getView(v);
			
		}
		v.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callback.onListItemClick(v, position, messages.get(position));
				
			}
		});
		return v;
	}
	public void setOnListItemClickCallback(ListitemClickInterface callback) {
		this.callback = callback;
	}

	protected int listLineResourceId() {
		return R.layout.list_map_generic_line;
	}

}
