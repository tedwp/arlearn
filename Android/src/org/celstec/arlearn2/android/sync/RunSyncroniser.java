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
package org.celstec.arlearn2.android.sync;

import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.RunAdapter;

import org.celstec.arlearn2.client.RunClient;
import org.celstec.arlearn2.client.UserClient;

import android.content.Context;
import android.util.Log;

public class RunSyncroniser extends GenericSyncroniser {

	public RunSyncroniser(Context ctx) {
		super(ctx);
	}

	public void runAuthenticated() {
//		syncronizeRuns(ctx, pa);
//		syncAdditionalInfo(ctx, pa);
//		increaseDelay();
	}

//	@Deprecated
//	public static RunList syncronizeRuns(Context ctx, PropertiesAdapter pa) {
//		String result;
//		try {
//			RunClient rc = RunClient.getRunClient();
//			RunList rl = rc.getRunsParticipate(pa.getFusionAuthToken());
//			DBAdapter db = new DBAdapter(ctx);
//			db.openForWrite();
//			if (rl.getError() == null) ((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).insert(rl.getRuns());
//			db.close();
//			return rl;
//		} catch (Exception e) {
//			Log.e("exception", e.getMessage(), e);
//		}
//		return null;
//		
//	}
	
//	public static void syncAdditionalInfo(Context ctx, PropertiesAdapter pa) {
//		String result;
//		try {
//			DBAdapter db = new DBAdapter(ctx);
//			db.openForWrite();
//			Run[] runs = db.getRunAdapter().runsWithoutGame();
//			for (Run r: runs) {
//				User u = UserClient.getUserClient().getUser(pa.getFusionAuthToken(), r.getRunId(), pa.getUsername());
//
//				Run fullRun = RunClient.getRunClient().getRun(r.getRunId(), pa.getFusionAuthToken());
//				if (fullRun.getGame() != null) {
//					db.getGameAdapter().insert(fullRun.getGame());
//					db.getRunAdapter().insert(fullRun, u.getRoles());
//				}
//				System.out.println("run found "+fullRun);
//			}
////			db.getGameAdapter();
//			//if (rl.getError() == null) ((RunAdapter)db.table(DBAdapter.RUN_ADAPTER)).insert(rl.getRuns());
//			db.close();
//		} catch (Exception e) {
//			Log.e("exception", e.getMessage(), e);
//		}
//	}
}
