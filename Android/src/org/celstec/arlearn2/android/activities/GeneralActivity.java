package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.menu.ActionDispatcher;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.android.service.ChannelAPINotificationService;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public abstract class GeneralActivity extends Activity implements IGeneralActivity, ARLearnBroadcastReceiver {

	protected MenuHandler menuHandler;
	private PropertiesAdapter pa;
	

	private GenericBroadcastReceiver broadcastReceiver;
	private NfcReceiver nfcReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		menuHandler = new MenuHandler(this);
		pa = new PropertiesAdapter(this);

		broadcastReceiver = new GenericBroadcastReceiver(this);
		if (Integer.parseInt(Build.VERSION.SDK) >= Build.VERSION_CODES.GINGERBREAD_MR1) nfcReceiver = new NfcReceiver(this);
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (pa.isAuthenticated()) ChannelAPINotificationService.startService(this);
		if  (showStatusLed()) LedStatus.updateStatus(this); //TODO pass pa object
		if (broadcastReceiver != null)
			broadcastReceiver.onResume();
		if (nfcReceiver != null)
			nfcReceiver.onResume();
	}

	@Override
	public void onNewIntent(Intent intent) {
		if (nfcReceiver != null)
			nfcReceiver.onNewIntent(intent, pa);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (broadcastReceiver != null)
			broadcastReceiver.onPause();
		if (nfcReceiver != null)
			nfcReceiver.onPause();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		if (scanMenu())
		if (menuHandler.getPropertiesAdapter().isAuthenticated()) {
			menu.add(0, MenuHandler.SCAN, 0, "scan-todo");
		} else {
			menu.add(0, MenuHandler.LOGIN, 0, getString(R.string.login));
		}
		return true;
	}
	
	public boolean scanMenu() {
		return true;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanResult != null) {
			newNfcAction(scanResult.getContents());
		}
	}
	
	protected void newNfcAction(String action) {
		ActionDispatcher.publishAction(this, action, pa.getCurrentRunId(), pa.getUsername(), null, null);
	}

	public void onBroadcastMessage(Bundle bundle) {
		Log.e("BROADCAST", "broadcast "+bundle);

		if (showStatusLed()) LedStatus.updateStatus(this);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		return menuHandler.onOptionsItemSelected(item);
	}

	public void notifyTaskFinished() {
		// do nothing unless subclass overrides this...
	}

	public PropertiesAdapter getPropertiesAdapter(){
		return pa;
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
	
	public boolean showStatusLed() {
		return false;
	}

}
