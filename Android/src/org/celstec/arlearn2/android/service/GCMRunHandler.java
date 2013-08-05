package org.celstec.arlearn2.android.service;

import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.android.delegators.RunDelegator;

import android.content.Context;

public class GCMRunHandler extends GCMHandler{

	
	protected GCMRunHandler(Context ctx){
		super(ctx);
	}
	@Override
	public void handle() {
		GameDelegator.getInstance().synchronizeParticipateGamesWithServer(ctx);
		RunDelegator.getInstance().synchronizeRunsWithServer(ctx);
	}

}
