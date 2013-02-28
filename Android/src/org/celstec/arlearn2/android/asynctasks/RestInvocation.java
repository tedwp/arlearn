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
