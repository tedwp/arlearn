package org.celstec.arlearn2.android.oauth;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.broadcast.NetworkSwitcher;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.client.AccountClient;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class AnonymousTokenAuthenticate extends AsyncTask<String, String, Void> {

	private Context ctx;

	public AnonymousTokenAuthenticate(Context ctx) {
		this.ctx = ctx;
	}
	
	@Override
	protected Void doInBackground(String... params) {
		String action = params[0];
		try {
			if (NetworkSwitcher.isOnline(ctx)) {
				AuthResponse resp = AccountClient.getAccountClient().anonymousLogin(action);
				if (resp.getAuth() == null) {
					PropertiesAdapter.getInstance(ctx).disAuthenticate();
				} else {
					PropertiesAdapter.getInstance(ctx).setAuthToken(resp.getAuth());
					PropertiesAdapter.getInstance(ctx).setIsAuthenticated();
					DownloadDetailsTask.downloadAndStoreAccountDetails(ctx);
				}
			} else {
				publishProgress(ctx.getString(R.string.networkUnavailable));
			}
		} catch (Exception e) {
			PropertiesAdapter.getInstance(ctx).disAuthenticate();
			publishProgress(ctx.getString(R.string.loginFailed).replace("***", action));
		}
		return null;
	}
	
	
	@Override
	protected void onProgressUpdate(String... voids) {
			Toast.makeText(ctx, voids[0], Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
	}
	
}
