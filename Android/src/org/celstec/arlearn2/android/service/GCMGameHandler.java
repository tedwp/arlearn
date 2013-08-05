package org.celstec.arlearn2.android.service;

import org.celstec.arlearn2.android.delegators.GameDelegator;

import android.content.Context;

public class GCMGameHandler extends GCMHandler{

	protected GCMGameHandler(Context ctx){
		super(ctx);
	}
	@Override
	public void handle() {
		GameDelegator.getInstance().synchronizeParticipateGamesWithServer(ctx);
	}

}
