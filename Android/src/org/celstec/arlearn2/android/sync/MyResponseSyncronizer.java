package org.celstec.arlearn2.android.sync;

import org.celstec.arlearn2.android.broadcast.ResponseService;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MyLocations;
import org.celstec.arlearn2.android.db.MyResponses;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.beans.BeanDeserialiser;
//import org.celstec.arlearn2.android.db.beans.LocationList;
//import org.celstec.arlearn2.android.db.beans.Response;
import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.client.ResponseClient;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyResponseSyncronizer extends GenericSyncroniser {

	private final int myResponsesSyncronizerDelay = 10;

	private static MyResponseSyncronizer  instance = null;
	
	public MyResponseSyncronizer(Context ctx) {
		super(ctx);
		instance = this;
	}
	
	public static MyResponseSyncronizer getInstance(){
		return instance;
	}

	
	@Override
	protected void runAuthenticated() {
//		Intent intent = new Intent();
//		intent.setAction(ResponseService.action);
//		ctx.sendBroadcast(intent);

		Intent intent = new Intent(ctx, ResponseService.class);
		
		ctx.startService(intent);
//			boolean increase = syncResponses(ctx, pa);
//			if (increase) {
//				increaseDelay();
//			} else {
//				setDelay(myResponsesSyncronizerDelay);
//			}
		
	}
	
//	public static boolean syncResponses(Context ctx, PropertiesAdapter pa) {
//		DBAdapter db = new DBAdapter(ctx);
//		db.openForWrite();
//		Response responses[] = (Response[]) ((MyResponses)db.table(DBAdapter.MYRESPONSES_ADAPTER)).queryRevoked();
//		publishResponse(db, responses, pa);
//
//		db.close();
//		return (responses.length == 0);
//	}
	
	public void setDelay(int seconds) {
		super.setDelay(seconds);
	}

	private static void publishResponse(DBAdapter db, Response[] responses, PropertiesAdapter pa) {
		for (Response r: responses) {
		try {
			Response result = ResponseClient.getResponseClient().publishAction(pa.getFusionAuthToken(), r);
//			String jsonAsString = HttpUtils.postAsJSON(HttpUtils.serviceUrl + "response/runId/"+pa.getCurrentRunId()+"/generalItemId/itemId", pa.getFusionAuthToken(), r);
//			Response result = (Response) (new BeanDeserialiser(jsonAsString)).deserialize(Response.class);
			
			MyResponses dbAdapter = ((MyResponses) db.table(DBAdapter.MYRESPONSES_ADAPTER));
			if (result.getError()== null) {
				dbAdapter.confirmReplicated(result);	
			} else {
				//TODO
//				increaseDelay();
			}
		} catch (Exception e) {
			Log.e("exception", e.getMessage(), e);
//			increaseDelay();
		}
		}
	}
	
	public void increaseDelay() {
		if (delay < 60) {
			delay *= 2;
		}
	}
	
	

}
