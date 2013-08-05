package org.celstec.arlearn2.android.oauth;

import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
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
			return "QR Login";
		case 1:
			return "Facebook";
		case 2:
			return "Google";
		case 3:
			return "LinkedIn";
        case 4:
            return "Twitter";
		default:
			break;
		}
		return "not registered provider";
	}
	
	public int getImageResourceId() {
		switch (oi.getProviderId()) {
		
		case 0:
			return R.drawable.oauth_scanbarcodeicon;
		case 1:
			return R.drawable.facebook;
		case 2:
			return R.drawable.google;
		case 3:
			return R.drawable.linkedin;
        case 4:
                return R.drawable.twitter;
		default:
			break;
		}
		return -1;
	}

    protected View getView(View v) {
        v = super.getView(v);
        TextView textHeader = (TextView) v.findViewById(R.id.textHeader);
            textHeader.setTypeface(null, Typeface.BOLD);
            textHeader.setTextColor(Color.BLACK);
        return v;
    }
	
}
