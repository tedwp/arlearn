package org.celstec.arlearn2.android.activities;


import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.asynctasks.AuthenticationTask;
import org.celstec.arlearn2.android.db.DBAdapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends GeneralActivity {
		ProgressDialog dialog;
//		int resultsCounter = 0;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.loginscreen);

			Button loginButton = (Button) findViewById(R.id.loginButton);
			loginButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					userClickedLoginButton();
					
				}
			});
		}

		
		public void notifyTaskFinished() {
//			resultsCounter++;
//			if (resultsCounter == 3) {
				if (dialog.isShowing()) dialog.dismiss();
				if (menuHandler.getPropertiesAdapter().isAuthenticated()) {
//					startActivity(new Intent(this, ListFusionMapsActivity.class));
					startActivity(new Intent(this, SplashScreenActivity.class));
				} else {
					startActivity(new Intent(this, SplashScreenActivity.class));
				}
				finish();
//
//			}
		}
		
		private void userClickedLoginButton(){
			dialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.checkCredentials), true);
			dialog.show();
			String username = ((EditText) findViewById(R.id.email)) .getText()+"";
			String password = ((EditText) findViewById(R.id.password)).getText()+"";
			Boolean sendInstructions = ((CheckBox) findViewById(R.id.sendInstructions)).isChecked();
			storeUsername(username);
			menuHandler.getPropertiesAdapter().setPassword(password);
			new AuthenticationTask(LoginActivity.this).execute(new Object[]{username, password, Constants.FUSION_SERVICE, sendInstructions});
			//new AuthenticationTask(LoginActivity.this).execute(new Object[]{username, password, Constants.MAPS_SERVICE, menuHandler.getDefaultPrefs()});
			////	new AuthenticationTask(LoginActivity.this).execute(new Object[]{username, password, Constants.PICASA_SERVICE, menuHandler.getDefaultPrefs()});
			//	new AuthenticationTask(LoginActivity.this).execute(new Object[]{username, password, Constants.SITES_SERVICE, menuHandler.getDefaultPrefs()});
		}
		
		private void storeUsername(String username) {
			String oldAddress = normalizeEmail(menuHandler.getPropertiesAdapter().getUsername());
			String newAddress = normalizeEmail(username);
			if (oldAddress!= null && !oldAddress.equals(newAddress)) {
				DBAdapter db = new DBAdapter(menuHandler.getContext());
				db.openForWrite();
				db.eraseAllData();
				db.close();
			}
			menuHandler.getPropertiesAdapter().setUsername(newAddress);
		}
		
		//TODO merge this method with the one in appengine/jdo/Userloggedinmanager
		private String normalizeEmail(String mail) {
			if (mail == null) return null;
			int posAt = mail.indexOf("@");
			if (posAt != -1) {
				mail = mail.substring(0, posAt);
			}
			return mail.replace(".", "").toLowerCase();
		}
		
		
		public boolean isGenItemActivity() {
			return false;
		}
}
