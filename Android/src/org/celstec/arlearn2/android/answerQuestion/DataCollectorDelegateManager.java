package org.celstec.arlearn2.android.answerQuestion;

import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;

import android.app.Activity;
import android.content.Intent;

public class DataCollectorDelegateManager {
	PictureDataCollectorDelegate pictureDC;
	AudioDataCollectorDelegate audioDC;
	VideoDataCollectorDelegate videoDC;
	TextDatacollectorDelegate textDC;
	ValueCollectorDelegate valueDC;
	
	public DataCollectorDelegateManager(OpenQuestion oq, NarratorItemActivity ctx, long runId, String account) {
		if (oq.isWithPicture()) {
			pictureDC =  new PictureDataCollectorDelegate(ctx, runId, account);
		}
		if (oq.isWithAudio()) {
			audioDC = new AudioDataCollectorDelegate(ctx, runId, account);
		}
		if (oq.isWithVideo()) {
			videoDC = new VideoDataCollectorDelegate(ctx, runId, account);
		}
		if (oq.isWithText()) {
			textDC = new TextDatacollectorDelegate(ctx, runId, account, oq.getTextDescription());
		}
		if (oq.isWithValue()){
			valueDC = new ValueCollectorDelegate(ctx, runId, account, oq.getValueDescription());
		}
		
	}
	public void processResult(int resultCode, Intent data) {
		switch (resultCode) {
		case DataCollectorDelegate.PICTURE_RESULT:
			pictureDC.processResult(data);
			break;
		case DataCollectorDelegate.AUDIO_RESULT:
			audioDC.processResult(data);
			break;
		case VideoDataCollectorDelegate.VIDEO_RESULT:
			videoDC.processResult(data);
			break;
		case TextDatacollectorDelegate.TEXT_RESULT:
//			textDC.pr
		default:
			break;
		}
		
	}

}
