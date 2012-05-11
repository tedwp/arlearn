package org.celstec.arlearn2.android.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.android.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TwoLineListItem;

public class MessageListAdapter extends BaseAdapter {

	private List<MessageLine> messages = new ArrayList<MessageListAdapter.MessageLine>();
	
	private Activity ctx;
	
	public MessageListAdapter(Activity ctx) {
		this.ctx = ctx;
	}
	public void addMessageLine(MessageLine ml) {
		messages.add(ml);
	}
	
	public void emptyList() {
		messages.clear();
	}	
	
	public void setReadMessages(long[] ids) {
		for (MessageLine ml: messages) {
			for (long id: ids) {
				if (ml.getGeneralItemId() == id) ml.setRead(true);
			}
			
		}
	}
	
	public int getCount() {
		return messages.size();
	}

	public MessageLine getItem(int position) {
		return messages.get(position);
	}

	public long getItemId(int position) {
		return getItem(position).generalItemId;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		MessageLine ml = getItem(position);
		TwoLineListItem result;
		 
//            if (convertView == null) {
                result = (TwoLineListItem) ctx.getLayoutInflater().inflate(R.layout.list_map_item_line, parent, false);
//            } else {
//                result = (TwoLineListItem) convertView;
//            }
            
            result.getText1().setText(ml.getLabel());
            result.getText2().setText(ml.getDistance());
            if (!ml.isRead()) {
            	result.getText1().setTypeface(null, Typeface.BOLD);
            }
            return result;
	}
	
	 public class MessageLine {
		 private long generalItemId;
		 private String label;
		 private String distance;
		 private boolean read;
		 
		public MessageLine(long generalItemId, String label, String distance, boolean read) {
			super();
			this.generalItemId = generalItemId;
			this.label = label;
			this.distance = distance;
			this.read = read;
		}
		public long getGeneralItemId() {
			return generalItemId;
		}
		public void setGeneralItemId(long generalItemId) {
			this.generalItemId = generalItemId;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getDistance() {
			return distance;
		}
		public void setDistance(String distance) {
			this.distance = distance;
		}
		public boolean isRead() {
			return read;
		}
		public void setRead(boolean read) {
			this.read = read;
		} 
	 }

		
}
