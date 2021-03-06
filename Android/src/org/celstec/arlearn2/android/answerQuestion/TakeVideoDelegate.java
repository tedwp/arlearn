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
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.android.answerQuestion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.*;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnnotateActivity;

public class TakeVideoDelegate {
	AnnotateActivity ctx;
	private ImageView videoFrame;
	private ImageView closeButton;
	private VideoView videoView;
	private RelativeLayout videoIconRelativeLayout;
	private TextView addCaptionVideo;
	private Uri videoUri;

	private int width;
	private int height;



	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}

	
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

	@SuppressLint("NewApi")
	private void setVideoInFrame() {
		videoView.setVideoURI(videoUri);
		showRecordedVideo();
		width = videoView.getWidth();
		height = videoView.getHeight();
		
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
