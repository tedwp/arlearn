package org.celstec.arlearn2.android.delegators.generalitem;

import java.util.List;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTaskHandler;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.client.GeneralItemClient;

import android.content.Context;
import android.os.Message;
import android.util.Log;

public class QueryGeneralItemsTask implements NetworkTask {
	
	private Context ctx;

	private String sMatchingString;
	
	private final GeneralItemList gl = null;

	
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
			final GeneralItemList rgl = GeneralItemClient.getGeneralItemClient().search(PropertiesAdapter.getInstance(ctx).getFusionAuthToken(), sMatchingString);
//			List<GeneralItem>l = rgl.getGeneralItems();
//			for (int i = 0; i < l.size(); i++) {
//				System.out.println("QueryGeneralItem.execute:"+l.get(i).getName());
//				
//			}
			GeneralItemsDelegator.getInstance().saveGeneralItemsSearchList(rgl);
		} catch (Exception e) {
			Log.e("exception", "in databasehandler", e);		
		}
		
	}


	public String getMatchingString() {
		return sMatchingString;
	}


	public void setMatchingString(String s) {
		this.sMatchingString = s;
	}


	public GeneralItemList getGeneralItems() {
		return gl;
	}




}
