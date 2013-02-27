package org.celstec.arlearn2.android.genItemActivities;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.R;
//import org.celstec.arlearn2.android.activities.UploadGeneralItemActivity;
import org.celstec.arlearn2.android.broadcast.MediaService;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.beans.generalItem.AudioObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AudiorecorderActivity extends Activity {

	private String CLASSNAME = this.getClass().getName();

	MediaRecorder recorder;

	private File audiofile = null;
	private View startAudioButton;
	private View stopAudioButton;
	private View publishAudioButton;

	// Object to be uploaded 
	private AudioObject audioObject = null;
	private Uri newUri = null;
	private long lCurrentTime = 0L;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle extras = getIntent().getExtras();
		audioObject = (AudioObject) extras.get("generalItem");

		setContentView(R.layout.gi_recordaudio_screen);

		startAudioButton = findViewById(R.id.imageStartAudioRecord);
		stopAudioButton = findViewById(R.id.imageStopAudioRecord);
		publishAudioButton = findViewById(R.id.imagePublishAudioRecord);

		stopAudioButton.setVisibility(View.VISIBLE);
		stopAudioButton.setVisibility(View.INVISIBLE);
		publishAudioButton.setVisibility(View.INVISIBLE);

		// etItemName = (EditText) findViewById(R.id.etNewGIName);

	}

	/**
	 * On click start audio recording
	 * 
	 * @param view
	 * @throws IOException
	 */
	public void startRecordingAudio(View view) throws IOException {

		startAudioButton.setVisibility(View.INVISIBLE);
		stopAudioButton.setVisibility(View.VISIBLE);
		publishAudioButton.setVisibility(View.INVISIBLE);

		try {
			String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
			audiofile = new File(path, getFileName());
			Log.d(CLASSNAME, "Created tmp file Name [" + audiofile.getName() + "] Absolute path:[" + audiofile.getAbsolutePath() + "]");

		} catch (Exception e) {
			Log.e(CLASSNAME, "sdcard access error");
			return;
		}
		
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(audiofile.getAbsolutePath());
		Toast.makeText(this, "Recording started...", Toast.LENGTH_LONG).show();
		recorder.prepare();
		recorder.start();
	}

	/**
	 * On click stop audio recording button
	 * 
	 * @param view
	 */
	public void stopRecordingAudio(View view) {

		startAudioButton.setVisibility(View.VISIBLE);
		stopAudioButton.setVisibility(View.INVISIBLE);
		publishAudioButton.setVisibility(View.VISIBLE);

		recorder.stop();
		recorder.release();
		Toast.makeText(this, "Recording stoped.", Toast.LENGTH_LONG).show();

		
		long current = System.currentTimeMillis();
		ContentValues values = new ContentValues(4);
		values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
		values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
		values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
		values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());
		ContentResolver contentResolver = getContentResolver();

		Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		newUri = contentResolver.insert(base, values);

		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
		Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();
		
		// TODO check if addRecordingToMediaLibrary went well

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(AudiorecorderActivity.this);
        alertDialog.setTitle("Publish file...");
        alertDialog.setMessage("Do you want to publish this file?");
        alertDialog.setIcon(R.drawable.cloud_up_48);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // User pressed YES button. Write Logic Here
    		uploadItem();
    		Toast.makeText(getApplicationContext(), "Audio successfully recorded", Toast.LENGTH_SHORT).show();
    		AudiorecorderActivity.this.finish();    		
    		
    		
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // User pressed No button. Write Logic Here
            Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
            // TODO delete file from HD??
            }
        });

        // Setting Netural "Cancel" Button
        alertDialog.setNeutralButton("Listen first", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // User pressed Cancel button. Write Logic Here
            Toast.makeText(getApplicationContext(), "You clicked on watch it first",
                                Toast.LENGTH_SHORT).show();
            
            
            }
        });

        // Showing Alert Message
        alertDialog.show();
        		
	}	
	
	/**
	 * On click publish button
	 * @param view
	 */
	public void publishRecordingAudio(View view) {
		

		startAudioButton.setVisibility(View.VISIBLE);
		stopAudioButton.setVisibility(View.INVISIBLE);
		publishAudioButton.setVisibility(View.INVISIBLE);

		// TODO Show play and publish buttons
//		uploadItem();
//		AudiorecorderActivity.this.finish();

	}

	
	/**
	 * Clicked "yes" on alert message prompting to upload the item
	 * 
	 */
	private void uploadItem() {
		
		PropertiesAdapter pa = new PropertiesAdapter(this);
		
		audioObject.setType(Constants.GI_TYPE_AUDIO_OBJECT);
		audioObject.setDeleted(false);
		audioObject.setScope("user");
		
		EditText etName = (EditText) findViewById(R.id.editTextAOName);
		if (etName.getText().length() > 0) {
			audioObject.setName(etName.getText().toString());
		} else {
			audioObject.setName(getFileName());
		}
		EditText etDesc = (EditText) findViewById(R.id.editTextAODescription);
		if (etDesc.getText().length() > 0) {
			audioObject.setDescription(etDesc.getText().toString());
		} else {
			audioObject.setDescription("");
		}
		EditText etRichText = (EditText) findViewById(R.id.editTextAORichText);
		if (etRichText.getText().length() > 0) {
			audioObject.setRichText(etRichText.getText().toString());
		} else {
			audioObject.setRichText("");
		}

		// TODO pending to update this with uploaded url of blob
		audioObject.setAudioFeed("http://www.celstec.org" + audiofile.getAbsolutePath());		

		Toast.makeText(this, "Publishing recording " + audiofile.getName(), Toast.LENGTH_LONG).show();
		
		// Create item in GenealItemJDO
		//GeneralItemsDelegator.getInstance().createGeneralItem(this, audioObject);
		
		// Create item in FileJDO
		// TODO
		Intent intent = new Intent(this, MediaService.class);
		intent.putExtra(MediaService.NEW_MEDIA, true);
		intent.putExtra(MediaService.RECORDING_PATH, newUri);
// 		intent.putExtra(MediaService.IMAGE_PATH, imagePath);
//		intent.putExtra(MediaService.IMAGE_PATH, "");
//		intent.putExtra(MediaService.VIDEO_URI, "");
		intent.putExtra(MediaService.USERNAME, pa.getUsername());
		intent.putExtra(MediaService.CURRENT_TIME, lCurrentTime);
		intent.putExtra(MediaService.RUNID, pa.getCurrentRunId());
		startService(intent);		
		
//
//		Intent intent = new Intent(AudiorecorderActivity.this, UploadGeneralItemActivity.class);
//		intent.putExtra("generalItem", audioObject);
//		intent.putExtra("filename", audiofile.getName());
//
//		AudiorecorderActivity.this.startActivity(intent);

	}


	/**
	 * Returns an string with an audio file name based on current time
	 * 
	 * @return
	 */
	private String getFileName() {

		Format dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Date date = new Date();
		lCurrentTime = date.getTime();
		String newDateString = "/arlearn_audio" + dateFormat.format(date).replace(" ", "_").replace(":", "") + ".3gp";
		return newDateString;
	}


}