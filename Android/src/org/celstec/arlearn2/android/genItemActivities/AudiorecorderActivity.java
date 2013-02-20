package org.celstec.arlearn2.android.genItemActivities;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.R;
// Commented by btb
//import org.celstec.arlearn2.android.activities.UploadGeneralItemActivity;
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
	// private EditText etItemName;
	private AudioObject audioObject = null;

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

	public void startRecordingAudio(View view) throws IOException {

		startAudioButton.setVisibility(View.INVISIBLE);
		stopAudioButton.setVisibility(View.VISIBLE);
		publishAudioButton.setVisibility(View.INVISIBLE);

		// File sampleDir = Environment.getExternalStorageDirectory();
		try {
			// audiofile = File.createTempFile(etItemName.getText().toString(),
			// ".3gp", sampleDir);

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

	public void stopRecordingAudio(View view) {

		startAudioButton.setVisibility(View.VISIBLE);
		stopAudioButton.setVisibility(View.INVISIBLE);
		publishAudioButton.setVisibility(View.VISIBLE);

		recorder.stop();
		recorder.release();
		Toast.makeText(this, "Recording stoped.", Toast.LENGTH_LONG).show();
		addRecordingToMediaLibrary();
		
		// TODO check if addRecordingToMediaLibrary went well
		dialogItem();
	}

	public void publishRecordingAudio(View view) {
		

		startAudioButton.setVisibility(View.VISIBLE);
		stopAudioButton.setVisibility(View.INVISIBLE);
		publishAudioButton.setVisibility(View.INVISIBLE);

		// TODO Show play and publish buttons
//		uploadItem();
//		AudiorecorderActivity.this.finish();

	}

	private void uploadItem() {
		
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
		
		// Commented by btb
//		Intent intent = new Intent(AudiorecorderActivity.this, UploadGeneralItemActivity.class);
//		intent.putExtra("generalItem", audioObject);
//		intent.putExtra("filename", audiofile.getName());
//
//		AudiorecorderActivity.this.startActivity(intent);

	}

	protected void addRecordingToMediaLibrary() {
		ContentValues values = new ContentValues(4);
		long current = System.currentTimeMillis();
		values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
		values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
		values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
		values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());
		ContentResolver contentResolver = getContentResolver();

		Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Uri newUri = contentResolver.insert(base, values);

		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
		Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();
	}

	private String getFileName() {

		Format dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Date date = new Date();
		String newDateString = "/arlearn_audio" + dateFormat.format(date).replace(" ", "_").replace(":", "") + ".3gp";
		return newDateString;
	}

	
	protected void dialogItem() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(AudiorecorderActivity.this);
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
    		Toast.makeText(getApplicationContext(), "Audio successfully recorded", Toast.LENGTH_SHORT).show();
    		AudiorecorderActivity.this.finish();    		
    		
    		
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


}