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

import org.celstec.arlearn2.android.db.PropertiesAdapter;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.*;
import android.os.Parcelable;

@TargetApi(10)
public class NfcReceiver {
	private GeneralActivity activity;

	private NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	
	@TargetApi(10)
	public NfcReceiver(GeneralActivity context) {
		this.activity = context;
		mAdapter = NfcAdapter.getDefaultAdapter(activity);
		mPendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		mFilters = new IntentFilter[] { ndef };
		// Setup a tech list for all NfcF tags
		mTechLists = new String[][] { new String[] { 
				NfcA.class.getName()
				}, new String[] { 
				NfcV.class.getName()
				} };
	}
	
	@TargetApi(10)
	protected void onPause() {
		if (mAdapter != null)
			mAdapter.disableForegroundDispatch(activity);
	}
	
	@TargetApi(10)
	protected void onResume() {
		if (mAdapter != null)
			mAdapter.enableForegroundDispatch(activity, mPendingIntent, mFilters, mTechLists);
	}
	
	public void onNewIntent(Intent intent, PropertiesAdapter pa) {
		Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		if (rawMsgs != null) {
			NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
			for (int i = 0; i < rawMsgs.length; i++) {
				msgs[i] = (NdefMessage) rawMsgs[i];
				String ttagUrl = new String(msgs[i].getRecords()[0].getPayload());
				if (ttagUrl.contains("ttag.be")) {
					ttagUrl = ttagUrl.substring(ttagUrl.indexOf("tag.be") + 9);
					if (pa.getCurrentRunId() != -1 && pa.getUsername() != null)
						activity.newNfcAction(ttagUrl);
				}
			}
		} else if (intent.getExtras()!= null) {
			byte [] idBytes = intent.getExtras().getByteArray("android.nfc.extra.ID");
			if (idBytes != null) {
				String s = "";
				for (byte b : idBytes) {
					s+= (((int)(b))+128);
				}
				activity.newNfcAction(s);
			}
		}
		
	}
	//{android.nfc.extra.ID=[B@40d80eb0, android.nfc.extra.TAG=TAG: Tech [android.nfc.tech.NfcV, android.nfc.tech.NdefFormatable], android.nfc.extra.NDEF_MESSAGES=null}
	
}
