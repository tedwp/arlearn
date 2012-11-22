package org.celstec.arlearn2.android.broadcast;


import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.broadcast.task.CreateNewGItemTask;
import org.celstec.arlearn2.android.broadcast.task.DeleteItemTask;
import org.celstec.arlearn2.android.broadcast.task.MakeGeneralItemDisappearedTask;
import org.celstec.arlearn2.android.broadcast.task.MakeGeneralItemVisibleTask;
import org.celstec.arlearn2.android.broadcast.task.SynchronizeGeneralItemsTask;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.OpenBadge;
import org.celstec.arlearn2.beans.generalItem.OpenUrl;
import org.celstec.arlearn2.beans.generalItem.ScanTag;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

@SuppressLint("ParserError")
public class GeneralItemReceiver extends BroadcastReceiver {

	public static String action = "org.celstec.arlearn2.beans.notification.GeneralItemModification";

	@Override
	public void onReceive(final Context context, final Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			GeneralItemModification bean = (GeneralItemModification) extras.getSerializable("bean");
			if (bean != null) {
				processItem(context, bean);
			}
		} else {
			(new SynchronizeGeneralItemsTask(context, (new PropertiesAdapter(context)).getCurrentRunId())).addTaskToQueue(context);
		}
	}

	private void processItem(Context ctx, GeneralItemModification bean) {
		if (!canDealWithitemType(bean.getGeneralItem()))
			return;

		DBAdapter.DatabaseTask task = null;
		switch (bean.getModificationType()) {
		case GeneralItemModification.CREATED:
			task = new CreateNewGItemTask();
			((CreateNewGItemTask) task).setAuthToken(PropertiesAdapter.getInstance(ctx).getFusionAuthToken());
			((CreateNewGItemTask) task).setGeneralItemId(bean.getGeneralItem().getId());
			((CreateNewGItemTask) task).setRunId(bean.getRunId());
			break;
		case GeneralItemModification.DELETED:
			task = new DeleteItemTask();
			((DeleteItemTask) task).setGeneralItemId(bean.getGeneralItem().getId());
			break;
		case GeneralItemModification.VISIBLE:
			task = new MakeGeneralItemVisibleTask();
			((MakeGeneralItemVisibleTask) task).setRunId(bean.getRunId());
			((MakeGeneralItemVisibleTask) task).setGi(bean.getGeneralItem());

			break;
		case GeneralItemModification.DISAPPEARED:
			task = new MakeGeneralItemDisappearedTask();
			((MakeGeneralItemDisappearedTask) task).setRunId(bean.getRunId());
			((MakeGeneralItemDisappearedTask) task).setGi(bean.getGeneralItem());
			break;
		default:
			break;
		}
		if (task != null) {
			Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
			m.obj = task;
			m.sendToTarget();
		}
	}

	private boolean canDealWithitemType(GeneralItem generalItem) {
		if (generalItem instanceof MultipleChoiceTest)
			return true;
		if (generalItem instanceof OpenUrl)
			return false;
		if (generalItem instanceof NarratorItem)
			return true;
		if (generalItem instanceof GeneralItem)
			return true;
		if (generalItem instanceof OpenBadge)
			return true;
		if (generalItem instanceof ScanTag)
			return true;
		return false;
	}

}
