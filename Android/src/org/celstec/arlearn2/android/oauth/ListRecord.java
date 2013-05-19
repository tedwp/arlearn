package org.celstec.arlearn2.android.oauth;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.beans.oauth.OauthInfo;



public class ListRecord extends GenericListRecord{

	private OauthInfo oi;
	public ListRecord(OauthInfo oi) {
		this.oi = oi;
	}
	
	public String getMessageHeader() {
		switch (oi.getProviderId()) {
		case 0:
			return "Qr code login";
		case 1:
			return "Facebook";
		case 2:
			return "Google";
		case 3:
			return "LinkedIn";

		default:
			break;
		}
		return "not registered provider";
	}
	
	public int getImageResourceId() {
		switch (oi.getProviderId()) {
		
		case 0:
			return R.drawable.scanbarcodeicon;
		case 1:
			return R.drawable.facebook;
		case 2:
			return R.drawable.google;
		case 3:
			return R.drawable.linkedin;

		default:
			break;
		}
		return -1;
	}
	
}
