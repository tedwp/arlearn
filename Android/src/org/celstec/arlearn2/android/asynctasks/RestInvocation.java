package org.celstec.arlearn2.android.asynctasks;

import org.celstec.arlearn2.android.activities.IGeneralActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

public abstract class RestInvocation extends AsyncTask<Object, String, Void>{

	protected IGeneralActivity callingActivity;
	
	public RestInvocation(IGeneralActivity activity){
		this.callingActivity = activity;
	}
	
	public RestInvocation(){
		
	}
	
	@Override
	protected void onPostExecute(Void result) {
		//callingActivity.notifyTaskFinished();
		super.onPostExecute(result);
	}
	
	/*
	 * This method is called implicitly through publishProgress (for instance in AuthenticationTask)
	 * 
	 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
	 */
	protected void onProgressUpdate(String... values) {
		if (callingActivity != null)
			Toast.makeText((Activity)callingActivity, values[0], Toast.LENGTH_LONG).show();
	}
}