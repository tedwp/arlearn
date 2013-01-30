package org.celstec.arlearn2.android.genItemActivities;

import java.util.HashMap;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class SingleChoiceImageActivity extends GeneralActivity {

	private long runId;
	private Long itemId;
	private String account;
	private static SoundPool soundPool;
	private static HashMap<String, Integer> soundPoolMap;
	// private static SparseArray<Uri> imageMap = new SparseArray<Uri>();
	private HashMap<String, Uri> mediaObjects = null;

	private SingleChoiceImageTest mct;
	private ImageView selectedView;
	private MultipleChoiceAnswerItem selected = null;

	private static final int COLUMNS = 2;

	@Override
	public boolean isGenItemActivity() {
		return true;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		runId = getMenuHandler().getPropertiesAdapter().getCurrentRunId();
		account = getMenuHandler().getPropertiesAdapter().getUsername();
		setContentView(R.layout.gi_detail_imagechoice);
		itemId = getIntent().getExtras().getLong(Constants.ITEM_ID);
		GeneralItem bean = (GeneralItem) getIntent().getExtras().getSerializable("generalItem");
		mct = (SingleChoiceImageTest) bean; // TODO check casting

		mediaObjects = GeneralItemsDelegator.getInstance().getLocalMediaUriMap(mct);
		initSoundPool();
		initMediaMaps();
		initWebView();
		initTableView();
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
	
	private void initWebView() {
		WebView webview = (WebView) findViewById(R.id.mct_webview);
		if (mct.getRichText() != null) {
			String html = mct.getRichText();
			webview.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", null);
		} else {
			webview.setVisibility(View.GONE);
		}
	}

	private void initTableView() {
		TableLayout tableView = (TableLayout) findViewById(R.id.multipleChoiceImageTable);
		TableRow row = null;

		for (int i = 0; i < mct.getAnswers().size(); i++) {
			final String answerId = mct.getAnswers().get(i).getId();
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
			im.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int[] colors = { 0xDD000000, 0xAA000000 };
					float[] radii = { 5, 5, 5, 5, 5, 5, 5, 5 };
					GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
					drawable.setCornerRadii(radii);
					drawable.setStroke(1, 0xFF000000);
					if (selectedView != null)
						selectedView.setBackgroundDrawable(null);
					im.setBackgroundDrawable(drawable);
					selectedView = im;
					playSound(answerId+":a");
				}
			});

		}
		tableView.addView(row);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (soundPoolMap != null) {
			playSound(GeneralItemsDelegator.AUDIO_LOCAL_ID);
		}
		
	}

	private void initSoundPool() {
		if (soundPool == null) {
			soundPool = new SoundPool(mediaObjects.size(), AudioManager.STREAM_MUSIC, 100);
			soundPoolMap = new HashMap<String, Integer>();

		}
	}

	public void playSound(String soundKey) {
		if (soundPoolMap.get(soundKey) != null) {
			AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
			float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			final float volume = streamVolumeCurrent / streamVolumeMax;

			soundPool.play(soundPoolMap.get(soundKey), volume, volume, 1, 0, 1f);
		}
	}

}
