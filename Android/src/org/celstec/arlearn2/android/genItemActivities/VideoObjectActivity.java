package org.celstec.arlearn2.android.genItemActivities;

import java.io.File;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.beans.MediaCacheItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
//import org.celstec.arlearn2.android.db.beans.AudioObject;
//import org.celstec.arlearn2.android.db.beans.GeneralItem;
//import org.celstec.arlearn2.android.db.beans.NarratorItem;
//import org.celstec.arlearn2.android.db.beans.VideoObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
//		MediaCacheItem mc = org.celstec.arlearn2.android.cache.MediaCache.getInstance().getMediaCacheItem(""+getVideoObject().getId());
		Uri videoUri = org.celstec.arlearn2.android.cache.MediaCache.getInstance().getLocalUri(getVideoObject().getId());
//		if (mc == null) {
//			Toast toast = Toast.makeText(this, "mc item is null", Toast.LENGTH_LONG);
//			toast.show();
//			return;
//		}
		if (videoUri == null) {
			Toast toast = Toast.makeText(this, getString(R.string.downloadBusy), Toast.LENGTH_LONG);
			toast.show();
			return;
		}
//		File audioFile = new File(mc.getLocalFile());
		Intent intentToPlayVideo = new Intent(Intent.ACTION_VIEW);
		
//		intentToPlayVideo.setDataAndType(Uri.parse("file://"+audioFile.getAbsolutePath()), "video/*");
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
