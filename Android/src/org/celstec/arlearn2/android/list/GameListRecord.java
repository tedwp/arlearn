package org.celstec.arlearn2.android.list;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.beans.game.Game;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class GameListRecord extends GenericListRecord {
	
	public static int GAME_ACTION_DELETE = 0;
	public static int GAME_ACTION_EDIT = 1;
	public static int GAME_ACTION_RUNS = 2;
	public static int GAME_ACTION_GENERALITEMS = 3;
	
	private boolean read = false;
	private int action = -1;

	public GameListRecord(Game game) {
		
		setMessageHeader(game.getTitle());

		String description = "";
		//description = game.getTitle()+" "+game.getGameId();
		description = game.getDescription();
		setMessageDetail(description);
		setRightDetail("");
		
		if (game.getConfig().getMapAvailable()){
			setImageResourceId(R.drawable.map_icon);
		}else{
			setImageResourceId(R.drawable.list_icon);
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
	

	
}
