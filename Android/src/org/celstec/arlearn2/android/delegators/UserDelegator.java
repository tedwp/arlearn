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
package org.celstec.arlearn2.android.delegators;

import org.celstec.arlearn2.android.asynctasks.network.SynchronizeUserTask;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.beans.run.User;

import android.content.Context;
import android.os.Message;

public class UserDelegator {

	private static UserDelegator instance;

	private UserDelegator() {

	}

	public static UserDelegator getInstance() {
		if (instance == null) {
			instance = new UserDelegator();
		}
		return instance;
	}

	public void synchronizeUserWithServer(Context ctx, Long runId, String username) {
		(new SynchronizeUserTask(ctx, runId, username)).addTaskToQueue(ctx);
	}

	public void saveUser(Context ctx, final User u) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = new DBAdapter.DatabaseTask() {

			@Override
			public void execute(DBAdapter db) {
				db.getRunAdapter().updateUserRole(u);
			}
		};
		m.sendToTarget();
	}

}
