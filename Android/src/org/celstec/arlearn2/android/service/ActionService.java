package org.celstec.arlearn2.android.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

public class ActionService extends IntentService {

	public ActionService(String name) {
		super("ActionService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		System.out.println("test");
		
		
	}
	

}
