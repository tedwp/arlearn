package org.celstec.arlearn2.android.broadcast;

import java.util.Iterator;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
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

@SuppressLint("ParserError")
public class GeneralItemReceiver extends BroadcastReceiver {

	public static String action = "org.celstec.arlearn2.beans.notification.GeneralItemModification";

	@Override
	public void onReceive(final Context context, final Intent intent) {
		new Thread(new Runnable() {
			public void run() {
				Bundle extras = intent.getExtras();
				if (extras != null) {
					GeneralItemModification bean = (GeneralItemModification) extras.getSerializable("bean");
					if (bean != null) {
						processItem(context, bean);
						(new GeneralItemDependencyHandler(context)).checkDependencies();

					} 
					long runId = extras.getLong("runId", -1l);
					if (runId != -1) {
						syncronizeGeneralItems(context, runId);
						(new GeneralItemDependencyHandler(context)).checkDependencies(runId);


					}
					
				} else {
					syncronizeGeneralItems(context);
					(new GeneralItemDependencyHandler(context)).checkDependencies();

				}
				updateActivities(context);
			}
		}).start();
	}

	private void updateActivities(Context context) {
		Intent updateIntent = new Intent();
		updateIntent.setAction("org.celstec.arlearn.updateActivities");
		updateIntent.putExtra(ListMessagesActivity.class.getCanonicalName(), true);
		updateIntent.putExtra(MapViewActivity.class.getCanonicalName(), true);
		updateIntent.putExtra(ListMapItemsActivity.class.getCanonicalName(), true);
		updateIntent.putExtra(NarratorItemActivity.class.getCanonicalName(), true);
		context.sendBroadcast(updateIntent);
	}

	private void processItem(Context ctx, GeneralItemModification bean) {
		bean.getGeneralItem().setRunId(bean.getRunId());
		if (!canDealWithitemType(bean.getGeneralItem()))
			return;
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
				break;
			case GeneralItemModification.VISIBLE:
				if (GeneralItemDependencyHandler.itemMatchesPlayersRole(db, bean.getRunId(), bean.getGeneralItem())) {
					if (((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).getVisiblityStatus(bean.getRunId(), bean.getGeneralItem().getId()) == GeneralItemAdapter.VISIBLE)
						break;
					((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).setVisiblityStatus(bean.getRunId(), bean.getGeneralItem().getId(), GeneralItemAdapter.VISIBLE,
							System.currentTimeMillis());
					(new GeneralItemDependencyHandler(ctx)).broadcastTroughIntent(bean.getGeneralItem()); // now
																											// this
																											// broadcast
																											// happens
																											// twice
				}
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
	private void syncronizeGeneralItems(final Context context) {
		PropertiesAdapter pa = PropertiesAdapter.getInstance(context);
		long currentRunId = pa.getCurrentRunId();
		if (currentRunId <= 0)
			return;
		syncronizeGeneralItems(context, currentRunId);
	}
	private void syncronizeGeneralItems(final Context context, long runId) {
		PropertiesAdapter pa = PropertiesAdapter.getInstance(context);
		DBAdapter db = new DBAdapter(context);
		db.openForWrite();
		try {
			GeneralItemList gl = GeneralItemClient.getGeneralItemClient().getRunGeneralItems(pa.getFusionAuthToken(), runId);

			if (gl.getErrorCode() != null && gl.getErrorCode() == GeneralItemList.RUNNOTFOUND) {
				notifyRunDeleted(context, runId);
			}

			Iterator<GeneralItem> it = gl.getGeneralItems().iterator();
			while (it.hasNext()) {
				GeneralItem item = it.next();
				item.setRunId(runId);
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
		runIntent.setAction(RunReceiver.action);
		runIntent.putExtra("bean", rm);
		context.sendBroadcast(runIntent);
	}

}
