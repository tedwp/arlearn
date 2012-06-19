package org.celstec.arlearn2.android.sync;

import java.util.List;

import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MyActions;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.client.ActionClient;
import org.celstec.arlearn2.android.db.beans.BeanDeserialiser;

import android.content.Context;
import android.util.Log;

public class MyActionsSyncronizer extends GenericSyncroniser {

	private final int myResponsesSyncronizerDelay = 10;

	private static MyActionsSyncronizer  instance = null;
	
	public MyActionsSyncronizer(Context ctx) {
		super(ctx);
		instance = this;
	}
	
	public static MyActionsSyncronizer getInstance(){
		return instance;
	}

	@Override
	protected synchronized void runAuthenticated() {
		DBAdapter db = new DBAdapter(ctx);
		db.openForWrite();
		List<Action> actions = ((MyActions)db.table(DBAdapter.MYACTIONS_ADAPTER)).query();
		if (actions.size() == 0) {
			increaseDelay();
		} else {
			setDelay(myResponsesSyncronizerDelay);
			publishAction(db, actions);
		}
		db.close();
		
	}
	
	private void publishAction(DBAdapter db, List<Action> responses) {
		for (Action action: responses) {
			ActionClient ac = ActionClient.getActionClient();
			Action result = ac.publishAction(pa.getFusionAuthToken(), action);
			MyActions dbAdapter = ((MyActions) db.table(DBAdapter.MYACTIONS_ADAPTER));
			if (result.getError()== null) {
				dbAdapter.confirmReplicated(result);	
			} else {
				if (result.getError() !=null && "User not found".equals(result.getError())) 				
					dbAdapter.confirmReplicated(result);	 //this is not elegant... but it mean the user was deleted, so don't try to sync in future
				increaseDelay();
			}
		}
	}
	public void setDelay(int seconds) {
		super.setDelay(seconds);
	}
}
