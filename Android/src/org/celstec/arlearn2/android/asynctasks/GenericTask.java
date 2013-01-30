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
		for (GenericTask gt: tasksToRunAfterExecute) {
			gt.run(ctx);
		}
	}

}
