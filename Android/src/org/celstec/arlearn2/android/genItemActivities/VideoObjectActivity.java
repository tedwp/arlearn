package org.celstec.arlearn2.android.genItemActivities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.VideoObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class VideoObjectActivity extends NarratorItemActivity {

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
		Uri videoUri = GeneralItemsDelegator.getInstance().getLocalMediaUriMap(getVideoObject()).get(GeneralItemsDelegator.VIDEO_LOCAL_ID);
		if (videoUri == null) {
			Toast toast = Toast.makeText(this, getString(R.string.downloadBusy), Toast.LENGTH_LONG);
			toast.show();
			return;
		}
		Intent intentToPlayVideo = new Intent(Intent.ACTION_VIEW);
		 
		intentToPlayVideo.setDataAndType(videoUri, "video/*");
		startActivity(intentToPlayVideo);
	}
	
	protected int getContentView() {
		return R.layout.gi_detail_videoobject;
	}
	
	protected void getGuiComponents() {
		super.getGuiComponents();
		startVideoImage = (ImageView) findViewById(R.id.startVideo);

	}
	
	private VideoObject getVideoObject() {
		return (VideoObject) narratorBean;
	}
	
	protected void unpackDataFromIntent() {
		GeneralItem gi = (GeneralItem) getIntent().getExtras().getSerializable("generalItem");
		narratorBean = (VideoObject) gi; //TODO check cast
	}
}
