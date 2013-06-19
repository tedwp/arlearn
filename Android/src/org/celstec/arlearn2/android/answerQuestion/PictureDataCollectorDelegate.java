package org.celstec.arlearn2.android.answerQuestion;

import java.io.File;
import java.io.FileNotFoundException;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.asynctasks.db.RegisterUploadInDbTask;
import org.celstec.arlearn2.android.asynctasks.network.UploadFileSyncTask;
import org.celstec.arlearn2.android.delegators.ResponseDelegator;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.util.MediaFolders;
import org.celstec.arlearn2.beans.run.Response;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PictureDataCollectorDelegate extends DataCollectorDelegate {
	private File bitmapFile;
	private Uri pictureUri;
	
	public PictureDataCollectorDelegate(final NarratorItemActivity ctx, long runId, String account){
		super(ctx, runId, account);
		ImageView pictureView = (ImageView) ctx.findViewById(R.id.picture_button);
		pictureView.setImageDrawable(ctx.getResources().getDrawable(R.drawable.dc_camera_128));
		pictureView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dcButtonClick();
				
			}
		});
	}
	
	@Override
	protected void dcButtonClick(){
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		bitmapFile = MediaFolders.createOutgoingJpgFile();
		cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(bitmapFile));
		ctx.startActivityForResult(cameraIntent, PICTURE_RESULT);
	}

	public void processResult(Intent data) {
		if (bitmapFile != null) pictureUri = Uri.fromFile(bitmapFile);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize = 8;
		options.inPurgeable = true;
		Bitmap preview_bitmap;
		try {
			preview_bitmap = BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(pictureUri),null,options);
			long currentTime = System.currentTimeMillis();
			RegisterUploadInDbTask task = RegisterUploadInDbTask.uploadFile(runId, 
					"picture:" + currentTime,  fullAccount, pictureUri, "application/jpg");
			task.taskToRunAfterExecute(new UploadFileSyncTask());
			task.run(ctx);
			Response r = createResponse(currentTime, null, pictureUri, null, null, preview_bitmap.getWidth(), preview_bitmap.getHeight());
			
			r.setGeneralItemId(ctx.getNarratorBean().getId());
			ResponseDelegator.getInstance().publishResponse(ctx, r);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
}
