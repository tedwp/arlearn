package org.celstec.arlearn2.android.delegators.generalitem;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTaskHandler;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.client.GeneralItemClient;

import android.content.Context;
import android.os.Message;
import android.util.Log;

public class QueryGeneralItemsTask implements NetworkTask {
	
	private Context ctx;

	private String owner;
	
	private GeneralItemList gl;

	
	public QueryGeneralItemsTask(Context ctx) {
		this.ctx = ctx;
	}
	

	public void addTaskToQueue(Context ctx) {
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		Message m = Message.obtain(nwHandler);
		m.obj = this;
		m.what = NetworkTaskHandler.GENERALITEM_QUERY;
		m.sendToTarget();
	}

	@Override
	public void execute() {

		try {
			// TODO fix this whenever it is ready in the server part
			gl = GeneralItemClient.getGeneralItemClient().search(PropertiesAdapter.getInstance(ctx).getFusionAuthToken(), owner);
		} catch (Exception e) {
			Log.e("exception", "in databasehandler", e);		
		}
		
	}


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public GeneralItemList getGeneralItems() {
		return gl;
	}




}
