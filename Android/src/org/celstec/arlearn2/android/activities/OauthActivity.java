package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.GCMIntentService;
import org.celstec.arlearn2.android.oauth.DownloadDetailsTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class OauthActivity extends GeneralActivity {

    public boolean downloadDetailsWasExecuted = false;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        downloadDetailsWasExecuted = false;
		// Allow the title bar to show loading progress.
		requestWindowFeature(Window.FEATURE_PROGRESS);

		WebView webview = new WebView(this);
		setContentView(webview);

		Intent intent = getIntent();
		if (intent.getData() != null) {
			webview.loadUrl(intent.getDataString());
		}
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebChromeClient(new WebChromeClient() {
		     // Show loading progress in activity's title bar.
		      @Override
		      public void onProgressChanged(WebView view, int progress) {
		        setProgress(progress * 100);
		      }
		    });
		  webview.setWebViewClient(new WebViewClient() {
		     // When start to load page, show url in activity's title bar
		      @Override
		      public void onPageStarted(WebView view, String url, Bitmap favicon) {
		        setTitle(url);
		        System.out.println("onPageStarted "+url);
		        if (url.contains("oauth.html?accessToken=")) {
		        	String token = url.substring(url.indexOf("?")+1);
		        	token = token.substring(token.indexOf("=")+1, token.indexOf("&"));
		        	System.out.println("auth token is "+token);
		        	 Intent result = new Intent();
			            result.putExtra("token", token);
		        	 OauthActivity.this.getMenuHandler().getPropertiesAdapter().setAuthToken(token);
						OauthActivity.this.getMenuHandler().getPropertiesAdapter().setIsAuthenticated();
						new DownloadDetailsTask(OauthActivity.this).execute();
			            setResult(RESULT_OK, result);
			    		new GCMCheck().execute();
			            finish();
		        }
		      }
		      
		      @Override 
		      public void onLoadResource(WebView view, String url) {
		    	  System.out.println("loading "+url);
		      }
		      
		      @Override
		      public void onPageFinished(WebView view, String url) {
                if (downloadDetailsWasExecuted) return;
		        CookieSyncManager.getInstance().sync();
		        // Get the cookie from cookie jar.
		        String cookie = CookieManager.getInstance().getCookie(url);
		        if (cookie == null) {
		          return;
		        }
		        // Cookie is a string like NAME=VALUE [; NAME=VALUE]
		        String[] pairs = cookie.split(";");
		        for (int i = 0; i < pairs.length; ++i) {
		          String[] parts = pairs[i].split("=", 2);
		          // If token is found, return it to the calling activity.
		          if (parts.length == 2 &&
		             parts[0].equalsIgnoreCase("arlearn.AccessToken")) {
		            Intent result = new Intent();
		            result.putExtra("token", parts[1]);
		            System.out.println("token is "+parts[1]);
		            OauthActivity.this.getMenuHandler().getPropertiesAdapter().setAuthToken(parts[1]);
					OauthActivity.this.getMenuHandler().getPropertiesAdapter().setIsAuthenticated();
					if (!downloadDetailsWasExecuted) {
                        downloadDetailsWasExecuted = true;
                        new DownloadDetailsTask(OauthActivity.this).execute();
                        setResult(RESULT_OK, result);
                        new GCMCheck().execute();
                        finish();

                    }

		          }
		        }
		      }
		    });
		  
		  
	}

	public static void processToken(Context ctx, String token) {
	
	}

	@Override
	public boolean isGenItemActivity() {
		return false;
	}
	
	public class GCMCheck extends AsyncTask<Object, Long, Void> {

		@Override
		protected Void doInBackground(Object... arg0) {
			try {
				 GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(OauthActivity.this);
				 String regid = gcm.register(GCMIntentService.SENDER_ID);
			} catch (Exception e) {
				e.printStackTrace();

			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Long... delta) {
		
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}
}
