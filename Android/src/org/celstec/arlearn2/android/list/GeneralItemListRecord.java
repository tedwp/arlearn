package org.celstec.arlearn2.android.list;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.android.Constants;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class GeneralItemListRecord extends GenericListRecord {
	
	public static int GI_ACTION_DELETE = 0;
	public static int GI_ACTION_EDIT = 1;
	public static int GI_ACTION_PLAY = 2;
	public static int GI_ACTION_UPLOAD = 3;
	
	
	private boolean read = false;
	private int action = -1;
	private GeneralItem gi;

	public GeneralItemListRecord(GeneralItem generalItem) {
		
		gi = generalItem;
		setMessageHeader(generalItem.getName());
		setMessageDetail(" "+generalItem.getDescription());
		setRightDetail("");

		
		if ( generalItem.getType().equals(Constants.GI_TYPE_AUDIO_OBJECT)){
			setImageResourceId(R.drawable.audio_icon);
		} else if ( generalItem.getType().equals(Constants.GI_TYPE_MULTIPLE_CHOICE)){
			setImageResourceId(R.drawable.mulchoice_48x48);
		} else if ( generalItem.getType().equals(Constants.GI_TYPE_NARRATOR_ITEM)){
			setImageResourceId(R.drawable.speechbubble_blue);
		} else if ( generalItem.getType().equals(Constants.GI_TYPE_VIDEO_OBJECT)){
			setImageResourceId(R.drawable.video_48x48);
		} else if ( generalItem.getType().equals(Constants.GI_TYPE_PICTURE)){
			setImageResourceId(R.drawable.oericon_48x);			
		} else if ( generalItem.getType().equals(Constants.GI_TYPE_YOUTUBE_MOVIE)){
			setImageResourceId(R.drawable.youtube_48x48);
		} else {
			setImageResourceId(R.drawable.speechbubble_blue);
		}
		
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

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getGeneralItemType() {
		return gi.getType();
	}
	
	public GeneralItem getGeneralItem(){
		return gi;
	}

	
}
