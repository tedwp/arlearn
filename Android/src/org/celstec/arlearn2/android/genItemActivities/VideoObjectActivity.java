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
package org.celstec.arlearn2.android.genItemActivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.VideoObject;

public class VideoObjectActivity extends NarratorItemActivity {

	private ImageView startVideoImage;
	private boolean firststart = false;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!firststart) {
			firststart = true;
//			startVideo();
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
	
	@Override
	public GeneralItem getGeneralItem() {
		return narratorBean;
	}

	@Override
	public void setGeneralItem(GeneralItem gi) {
		narratorBean = (VideoObject) gi;		
	}
}
