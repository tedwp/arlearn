package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.android.service.BackgroundService;
import org.celstec.arlearn2.android.service.NotificationService;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SplashScreenActivity extends GeneralActivity {
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		ImageView view = (ImageView) findViewById(R.id.logo);
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				userClickedLogo();
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		if (menuHandler.getPropertiesAdapter().isAuthenticated()) {
			menu.add(0, MenuHandler.LOGOUT, 0, getString(R.string.logout));
		} else {
			menu.add(0, MenuHandler.LOGIN, 0, getString(R.string.login));
		}
		menu.add(0, MenuHandler.EXIT, 0, getString(R.string.exit));
		return true;
	}

	private void userClickedLogo() {
		if (menuHandler.getPropertiesAdapter().isAuthenticated()) {
			startActivity(new Intent(SplashScreenActivity.this, ListExcursionsActivity.class));
			Intent intent = new Intent(this, BackgroundService.class);
			startService(intent);
			intent = new Intent(this, NotificationService.class);
			startService(intent);
		} else {
			startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));

		}
	}
	
	
	public boolean isGenItemActivity() {
		return false;
	}

}
