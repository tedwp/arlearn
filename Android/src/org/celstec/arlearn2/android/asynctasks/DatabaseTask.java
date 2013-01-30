package org.celstec.arlearn2.android.asynctasks;

import org.celstec.arlearn2.android.db.DBAdapter;

public interface DatabaseTask extends DBAdapter.DatabaseTask{
	
	public void execute(DBAdapter db);
	
}
