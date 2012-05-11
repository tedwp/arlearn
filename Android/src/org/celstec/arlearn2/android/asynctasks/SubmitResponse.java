package org.celstec.arlearn2.android.asynctasks;

import org.celstec.arlearn2.android.activities.IGeneralActivity;
import org.celstec.arlearn2.android.db.PropertiesAdapter;

public class SubmitResponse extends RestInvocation {

	public SubmitResponse(IGeneralActivity activity) {
		super(activity);
	}
	
	protected Void doInBackground(Object... params) {
		String itemId =(String) params[0];
		String json =(String) params[1];
		PropertiesAdapter pa = callingActivity.getMenuHandler().getPropertiesAdapter();
		//TODO implement postresponse
//		try {
//			
//			String result = HttpUtils.postAsJSON(HttpUtils.serviceUrl + "response/runId/"+ pa.getCurrentRunId()+"/generalItemId/"+itemId , pa.getFusionAuthToken(), json, "application/json");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		return null;
	}
	
	

}
