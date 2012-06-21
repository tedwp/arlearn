package org.celstec.arlearn2.android.broadcast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.RunAdapter;
import org.celstec.arlearn2.android.service.GeneralItemDependencyHandler;
import org.celstec.arlearn2.android.sync.MediaCacheSyncroniser;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.client.GeneralItemClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class GeneralItemReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			GeneralItemModification bean = (GeneralItemModification) extras.getSerializable("bean");
			if (bean != null) {
				processItem(context, bean);
			}
		} else {
			syncronizeGeneralItems(context);
		}
		(new GeneralItemDependencyHandler(context)).checkDependencies();
		updateActivities(context);
	}

	private void updateActivities(Context context) {
		Intent updateIntent = new Intent();
		updateIntent.setAction("org.celstec.arlearn.updateActivities");
		updateIntent.putExtra(ListMessagesActivity.class.getCanonicalName(), true);
		updateIntent.putExtra(MapViewActivity.class.getCanonicalName(), true);
		updateIntent.putExtra(ListMapItemsActivity.class.getCanonicalName(), true);
		context.sendBroadcast(updateIntent);
	}

	private void processItem(Context ctx, GeneralItemModification bean) {
		bean.getGeneralItem().setRunId(bean.getRunId());
		DBAdapter db = new DBAdapter(ctx);
		try {
			db.openForWrite();
			switch (bean.getModificationType()) {
			case GeneralItemModification.CREATED:
				GeneralItem generalItem = GeneralItemClient.getGeneralItemClient().getRunGeneralItem(PropertiesAdapter.getInstance(ctx).getFusionAuthToken(), bean.getRunId(),
						bean.getGeneralItem().getId());
				generalItem.setRunId(bean.getRunId());
				generalItemToDb(db, generalItem);
				break;
			case GeneralItemModification.DELETED:
				((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).delete(bean.getGeneralItem().getId(), bean.getRunId());

			case GeneralItemModification.VISIBLE:
				((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).setVisiblityStatus(bean.getRunId(), bean.getGeneralItem().getId(), GeneralItemAdapter.VISIBLE, System.currentTimeMillis());

				break;
			// case RunModification.ALTERED:
			// ((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).delete(rm.getRun().getRunId());
			// ((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).insert(rm.getRun());
			// break;
			default:
				break;
			}
		} finally {
			db.close();
		}
	}

	private void syncronizeGeneralItems(Context context) {
		PropertiesAdapter pa = PropertiesAdapter.getInstance(context);
		long currentRunId = pa.getCurrentRunId();
		if (currentRunId <= 0)
			return;
		DBAdapter db = new DBAdapter(context);
		db.openForWrite();
		try {
			GeneralItemList gl = GeneralItemClient.getGeneralItemClient().getRunGeneralItems(pa.getFusionAuthToken(), currentRunId);

			if (gl.getErrorCode() != null && gl.getErrorCode() == GeneralItemList.RUNNOTFOUND) {
				notifyRunDeleted(context, currentRunId);
			}

			Iterator<GeneralItem> it = gl.getGeneralItems().iterator();
			while (it.hasNext()) {
				GeneralItem item = it.next();
				item.setRunId(currentRunId);
				generalItemToDb(db, item);
			}
		} catch (ARLearnException ae) {
			ae.printStackTrace();
		} finally {
			db.close();
		}
	}

	private void generalItemToDb(DBAdapter db, GeneralItem item) {
		boolean newInsert = db.table(DBAdapter.GENERALITEM_ADAPTER).insert(item);
		if (newInsert) {
			if (item instanceof AudioObject) {
				AudioObject aItem = (AudioObject) item;
				((MediaCache) db.table(DBAdapter.MEDIA_CACHE)).addIncommingObject(aItem.getId(), aItem.getAudioFeed(), aItem.getRunId());
				MediaCacheSyncroniser.getInstance().resetDelay();
			}
			if (item instanceof VideoObject) {
				VideoObject vItem = (VideoObject) item;
				((MediaCache) db.table(DBAdapter.MEDIA_CACHE)).addIncommingObject(vItem.getId(), vItem.getVideoFeed(), vItem.getRunId());
				MediaCacheSyncroniser.getInstance().resetDelay();
			}
		}
	}

	private void notifyRunDeleted(Context context, long runId) {
		RunModification rm = new RunModification();
		rm.setModificationType(RunModification.DELETED);
		rm.setRun(new Run());
		rm.getRun().setRunId(runId);
		Intent runIntent = new Intent();
		runIntent.setAction("org.celstec.arlearn2.beans.notification.RunModification");
		runIntent.putExtra("bean", rm);
		context.sendBroadcast(runIntent);
	}

}
