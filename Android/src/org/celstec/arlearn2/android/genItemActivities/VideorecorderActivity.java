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
 * Contributors: Bernardo Tabuenca
 ******************************************************************************/
package org.celstec.arlearn2.android.genItemActivities;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
// Commented by btb
//import org.celstec.arlearn2.android.activities.UploadGeneralItemActivity;
import org.celstec.arlearn2.beans.generalItem.VideoObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.Toast;

public class VideorecorderActivity extends Activity implements Callback {

	private String CLASSNAME = this.getClass().getName();

	private VideoObject videoObject;
	private File videoFile;
	private SurfaceHolder surfaceHolder;
	private SurfaceView surfaceView;
	public MediaRecorder mrec = new MediaRecorder();
	private Camera mCamera;

	@Override
	protected void onDestroy() {
		stopRecording();
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gi_recordvideo_screen);

		videoObject = (VideoObject) getIntent().getExtras().get("generalItem");

		surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		mCamera = Camera.open();

		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, 0, 0, "Start");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("Start")) {
			try {

				startRecording();
				item.setTitle("Stop");

			} catch (Exception e) {

				String message = e.getMessage();
				Log.i(CLASSNAME, "Problem " + message);
				mrec.release();
			}

		} else if (item.getTitle().equals("Stop")) {
			dialogItem();
			mrec.stop();
			mrec.release();
			mrec = null;
			item.setTitle("Start");
		}

		return super.onOptionsItemSelected(item);
	}

	private String getFileName() {

		Format dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Date date = new Date();
		String newDateString = "/arlearn_video" + dateFormat.format(date).replace(" ", "_").replace(":", "") + ".mp4";
		return newDateString;
	}

	protected void startRecording() throws IOException {
		if (mCamera == null)
			mCamera = Camera.open();

		String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
		String filename = getFileName();

		Log.d(CLASSNAME, "Path [" + path + "][" + filename + "]");
		videoFile = new File(path, filename);

		mrec = new MediaRecorder();

		mCamera.lock();
		mCamera.unlock();

		// Maintain sequence of following code.
		// If you change sequence it will not work.
		mrec.setCamera(mCamera);
		mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mrec.setAudioSource(MediaRecorder.AudioSource.MIC);
		mrec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mrec.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
		mrec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mrec.setPreviewDisplay(surfaceHolder.getSurface());
		mrec.setOutputFile(path + filename);
		mrec.prepare();
		mrec.start();

	}

	protected void stopRecording() {
		
		Log.d(CLASSNAME, "Video stopped");
		

		if (mrec != null) {
			mrec.stop();
			mrec.release();
			mCamera.release();
			mCamera.lock();
		}

//		buildItem();
//		if (!insertGeneralItem(videoObject)) {
//			// TODO Could not insert GeneralItem into DB
//			Log.e(CLASSNAME, "Could not insert GeneralItem into DB");
//
//		}
//		// TODO Show play and publish buttons
//		VideorecorderActivity.this.finish();

	}
	


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		if (mCamera != null) {
			Parameters params = mCamera.getParameters();
			mCamera.setParameters(params);
			Log.i("Surface", "Created");
		} else {
			Toast.makeText(getApplicationContext(), "Camera not available!", Toast.LENGTH_LONG).show();
			finish();
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
		}

	}

//	OK
//	private boolean insertGeneralItem(GeneralItem theGeneralItem) {
//
//		GeneralItem g = new GeneralItem();
//		PropertiesAdapter pa = new PropertiesAdapter(this);
//
//		g = GeneralItemClient.getGeneralItemClient().createGeneralItem(pa.getFusionAuthToken(), theGeneralItem);
//
//		if (g.getErrorCode() != null) {
//			if (g.getErrorCode() == GameClient.ERROR_DESERIALIZING) {
//				Log.e(CLASSNAME, "Exception deserializing " + theGeneralItem.getType() + " item " + theGeneralItem.getName() + ".");
//				return false;
//			}
//		}
//		return true;
//
//	}

	private void uploadItem() {
		PropertiesAdapter pa = new PropertiesAdapter(this);
		
		videoObject.setType(Constants.GI_TYPE_VIDEO_OBJECT);
		videoObject.setDeleted(false);
		videoObject.setScope("user");
		EditText etName = (EditText) findViewById(R.id.editTextVOName);
		if (etName.getText().length() > 0) {
			videoObject.setName(etName.getText().toString());
		} else {
			videoObject.setName(getFileName());
		}
		EditText etDesc = (EditText) findViewById(R.id.editTextVODescription);
		if (etDesc.getText().length() > 0) {
			videoObject.setDescription(etDesc.getText().toString());
		} else {
			videoObject.setDescription("");
		}
		EditText etRichText = (EditText) findViewById(R.id.editTextVORichText);
		if (etRichText.getText().length() > 0) {
			videoObject.setRichText(etRichText.getText().toString());
		} else {
			videoObject.setRichText("");
		}

		// TODO pending to update this with uploaded url of blob
		videoObject.setVideoFeed("http://www.celstec.org" + videoFile.getAbsolutePath());
		
		
		Toast.makeText(this, "Publishing recording " + videoFile.getName() + " - "+videoFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
		
		// Upload file into Blob store
		GeneralItemsDelegator.getInstance().uploadGeneralItem(this, videoObject, pa.getFullId(), Uri.parse(videoFile.getAbsolutePath()));
		
		// Create item in GenealItemJDO
		GeneralItemsDelegator.getInstance().createGeneralItem(this, videoObject);
		
		
// Commented by btb		
//		Intent intent = new Intent(VideorecorderActivity.this, UploadGeneralItemActivity.class);
//		intent.putExtra("generalItem", videoObject);
//		intent.putExtra("filename", videoFile.getName());
//
//		VideorecorderActivity.this.startActivity(intent);		

	}
	
	
	protected void dialogItem() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(VideorecorderActivity.this);
		// Setting Dialog Title
        alertDialog.setTitle("Publish file...");

        // Setting Dialog Message
        alertDialog.setMessage("Do you want to publish this file?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.cloud_up_48);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // User pressed YES button. Write Logic Here
            
            
    		uploadItem();
    		
    		// TODO if uploadItem goes ok
    		Toast.makeText(getApplicationContext(), "Video successfully recorded", Toast.LENGTH_SHORT).show();
    		VideorecorderActivity.this.finish();    		
    		
    		
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // User pressed No button. Write Logic Here
            Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
            // Todo delete file from HD??
            }
        });

        // Setting Netural "Cancel" Button
        alertDialog.setNeutralButton("Watch it first", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // User pressed Cancel button. Write Logic Here
            Toast.makeText(getApplicationContext(), "You clicked on watch it first",
                                Toast.LENGTH_SHORT).show();
            
            
            }
        });

        // Showing Alert Message
        alertDialog.show();
	}	

}