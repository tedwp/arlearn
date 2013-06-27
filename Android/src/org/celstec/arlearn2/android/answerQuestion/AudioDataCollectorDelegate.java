package org.celstec.arlearn2.android.answerQuestion;

import java.io.File;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.asynctasks.db.RegisterUploadInDbTask;
import org.celstec.arlearn2.android.asynctasks.network.UploadFileSyncTask;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ResponseDelegator;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.util.MediaFolders;
import org.celstec.arlearn2.beans.run.Response;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class AudioDataCollectorDelegate extends DataCollectorDelegate {
	private File audiofile;
	
	protected AudioDataCollectorDelegate(NarratorItemActivity ctx, long runId, String fullAccount) {
		super(ctx, runId, fullAccount);
		ImageView pictureView = (ImageView) ctx.findViewById(R.id.speech_button);
		pictureView.setImageDrawable(ctx.getResources().getDrawable(R.drawable.dc_voice_search_128));
		pictureView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dcButtonClick();
				
			}
		});
	}

	@Override
	protected void dcButtonClick() {
		Intent audioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		audiofile = MediaFolders.createOutgoingAmrFile();
		audioIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(audiofile));
		ctx.startActivityForResult(audioIntent, AUDIO_RESULT);
		
	}
	
	public void processResult(Intent data) {
		String sourcePath = getRealPathFromURI(data.getData());
		 File sourceF = new File(sourcePath);
	        try {
	            boolean success = sourceF.renameTo(audiofile);
	            Uri audioUri = Uri.fromFile(audiofile);
	            publishResponseWithFile(audioUri, createAudioResponse(audioUri));
	            
	        } catch (Exception e) {
	            Toast.makeText(ctx, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
	        }
	}
	
	protected String getMimeType() {
		return "audio/AMR";
	}

	
	
	public String getRealPathFromURI(Uri contentUri) {
	    String[] proj = { MediaStore.Images.Media.DATA };
	    Cursor cursor = ctx.managedQuery(contentUri, proj, null, null, null);
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
}
