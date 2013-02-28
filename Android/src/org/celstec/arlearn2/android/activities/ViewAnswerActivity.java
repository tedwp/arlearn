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
package org.celstec.arlearn2.android.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.cache.MediaUploadCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MyResponses;
import org.celstec.arlearn2.android.db.beans.MediaCacheItem;
import org.celstec.arlearn2.android.delegators.ResponseDelegator;
import org.celstec.arlearn2.android.genItemActivities.AudioPlayerDelegate;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.beans.run.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class ViewAnswerActivity extends GeneralActivity {
	private Response resp;
	private ImageView image;
	private VideoView video;
	int IMAGE_MAX_SIZE = 600;
	private AudioPlayerDelegate apd;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_answer);
		resp = (Response) getIntent().getSerializableExtra("response");
		image = (ImageView) findViewById(R.id.answerImage);
		video = (VideoView) findViewById(R.id.videoView);
		initGui();
		try {
			JSONObject json = new JSONObject(resp.getResponseValue());
			if (json.has(ResponseDelegator.IMAGEURL)) {
				Uri uri = ResponseDelegator.getInstance().getLocalMediaUri(resp, ResponseDelegator.IMAGEURL);
				if (uri != null) {
					image.setVisibility(View.VISIBLE);
					BitmapFactory.Options options=new BitmapFactory.Options();
					options.inSampleSize = 4;
					options.inPurgeable = true;
					try {
						Bitmap preview_bitmap=BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri),null,options);
						image.setImageBitmap(preview_bitmap);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			} else {
				image.setVisibility(View.GONE);
			}
			if (json.has(ResponseDelegator.VIDEOURL)) {
				Uri uri = ResponseDelegator.getInstance().getLocalMediaUri(resp, ResponseDelegator.VIDEOURL);
				if (uri != null) {
					video.setVisibility(View.VISIBLE);
					video.setVideoURI(uri);
					MediaController mediaController = new MediaController(this);
					video.setMediaController(mediaController);
					video.requestFocus();
					video.seekTo(1);
				}
			} else {
				video.setVisibility(View.GONE);
			}
			if (json.has(ResponseDelegator.AUDIOURL)) {
				Uri uri = ResponseDelegator.getInstance().getLocalMediaUri(resp, ResponseDelegator.AUDIOURL);

				((LinearLayout) findViewById(R.id.playButtonsAnswer)).setVisibility(View.VISIBLE);
				if (uri != null)
					apd = new AudioPlayerDelegate(uri, this);

			} else {
				((LinearLayout) findViewById(R.id.playButtonsAnswer)).setVisibility(View.GONE);
			}
			
			if (json.has("text")) {
				((TextView) findViewById(R.id.answerText)).setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.answerText)).setText(json.getString("text"));

			} else {
				((TextView) findViewById(R.id.answerText)).setVisibility(View.GONE);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (apd != null)
			apd.setStopButton((ImageView) findViewById(R.id.ao_stopButton_answer));
		if (apd != null)
			apd.setPlayButton((ImageView) findViewById(R.id.ao_playButton_answer));
	}

	

	public static Bitmap decodeFile(File f, int IMAGE_MAX_SIZE) {
		Bitmap b = null;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			FileInputStream fis = new FileInputStream(f);
			BitmapFactory.decodeStream(fis, null, o);
			fis.close();

			int scale = 1;
			if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
				scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			fis = new FileInputStream(f);
			b = BitmapFactory.decodeStream(fis, null, o2);
			fis.close();
		} catch (IOException e) {
		}
		return b;
	}

	private void initGui() {
		image = (ImageView) findViewById(R.id.answerImage);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MenuHandler.DELETE_ANSWER, 0, getString(R.string.deleteAnswer));
		return true;
	}

	public void deleteAnswer() {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(menuHandler.getContext()));
		m.obj = new DBAdapter.DatabaseTask() {
			@Override
			public void execute(DBAdapter db) {
				((MyResponses) db.table(DBAdapter.MYRESPONSES_ADAPTER)).revoke(resp);
			}
		};
		m.sendToTarget();
		setResult(1);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
//		if (apd != null)
//			apd.unbind();
	}

	@Override
	protected void onPause() {
		super.onPause();
		System.gc();

		if (apd != null)
			apd.stopPlaying();

	}

	public boolean isGenItemActivity() {
		return false;
	}

	@Override
	protected void newNfcAction(String action) {
		// TODO Auto-generated method stub

	}

}
