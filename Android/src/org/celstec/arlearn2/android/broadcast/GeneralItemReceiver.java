package org.celstec.arlearn2.android.broadcast;

import java.util.Iterator;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.broadcast.task.CreateNewGItemTask;
import org.celstec.arlearn2.android.broadcast.task.DeleteItemTask;
import org.celstec.arlearn2.android.broadcast.task.MakeGeneralItemVisibleTask;
import org.celstec.arlearn2.android.broadcast.task.SynchronizeGeneralItemsTask;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.GeneralItemVisibility;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.service.GeneralItemDependencyHandler;
import org.celstec.arlearn2.android.sync.MediaCacheSyncroniser;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.OpenUrl;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.client.GeneralItemClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

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
			SynchronizeGeneralItemsTask task = new SynchronizeGeneralItemsTask();
			task.ctx = context;
			Message m = Message.obtain(NetworkQueue.getNetworkTaskHandler());
			m.obj = task;
			m.sendToTarget();
		}
	}

	private void processItem(Context ctx, GeneralItemModification bean) {
		bean.getGeneralItem().setRunId(bean.getRunId());
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
		return false;
	}

}
