package org.celstec.arlearn2.android.service;

import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.notificationbeans.GeneralItem;
import org.celstec.arlearn2.android.sync.GeneralItemsSyncroniser;

import android.content.Context;

public class GeneralItemNotifications {
	private Context ctx;
	
	public GeneralItemNotifications(Context ctx) {
		this.ctx = ctx;
	}

	public void makeGeneralItemVisible(GeneralItem gibean) {
		
		GeneralItemsSyncroniser.syncronizeItems(ctx, new PropertiesAdapter(ctx));
		//download generalItem
		//send notification
		
	}

}
