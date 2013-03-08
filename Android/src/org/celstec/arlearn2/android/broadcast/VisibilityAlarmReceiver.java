package org.celstec.arlearn2.android.broadcast;

import org.celstec.arlearn2.android.asynctasks.db.MakeGiAutomaticallyAppearAndPlaySoundTask;
import org.celstec.arlearn2.android.db.PropertiesAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class VisibilityAlarmReceiver extends GenericReceiver {

	public final static String ITEM_ID = "itemId";
	public final static String RUN_ID = "runId";
	public final static String APPEAR_AT = "appearAt";
	public final static String DISAPPEAR_AT = "disappearAt";
	public final static String STATUS = "status";
	
	public final static String ACTION = "org.celstec.arlearn.visibility";

	
	@Override
	public void onReceive(Context ctx, Intent intent) {
		Bundle bundle = intent.getExtras();
		
	    Long itemId = bundle.getLong(ITEM_ID);
	    Long runId = bundle.getLong(RUN_ID);
	    Long appearAt = bundle.getLong(APPEAR_AT);
	    Long disappearAt = bundle.getLong(DISAPPEAR_AT);
	    int status = bundle.getInt(STATUS, 1);
	    //check currentRunId
	    System.out.println("time to call alarm clock"+itemId+" "+runId+ " appear "+appearAt+ "dis "+disappearAt );
		Log.i("VISIBLE", "receive alarm"+itemId+" "+runId+ " appear "+appearAt+ "dis "+disappearAt );
		if (PropertiesAdapter.getInstance(ctx).getCurrentRunId() == runId.longValue()) {
			new MakeGiAutomaticallyAppearAndPlaySoundTask(itemId, status).run(ctx);
		}

	}

}
