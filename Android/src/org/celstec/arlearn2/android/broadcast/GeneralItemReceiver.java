/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.android.broadcast;


import org.celstec.arlearn2.android.cache.RunCache;
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

@SuppressLint("ParserError")
public class GeneralItemReceiver extends BroadcastReceiver {

	public static String action = "org.celstec.arlearn2.beans.notification.GeneralItemModification";

	@Override
	public void onReceive(final Context context, final Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			GeneralItemModification bean = (GeneralItemModification) extras.getSerializable("bean");
			if (bean != null) {
//				processItem(context, bean);
			}
		} else {
			Long runId = (new PropertiesAdapter(context)).getCurrentRunId();
			if (runId != null) {
//				(new SynchronizeGeneralItemsTaskOld(context, runId)).addTaskToQueue(context);
				Long gameId = RunCache.getInstance().getGameId(runId);
//				if (gameId != null)
////					(new SynchronizeGeneralItemsTaskOld(gameId, context)).addTaskToQueue(context);
			}

		}
	}

//	private void processItem(Context ctx, GeneralItemModification bean) {
//		if (!canDealWithitemType(bean.getGeneralItem()))
//			return;
//
//		DBAdapter.DatabaseTask task = null;
//		switch (bean.getModificationType()) {
//		case GeneralItemModification.CREATED:
//			task = new CreateNewGItemTask();
//			((CreateNewGItemTask) task).setAuthToken(PropertiesAdapter.getInstance(ctx).getFusionAuthToken());
//			((CreateNewGItemTask) task).setGeneralItemId(bean.getGeneralItem().getId());
//			((CreateNewGItemTask) task).setRunId(bean.getRunId());
//			break;
//		case GeneralItemModification.DELETED:
//			task = new DeleteItemTask();
//			((DeleteItemTask) task).setGeneralItemId(bean.getGeneralItem().getId());
//			break;
//		case GeneralItemModification.VISIBLE:
//			task = new MakeGeneralItemVisibleTask();
////			((MakeGeneralItemVisibleTask) task).setRunId(bean.getRunId());
//			((MakeGeneralItemVisibleTask) task).setGi(bean.getGeneralItem());
//
//			break;
//		case GeneralItemModification.DISAPPEARED:
//			task = new MakeGeneralItemDisappearedTask();
//			((MakeGeneralItemDisappearedTask) task).setRunId(bean.getRunId());
//			((MakeGeneralItemDisappearedTask) task).setGi(bean.getGeneralItem());
//			break;
//		default:
//			break;
//		}
//		if (task != null) {
//			Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
//			m.obj = task;
//			m.sendToTarget();
//		}
//	}

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
