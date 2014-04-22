package net.wespot.pim;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import daoBase.DaoConfiguration;
import net.wespot.pim.controller.Adapters.InitialPagerAdapter;
import net.wespot.pim.utils.Constants;
import net.wespot.pim.utils.layout.CirclePageIndicator;
import net.wespot.pim.utils.layout.PageIndicator;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.delegators.ARL;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Angel Suarez
 * ****************************************************************************
 */
@SuppressLint("NewApi")
public class LoginActivity extends FragmentActivity {

    private static final String TAG = "LoginActivity";
    private Button loginButton;
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (INQ.isOnline()){

            requestWindowFeature(Window.FEATURE_PROGRESS);

            Log.e(TAG, "Version SDK: "+ Build.VERSION.SDK_INT+"");

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                getActionBar().hide();
            }

            if (!INQ.accounts.isAuthenticated()){
                setContentView(R.layout.activity_login);

                webView = (WebView) findViewById(R.id.login_webpage);

                Bundle extras = getIntent().getExtras();

                if (extras != null) {
                    String url = extras.getString(Constants.URL_LOGIN_NAME);
                    webView.loadUrl(url);
                }
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient() {
                    // Show loading progress in activity's title bar.
                    @Override
                    public void onProgressChanged(WebView view, int progress) {
                        setProgress(progress * 100);
                    }
                });

                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        setTitle(url);
                        Log.d(TAG, "onPageStarted ");
                        if (url.contains("oauth.html?accessToken=")) {
                            String token = url.substring(url.indexOf("?")+1);
                            token = token.substring(token.indexOf("=")+1, token.indexOf("&"));

                            // DisAuthenticate also here because if app lost the session
                            // and then you login with other account you will see the details
                            // from the other account. Now we are sure that when you login
                            // user details are displayed.
                            INQ.accounts.disAuthenticate();
                            INQ.properties.setAccount(0l);

                            // Authenticate the user
                            INQ.properties.setAuthToken(token);
                            INQ.properties.setIsAuthenticated();
                            INQ.accounts.syncMyAccountDetails();

                            // Remove all inquiries and sync the new ones.
                            DaoConfiguration.getInstance().getInquiryLocalObjectDao().deleteAll();
                            INQ.inquiry.syncInquiries();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                            finish();
                        }
                    }
                });
            }
            else{
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                ARL.accounts.syncMyAccountDetails();
            }
        }else{
            Toast.makeText(this, R.string.network_connection, Toast.LENGTH_SHORT).show();
        }

    }
}