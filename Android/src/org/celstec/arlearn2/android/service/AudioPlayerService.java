package org.celstec.arlearn2.android.service;

import org.celstec.arlearn2.android.db.PropertiesAdapter;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class AudioPlayerService extends Service {

	private int status = STOPPED;
	private String identifier = null;
	private IAudioPlayerCallback callback;
	private MediaPlayer mPlayer;
	private PropertiesAdapter pa;
	private final IAudioPlayerService.Stub mBinder = new AudioPlayerBinder(this);
	
	public static final int STOPPED = 0;
	public static final int PLAYING = 1;
	public static final int PAUSE = 2;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		pa = new PropertiesAdapter(this);
		super.onCreate();
	}

	public int getStatus() {
		return status;
	}
	
	public int getStatus(String identifier) {
		if (identifier == null) return status;
		if (AudioPlayerService.this.identifier == null) return STOPPED;
		if (!AudioPlayerService.this.identifier.equals(identifier)) return STOPPED;
		return status;
	}
	
	public void startPlaying() {
		status = PLAYING;
		this.mPlayer.start();
		pa.setIsPlaying(true);
	}
	
	public void pausePlaying() {
		status = PAUSE;
		if (this.mPlayer !=null) this.mPlayer.pause();
	}
	
	public void stopPlaying(){
		status = STOPPED;
		pa.setIsPlaying(false);
		if (this.mPlayer == null) return; 
		if (this.mPlayer !=null) {
			this.mPlayer.stop();
		}
		try {
			if (this.mPlayer !=null) this.mPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void resumePlaying() {
		status = PLAYING;
		if (this.mPlayer !=null) this.mPlayer.start();
	}
	
	public void setVolume(float level) {
		if (status == PLAYING) {
			if (this.mPlayer != null) this.mPlayer.setVolume(level, level);
		}
	}
	
	protected void initiateMediaPlayer(Uri audioFile) {
		if (this.mPlayer == null) {
			this.mPlayer = new MediaPlayer();
		}
		this.mPlayer.reset();
		
		try {
			this.mPlayer.setDataSource(this, audioFile);
//			this.mPlayer.setDataSource(audioFile);
			this.mPlayer.prepare();
			
		} catch (Exception e) {
			Log.e("mediaplayer", "unable to read datasource: "+audioFile, e);
		} 
		
	}
	
	protected void setOnCompletionListener() {
		this.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {		
				stopPlaying();
				pa.setIsPlaying(false);
				try {
					if (callback != null) callback.stop(identifier);
				} catch (RemoteException e) {
					Log.e("remote exception in audioplayerservice", e.getMessage(), e);
				}
			}
		});
	}
	
	public void setCallback(IAudioPlayerCallback callback) {
		this.callback = callback;
	}
	
	public void setAudioIdentifier(String identifier) {
		this.identifier = identifier;
	}
	

}
