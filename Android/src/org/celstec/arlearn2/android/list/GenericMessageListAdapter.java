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
 * Contributors: Stefaan Ternier
 ******************************************************************************/
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
		v.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				return callback.setOnLongClickListener(v, position, messages.get(position));
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
	
	public boolean isEqual(ArrayList<GenericListRecord> newMessages) {
		if (messages == null || newMessages == null) return false;
		if (messages.size() != newMessages.size()) return false;
		for (int i=0; i< messages.size(); i++) {
			if (!messages.get(i).equals(newMessages.get(i))) {
				return false;
			}
		}
		return true;
	}

}
