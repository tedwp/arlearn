package net.wespot.pim.utils.layout;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import net.wespot.pim.R;
import org.celstec.arlearn.delegators.INQ;

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
public class _ActBar_FragmentActivity extends FragmentActivity {

    private static final String TAG = "_ActBar_FragmentActivity";
    private ActionBarHelper mActionBarHelper;


    @Override
    protected void onResume() {
        super.onResume();

        // Avoiding NULL exceptions when resuming the PIM
        if (INQ.inquiry == null){
            INQ.init(this);
            INQ.inquiry.syncInquiries();
            Log.e(TAG, "recover INQ.inquiry is needed.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mActionBarHelper = createActionBarHelper();
        mActionBarHelper.init();
    }

    /**
     * Create a compatible helper that will manipulate the action bar if available.
     */
    private ActionBarHelper createActionBarHelper() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return new ActionBarHelperICS();
        } else {
            return new ActionBarHelper();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ActionBarHelper getmActionBarHelper() {
        return mActionBarHelper;
    }

    public void setmActionBarHelper(ActionBarHelper mActionBarHelper) {
        this.mActionBarHelper = mActionBarHelper;
    }

    /**
     * Stub action bar helper; this does nothing.
     */
    public class ActionBarHelper {
        public void init() {}
        public void onDrawerClosed() {}
        public void onDrawerOpened() {}
        public void setTitle(CharSequence title) {}
        public void setSelectedNavigationItem(int pos) {}
        public void setNavigationMode(int a){}
        public void addTab(ActionBar.Tab tab) {}
        public ActionBar.Tab newTab() {return null;}
    }

    /**
     * Action bar helper for use on ICS and newer devices.
     */
    private class ActionBarHelperICS extends ActionBarHelper {
        private final ActionBar mActionBar;
        private CharSequence mDrawerTitle;
        private CharSequence mTitle;


        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        ActionBarHelperICS() {
//            mActionBar = getSupportActionBar();
            mActionBar = getActionBar();
            mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    mActionBar.setDisplayShowHomeEnabled(false);
            //        actionBar.setHomeButtonEnabled(false);

        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @Override
        public void init() {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mTitle = mDrawerTitle = getTitle();
        }

        /**
         * When the drawer is closed we restore the action bar state reflecting
         * the specific contents in view.
         */
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onDrawerClosed() {
            super.onDrawerClosed();
            mActionBar.setTitle(mTitle);
        }

        /**
         * When the drawer is open we set the action bar to a generic title.
         * The action bar should only contain data relevant at the top level of
         * the nav hierarchy represented by the drawer, as the rest of your content
         * will be dimmed down and non-interactive.
         */
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onDrawerOpened() {
            super.onDrawerOpened();
            mActionBar.setTitle(mDrawerTitle);
        }

        @Override
        public void setTitle(CharSequence title) {
            mTitle = title;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void setSelectedNavigationItem(int pos) {
            mActionBar.setSelectedNavigationItem(pos);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public ActionBar.Tab newTab() {
            return mActionBar.newTab();
        }

        @Override
        public void addTab(ActionBar.Tab tab) {
            mActionBar.addTab(tab);
        }

        @Override
        public void setNavigationMode(int a) {
            mActionBar.setNavigationMode(a);
        }
    }


}
