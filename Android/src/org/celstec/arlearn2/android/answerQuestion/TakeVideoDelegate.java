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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class TakeVideoDelegate {
	AnnotateActivity ctx;
	private ImageView video;
	private ImageView closeButton;
	private VideoView mVideoView;
	private FrameLayout videoLayout;
	private Uri videoUri;

	public TakeVideoDelegate(AnnotateActivity answerQuestionActivity) {
		this.ctx = answerQuestionActivity;
		video = (ImageView) ctx.findViewById(R.id.videoFrame);
		mVideoView = (VideoView) ctx.findViewById(R.id.videoView);
		closeButton = (ImageView) ctx.findViewById(R.id.deleteVideoImage);
		videoLayout = (FrameLayout) ctx.findViewById(R.id.videoViewLayout);

		initImageCapture();
	}

	public void hide() {
		video.setVisibility(View.GONE);
	}
	
	private void initImageCapture() {
		video.setImageResource(R.drawable.voegfototoeen);
		video.setVisibility(View.VISIBLE);
		videoUri = null;
		video.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				ctx.setRecordingMedia();
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
				ctx.getRecordAudioDelegate().releaseRecorder();
				ctx.terminateRecordingOrPlaying();
				ctx.startActivityForResult(cameraIntent, ctx.ACTION_TAKE_VIDEO);
			}
		});
	}

	public void onActivityResult(Intent data) {
		videoUri = data.getData();
		setVideoInFrame();
	}

	private void setVideoInFrame() {
		mVideoView.setVideoURI(videoUri);
		video.setVisibility(View.GONE);
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
		ctx.displayPublishButton();
	}
	
	public Uri getUri(){
		return videoUri;
	}

	public void setVideoUri(Uri uri) {
		this.videoUri = uri;
		if (uri != null) {
			setVideoInFrame();
		}
	}

	public void reset() {
		videoUri = null;		
		initImageCapture();
	}

}
