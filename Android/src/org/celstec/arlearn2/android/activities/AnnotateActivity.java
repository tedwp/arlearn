package org.celstec.arlearn2.android.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.answerQuestion.RecordAudioDelegate;
import org.celstec.arlearn2.android.answerQuestion.TakePictureDelegate;
import org.celstec.arlearn2.android.answerQuestion.TakeVideoDelegate;
import org.celstec.arlearn2.android.broadcast.MediaService;
import org.celstec.arlearn2.android.broadcast.ResponseService;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.beans.MediaCacheItem;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.android.sync.MyResponseSyncronizer;
import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.client.GenericClient;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AnnotateActivity extends Activity implements IGeneralActivity {

	private static final String RECORDING = "recording";
	private static final String PICTUREURI = "pictureURI";
	private static final String VIDEOURI = "videoURI";
	private static final String AUDIOSTATUS = "audioStatus";
	private static final String AUDIOFILE = "audioFile";
	private ImageView publishButton;
	protected long runId;
	public PropertiesAdapter pa;
	private static final int LAT_LNG_NOT_AVAILABLE = -1000;
	private double lat = LAT_LNG_NOT_AVAILABLE;
	private double lng = LAT_LNG_NOT_AVAILABLE;
	private File sampleFile;
	private boolean recordingMedia = false;
	public int CAMERA_PIC_REQUEST = 0;
	public int ACTION_TAKE_VIDEO = 1;

	protected TakePictureDelegate tpd;
	protected TakeVideoDelegate tvd;
	protected RecordAudioDelegate rad;
	protected MenuHandler menuHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//TODO read recordingMedia + save this in onStop
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			recordingMedia = savedInstanceState.getBoolean(RECORDING, false);
		}
		setContentView(R.layout.answer_question);
		menuHandler = new MenuHandler(this);
		pa = new PropertiesAdapter(this);
		
		lat = getIntent().getDoubleExtra("lat", LAT_LNG_NOT_AVAILABLE);
		lng = getIntent().getDoubleExtra("lng", LAT_LNG_NOT_AVAILABLE);
		runId = getIntent().getLongExtra("runId", 0);
		
		publishButton = (ImageView) findViewById(R.id.publishAnnotation);
		publishButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				publish();
			}
		});
		tpd = new TakePictureDelegate(AnnotateActivity.this);
		rad = new RecordAudioDelegate(AnnotateActivity.this);
		tvd = new TakeVideoDelegate(AnnotateActivity.this);
		if (savedInstanceState != null) {
			tpd.setPictureUri((Uri) savedInstanceState.getParcelable(PICTUREURI));
			tvd.setVideoUri((Uri) savedInstanceState.getParcelable(VIDEOURI));
			rad.setStatus(savedInstanceState.getInt(AUDIOSTATUS));
			rad.setRecordingPath(savedInstanceState.getString(AUDIOFILE));
		}

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!recordingMedia) {
			//todo reset...
		
		displayPublishButton();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == -1 && requestCode == CAMERA_PIC_REQUEST) {
			if (tpd != null) tpd.onActivityResult(data);
		}
		if (resultCode == -1 && requestCode == ACTION_TAKE_VIDEO) {
			if (tvd != null) tvd.onActivityResult(data);
		}
	}

	public void terminateRecordingOrPlaying() {
		// TODO
	}

	public void displayPublishButton() {
//		if (rad.getRecordingPath() == null && tpd.getBitmapFile() == null && tvd.getUri() == null) {
		if (rad != null && tpd != null && tvd != null && rad.getRecordingPath() == null && tpd.getUri() == null && tvd.getUri() == null) {
			publishButton.setVisibility(View.GONE);
		} else {
			publishButton.setVisibility(View.VISIBLE);
			publishButton.setClickable(true);
		}
	}

	public void publish() {
		rad.publish();
		publishButton.setClickable(false);

		final long currentTime = System.currentTimeMillis();
		final String recordingPath = rad.getRecordingPath();
//		Uri recording = rad.getUri();
		final Response r = createResponse(currentTime, rad.getUri(), tpd.getUri(), tvd.getUri());

		
		
	
		Intent intent = new Intent(this, ResponseService.class);
		intent.putExtra("bean", r);
		startService(intent);
		
		intent = new Intent(this, MediaService.class);
		intent.putExtra(MediaService.NEW_MEDIA, rad.getUri() != null || tpd.getUri() != null||tvd.getUri()!= null);
		intent.putExtra(MediaService.RECORDING_PATH, rad.getUri());
//		intent.putExtra(MediaService.IMAGE_PATH, imagePath);
		intent.putExtra(MediaService.IMAGE_PATH, tpd.getUri());
		intent.putExtra(MediaService.VIDEO_URI, tvd.getUri());
		intent.putExtra(MediaService.USERNAME, pa.getUsername());
		intent.putExtra(MediaService.CURRENT_TIME, currentTime);
		intent.putExtra(MediaService.RUNID, pa.getCurrentRunId());
		startService(intent);
		setResult(1);
		finish();
	}

//	public String buildRemotePath(String localFileString, long runId, String account) {
//		File localFile = new File(localFileString);
//		return GenericClient.urlPrefix + "/uploadService/" + runId + "/" + account + "/" + localFile.getName();
//	}
	public String buildRemotePath(Uri uri, long runId, String account) {
		return GenericClient.urlPrefix + "/uploadService/" + runId + "/" + account + "/" + uri.getLastPathSegment();
	}
	public Response createResponse(long currentTime, Uri recordingUri, Uri imageUri, Uri videoUri) {
		Response r = createResponse(currentTime);
		JSONObject jsonResponse = new JSONObject();
		try {
			if (recordingUri != null) {
				jsonResponse.put("audioUrl", buildRemotePath(recordingUri, runId, pa.getUsername()));
			}
			if (imageUri != null) {
				jsonResponse.put("imageUrl", buildRemotePath(imageUri, runId, pa.getUsername()));
			}
			if (videoUri != null) {
				jsonResponse.put("videoUrl", buildRemotePath(videoUri, runId, pa.getUsername()));
			}
			if (lat != LAT_LNG_NOT_AVAILABLE)
				jsonResponse.put("lat", lat);
			if (lng != LAT_LNG_NOT_AVAILABLE)
				jsonResponse.put("lng", lng);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		r.setResponseValue(jsonResponse.toString());
		return r;
	}

	public Response createResponse(long currentTime) {
		Response r = new Response();
		r.setUserEmail(pa.getUsername());
		r.setRunId(pa.getCurrentRunId());
		r.setTimestamp(currentTime);
		return r;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (!recordingMedia) {
			tpd.reset();
			rad.stop();
			tvd.reset();
			recordingMedia = false;
		}
	}
	
	public void setRecordingMedia() {
		recordingMedia = true;
	}

	public void notifyTaskFinished() {

	}

	public MenuHandler getMenuHandler() {
		return menuHandler;
	}

	public boolean isGenItemActivity() {
		return false;
	}
	
	@Override
	protected void onSaveInstanceState (Bundle outState){
		outState.putBoolean(RECORDING, recordingMedia);
		outState.putParcelable(PICTUREURI, tpd.getUri());
		outState.putParcelable(VIDEOURI, tvd.getUri());
		outState.putInt(AUDIOSTATUS, rad.getStatus());
		outState.putString(AUDIOFILE, rad.getRecordingPath());
	}
	
	public RecordAudioDelegate getRecordAudioDelegate() {
		return rad;
	}

}
