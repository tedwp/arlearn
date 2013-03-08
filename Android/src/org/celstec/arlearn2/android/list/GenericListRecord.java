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

import java.util.HashMap;

import org.celstec.arlearn2.android.R;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class GenericListRecord {

	private int imageResourceId = 0;
	private String messageHeader;
	private String messageDetail;
	private String rightDetail;
	private boolean showCheckBox = false;
	private long id;
//	private boolean checked = false;
	
	private static HashMap<Long, Boolean> checked = new HashMap<Long, Boolean>();
	
	public int getImageResourceId() {
		return imageResourceId;
	}
	public void setImageResourceId(int imageResourceId) {
		this.imageResourceId = imageResourceId;
	}
	public String getMessageHeader() {
		return messageHeader;
	}
	public void setMessageHeader(String messageHeader) {
		this.messageHeader = messageHeader;
	}
	public String getMessageDetail() {
		return messageDetail;
	}
	public void setMessageDetail(String messageDetail) {
		this.messageDetail = messageDetail;
	}
	public String getRightDetail() {
		return rightDetail;
	}
	public void setRightDetail(String rightDetail) {
		this.rightDetail = rightDetail;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
	public boolean isShowCheckBox() {
		return showCheckBox;
	}
	
	public boolean isChecked(){
		Boolean c = checked.get(id);
		if (c == null) return false;
		return c;
	}
	
	public void setShowCheckBox(boolean showCheckBox) {
		this.showCheckBox = showCheckBox;
	}
	protected View getView(View v) {
		TextView textHeader = (TextView) v.findViewById(R.id.textHeader);
		TextView textDetail = (TextView) v.findViewById(R.id.textDetail);
		TextView textRightDetail = (TextView) v.findViewById(R.id.textRightDetail);
	      ImageView iv = (ImageView) v.findViewById(R.id.list_icon);


		if (textHeader != null) {
			textHeader.setText(getMessageHeader());
		}
		if (textDetail != null) {
			if (getMessageDetail() == null) {
				textDetail.setVisibility(View.GONE);
			} else {
				textDetail.setText(getMessageDetail());	
			}
		}
		if (textRightDetail != null) {
			textRightDetail.setText(getRightDetail());
		}
		if (iv != null && getImageResourceId()!=0) {
			iv.setImageResource(getImageResourceId());
		}
		if (isShowCheckBox()) {
			final CheckBox cb = (CheckBox) v.findViewById(R.id.unRegisterCheckBox);
			cb.setChecked(isChecked());
			cb.setVisibility(View.VISIBLE);
			cb.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					checked.put(id, !isChecked());
					cb.setChecked(isChecked());
				}
			});
		}
		return v;
	}
	
	public boolean equals(GenericListRecord other) {
		if (messageDetail == null) {
			if (other.messageDetail != null)
				return false;
		} else {
			if (!messageDetail.equals(other.messageDetail))
				return false;
		}
		if (messageHeader == null) {
			if (other.messageHeader != null)
				return false;
		} else {
			if (!messageHeader.equals(other.messageHeader))
				return false;
		}
		if (rightDetail == null) {
			if (other.rightDetail != null)
				return false;
		} else {
			if (!rightDetail.equals(other.rightDetail))
				return false;
		} 
		if (isShowCheckBox() != other.showCheckBox) {
				return false;
		}
		if (checked != other.checked) {
			return false;
		}
		
		return true;
	}
	
}
