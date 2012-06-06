package org.celstec.arlearn2.android.service;

import java.util.HashMap;
import java.util.List;

import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class GeneralItemReceiver  extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("in GeneralItemReceiver ");
		Bundle extras = intent.getExtras();
		if (extras != null) {
			GeneralItemModification bean = (GeneralItemModification) extras.getSerializable("bean");
			if (bean != null) {
				System.out.println("in GeneralItemReceiver before store");
				processItem(context, bean);
				GeneralItemDependencyHandler gdh = new GeneralItemDependencyHandler(context);
				gdh.checkDependencies();
				Intent updateIntent = new Intent();
				updateIntent.setAction("org.celstec.arlearn.updateActivities");
				updateIntent.putExtra(ListMessagesActivity.class.getCanonicalName(), true);

				context.sendBroadcast(updateIntent);
			}
		}
		System.out.println("in GeneralItemReceiver before dep checker");
//		GeneralItemDependencyHandler gdh = new GeneralItemDependencyHandler(context);
//		gdh.checkDependencies();
		
	}
	
	private void processItem(Context ctx, GeneralItemModification bean) {
		bean.getGeneralItem().setRunId(bean.getRunId());
		DBAdapter db = new DBAdapter(ctx);
		db.openForWrite();
		switch (bean.getModificationType()) {
		case GeneralItemModification.CREATED:
			boolean result = ((GeneralItemAdapter)db.table(DBAdapter.GENERALITEM_ADAPTER)).insert(bean.getGeneralItem());
			System.out.println("insert succeed for run "+bean.getRunId()+" "+result);
			break;
		case GeneralItemModification.DELETED:
			((GeneralItemAdapter)db.table(DBAdapter.GENERALITEM_ADAPTER)).delete(bean.getGeneralItem().getId(), bean.getRunId());

			break;
//		case RunModification.ALTERED:
//			((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).delete(rm.getRun().getRunId());
//			((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).insert(rm.getRun());
//			break;
		default:
			break;
		}
		db.close();
	}
	

}
