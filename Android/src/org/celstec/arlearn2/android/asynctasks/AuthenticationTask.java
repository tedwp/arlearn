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

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.IGeneralActivity;
import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.client.LoginClient;


import android.app.Activity;

public class AuthenticationTask extends RestInvocation {

	private String email;
	private String password;
	private String service;
	private Boolean instructions;

	public AuthenticationTask(IGeneralActivity activity) {
		super(activity);
	}

	private void readParameters(Object... params) {
		email = (String) params[0];
		password = (String) params[1];
		service = (String) params[2];
		instructions = (Boolean) params[3];
	}

	private boolean checkConstraints() {
		if (email == null || email.equals("")) {
			publishProgress(((Activity) callingActivity).getString(R.string.emailMissing));
			return false;
		}

		if (password == null || password.equals("")) {
			publishProgress(((Activity) callingActivity).getString(R.string.pwMissing));
			return false;
		}
		if (service == null || service.equals("")) {
			publishProgress(((Activity) callingActivity).getString(R.string.serviceMissing));
			return false;
		}
		return true;
	}

	@Override
	protected Void doInBackground(Object... params) {
		readParameters(params);
		if (!checkConstraints())
			return null;
		AuthResponse resp = LoginClient.getLoginClient().authenticate(email, password);
		if (resp.getAuth() == null) {
			callingActivity.getMenuHandler().getPropertiesAdapter().disAuthenticate();
			publishProgress(((Activity) callingActivity).getString(R.string.loginFailed).replace("***", email));
		} else {
			callingActivity.getMenuHandler().getPropertiesAdapter().setAuthToken(resp.getAuth());
			callingActivity.getMenuHandler().getPropertiesAdapter().setIsAuthenticated();
			if (instructions != null && instructions) LoginClient.getLoginClient().instructions(resp.getAuth(), email);
		}
//		doGoogleAuthenticate();
		return null;
	}

//	@Deprecated
//	private void doGoogleAuthenticate() {
//
////		try {
////			FusionClient fc = new FusionClient(email, password);
////			CSV c = fc.showTables();
////		} catch (Exception e1) {
////			Log.e("err", "" + e1.getMessage(), e1);
////		}
//		try {
//			String result = null;
//			if (service.equals(Constants.FUSION_SERVICE)) {
//				result = GoogleLogin.authenticateAppEngineProject(email, password);
//			} else {
//				result = GoogleLogin.authenticate(email, password, service);
//			}
//
//			if (result == null || result.contains("BadAuthentication")) {
//				callingActivity.getMenuHandler().getPropertiesAdapter().disAuthenticate();
//				if (service.equals(Constants.FUSION_SERVICE))
//					publishProgress(((Activity) callingActivity).getString(R.string.loginFailed).replace("***", email));
//			} else {
//				if (service.equals(Constants.FUSION_SERVICE)) {
//					callingActivity.getMenuHandler().getPropertiesAdapter().setFusionAuthToken(result);
//					callingActivity.getMenuHandler().getPropertiesAdapter().setIsAuthenticated();
//				}
//			}
//		} catch (IOException e) {
//			callingActivity.getMenuHandler().getPropertiesAdapter().disAuthenticate();
//			publishProgress(((Activity) callingActivity).getString(R.string.loginFailed).replace("***", email) + e.getMessage());
//		}
//	}
	
	protected void onPostExecute(Void result) {
		callingActivity.notifyTaskFinished();
		super.onPostExecute(result);
	}

}
