package org.celstec.arlearn2.android.service;

import org.celstec.arlearn2.android.service.IAudioPlayerCallback;

interface IAudioPlayerService {
	
	int getStatusFromIdentifier(String identifier);
	
	void setCallback(IAudioPlayerCallback callback);
	
	void setAudioIdentifier(String audioIdentifier);
	
	void start(String identifier, IAudioPlayerCallback callback);
	
	void play();
	
	void pause();
	
	void stop();
	
	void setVolume(float level);	

}