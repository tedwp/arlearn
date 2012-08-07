package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.db.PropertiesAdapter;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
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
		mTechLists = new String[][] { new String[] { NfcA.class.getName() } };
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
		}
	}
	
}
