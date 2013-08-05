package org.celstec.arlearn2.android.answerQuestion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;

import java.io.File;

public class VideoDataCollectorDelegate extends DataCollectorDelegate {
	
	private File bitmapFile;
	private Uri videoUri;
	
	public VideoDataCollectorDelegate(final NarratorItemActivity ctx, long runId, String account){
		super(ctx, runId, account);
		ImageView pictureView = (ImageView) ctx.findViewById(R.id.video_button);
		pictureView.setImageDrawable(ctx.getResources().getDrawable(R.drawable.dc_video_128));
		pictureView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dcButtonClick();
				
			}
		});
	}
	
	@Override
	protected void dcButtonClick(){		
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
		ctx.startActivityForResult(cameraIntent, VIDEO_RESULT);
	}
	
	@SuppressLint("NewApi")
	public void processResult(Intent data) {
		videoUri = data.getData();
		int width = 1280;
		int height = 720;
		if (Integer.parseInt(Build.VERSION.SDK) >= Build.VERSION_CODES.GINGERBREAD_MR1) {
			MediaMetadataRetriever retriever = new MediaMetadataRetriever();
			Bitmap bmp = null;
			
			try {
				retriever.setDataSource(ctx, videoUri);
				bmp = retriever.getFrameAtTime();
				height = bmp.getHeight();
				width = bmp.getWidth();
			} catch (RuntimeException ex) {
				// Assume this is a corrupt video file.
			} finally {
				try {
					retriever.release();
				} catch (RuntimeException ex) {
					// Ignore failures while cleaning up.
				}
			}
		}
		publishResponseWithFile(videoUri, createVideoResponse(videoUri,width, height));

	}
	
	@Override
	protected String getMimeType() {
		return "video/mp4";
	}
}
