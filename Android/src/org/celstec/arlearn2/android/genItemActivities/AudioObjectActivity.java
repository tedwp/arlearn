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