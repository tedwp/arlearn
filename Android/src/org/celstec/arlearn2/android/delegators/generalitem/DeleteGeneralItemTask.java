package org.celstec.arlearn2.android.delegators.generalitem;

import org.celstec.arlearn2.android.activities.ListGamesActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTaskHandler;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.client.GeneralItemClient;

import android.content.Context;
import android.os.Message;
import android.util.Log;

public class DeleteGeneralItemTask implements NetworkTask {

	private Context ctx;

	private Long generalItemId;
	private Long gameId;
	
	public DeleteGeneralItemTask(Context ctx) {
		this.ctx = ctx;
	}


	public void addTaskToQueue(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		Message m = Message.obtain(nwHandler);
		m.obj = this;
		//m.what = NetworkTaskHandler.SYNC_GENERALITEMS;
		m.sendToTarget();

	}	

	public Long getGeneralItemId() {
		return generalItemId;
	}

	public void setGeneralItemId(Long giId) {
		this.generalItemId = giId;
	}
	
	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	
	
	
	@Override
	public void execute() {
		try {
			GeneralItemClient.getGeneralItemClient().delete(PropertiesAdapter.getInstance(ctx).getFusionAuthToken(), generalItemId, gameId);
		} catch (Exception e) {
			Log.e("exception", "in databasehandler", e);		
		}
		
	}


}
