package net.wespot.pim;

import android.content.Intent;
import android.graphics.Bitmap;
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
public class LoginActivity extends FragmentActivity {

    private static final String TAG = "LoginActivity";
    private Button loginButton;
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_PROGRESS);
        getActionBar().hide();

        setContentView(R.layout.activity_login);

        webView = (WebView) findViewById(R.id.login_webpage);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            webView.loadUrl(extras.getString(Constants.URL_LOGIN_NAME));
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            // Show loading progress in activity's title bar.
            @Override
            public void onProgressChanged(WebView view, int progress) {
                setProgress(progress * 100);
            }
        });


        INQ.init(this);
        INQ.inquiry.syncInquiries();
        INQ.inquiry.syncHypothesis(151l);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                setTitle(url);
                Log.e(TAG, "onPageStarted " + url);
                if (url.contains("oauth.html?accessToken=")) {
                    String token = url.substring(url.indexOf("?")+1);
                    token = token.substring(token.indexOf("=")+1, token.indexOf("&"));
                    Log.e(TAG, "auth token is "+token);


                    ARL.properties.setAuthToken(token);
                    ARL.accounts.syncMyAccountDetails();

                    finish();
                }
            }
        });
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}