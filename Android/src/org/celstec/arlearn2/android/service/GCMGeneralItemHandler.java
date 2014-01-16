package org.celstec.arlearn2.android.service;

import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;

import android.content.Context;

public class GCMGeneralItemHandler extends GCMHandler {

	protected long gameId;
	protected long runId;
	
	protected GCMGeneralItemHandler(Context ctx, long runId, long gameId) {
		super(ctx);
		this.gameId = gameId;
		this.runId = runId;
	}

	@Override
	public void handle() {
		GeneralItemsDelegator.getInstance().synchronizeGeneralItemsWithServer(ctx, runId, gameId, true);

	}

}
