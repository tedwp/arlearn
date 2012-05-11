package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.menu.ActionDispatcher;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

public abstract class GeneralActivity extends Activity implements IGeneralActivity{

	
	protected MenuHandler menuHandler; 

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		menuHandler = new MenuHandler(this);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		return menuHandler.onOptionsItemSelected(item);
	}

	public void notifyTaskFinished() {
		// do nothing unless subclass overrides this...
	}

	public MenuHandler getMenuHandler() {
		return menuHandler;
	}
	
	protected void cancelNotification(int id) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		mNotificationManager.cancel(id);
	}
	
	protected void fireReadAction(GeneralItem item) {
		PropertiesAdapter pa = getMenuHandler().getPropertiesAdapter();
		Long generalItemId = null;
		String generalItemType = null;
		if (item != null) {
			generalItemId = item.getId();
			generalItemType = item.getClass().getName();
		}
		ActionDispatcher.publishAction(this, "read", pa.getCurrentRunId(), pa.getUsername(), generalItemId, generalItemType);

	}

}
