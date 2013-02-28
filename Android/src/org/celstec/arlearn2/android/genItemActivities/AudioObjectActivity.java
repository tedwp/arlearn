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

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Action;

import android.net.Uri;
import android.os.Bundle;

import android.widget.ImageView;

public class AudioObjectActivity extends NarratorItemActivity {

	private AudioPlayerDelegate apd;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Action completeAction = new Action();
		completeAction.setAction("complete");
		completeAction.setRunId(getMenuHandler().getPropertiesAdapter().getCurrentRunId());
		completeAction.setUserEmail(getMenuHandler().getPropertiesAdapter().getUsername());
		completeAction.setGeneralItemId(getAudioObject().getId());
		completeAction.setGeneralItemType(getAudioObject().getType());
		
		Uri localAudioUri = GeneralItemsDelegator.getInstance().getLocalMediaUriMap(getAudioObject()).get("audio");
		apd =  new AudioPlayerDelegate(localAudioUri, this, completeAction);
		apd.setPlayButton((ImageView) findViewById(R.id.ao_playButton));
		apd.setStopButton((ImageView) findViewById(R.id.ao_stopButton));
	}
	
	protected int getContentView() {
		return R.layout.gi_detail_audioobject;
	}
	
	protected void getGuiComponents() {
		super.getGuiComponents();
	}
	
	protected void unpackDataFromIntent() {
		GeneralItem gi = (GeneralItem) getIntent().getExtras().getSerializable("generalItem");
//		this.bean = new AudioObject(gi);
		narratorBean = ( AudioObject) gi; //TODO check cast
	}
	
	protected void loadDataToGui(){
		super.loadDataToGui();
	}
	
	
	
	
	private AudioObject getAudioObject() {
		return (AudioObject) narratorBean;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		apd.unbind();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		apd.stopPlaying();
		
	}

	

	
	
	
	
}
