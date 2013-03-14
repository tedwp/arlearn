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
package org.celstec.arlearn2.android.activities;

import java.net.ResponseCache;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.asynctasks.AuthenticationTask;
import org.celstec.arlearn2.android.cache.ActionCache;
import org.celstec.arlearn2.android.cache.GameCache;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.GenericCache;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.notification.Ping;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.client.ChannelClient;
import org.codehaus.jettison.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends GeneralActivity {
	ProgressDialog dialog;

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
		if (dialog.isShowing())
			dialog.dismiss();
		if (menuHandler.getPropertiesAdapter().isAuthenticated()) {
			startActivity(new Intent(this, SplashScreenActivity.class));
		} else {
			startActivity(new Intent(this, SplashScreenActivity.class));
		}
		finish();
	}

	@Override
	protected void onDestroy() {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
		super.onDestroy();
	}

	private void userClickedLoginButton() {
		dialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.checkCredentials), true);
		dialog.show();
		String username = ((EditText) findViewById(R.id.email)).getText() + "";
		String password = ((EditText) findViewById(R.id.password)).getText() + "";
		Boolean sendInstructions = ((CheckBox) findViewById(R.id.sendInstructions)).isChecked();
		PropertiesAdapter.getInstance(this).databaseReset();
		storeUsername(username);
		menuHandler.getPropertiesAdapter().setPassword(password);
		new AuthenticationTask(LoginActivity.this).execute(new Object[] { username, password, Constants.FUSION_SERVICE, sendInstructions });
	}

	private void storeUsername(String username) {
		String oldAddress = User.normalizeEmail(menuHandler.getPropertiesAdapter().getUsername());
		String newAddress = User.normalizeEmail(username);
		if (oldAddress != null && !oldAddress.equals(newAddress)) {
			Message m = Message.obtain(DBAdapter.getDatabaseThread(menuHandler.getContext()));
			m.obj = new DBAdapter.DatabaseTask() {
				@Override
				public void execute(DBAdapter db) {
					db.eraseAllData();		
					GenericCache.emptyAllCaches();
				}
			};
			m.sendToTarget();
		}
		menuHandler.getPropertiesAdapter().setUsername(newAddress);
	}

	public boolean isGenItemActivity() {
		return false;
	}
}
