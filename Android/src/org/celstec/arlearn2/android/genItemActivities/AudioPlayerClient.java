package org.celstec.arlearn2.android.genItemActivities;

import org.celstec.arlearn2.android.activities.IGeneralActivity;
import org.celstec.arlearn2.android.menu.ActionDispatcher;
import org.celstec.arlearn2.android.service.IAudioPlayerCallback;
import org.celstec.arlearn2.android.service.IAudioPlayerService;
import org.celstec.arlearn2.beans.run.Action;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

public class AudioPlayerClient extends IAudioPlayerCallback.Stub{

	public final int STOPPED = 0;
	public final int PLAYING = 1;
	public final int PAUSE = 2;
	
	private int status;
	private String audioIdentifier;
	private boolean audioStartedByActivity;
	private Action completeAction;
	private  IGeneralActivity ctx;
	
	private IAudioPlayerService audioPlayerService;
	private AudioPlayerDelegate.ManipulateButtons buttonsManipulator;
	
	public AudioPlayerClient(String audioIdentifier) {
		setAudioIdentifier(audioIdentifier);
	}
	
	public AudioPlayerClient(String audioIdentifier, Action completeAction, IGeneralActivity ctx) {
		this(audioIdentifier);
		this.completeAction = completeAction;
		this.ctx = ctx;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
		switch (status) {
		case PLAYING:
			buttonsManipulator.setPlayingButtons();
			break;
		case PAUSE:
			buttonsManipulator.setPauseButtons();
			break;
		case STOPPED:
			buttonsManipulator.setStopButtons();
			break;
		default:
			break;
		}
	}
	
	public AudioPlayerDelegate.ManipulateButtons getButtonsManipulator() {
		return buttonsManipulator;
	}

	public void setButtonsManipulator(AudioPlayerDelegate.ManipulateButtons buttonsManipulator) {
		this.buttonsManipulator = buttonsManipulator;
	}

	public IAudioPlayerService getAudioPlayerService() {
		return audioPlayerService;
	}

	public void setAudioPlayerService(IAudioPlayerService audioPlayerService) {
		this.audioPlayerService = audioPlayerService;
		if (audioPlayerService !=null) {
			try {
				setStatus(audioPlayerService.getStatusFromIdentifier(audioIdentifier));
			} catch (RemoteException e) {
				Log.e("error while fetching status for "+audioIdentifier, e.getMessage(), e);
			}
		}
	}

	public String getAudioIdentifier() {
		return audioIdentifier;
	}

	public void setAudioIdentifier(String audioIdentifier) {
		this.audioIdentifier = audioIdentifier;
	}

	public void startPlaying() {
		setStatus(PLAYING);
		audioStartedByActivity = true;
		try {
			audioPlayerService.start(getAudioIdentifier(), this);
		} catch (RemoteException e) {
			Log.e("error while starting playing", e.getMessage(), e);
		}
	}
	
	public void pausePlaying() {
		setStatus(PAUSE);
		try {
			audioPlayerService.pause();
		} catch (RemoteException e) {
			Log.e("error while pause playing", e.getMessage(), e);
		}
	}
	
	public void resumePlaying() {
		setStatus(PLAYING);
		try {
			audioPlayerService.play();
		} catch (RemoteException e) {
			Log.e("error while resume playing", e.getMessage(), e);
		}
	}
	
	public void stopPlaying() {
		setStatus(STOPPED);
		try {
			audioPlayerService.stop();
		} catch (RemoteException e) {
			Log.e("error while stop playing", e.getMessage(), e);
		}
	}
	
	

	
	public void stop(String id) throws RemoteException {
		if (completeAction != null) {
			completeAction.setTime(System.currentTimeMillis());
			ActionDispatcher.publishAction(ctx, completeAction);
		}
		setStatus(STOPPED);
	}

	public void userClickedPlayButton() {
		switch (status) {
		case STOPPED:
			startPlaying();
			break;
		case PLAYING:
			pausePlaying();
			break;
		case PAUSE:
			resumePlaying();
			break;
		default:
			break;
		}
		
	}
	
	public void userClickedStopButton() {
		switch (status) {
		case PLAYING:
			stopPlaying();
			break;
		case PAUSE:
			stopPlaying();
			break;
		default:
			break;
		}
		
	}
	
	
}
