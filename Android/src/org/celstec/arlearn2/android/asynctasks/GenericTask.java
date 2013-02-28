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
package org.celstec.arlearn2.android.asynctasks;

import java.util.ArrayList;

import android.content.Context;


public abstract class GenericTask {

	ArrayList<GenericTask> tasksToRunAfterExecute;
	
	protected abstract void run(Context ctx);
	
	public void taskToRunAfterExecute(GenericTask gt) {
		if (tasksToRunAfterExecute == null) {
			tasksToRunAfterExecute = new ArrayList<GenericTask>();
		}
		tasksToRunAfterExecute.add(gt);
	}
	
	protected void runAfterTasks(Context ctx) {
		if (tasksToRunAfterExecute == null) return;
		for (GenericTask gt: tasksToRunAfterExecute) {
			gt.run(ctx);
		}
	}

}
