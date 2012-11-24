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
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class TakeVideoDelegate {
	AnnotateActivity ctx;
	private ImageView videoFrame;
	private ImageView closeButton;
	private VideoView videoView;
	private RelativeLayout videoIconRelativeLayout;
	private TextView addCaptionVideo;
	private Uri videoUri;

	public TakeVideoDelegate(AnnotateActivity answerQuestionActivity) {
		this.ctx = answerQuestionActivity;
		videoFrame = (ImageView) ctx.findViewById(R.id.videoFrame);
		videoView = (VideoView) ctx.findViewById(R.id.videoView);
		closeButton = (ImageView) ctx.findViewById(R.id.deleteVideoImage);
		videoIconRelativeLayout = (RelativeLayout) ctx.findViewById(R.id.videoIconRelativeLayout);
		addCaptionVideo = (TextView) ctx.findViewById(R.id.addCaptionVideo);

		initImageCapture();
	}

	private void initImageCapture() {
		showIconToRecordVideo();
		videoUri = null;
		videoFrame.setOnClickListener(new View.OnClickListener() {
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
		videoView.setVideoURI(videoUri);
		showRecordedVideo();
		
		MediaController mc = new MediaController(ctx);
		videoView.setMediaController(mc);
		videoView.requestFocus();
		videoView.seekTo(1);
		closeButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				showIconToRecordVideo();
			}
		});
		ctx.displayPublishButton();
	}
	
	private void showRecordedVideo() {
		videoIconRelativeLayout.setVisibility(View.VISIBLE);
		videoView.setVisibility(View.VISIBLE);
		videoFrame.setVisibility(View.GONE);
		addCaptionVideo.setVisibility(View.GONE);


	}
	
	private void showIconToRecordVideo() {
		videoIconRelativeLayout.setVisibility(View.GONE);
		videoView.setVisibility(View.GONE);
		videoFrame.setVisibility(View.VISIBLE);
		addCaptionVideo.setVisibility(View.VISIBLE);
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
	}

}
