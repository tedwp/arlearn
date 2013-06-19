package org.celstec.arlearn2.android.answerQuestion;

import org.celstec.arlearn2.beans.generalItem.OpenQuestion;

import android.app.Activity;
import android.content.Intent;

public class DataCollectorDelegateManager {
	PictureDataCollectorDelegate pictureDC;
	public DataCollectorDelegateManager(OpenQuestion oq, Activity ctx) {
		if (oq.isWithPicture()) {
			pictureDC =  new PictureDataCollectorDelegate(ctx);
		}
		
	}
	public void processResult(int resultCode, Intent data) {
		switch (resultCode) {
		case DataCollectorDelegate.PICTURE_RESULT:
			pictureDC.processResult(data);
			break;

		default:
			break;
		}
		
	}

}
