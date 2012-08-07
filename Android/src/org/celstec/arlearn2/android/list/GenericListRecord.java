package org.celstec.arlearn2.android.list;

import org.celstec.arlearn2.android.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GenericListRecord {

	private int imageResourceId = 0;
	private String messageHeader;
	private String messageDetail;
	private String rightDetail;
	private long id;
	
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
//				textDetail.setHeight(60);
			}
			
		}
		if (textRightDetail != null) {
			textRightDetail.setText(getRightDetail());
		}
		if (iv != null && getImageResourceId()!=0) {
			iv.setImageResource(getImageResourceId());
		}
		return v;
	}
	
}
