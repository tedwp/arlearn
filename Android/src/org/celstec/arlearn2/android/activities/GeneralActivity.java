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
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.android.menu.ActionDispatcher;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.android.service.ChannelAPINotificationService;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Run;

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
import android.view.View;

public abstract class GeneralActivity extends Activity implements IGeneralActivity, ARLearnBroadcastReceiver {

	protected MenuHandler menuHandler;
	private PropertiesAdapter pa;
	

	private GenericBroadcastReceiver broadcastReceiver;
	private NfcReceiver nfcReceiver;
	
	private long runId;
	private long gameId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			unpackBundle(savedInstanceState);
		} else {
			if (getIntent().getExtras() != null) {
				unpackBundle(getIntent().getExtras());
			}
		}
		menuHandler = new MenuHandler(this);
		pa = new PropertiesAdapter(this);

		broadcastReceiver = new GenericBroadcastReceiver(this);
		if (Integer.parseInt(Build.VERSION.SDK) >= Build.VERSION_CODES.GINGERBREAD_MR1) nfcReceiver = new NfcReceiver(this);
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
//		if (pa.isAuthenticated()) ChannelAPINotificationService.startService(this);
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
	
	@Override
	protected void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState(outState);
		if (isGenItemActivity()) {
			outState.putSerializable(GeneralItem.class.getCanonicalName(), getGeneralItem());
			outState.putLong(Run.class.getCanonicalName(), getRunId());
			outState.putLong(Game.class.getCanonicalName(), getGameId());
		}
		
	}
	
	protected void unpackBundle(Bundle inState) {
		if (isGenItemActivity()) {
			setGeneralItem((GeneralItem) inState.getSerializable(GeneralItem.class.getCanonicalName()));
			setRunId(inState.getLong(Run.class.getCanonicalName()));
			setGameId(inState.getLong(Game.class.getCanonicalName()));
		}
	}
	
	public GeneralItem getGeneralItem() {return null;};
	
	public void setGeneralItem(GeneralItem gi){};
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		if (scanMenu())
		if (menuHandler.getPropertiesAdapter().isAuthenticated()) {
			menu.add(0, MenuHandler.SCAN, 0, getString(R.string.scanTagMenu));
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
		ActionsDelegator.getInstance().publishAction(this, action, pa.getCurrentRunId(), pa.getFullId(), null, null);
	}

	public void onBroadcastMessage(Bundle bundle, boolean render) {
		if (showStatusLed()) LedStatus.updateStatus(this);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		return menuHandler.onOptionsItemSelected(item);
	}

	public long getRunId() {
		return runId;
	}

	public void setRunId(long runId) {
		this.runId = runId;
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
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

//	protected void cancelNotification(int id) {
//		String ns = Context.NOTIFICATION_SERVICE;
//		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
//		mNotificationManager.cancel(id);
//	}

	protected void fireReadAction(GeneralItem item) {
		PropertiesAdapter pa = getMenuHandler().getPropertiesAdapter();
		Long generalItemId = null;
		String generalItemType = null;
		if (item != null) {
			generalItemId = item.getId();
			generalItemType = item.getClass().getName();
		}
		ActionsDelegator.getInstance().publishAction(this, "read", pa.getCurrentRunId(), pa.getFullId(), generalItemId, generalItemType);
	}
	
	public boolean showStatusLed() {
		return false;
	}

}
