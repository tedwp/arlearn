package org.celstec.arlearn2.android.broadcast;


import org.celstec.arlearn2.android.broadcast.task.SynchronizeGamesTask;
import org.celstec.arlearn2.beans.notification.RunModification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class GameReceiver extends GenericReceiver {

	public static String action = "org.celstec.arlearn2.beans.notification.GameModification";
	public static final String GAME_ID = "gameId";

	@Override
	public void onReceive(final Context context, final Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Long gameId = extras.getLong(GAME_ID);
			(new SynchronizeGamesTask(context, gameId)).addTaskToQueue(context);
		} else {
			(new SynchronizeGamesTask(context)).addTaskToQueue(context);	
		}
		
	}
}
