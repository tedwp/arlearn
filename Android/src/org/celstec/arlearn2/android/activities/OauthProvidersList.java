package org.celstec.arlearn2.android.activities;

import java.util.ArrayList;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.ListOpenRunsActivity.QueryRuns;
import org.celstec.arlearn2.android.activities.ListOpenRunsActivity.SelfRegisterRun;
import org.celstec.arlearn2.android.broadcast.NetworkSwitcher;
import org.celstec.arlearn2.android.cache.GenericCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.GenericMessageListAdapter;
import org.celstec.arlearn2.android.list.ListitemClickInterface;
import org.celstec.arlearn2.android.oauth.AnonymousTokenAuthenticate;
import org.celstec.arlearn2.android.oauth.ListRecord;
import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.oauth.OauthInfo;
import org.celstec.arlearn2.beans.oauth.OauthInfoList;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.client.AccountClient;
import org.celstec.arlearn2.client.LoginClient;
import org.celstec.arlearn2.client.OauthClient;
import org.celstec.arlearn2.client.RunClient;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class OauthProvidersList extends GeneralActivity implements ListitemClickInterface {

	private OauthInfoList infoList;
	private ArrayList<GenericListRecord> runsRecordList;
	private GenericMessageListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Authenticate"); //TODO Localize this
		setContentView(R.layout.listexcursionscreen);
	}

	@Override
	protected void onResume() {
		super.onResume();
		new FetchOauthConfig().execute();
	}

	private void renderOauthProviderList() {
		runsRecordList = new ArrayList<GenericListRecord>();
		OauthInfo qrAuth = new OauthInfo();
		qrAuth.setProviderId(0);
		runsRecordList.add(new ListRecord(qrAuth));
		for (final OauthInfo info : infoList.getOauthInfoList()) {
			runsRecordList.add(new ListRecord(info));
		}
		ListView listView = (ListView) findViewById(R.id.listRuns);
		if (adapter == null || !adapter.isEqual(runsRecordList)) {
			adapter = new GenericMessageListAdapter(this, R.layout.listexcursionscreen, runsRecordList);
            adapter.setLineResourceId(R.layout.list_oauth_provider_line);
			adapter.setOnListItemClickCallback(this);
			listView.setAdapter(adapter);
		}

	}

	public class FetchOauthConfig extends AsyncTask<Object, OauthInfoList, Void> {

		@Override
		protected Void doInBackground(Object... arg0) {
			try {
				if (NetworkSwitcher.isOnline(OauthProvidersList.this)) {
					OauthInfoList list = OauthClient.getOauthClient().getOauthInfo();
					publishProgress(list);
				} else {
					publishProgress();
				}
			} catch (Exception e) {
				publishProgress();

			}
			return null;
		}

		@Override
		protected void onProgressUpdate(OauthInfoList... oauthList) {
			if (oauthList.length == 0) {
				Toast.makeText(OauthProvidersList.this, getString(R.string.networkUnavailable), Toast.LENGTH_LONG).show();
			} else {
				infoList = oauthList[0];
				renderOauthProviderList();
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}

	@Override
	public boolean isGenItemActivity() {
		return false;
	}

	protected void newNfcAction(final String action) {
		new AnonymousTokenAuthenticate(this).execute(action);
		this.finish();
	}

	@Override
	public void onListItemClick(View v, int position, GenericListRecord messageListRecord) {
		OauthInfo oi = null;
		Intent intent2 = null;
		if (position > 0) {
			oi = infoList.getOauthInfoList().get(position-1);
			intent2 = new Intent(OauthProvidersList.this, OauthActivity.class);
		}
		switch (position) {
		case 0:
			IntentIntegrator integrator = new IntentIntegrator(this);
			integrator.initiateScan();
			return;
		case 1:
			intent2.setData(Uri.parse(getFbRedirectURL(oi.getRedirectUri(), oi.getClientId())));
			finish();
			break;
		case 2:
			intent2.setData(Uri.parse(getGoogleLoginRedirectURL(oi.getRedirectUri(), oi.getClientId())));
			finish();
			break;
		case 3:
			intent2.setData(Uri.parse(getLinkedInLoginRedirectURL(oi.getRedirectUri(), oi.getClientId())));
			finish();
			break;
		default:
			break;
		}
        
        startActivity(intent2);
	}

	@Override
	public boolean setOnLongClickListener(View v, int position, GenericListRecord messageListRecord) {
		return false;
	}
	
	public String getFbRedirectURL(String redirect_uri, String client_id)  {
		return "https://graph.facebook.com/oauth/authorize?client_id=" + client_id + "&display=page&redirect_uri=" + redirect_uri + "&scope=publish_stream,email";

	}
	public String getGoogleLoginRedirectURL(String redirect_uri, String client_id_google) {
		return "https://accounts.google.com/o/oauth2/auth?redirect_uri=" + redirect_uri + "&response_type=code&client_id=" + client_id_google + "&approval_prompt=force&scope=profile+email";
	}
	public String getLinkedInLoginRedirectURL(String redirect_uri, String client_id) {
		return "https://www.linkedin.com/uas/oauth2/authorization?response_type=code&" +
				"client_id="+client_id+
				"&scope=r_fullprofile%20r_emailaddress%20r_network" +
				"&state=BdhOU9fFb6JcK5BmoDeOZbaY58" +
				"&redirect_uri="+redirect_uri;
	}
}
