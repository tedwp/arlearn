package org.celstec.arlearn2.android.genItemActivities;

import java.io.File;
import java.util.StringTokenizer;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.celstec.arlearn2.beans.generalItem.YoutubeObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class YoutubeObjectActivity extends NarratorItemActivity {

	private ImageView startVideoImage;
	private boolean firststart = false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!firststart) {
			firststart = true;
			startVideo();
		}
		startVideoImage.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {

				startVideo();
			}
		});		
	}
	
	private void startVideo() {
//		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getVideoObject().getYoutubeUrl())));
		String url = getVideoObject().getYoutubeUrl();
		String id = null;
		if (url.startsWith("http://www.youtube.com") || url.startsWith("https://www.youtube.com")) {
			url = url.substring(url.indexOf("?")+1);
			if (url == null) url = "";
			StringTokenizer st = new StringTokenizer(url, "&");
			
			while (st.hasMoreTokens()) {
				StringTokenizer st2 = new StringTokenizer(st.nextToken(), "=");
				while (st2.hasMoreTokens()) {
					if (st2.nextToken().equals("v"))
						id = st2.nextToken();
				}

			}
		}
		if (id != null) {
			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+id));
			startActivity(i);
		}
		
	}
	
	protected int getContentView() {
		return R.layout.gi_detail_videoobject;
	}
	
	protected void getGuiComponents() {
		super.getGuiComponents();
		startVideoImage = (ImageView) findViewById(R.id.startVideo);

	}
	
	private YoutubeObject getVideoObject() {
		return (YoutubeObject) narratorBean;
	}
	
	protected void unpackDataFromIntent() {
		GeneralItem gi = (GeneralItem) getIntent().getExtras().getSerializable("generalItem");
		narratorBean = (YoutubeObject) gi; //TODO check cast
	}
}
