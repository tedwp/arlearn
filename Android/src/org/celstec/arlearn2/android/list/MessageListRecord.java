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

import java.text.MessageFormat;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.cache.ActionCache;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class MessageListRecord extends GenericListRecord {
	
	private boolean read = false;

	public MessageListRecord(GeneralItem generalItem, long runId) {
		setMessageHeader(generalItem.getName());
		setRead(ActionCache.getInstance().isRead(runId, generalItem.getId()));
		String description = "";
		double percentage = GeneralItemsDelegator.getInstance().getDownloadPercentage(generalItem);
		if (percentage < 1 && percentage != -1) description += MessageFormat.format("{0,number,#.##%}", percentage);

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
