package org.celstec.arlearn2.android.answerQuestion;

import java.io.File;
import java.io.FileOutputStream;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnnotateActivity;
import org.celstec.arlearn2.android.util.MediaFolders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class TakeVideoDelegate {
	AnnotateActivity ctx;
	private ImageView video;
	private ImageView closeButton;
	private VideoView mVideoView;
	private RelativeLayout videoLayout;
	private Uri videoUri;

	public TakeVideoDelegate(AnnotateActivity answerQuestionActivity) {
		this.ctx = answerQuestionActivity;
		video = (ImageView) ctx.findViewById(R.id.videoFrame);
		mVideoView = (VideoView) ctx.findViewById(R.id.videoView);
		closeButton = (ImageView) ctx.findViewById(R.id.deleteVideoImage);
		videoLayout = (RelativeLayout) ctx.findViewById(R.id.videoViewLayout);

		initImageCapture();
	}

	public void hide() {
		video.setVisibility(View.GONE);
	}
	
	private void initImageCapture() {
		video.setImageResource(R.drawable.voegfototoeen);
		videoUri = null;
		video.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {

				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
				ctx.terminateRecordingOrPlaying();
				ctx.startActivityForResult(cameraIntent, ctx.ACTION_TAKE_VIDEO);
			}
		});
	}

	public void onActivityResult(Intent data) {
		videoUri = data.getData();
		mVideoView.setVideoURI(videoUri);
		video.setVisibility(View.INVISIBLE);
		mVideoView.setVisibility(View.VISIBLE);
		videoLayout.setVisibility(View.VISIBLE);
		
		MediaController mc = new MediaController(ctx);
		mVideoView.setMediaController(mc);
		mVideoView.requestFocus();
		mVideoView.seekTo(1);
		closeButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				video.setVisibility(View.VISIBLE);
				videoLayout.setVisibility(View.GONE);
				mVideoView.setVisibility(View.GONE);
				videoLayout.invalidate();
				
			}
		});
//		((RelativeLayout) ctx.findViewById(R.id.videoViewLayout)).setLayoutParams(new LayoutParams(mVideoView.getWidth(), mVideoView.getHeight()));
		
//		mVideoView.set
//		mVideoView.start();
//		Bitmap thumbnail = (Bitmap) data.getData();
//		try {
////			bitmapFile = MediaFolders.createOutgoingJpgFile();
////			FileOutputStream out = new FileOutputStream(bitmapFile);
////			thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, out);
//		} catch (Exception e) {
//			Log.e("bitmap", "failed to save", e);
//		}
////		setPictureInFrame(thumbnail);
		ctx.displayPublishButton();
		
	}
	
//	private void setPictureInFrame(Bitmap thumbnail) {
//		video.setImageBitmap(thumbnail);
//		video.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
//				builder.setMessage(ctx.getString(R.string.removePicture));
//				builder.setCancelable(false);
//				builder.setPositiveButton(ctx.getString(R.string.yes), new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						initImageCapture();
//
//						ctx.displayPublishButton();
//					}
//				});
//				builder.setNegativeButton(ctx.getString(R.string.no), new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						dialog.cancel();
//					}
//				});
//				AlertDialog alert = builder.create();
//				alert.show();
//				// removeButton.setVisibility(removeButton.INVISIBLE);
//			}
//		});
//	}
	public Uri getUri(){
		return videoUri;
	}

	public String getImagePath() {
		if (videoUri == null) return null;
		return videoUri.getPath();
	}

}
