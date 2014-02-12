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

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.activities.ListRunsParticipateActivity;
import org.celstec.arlearn2.android.activities.OauthProvidersList;
import org.celstec.arlearn2.android.asynctasks.db.CleanUpFilesThatAreNotInDatabase;
import org.celstec.arlearn2.android.asynctasks.db.LoadRunsAndGamesToCache;
import org.celstec.arlearn2.android.broadcast.NetworkSwitcher;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.RunDelegator;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.android.service.ChannelAPINotificationService;
import org.celstec.arlearn2.beans.Info;
import org.celstec.arlearn2.beans.notification.GCMDeviceDescription;
import org.celstec.arlearn2.client.InfoClient;
import org.celstec.arlearn2.client.NotificationClient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class SplashScreenActivity extends GeneralActivity {

	private boolean clicked = false;
	private boolean blocked = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		getPropertiesAdapter().setStatus(ChannelAPINotificationService.OFFLINE_STATUS);

		LinearLayout view = (LinearLayout) findViewById(R.id.splashscreen);
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				userClickedLogo();
			}
		});
		SplashCounter task = new SplashCounter();
		task.execute(new Object[] {});

		RunDelegator.getInstance().initRunsAndGamesFromDb(this);

		CleanUpFilesThatAreNotInDatabase cleanUpTask = new CleanUpFilesThatAreNotInDatabase();
		cleanUpTask.run(this);

		new TimeCheck().execute();
//		setGCMConfiguration();
	}

	

	@Override
	protected void onResume() {
		super.onResume();
		if (blocked) {
			new TimeCheck().execute();
		}
		clicked = false;
		String fullName = PropertiesAdapter.getInstance(this).getFullName();
		String pictureUrl = PropertiesAdapter.getInstance(this).getPicture();
		LinearLayout accountLayout = (LinearLayout) findViewById(R.id.accountLayout);
		if (fullName == null) {
			
			accountLayout.setVisibility(View.GONE);
		} else {
			accountLayout.setVisibility(View.VISIBLE);
			if (fullName != null) {
				TextView tv = (TextView) findViewById(R.id.fullName);
				tv.setText(fullName);

			}
			if (pictureUrl != null) {
				ImageView iv = (ImageView) findViewById(R.id.accountPicture);
				Bitmap myBitmap = BitmapFactory.decodeFile(pictureUrl);
				iv.setImageBitmap(myBitmap);
			}
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		System.out.println("create options");
		if (menuHandler.getPropertiesAdapter().isAuthenticated()) {
			menu.add(0, MenuHandler.LOGOUT, 0, getString(R.string.logout));
			menu.add(0, MenuHandler.GAME_AUTHOR, 0, getString(R.string.creategame));
		} else {
			menu.add(0, MenuHandler.OAUTH_LOGIN, 0, "LOGIN");
			menu.add(0, MenuHandler.GAME_AUTHOR, 0, getString(R.string.creategame));
//			menu.add(0, MenuHandler.LOGIN, 0, getString(R.string.login) + " old");

		}
		menu.add(0, MenuHandler.EXIT, 0, getString(R.string.exit));
		return true;
	}

	private void userClickedLogo() {
//		if (blocked)
//			return;
//		if (!clicked) {
//			clicked = true;
//			if (menuHandler.getPropertiesAdapter().isAuthenticated()) {
//				startActivity(new Intent(SplashScreenActivity.this, ListRunsParticipateActivity.class));
//			} else {
//				// startActivity(new Intent(SplashScreenActivity.this,
//				// LoginActivity.class));
//				startActivity(new Intent(SplashScreenActivity.this, OauthProvidersList.class));
//
//			}
//		}
//		
		startActivity(new Intent(SplashScreenActivity.this, ListGamesActivity.class));
		
	}

	public boolean isGenItemActivity() {
		return false;
	}

	class SplashCounter extends AsyncTask<Object, String, Void> {

		protected Void doInBackground(Object... params) {
			synchronized (this) {
				try {
					wait(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			publishProgress();
			return null;
		}

		protected void onProgressUpdate(String... values) {
			//userClickedLogo();
		}

	}

	@Override
	protected void newNfcAction(String action) {
		// do nothing when tag is scanned in splashscreen

	}

	public class TimeCheck extends AsyncTask<Object, Long, Void> {

		@Override
		protected Void doInBackground(Object... arg0) {
			try {
				if (NetworkSwitcher.isOnline(SplashScreenActivity.this)) {
					Info info = InfoClient.getVersionClient().getInfo();
					Long phoneTime = System.currentTimeMillis();
					Long delta = phoneTime - info.getTimestamp();
					if (delta < 0)
						delta *= -1l;
					if (delta < 60000)
						delta = 0l;
					publishProgress(delta);
				} else {
					publishProgress();
				}
			} catch (Exception e) {
				publishProgress();

			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Long... delta) {
			if (delta.length == 0) {
				Toast.makeText(SplashScreenActivity.this, getString(R.string.networkUnavailable2), Toast.LENGTH_LONG).show();
			} else {
				Long time = delta[0];
				if (time != 0) {
					blocked = true;
					Toast.makeText(SplashScreenActivity.this, getString(R.string.correctDateSettings) + " " + time, Toast.LENGTH_LONG).show();
				}
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}
	
	
	
}
