package org.celstec.arlearn2.android.activities;

import android.os.Bundle;

public interface ARLearnBroadcastReceiver {

	public void onBroadcastMessage(Bundle bundle);
	
	public boolean showStatusLed();
}
