package org.celstec.arlearn2.android.genItemActivities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.android.service.AudioPlayerService;
import org.celstec.arlearn2.android.service.IAudioPlayerService;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Action;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

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
		
		apd =  new AudioPlayerDelegate(getAudioObject().getId(), this, completeAction);
		apd.setPlayButton((ImageView) findViewById(R.id.ao_playButton));
		apd.setStopButton((ImageView) findViewById(R.id.ao_stopButton));
	}
	
	protected int getContentView() {
		return R.layout.gi_detail_audioobject;
	}
	
	protected void getGuiComponents() {
		super.getGuiComponents();

//		playButton = (ImageView) findViewById(R.id.ao_playButton);
//		stopButton = (ImageView) findViewById(R.id.ao_stopButton);
//		if (playButton != null)
//			playButton.setOnClickListener(playListener);
//		if (stopButton != null)
//			stopButton.setOnClickListener(stopListener);
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
		apd.unbind();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		apd.stopPlaying();
		
	}

	

	
	
	
	
}