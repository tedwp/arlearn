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

import java.util.StringTokenizer;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.YoutubeObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
	
//	protected void unpackDataFromIntent() {
//		GeneralItem gi = (GeneralItem) getIntent().getExtras().getSerializable("generalItem");
//		narratorBean = (YoutubeObject) gi; //TODO check cast
//	}
	
	@Override
	public YoutubeObject getGeneralItem() {
		return (YoutubeObject) narratorBean;
	}

	@Override
	public void setGeneralItem(GeneralItem gi) {
		narratorBean = (YoutubeObject) gi;		
	}
	
}
