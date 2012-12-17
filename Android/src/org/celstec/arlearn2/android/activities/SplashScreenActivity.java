package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.asynctasks.RestInvocation;
import org.celstec.arlearn2.android.broadcast.ActionReceiver;
import org.celstec.arlearn2.android.broadcast.RunReceiver;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.android.service.ChannelAPINotificationService;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.client.RunClient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashScreenActivity extends GeneralActivity {

	private boolean clicked = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		getPropertiesAdapter().setStatus(ChannelAPINotificationService.OFFLINE_STATUS);
		String uri = getIntent().toURI();
		if (uri.contains("#"))
			uri = uri.substring(0, uri.indexOf("#"));
		if (uri.startsWith("http://arlearn")) {
			Intent actionIntent = new Intent();
			actionIntent.setAction(ActionReceiver.action);
			actionIntent.putExtra("action", uri.substring(uri.lastIndexOf("/") + 1));
			sendBroadcast(actionIntent);
			// this.finish();
		}
		ImageView view = (ImageView) findViewById(R.id.logo);
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				userClickedLogo();
			}
		});
		SplashCounter task = new SplashCounter();
		task.execute(new Object[] {});
	}

	@Override
	protected void onResume() {
		super.onResume();
		clicked = false;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		if (menuHandler.getPropertiesAdapter().isAuthenticated()) {
			menu.add(0, MenuHandler.LOGOUT, 0, getString(R.string.logout));
			menu.add(0, MenuHandler.GAME_AUTHOR, 0, getString(R.string.creategame));
		} else {
			menu.add(0, MenuHandler.LOGIN, 0, getString(R.string.login));
		}
		menu.add(0, MenuHandler.EXIT, 0, getString(R.string.exit));
		return true;
	}

	private void userClickedLogo() {
		if (!clicked) {
			clicked = true;
			if (menuHandler.getPropertiesAdapter().isAuthenticated()) {
//				Intent runSyncIntent = new Intent();
//				runSyncIntent.setAction(RunReceiver.action);
//				sendBroadcast(runSyncIntent);

				startActivity(new Intent(SplashScreenActivity.this, ListExcursionsActivity.class));
//				Intent intent = new Intent(this, BackgroundService.class);
//				startService(intent);
			} else {
				startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));

			}
		}
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
			userClickedLogo();
		}

	}

	@Override
	protected void newNfcAction(String action) {
		// do nothing when tag is scanned in splashscreen

	}

}
