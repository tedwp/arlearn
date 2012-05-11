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
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.android.sync.MyResponseSyncronizer;
import org.celstec.arlearn2.beans.run.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AnnotateActivity  extends Activity implements IGeneralActivity{

	private ImageView publishButton;
	protected long runId;
	public PropertiesAdapter pa ;
	private double lat = -1000;
	private double lng = -1000;
	private File sampleFile;
	public int CAMERA_PIC_REQUEST = 0;

	
	protected TakePictureDelegate tpd;
	protected RecordAudioDelegate rad;
	protected MenuHandler menuHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.answer_question);
		menuHandler = new MenuHandler(this);
		pa = new PropertiesAdapter(this);
        tpd = new TakePictureDelegate(AnnotateActivity.this);
        rad = new RecordAudioDelegate(AnnotateActivity.this);
        lat = getIntent().getDoubleExtra("lat", -1000);
        lng = getIntent().getDoubleExtra("lng", -1000);
        runId = getIntent().getLongExtra("runId", 0);
        displayPublishButton();
        publishButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				publish();
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == -1 && requestCode == CAMERA_PIC_REQUEST) {
			tpd.onActivityResult(data);
		}
	}

	public void terminateRecordingOrPlaying() {
		// TODO
	}

	public void displayPublishButton() {
		publishButton = (ImageView) findViewById(R.id.publishAnnotation);
		if (rad.getRecordingPath() == null && tpd.getBitmapFile() == null) {
			publishButton.setVisibility(View.GONE);
		} else {
			publishButton.setVisibility(View.VISIBLE);
			publishButton.setClickable(true);
		}
	}
	
	public void publish() {
		rad.publish();
		publishButton.setClickable(false);
		
		Thread t = new Thread(new Runnable() {
			
			public void run() {
				DBAdapter db = new DBAdapter(AnnotateActivity.this);
				db.openForWrite();
				MediaCache mc = ((MediaCache) db.table(DBAdapter.MEDIA_CACHE));
				long currentTime = System.currentTimeMillis();
				Response r = createResponse(currentTime);
				
				JSONObject jsonResponse = new JSONObject();
				if (rad.getRecordingPath() != null) {
					MediaCacheItem mci = createMediaCacheAudioItem(currentTime);
					mc.addOutgoingObject(mci);
					try {
						jsonResponse.put("audioUrl", mci.buildRemotePath());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				if (tpd.getImagePath() != null) {
					MediaCacheItem mci = createMediaCacheImageItem(currentTime);
					mc.addOutgoingObject(mci);
					try {
						jsonResponse.put("imageUrl", mci.buildRemotePath());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
					try {
						if (lat != -1000) jsonResponse.put("lat", lat);
						if (lng != -1000) jsonResponse.put("lng", lng);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				r.setResponseValue(jsonResponse.toString());
				db.table(DBAdapter.MYRESPONSES_ADAPTER).insert(r);
				db.close();
				setResult(1);
				PropertiesAdapter pa = new PropertiesAdapter(AnnotateActivity.this);
				MyResponseSyncronizer.syncResponses(AnnotateActivity.this, pa);
				
			}
		});
		t.start();
		
		
		finish();
	}
	
	public Response createResponse(long currentTime) {
		Response r = new Response();
		r.setUserEmail(pa.getUsername());
		r.setRunId(pa.getCurrentRunId());
		r.setTimestamp(currentTime);
		return r;
	}
	
	public MediaCacheItem createMediaCacheAudioItem(long currentTime){
		MediaCacheItem mci = new MediaCacheItem();
		mci.setItemId( runId+":audio:"+currentTime);
		mci.setLocalFile(rad.getRecordingPath());
		mci.setRunId(runId);
		mci.setAccount(pa.getUsername());
		mci.setMimetype("audio/AMR");
		return mci;
	}

	public MediaCacheItem createMediaCacheImageItem(long currentTime){
		MediaCacheItem mci = new MediaCacheItem();
		mci.setItemId( runId+":img:"+currentTime);
		mci.setLocalFile(tpd.getImagePath());
		mci.setLocalThumbnail(getThumbnail(tpd.getImagePath()));
		mci.setRunId(runId);
		mci.setAccount(pa.getUsername());
		mci.setMimetype("image/jpeg");
		return mci;
	}
	
	private String getThumbnail(String imagePath) {
		byte[] imageData = null;

        try     
        {

            final int THUMBNAIL_SIZE = 64;
            File thumbFile = new File(imagePath);
            FileInputStream fis = new FileInputStream(thumbFile);
            Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            imageData = baos.toByteArray();
            FileOutputStream foStream = new FileOutputStream(new File(imagePath+".thumb"));
            baos.writeTo(foStream);
            return imagePath+".thumb";

        }
        catch(Exception ex) {
        	ex.printStackTrace();
        	return null;
        }
	}

	public void notifyTaskFinished() {
		// TODO Auto-generated method stub
		
	}

	public MenuHandler getMenuHandler() {
		return menuHandler;
	}
	
	public boolean isGenItemActivity() {
		return false;
	}
	
}
