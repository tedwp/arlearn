package org.celstec.arlearn2.android.service;

import java.io.Serializable;

import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.RunAdapter;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.run.Run;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class BeanReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		System.out.println("action "+intent.getAction());
		if (extras != null) {
			Serializable bean = extras.getSerializable("bean");
			
			if (bean instanceof RunModification) {
				Intent runIntent = new Intent();
				runIntent.setAction("org.celstec.arlearn2.beans.notification.RunModification");
				runIntent.putExtra("bean", bean);
				context.sendBroadcast(runIntent);
				
//				process(context, (RunModification) bean);
			}
			if (bean instanceof GeneralItemModification) {
				Intent gimIntent = new Intent();
				gimIntent.setAction("org.celstec.arlearn2.beans.notification.GeneralItemModification");
				gimIntent.putExtra("bean", bean);

				System.out.println("canoncial name is "+GeneralItemModification.class.getCanonicalName());
				context.sendBroadcast(gimIntent);
//				process(context, (GeneralItemModification) bean);
			}
			
			Log.w("DEBUG", "bean received"+ bean);
		}
	}
	
	

	
}
