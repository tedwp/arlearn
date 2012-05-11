package org.celstec.arlearn2.android.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.answerQuestion.RecordAudioDelegate;
import org.celstec.arlearn2.android.answerQuestion.TakePictureDelegate;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.beans.MediaCacheItem;
import org.celstec.arlearn2.android.menu.ActionDispatcher;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.celstec.arlearn2.beans.run.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.location.Location;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class AnswerQuestionActivity extends AnnotateActivity {
	
//	private ImageView publishButton;
	private Long generalItemId;
	private NarratorItem narratorItemBean;
//	private long runId;
//	public PropertiesAdapter pa ;
//	
//	private File sampleFile;
//
//	public int CAMERA_PIC_REQUEST = 0;
//
//	
//	TakePictureDelegate tpd;
//	RecordAudioDelegate rad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 generalItemId = getIntent().getLongExtra("generalItemId", 0l);
		 narratorItemBean = (NarratorItem) getIntent().getSerializableExtra("bean");
		 OpenQuestion oq = narratorItemBean.getOpenQuestion();
		 if (!oq.isWithAudio()) {
			 rad.hide();
		 }
		 if (!oq.isWithPicture()) {
			 tpd.hide();
		 }
		 
//		setContentView(R.layout.answer_question);
//		pa = new PropertiesAdapter(this);
//        tpd = new TakePictureDelegate(AnswerQuestionActivity.this);
//        rad = new RecordAudioDelegate(AnswerQuestionActivity.this);
//        generalItemId = getIntent().getStringExtra("generalItemId");
//        runId = getIntent().getLongExtra("runId", 0);
//        displayPublishButton();
//        publishButton.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				publish();
//			}
//		});
	}
	
	public Response createResponse(long currentTime) {
		Response r = super.createResponse(currentTime);
		r.setGeneralItemId(generalItemId);
		return r; 
	}
	
	public MediaCacheItem createMediaCacheAudioItem(long currentTime){
		MediaCacheItem mci = super.createMediaCacheAudioItem(currentTime);
		mci.setItemId(runId+":"+generalItemId+":audio:"+currentTime);
		return mci;
	}

	public MediaCacheItem createMediaCacheImageItem(long currentTime){
		MediaCacheItem mci = super.createMediaCacheImageItem(currentTime);
		mci.setItemId( runId+":"+generalItemId+":img:"+currentTime);
		return mci;
	}
	
	public void publish() {
		super.publish();
		PropertiesAdapter pa = new PropertiesAdapter(this);
		ActionDispatcher.publishAction(this, "answer_given", runId, pa.getUsername(), generalItemId, null);
		
	}

//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode == -1 && requestCode == CAMERA_PIC_REQUEST) {
//			tpd.onActivityResult(data);
//		}
//	}
//
//	public void terminateRecordingOrPlaying() {
//		// TODO
//	}
//
//	public void displayPublishButton() {
//		publishButton = (ImageView) findViewById(R.id.publishAnnotation);
//		if (rad.getRecordingPath() == null && tpd.getBitmapFile() == null) {
//			publishButton.setVisibility(View.GONE);
//		} else {
//			publishButton.setVisibility(View.VISIBLE);
//		}
//	}
//	
//	public void publish() {
//		rad.publish();
//		DBAdapter db = new DBAdapter(this);
//		db.openForWrite();
//		MediaCache mc = ((MediaCache) db.table(DBAdapter.MEDIA_CACHE));
//		long currentTime = System.currentTimeMillis();
//		Response r = new Response();
//		r.setUserEmail(pa.getUsername());
//		r.setRunId(pa.getCurrentRunId());
//		r.setTimestamp(currentTime);
//		r.setGeneralItemId(generalItemId);
//		JSONObject jsonResponse = new JSONObject();
//		if (rad.getRecordingPath() != null) {
//			MediaCacheItem mci = new MediaCacheItem();
//			mci.setItemId( runId+":"+generalItemId+":audio:"+currentTime);
//			mci.setLocalFile(rad.getRecordingPath());
//			mci.setRunId(runId);
//			mci.setAccount(pa.getUsername());
//			mci.setMimetype("audio/AMR");
//			mc.addOutgoingObject(mci);
//			try {
//				jsonResponse.put("audioUrl", mci.buildRemotePath());
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		if (tpd.getImagePath() != null) {
//			String id = runId+":"+generalItemId+":img:"+currentTime;
//			MediaCacheItem mci = new MediaCacheItem();
//			mci.setItemId( runId+":"+generalItemId+":img:"+currentTime);
//			mci.setLocalFile(tpd.getImagePath());
//			mci.setLocalThumbnail(getThumbnail(tpd.getImagePath()));
//			mci.setRunId(runId);
//			mci.setAccount(pa.getUsername());
//			mci.setMimetype("image/jpeg");
//			mc.addOutgoingObject(mci);
//			try {
//				jsonResponse.put("imageUrl", mci.buildRemotePath());
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		r.setResponseValue(jsonResponse.toString());
//		db.table(DBAdapter.MYRESPONSES_ADAPTER).insert(r);
//		db.close();
//		setResult(1);
//		finish();
//	}
//
//	private String getThumbnail(String imagePath) {
//		byte[] imageData = null;
//
//        try     
//        {
//
//            final int THUMBNAIL_SIZE = 64;
//            File thumbFile = new File(imagePath);
//            FileInputStream fis = new FileInputStream(thumbFile);
//            Bitmap imageBitmap = BitmapFactory.decodeStream(fis);
//
//            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
//            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
////            imageData = baos.toByteArray();
//            FileOutputStream foStream = new FileOutputStream(new File(imagePath+".thumb"));
//            baos.writeTo(foStream);
//            return imagePath+".thumb";
//
//        }
//        catch(Exception ex) {
//        	ex.printStackTrace();
//        	return null;
//        }
//	}
}
