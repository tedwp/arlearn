package org.celstec.arlearn2.android.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.MyResponses;
import org.celstec.arlearn2.android.db.beans.MediaCacheItem;
import org.celstec.arlearn2.android.genItemActivities.AudioPlayerDelegate;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.beans.run.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ViewAnswerActivity extends GeneralActivity {
	private Response resp;
	private ImageView image;
	
	private AudioPlayerDelegate apd;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_answer);
		resp = (Response) getIntent().getSerializableExtra("response");
		image = (ImageView) findViewById(R.id.answerImage);
		initGui();
		try {
			DBAdapter db = new DBAdapter(this);
			db.openForRead();
			MediaCache mc = ((MediaCache) db.table(DBAdapter.MEDIA_CACHE));
			JSONObject json = new JSONObject(resp.getResponseValue());
			String genId = "";
			if (resp.getGeneralItemId() != null) genId = ":"+resp.getGeneralItemId();
			if (json.has("imageUrl")) {
				String imageUrl =  json.getString("imageUrl");
				
				
				MediaCacheItem mci =mc.queryById(resp.getRunId()+genId+":img:"+resp.getTimestamp());
				
				
				 File thumbFile = new File(mci.getLocalFile());
//		            FileInputStream fis = new FileInputStream(thumbFile);
//		            
//		            image.setImageBitmap(BitmapFactory.decodeStream(fis));
		            image.setImageBitmap(decodeFile(thumbFile, 500));
			}
			if (json.has("audioUrl")) {
				String audioUrl =  json.getString("audioUrl");
				
				MediaCacheItem mci =mc.queryById(resp.getRunId()+genId+":audio:"+resp.getTimestamp());
				apd =  new AudioPlayerDelegate(mci.getItemId(), this);

			} else {
				((LinearLayout) findViewById(R.id.playButtonsAnswer)).setVisibility(View.GONE);
			}
			db.close();
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		if (apd != null) apd.setStopButton((ImageView) findViewById(R.id.ao_stopButton_answer));
		if (apd != null) apd.setPlayButton((ImageView) findViewById(R.id.ao_playButton_answer));
	}
	int IMAGE_MAX_SIZE = 600;
	
	public static Bitmap decodeFile(File f, int IMAGE_MAX_SIZE){
	    Bitmap b = null;
	    try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;

	        FileInputStream fis = new FileInputStream(f);
	        BitmapFactory.decodeStream(fis, null, o);
	        fis.close();

	        int scale = 1;
	        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
	            scale = (int)Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        }

	        //Decode with inSampleSize
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
		DBAdapter db = new DBAdapter(this);
		db.openForWrite();
		((MyResponses) db.table(DBAdapter.MYRESPONSES_ADAPTER)).revoke(resp);
		db.close();
		setResult(1);
		finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		 System.gc();
		if (apd != null) apd.unbind();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		 System.gc();
		
		if (apd != null) apd.stopPlaying();
		
	}
	
	
	public boolean isGenItemActivity() {
		return false;
	}
	
}