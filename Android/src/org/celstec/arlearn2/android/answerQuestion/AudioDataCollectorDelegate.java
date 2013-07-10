package org.celstec.arlearn2.android.answerQuestion;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.util.MediaFolders;

import java.io.File;

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
        try {
		    Intent audioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);

		    audiofile = MediaFolders.createOutgoingAmrFile();
		    audioIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(audiofile));
		    ctx.startActivityForResult(audioIntent, AUDIO_RESULT);
        } catch (ActivityNotFoundException notFound) {
            Intent audioIntent = new Intent(ctx, RecordAudioActivity.class);
            audiofile = MediaFolders.createOutgoingAmrFile();
           // audioIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(audiofile));
            ctx.startActivityForResult(audioIntent, AUDIO_RESULT);
        }
		
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
        if (contentUri.getScheme().equals("file"))    return  contentUri.getPath();
	    String[] proj = { MediaStore.Images.Media.DATA };
	    Cursor cursor = ctx.managedQuery(contentUri, proj, null, null, null);
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
}
