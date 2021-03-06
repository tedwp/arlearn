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

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnnotateActivity;
import org.celstec.arlearn2.android.util.MediaFolders;

import java.io.File;
import java.io.IOException;

public class RecordAudioDelegate {
	
	private int status = STOPPED_NO_AUDIO;
	public static final int STOPPED_NO_AUDIO = 1;
	private static final int STOPPED_WITH_AUDIO = 2;
	private static final int RECORDING_STATUS = 3;
	private static final int PLAYBACK_AUDIO = 4;
	private static final int PAUSE_WITH_AUDIO = 5;
	
//	AnnotateActivity ctx;
    RecordAudioActivity ctx;
	
	private MediaRecorder mRecorder;
	private MediaPlayer mPlayer;
	private Handler mHandler = new Handler();

	private String defaultCountingText = "00:00.0";
	private TextView counter;
	private long start;
	private File sampleFile;


	private ImageView stopButton;
	private ImageView startRecordButton;
	private ImageView playButton;
	
	private LinearLayout audioButtons;

	
	public RecordAudioDelegate(AnnotateActivity answerQuestionActivity) {
//		this.ctx = answerQuestionActivity;
//
//		if (mRecorder == null) mRecorder = new MediaRecorder();
//
//		audioButtons = (LinearLayout) ctx.findViewById(R.id.audioButtons);
//		audioButtons.setVisibility(audioButtons.GONE);
//		initSoundCapture();
	}

    public RecordAudioDelegate(RecordAudioActivity recordAudioActivity) {
        this.ctx = recordAudioActivity;
        if (mRecorder == null) mRecorder = new MediaRecorder();

        audioButtons = (LinearLayout) ctx.findViewById(R.id.audioButtons);
        audioButtons.setVisibility(audioButtons.GONE);
        initSoundCapture();
    }

    private void initSoundCapture() {
		counter = (TextView) ctx.findViewById(R.id.timeRecording);
		counter.setText(ctx.getString(R.string.annotateCounterReset));
		
		stopButton = (ImageView) ctx.findViewById(R.id.recordButton);
		stopButton.setOnClickListener(stopListener);
		
		startRecordButton = (ImageView) ctx.findViewById(R.id.startRecording);
		startRecordButton.setOnClickListener(startRecordListener);
		
		playButton = (ImageView) ctx.findViewById(R.id.playButton);
		playButton.setOnClickListener(playListener);
	}
	
	View.OnClickListener stopListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			switch (status) {
			case PLAYBACK_AUDIO:
				stopPlaying();
				break;
			case PAUSE_WITH_AUDIO:
				stopPlaying();
				break;
			case STOPPED_WITH_AUDIO:
				stopPlaying();
				break;
			default:
				break;
			}
		}
	};
	
	View.OnClickListener startRecordListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (status) {
			case STOPPED_NO_AUDIO:
				startRecording();
				break;
			case STOPPED_WITH_AUDIO:
				removeRecording();
				break;
			case RECORDING_STATUS:	
				stopRecording();
				break;
			default:
				break;
			}
		}
	};
	
	View.OnClickListener playListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (status) {
			case STOPPED_WITH_AUDIO:
				startPlaying();
				break;
			case PAUSE_WITH_AUDIO:
				resumePlaying();
				break;
			case PLAYBACK_AUDIO:
				pausePlaying();
				break;
			default:
				break;
			}
		}
	};
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}
	
	
	private void startPlaying() {
		stopButton.setImageResource(R.drawable.stop);
		playButton.setImageResource(R.drawable.pause);
		setStatus(PLAYBACK_AUDIO);
		initiateMediaPlayer();
		this.mPlayer.start();
		this.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				 stopButton.setImageResource(R.drawable.stop);
					playButton.setImageResource(R.drawable.play);
					setStatus(STOPPED_WITH_AUDIO);
			}
		});
		start = SystemClock.uptimeMillis();
		startCounting();
	}
	
	private void resumePlaying() {
		playButton.setImageResource(R.drawable.pause);
		setStatus(PLAYBACK_AUDIO);
		this.mPlayer.start();
		this.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				 stopButton.setImageResource(R.drawable.stop);
					playButton.setImageResource(R.drawable.play);
					setStatus(STOPPED_WITH_AUDIO);
			}
		});
		startCounting();
	}
	
	private void stopPlaying() {
		playButton.setImageResource(R.drawable.play);
		setStatus(STOPPED_WITH_AUDIO);
		counter.setText("00:00.0");
		if (this.mPlayer !=null && this.mPlayer.isPlaying()) {
			this.mPlayer.stop();	
			mHandler.removeCallbacks(counterTask);
		} 
	}
	
	private void pausePlaying() {
		stopButton.setImageResource(R.drawable.stop);
		playButton.setImageResource(R.drawable.play);
		setStatus(PAUSE_WITH_AUDIO); // TODO set to pause
		if (this.mPlayer.isPlaying()) {
			this.mPlayer.pause();
			mHandler.removeCallbacks(counterTask);
		} else {
			setStatus(PAUSE_WITH_AUDIO);
		}
		
	}
	
	public void startRecording() {
		startRecordButton.setImageResource(R.drawable.stopaudioopname1en);
		setStatus(RECORDING_STATUS);
		this.sampleFile = MediaFolders.createOutgoingAmrFile();
		initiateMediaRecorder();
		this.mRecorder.start();
		start = SystemClock.uptimeMillis();
		counter.setText("00:00.0");
		startCounting();
		setPrefsRecordingStatus(true);
	}
	
	public void removeRecording(){
		startRecordButton.setImageResource(R.drawable.startaudioopname1en);
		this.sampleFile.delete();	
		this.sampleFile = null;
		this.mRecorder.release();
		this.mRecorder = null;
		counter.setText("00:00.0");
		audioButtons.setVisibility(audioButtons.GONE);
		setStatus(STOPPED_NO_AUDIO);
		ctx.displayPublishButton();
	}
	
	public void removeRecording(boolean deleteSampleFile){
		startRecordButton.setImageResource(R.drawable.startaudioopname1en);
		if (deleteSampleFile){
			this.sampleFile.delete();	
		}
		this.sampleFile = null;
		this.mRecorder.release();
		this.mRecorder = null;
		counter.setText("00:00.0");
		audioButtons.setVisibility(audioButtons.GONE);
		setStatus(STOPPED_NO_AUDIO);
		ctx.displayPublishButton();
	}
	
	public void stopRecording() {
		Log.e("RECORDING", "in stop recording");
		startRecordButton.setImageResource(R.drawable.verwijderopname1en);
		audioButtons.setVisibility(audioButtons.VISIBLE);

		setStatus(STOPPED_WITH_AUDIO);
		setPrefsRecordingStatus(false);
		this.mRecorder.stop();
		ctx.displayPublishButton();

		
	}
	
	public void releaseRecorder() {
		if (this.mRecorder != null) {
			this.mRecorder.release();
		}
	}
	private void setPrefsRecordingStatus(boolean status) {
		
		ctx.pa.setIsRecording(status);
	}
	
	private void startCounting(){
		counter.setText(defaultCountingText);
		mHandler.removeCallbacks(counterTask);
		mHandler.postDelayed(counterTask, 1);
	}
	
	private void initiateMediaPlayer() {
		if (this.mPlayer == null) {
			this.mPlayer = new MediaPlayer();
		}
		this.mPlayer.reset();
		try {
			this.mPlayer.setDataSource(this.sampleFile.getAbsolutePath());
			this.mPlayer.prepare();
		} catch (Exception e) {
			Log.e("mediaplayer", "unable to read datasource: "+this.sampleFile, e);
		} 
		
	}
	
	private void initiateMediaRecorder() {
		if (this.mRecorder == null) {
			this.mRecorder = new MediaRecorder();
		}
		this.mRecorder.reset();
		this.mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		this.mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		this.mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		
		
		this.mRecorder.setOutputFile(this.sampleFile.getAbsolutePath());
		try {
			this.mRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Runnable counterTask = new Runnable() {
		   public void run() {
			   long millis = SystemClock.uptimeMillis() - start;
			   if (status == PLAYBACK_AUDIO) {
				   millis = mPlayer.getCurrentPosition();
			   }
			   int tens = ((int)millis / 100) %10;
		       int seconds = (int) (millis / 1000);
		       int minutes = seconds / 60;
		       seconds     = seconds % 60;
		       if (seconds < 10) {
		    	   defaultCountingText = minutes+":0"+seconds+"."+tens;
		       } else {
		    	   defaultCountingText = minutes+":"+seconds+"."+tens;
		       }
	    	   counter.setText(defaultCountingText);
	    	   if (status == RECORDING_STATUS || status == PLAYBACK_AUDIO) {
			       mHandler.postDelayed(this, 100);		    	   
		       } else {
		    	   mHandler.removeCallbacks(this);
		       }
		       
		   }
	};
	
	public void publish() {
		if (status == RECORDING_STATUS) {
			stopRecording();
		}
		if (status == PLAYBACK_AUDIO) {
			stopPlaying();
		}
	}

	public String getRecordingPath() {
		if (this.sampleFile == null) return null;
		return sampleFile.getAbsolutePath();
	}
	
	public void setRecordingPath(String path) {
		if (path != null) sampleFile = new File(path);
		if (sampleFile != null && sampleFile.exists()) {
			
				stopButton.setImageResource(R.drawable.stop);
				playButton.setImageResource(R.drawable.play);
			
		}
	}
	
	public Uri getUri(){
		if (this.sampleFile == null) return null;
		return Uri.fromFile(sampleFile);
	}
	
	public void stop() {
		switch (status) {
		case STOPPED_WITH_AUDIO:
			removeRecording(false);
			break;
		case PAUSE_WITH_AUDIO:
			stopPlaying();
			removeRecording(false);
			break;
		case PLAYBACK_AUDIO:
			
			stopPlaying();
			removeRecording(false);
			break;
		case RECORDING_STATUS:
			stopRecording();
			removeRecording(false);
			break;
		default:
			break;
		}
		
	}
	

}
		
	

	

