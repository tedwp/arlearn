package org.celstec.arlearn2.android.asynctasks;

import org.celstec.arlearn2.android.activities.IGeneralActivity;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

//import org.celstec.arlearn2.android.db.beans.GeneralItem;

import android.content.Context;
@Deprecated
public class GetLocationLessMessages extends RestInvocation {

	GeneralItem[] gis =  null;
	
	@Deprecated
	public GetLocationLessMessages(IGeneralActivity activity) {
		super(activity);
	}

	protected Void doInBackground(Object... params) {
		Context ctx = (Context) callingActivity;
		DBAdapter db = new DBAdapter(ctx);
		db.openForRead();
		gis = (GeneralItem[]) ((GeneralItemAdapter)db.table(DBAdapter.GENERALITEM_ADAPTER)).queryMessages(callingActivity.getMenuHandler().getPropertiesAdapter().getCurrentRunId());
		db.close();
		return null;
	}
	
	protected void onPostExecute(Void result) {
		callingActivity.notifyTaskFinished();
		super.onPostExecute(result);
	}
	
	public GeneralItem[] getResults() {
		return gis;
	}
}
