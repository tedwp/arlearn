package org.celstec.arlearn2.android.list;

import java.text.MessageFormat;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.VideoObject;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class MessageListRecord extends GenericListRecord {
	
	private boolean read = false;

	public MessageListRecord(GeneralItem generalItem, long[] readIds, MediaCache mc) {
		setMessageHeader(generalItem.getName());
		for (long id: readIds) {
			if (generalItem.getId() == id) setRead(true);
		}
		String description = "";
		if (generalItem.getType().equals(AudioObject.class.getName())){
			AudioObject ao = (AudioObject) generalItem;
			double percentage = mc.getPercentageUploaded(ao.getAudioFeed());
			if (percentage != 1) description += MessageFormat.format("{0,number,#.##%}", percentage);
		}
		
		if (generalItem.getType().equals(VideoObject.class.getName())){
			VideoObject ao = (VideoObject) generalItem;
			double percentage = mc.getPercentageUploaded(ao.getVideoFeed());
			if (percentage != 1) description += MessageFormat.format("{0,number,#.##%}", percentage);
			
		}
		if ("".equals(description)) description = generalItem.getDescription().trim();

		if (description.length() > 100) description = description.subSequence(0, 100) +" [..]";
		

		setMessageDetail(description);
		setRightDetail("");
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public void setDistance(String distance) {
		setRightDetail(distance);
	}
	
	protected View getView(View v) {
		v = super.getView(v);
		TextView textHeader = (TextView) v.findViewById(R.id.textHeader);
		if (isRead()) {
			textHeader.setTextColor(Color.GRAY);
		} else {
			textHeader.setTypeface(null, Typeface.BOLD);
			textHeader.setTextColor(Color.BLACK);
		}
		return v;
	}
}
