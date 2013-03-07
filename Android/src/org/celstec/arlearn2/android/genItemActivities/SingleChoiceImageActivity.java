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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.delegators.ResponseDelegator;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class SingleChoiceImageActivity extends GeneralActivity {

	private static SoundPool soundPool;
	private static HashMap<String, Integer> soundPoolMap;
	private static boolean soundPoolLoaded = false;
	private HashMap<String, Uri> mediaObjects = null;
	protected Button submitVoteButton;
	private MultipleChoiceAnswerItem selected = null;

	private SingleChoiceImageTest mct;
	protected ImageView selectedView;
	protected HashMap<MultipleChoiceAnswerItem, ImageView> answerViewMapping = new HashMap<MultipleChoiceAnswerItem, ImageView>();

	private static final int COLUMNS = 3;

	protected GradientDrawable drawable;
	@Override
	public boolean isGenItemActivity() {
		return true;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gi_detail_imagechoice);
		setGeneralItemBean();
		mediaObjects = GeneralItemsDelegator.getInstance().getLocalMediaUriMap(getBean());
		initSoundPool();
		initMediaMaps();
		initWebView();
		initUi();
		initTableView();
		fireReadAction(getBean());
	}

	protected GeneralItem getBean() {
		return mct;
	}
	
	protected void setGeneralItemBean() {
		GeneralItem bean = (GeneralItem) getIntent().getExtras().getSerializable("generalItem");
		mct = (SingleChoiceImageTest) bean; // TODO check casting

	}
	
	private void initUi(){
		drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{ 0xDD000000, 0xAA000000 });
		drawable.setCornerRadii(new float[]{ 5, 5, 5, 5, 5, 5, 5, 5 });
		drawable.setStroke(1, 0xFF000000);
		
		submitVoteButton = (Button) findViewById(R.id.mct_submit);
		submitVoteButton.setEnabled(false);
		submitVoteButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				submitButtonClick();
			}
		});
	}
	
	protected void submitButtonClick() {
		if (selected != null) {
			castResponse();

		}
	}
	
	private void initMediaMaps() {
		for (String key : mediaObjects.keySet()) {
			if (key.endsWith(":a")) {
				soundPoolMap.put(key, soundPool.load(mediaObjects.get(key).getPath(), 1));
			}
		}
		if (mediaObjects.containsKey(GeneralItemsDelegator.AUDIO_LOCAL_ID)) {
			soundPoolMap.put(GeneralItemsDelegator.AUDIO_LOCAL_ID, soundPool.load(mediaObjects.get(GeneralItemsDelegator.AUDIO_LOCAL_ID).getPath(), 1));

		}
	}

	protected String getRichText() {
		return mct.getRichText();
	}
	private void initWebView() {
		WebView webview = (WebView) findViewById(R.id.mct_webview);
		if (getRichText() != null) {
			String html = getRichText();
			webview.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", null);
		} else {
			webview.setVisibility(View.GONE);
		}
	}

	protected List<MultipleChoiceAnswerItem> getMultipleChoiceAnswers () {
		return mct.getAnswers();
	}
	private void initTableView() {
		TableLayout tableView = (TableLayout) findViewById(R.id.multipleChoiceImageTable);
		TableRow row = null;

		for (int i = 0; i < getMultipleChoiceAnswers().size(); i++) {
			final String answerId = getMultipleChoiceAnswers().get(i).getId();
			if ((i % COLUMNS) == 0) {
				System.out.println("new row");
				if (row != null) {
					tableView.addView(row);
				}
				row = new TableRow(this);

			}
			final ImageView im = new ImageView(this);
			im.setPadding(5, 5, 5, 5);
			Uri imageUri = mediaObjects.get(answerId + ":i");
			if (imageUri != null) {
				im.setImageURI(imageUri);
			}
			row.addView(im);
			TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
			layoutParams.width = 0;
			layoutParams.weight = 1;
			
			im.setLayoutParams(layoutParams);
			answerViewMapping.put(getMultipleChoiceAnswers().get(i), im);
			im.setOnClickListener(createImageViewClickerListener(answerId, im));

		}
		tableView.addView(row);
	}
	
	protected OnClickListener createImageViewClickerListener(final String answerId, final ImageView im) {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleSelectedView(im);
				playSound(answerId + ":a");

				submitVoteButton.setEnabled(true);
				for (MultipleChoiceAnswerItem mcai :getMultipleChoiceAnswers()) {
					if (mcai.getId().equals(answerId)) {
						selected = mcai;
					}
				}
			}
		};
	}
		
	

	protected void toggleSelectedView(ImageView im) {
		if (selectedView != null)
			selectedView.setBackgroundDrawable(null);
			im.setBackgroundDrawable(drawable);	
			selectedView = im;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (soundPoolMap != null) {
			new PlayAudioTask().execute(GeneralItemsDelegator.AUDIO_LOCAL_ID);
		}

	}

	private void initSoundPool() {
		if (soundPool == null) {
			soundPool = new SoundPool(mediaObjects.size(), AudioManager.STREAM_MUSIC, 100);
			soundPoolMap = new HashMap<String, Integer>();
			soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
				
				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
					soundPoolLoaded = true;
					Log.i("SOUND", "loaded "+soundPoolLoaded +" "+System.currentTimeMillis());
				}
			});

		}
	}

	

	private void castResponse() {

		ResponseDelegator.getInstance().publishMultipleChoiceResponse(SingleChoiceImageActivity.this, getBean(), selected);

		SingleChoiceImageActivity.this.finish();

	}
	

	public void playSound(String soundKey) {
		if (soundPoolMap.get(soundKey) != null) {
			Log.i("SOUND", "found key");

			AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
			float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			final float volume = streamVolumeCurrent / streamVolumeMax;

			int returnValue = soundPool.play(soundPoolMap.get(soundKey), volume, volume, 1, 0, 1f);
			Log.i("SOUND", "return value "+returnValue);
		}
	}
	
	protected void fireReadAction(GeneralItem item) {
		PropertiesAdapter pa = getMenuHandler().getPropertiesAdapter();
		Long generalItemId = null;
		String generalItemType = null;
		if (item != null) {
			generalItemId = item.getId();
			generalItemType = item.getClass().getName();
		}
		ActionsDelegator.getInstance().publishAction(this, "read", pa.getCurrentRunId(), pa.getUsername(), generalItemId, generalItemType);
	}

	public class PlayAudioTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			if (mediaObjects.get(params[0]) == null) return null;
			Log.i("SOUND", "loaded "+soundPoolLoaded +" "+System.currentTimeMillis());
			final MediaPlayer mPlayer = new MediaPlayer();
			try {
				mPlayer.setDataSource(SingleChoiceImageActivity.this, mediaObjects.get(params[0]));
				mPlayer.prepare();
				mPlayer.start();
				mPlayer.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						mPlayer.stop();
						mPlayer.release();
						
					}
				});
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}		
		
		
	}
}
