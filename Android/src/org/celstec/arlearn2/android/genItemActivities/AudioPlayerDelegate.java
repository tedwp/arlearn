package org.celstec.arlearn2.android.genItemActivities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.IGeneralActivity;
import org.celstec.arlearn2.android.menu.ActionDispatcher;
import org.celstec.arlearn2.android.service.AudioPlayerService;
import org.celstec.arlearn2.android.service.IAudioPlayerService;
import org.celstec.arlearn2.beans.run.Action;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class AudioPlayerDelegate {
	
	private ImageView playButton;
	private ImageView stopButton;
	
	private Context ctx;
	
	public AudioPlayerDelegate(String identifier, Context ctx) {
		this.ctx = ctx;
		
		apClient = new AudioPlayerClient(identifier);
	
		apClient.setButtonsManipulator(new ManipulateButtons());

		if (apClient.getAudioPlayerService() == null) {
			ctx.bindService(new Intent(ctx, AudioPlayerService.class), serviceConnection, Context.BIND_AUTO_CREATE);
		}
	}
	
	public AudioPlayerDelegate(String identifier, AudioObjectActivity ctx, Action action) {
		this.ctx = ctx;
		
		if (action == null) {
			apClient = new AudioPlayerClient(identifier);
		} else {
			apClient = new AudioPlayerClient(identifier, action, ctx);
		}
	
		apClient.setButtonsManipulator(new ManipulateButtons());

		if (apClient.getAudioPlayerService() == null) {
			ctx.bindService(new Intent(ctx, AudioPlayerService.class), serviceConnection, Context.BIND_AUTO_CREATE);
		}
	}
	
	private AudioPlayerClient apClient;

	private IAudioPlayerService audioPlayerService;
	
	
	private ServiceConnection serviceConnection = new ServiceConnection() {

		
		public void onServiceConnected(ComponentName name, IBinder service) {
			audioPlayerService = IAudioPlayerService.Stub.asInterface(service);
			try {
				audioPlayerService.setCallback(apClient);
				apClient.setAudioPlayerService(audioPlayerService);
			} catch (RemoteException e) {
				Log.e("serviceConnection in audioobjectactivity failed", e.getMessage(), e);
			}
		}

		
		public void onServiceDisconnected(ComponentName name) {
			if (apClient != null) {
				apClient.setAudioPlayerService(null);
			}
		}
	};
	
	public class ManipulateButtons {
		
		public void setPlayingButtons() {
			playButton.setImageResource(R.drawable.black_pause); //todo must be black_pause
			stopButton.setImageResource(R.drawable.black_stop); //tod must be black_stop
		}
		
		public void setPauseButtons() {
			playButton.setImageResource(R.drawable.black_play); 
			stopButton.setImageResource(R.drawable.black_stop); //tod must be black_stop
		}
		
		public void setStopButtons() {
			playButton.setImageResource(R.drawable.black_play); 
			stopButton.setImageResource(R.drawable.grey_stop); 
		}
	}

	public void setPlayButton(ImageView iv) {
		playButton = iv;
		if (playButton != null)
			playButton.setOnClickListener(playListener);
		
	}

	public void setStopButton(ImageView iv) {
		stopButton = iv;
		if (stopButton != null)
			stopButton.setOnClickListener(stopListener);
	};
	
	View.OnClickListener playListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			apClient.userClickedPlayButton();
		}
	};

	View.OnClickListener stopListener = new View.OnClickListener() {

		
		public void onClick(View v) {
			apClient.userClickedStopButton();
		}
	};

	public void unbind() {
		if (audioPlayerService != null) ctx.unbindService(serviceConnection);		
	}

	public void stopPlaying() {
		apClient.setStatus(apClient.STOPPED);
		if (audioPlayerService != null) {
			try {
				audioPlayerService.stop();
			} catch (RemoteException e) {
				Log.e("error in onPause", e.getMessage(), e);
			}
		}
		
	}

}
