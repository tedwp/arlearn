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

import java.text.SimpleDateFormat;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.run.Run;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class RunListRecord extends GenericListRecord {

	
	private static SimpleDateFormat formatterDay = new SimpleDateFormat("d MMM \n HH:mm:ss");

	private Long gameId;

	public RunListRecord(Run run) {
		this.setId(run.getRunId());
		Game game = GameDelegator.getInstance().getGame(run.getGameId());
		if (game != null)
		if (game.getConfig().getMapAvailable()) {
			setImageResourceId(R.drawable.map_icon);
		} else {
			setImageResourceId(R.drawable.list_icon);
		}
		setMessageHeader(run.getTitle());
		
		if (game != null) {
			gameId = game.getGameId();
			int amount = GameDelegator.getInstance().getAmountOfUncachedItems(game.getGameId());
			String message = null;
			if (amount == 0) {
				message = game.getTitle()+" ("+game.getCreator()+")";
			} else {
				message = game.getTitle()+" ("+amount+ " uncached)";
			}
			setMessageDetail(message);
		}
		if (run.getLastModificationDate() != null) setRightDetail(formatterDay.format(run.getLastModificationDate()));
	}

	
	
	protected View getView(View v) {
		v = super.getView(v);
		TextView textHeader = (TextView) v.findViewById(R.id.textHeader);

		textHeader.setTypeface(null, Typeface.BOLD);
		textHeader.setTextColor(Color.BLACK);
		
		TextView textRightDetail = (TextView) v.findViewById(R.id.textRightDetail);

		if (textRightDetail != null) {
			textRightDetail.setTextSize(10);
		}
		return v;
	}



	public Long getGameId() {
		return gameId;
	}



	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	
	

}
