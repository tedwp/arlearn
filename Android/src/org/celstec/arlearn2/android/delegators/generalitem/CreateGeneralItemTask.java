package org.celstec.arlearn2.android.delegators.generalitem;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTaskHandler;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.client.GeneralItemClient;

import android.content.Context;
import android.os.Message;
import android.util.Log;

public class CreateGeneralItemTask implements NetworkTask {

	private Context ctx;

	private GeneralItem generalItem;
	
	public CreateGeneralItemTask(Context ctx, GeneralItem gi) {
		this.ctx = ctx;
		this.generalItem = gi;
	}


	public void addTaskToQueue(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		Message m = Message.obtain(nwHandler);
		m.obj = this;
		m.what = NetworkTaskHandler.GENERALITEM_CREATE;
		m.sendToTarget();

	}	

	
	
	@Override
	public void execute() {	
		
		try {
			GeneralItemClient.getGeneralItemClient().postGeneralItem(PropertiesAdapter.getInstance(ctx).getAuthToken(), generalItem.toString());
			GeneralItemsDelegator.getInstance().synchronizeGeneralItemsWithServer(ctx, generalItem.getGameId());

		} catch (Exception e) {
			Log.e("exception", "in databasehandler", e);		
		}
		
	}


}
