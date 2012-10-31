package org.celstec.arlearn2.android.service;

import java.io.File;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.genItemActivities.AudioObjectActivity;

import android.net.Uri;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;

public class AudioPlayerBinder extends IAudioPlayerService.Stub {

	private AudioPlayerService service;
	public AudioPlayerBinder(AudioPlayerService service) {
		super();
		this.service = service;
	}
	
	
	public int getStatusFromIdentifier(String identifier) throws RemoteException {
		return service.getStatus(identifier);
	}

	
	public void setCallback(IAudioPlayerCallback callback) throws RemoteException {
		service.setCallback(callback);
	}

	
	public void start(final String audioIdentifier, IAudioPlayerCallback callback) throws RemoteException {
		service.setCallback(callback);
		service.setAudioIdentifier(audioIdentifier);
		Message m = Message.obtain(DBAdapter.getDatabaseThread(service));
		m.obj = new DBAdapter.DatabaseTask() {
			
			@Override
			public void execute(DBAdapter db) {
				Uri audioUri = ((MediaCache) db.table(DBAdapter.MEDIA_CACHE)).getUriFromIdIgnoreReplication(audioIdentifier);
				service.initiateMediaPlayer(audioUri);
				try {
					setVolume(1);
					service.startPlaying();
					service.setOnCompletionListener();	
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		};
		m.sendToTarget();			
	}

	
	public void play() throws RemoteException {
		if (service.getStatus() == service.PAUSE) service.startPlaying();
	}

	
	public void pause() throws RemoteException {
		if (service.getStatus() == service.PLAYING) service.pausePlaying();
	}

	
	public void stop() throws RemoteException {
		if (service.getStatus() == service.PAUSE ||service.getStatus() == service.PLAYING) service.stopPlaying();
	}

	
	public void setVolume(float level) throws RemoteException {
		service.setVolume(level);
	}

	
	public void setAudioIdentifier(String audioIdentifier) throws RemoteException {
		service.setAudioIdentifier(audioIdentifier);	
		//TODO delete
		
	}
}
