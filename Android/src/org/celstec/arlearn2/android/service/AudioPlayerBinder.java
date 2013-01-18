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
//import android.widget.Toast;
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

	
	public void startItem(final long audioIdentifier, IAudioPlayerCallback callback) throws RemoteException {
		service.setCallback(callback);
		service.setAudioIdentifier(""+audioIdentifier);
		Uri audioUri = org.celstec.arlearn2.android.cache.MediaCache.getInstance().getLocalUri(audioIdentifier, "audio");
		if (audioUri != null) {
			startUri(audioUri);
		} else {
			Toast toast = Toast.makeText(service, service.getString(R.string.downloadBusy), Toast.LENGTH_LONG);
			toast.show();
			callback.stop(""+audioIdentifier);
		}
		
	}
	public void start(final String audioIdentifier, IAudioPlayerCallback callback) throws RemoteException {
		service.setCallback(callback);
		service.setAudioIdentifier(audioIdentifier);
		String localFile = org.celstec.arlearn2.android.cache.MediaCache.getInstance().getMediaCacheItem(Long.parseLong(audioIdentifier), "audio").getLocalFile(); //idToMediaCacheItem.get("144008")
		if (localFile != null) {
			Uri audioUri = Uri.fromFile(new File(localFile));
			startUri(audioUri);
		} else {
			Uri audioUri = org.celstec.arlearn2.android.cache.MediaCache.getInstance().getMediaCacheItem(Long.parseLong(audioIdentifier), "audio").getUri();
			startUri(audioUri);
		}
	}
	
	private void startUri(Uri audioUri) {
		service.initiateMediaPlayer(audioUri);
		try {
			setVolume(1);
			service.startPlaying();
			service.setOnCompletionListener();	
		} catch (RemoteException e) {
			e.printStackTrace();
		}
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
