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
package org.celstec.arlearn2.android.genItemActivities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.beans.run.Action;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class AudioPlayerDelegate {

	private ImageView playButton;
	private ImageView stopButton;

	private Context ctx;

	private MediaPlayer mPlayer;
	private int status = STOPPED;

	public static final int STOPPED = 0;
	public static final int PLAYING = 1;
	public static final int PAUSE = 2;

	private Action completeAction;

	public AudioPlayerDelegate(Uri localPath, Context ctx) {
		this.ctx = ctx;
		if (this.mPlayer == null) {
			this.mPlayer = new MediaPlayer();
		}
		this.mPlayer.reset();

		try {
			this.mPlayer.setDataSource(ctx, localPath);
			this.mPlayer.prepare();

		} catch (Exception e) {
			Log.e("mediaplayer", "unable to read datasource: " + localPath, e);
		}
	}

	public AudioPlayerDelegate(Uri localAudioUri, AudioObjectActivity audioObjectActivity, Action completeAction) {
		this (localAudioUri, audioObjectActivity);
		this.completeAction = completeAction;
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

	public void stopPlaying() {
		setStatus(STOPPED);
		if (this.mPlayer == null)
			return;
		if (this.mPlayer != null) {
			this.mPlayer.stop();
		}
		try {
			if (this.mPlayer != null)
				this.mPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setPlayingButtons() {
		playButton.setImageResource(R.drawable.black_pause); 
		stopButton.setImageResource(R.drawable.black_stop); 
	}

	private void setPauseButtons() {
		playButton.setImageResource(R.drawable.black_play);
		stopButton.setImageResource(R.drawable.black_stop); 
	}

	private void setStopButtons() {
		playButton.setImageResource(R.drawable.black_play);
		stopButton.setImageResource(R.drawable.grey_stop);
	}

	View.OnClickListener playListener = new View.OnClickListener() {

		public void onClick(View v) {
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
	};

	View.OnClickListener stopListener = new View.OnClickListener() {
		public void onClick(View v) {
			stopPlaying();
		}
	};

	MediaPlayer.OnCompletionListener completeListener = new MediaPlayer.OnCompletionListener() {

		public void onCompletion(MediaPlayer mp) {
			stopPlaying();
			playbackCompleted();
		}
	};

	public void startPlaying() {
		setStatus(PLAYING);
		this.mPlayer.seekTo(0);
		this.mPlayer.start();
		this.mPlayer.setOnCompletionListener(completeListener);
	}

	private void pausePlaying() {
		setStatus(PAUSE);
		if (this.mPlayer != null)
			this.mPlayer.pause();
	}

	private void resumePlaying() {
		setStatus(PLAYING);
		if (this.mPlayer != null)
			this.mPlayer.start();
	}

	private void setStatus(int status) {
		this.status = status;
		switch (status) {
		case PLAYING:
			setPlayingButtons();
			break;
		case PAUSE:
			setPauseButtons();
			break;
		case STOPPED:
			setStopButtons();
			break;
		default:
			break;
		}
	}

	private void playbackCompleted() {
		if (completeAction != null) {
			completeAction.setTime(System.currentTimeMillis());
			ActionsDelegator.getInstance().publishAction((Context) ctx, completeAction);
		}
		setStatus(STOPPED);
	}

}
