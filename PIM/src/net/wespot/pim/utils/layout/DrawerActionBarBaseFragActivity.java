package net.wespot.pim.utils.layout;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.RelativeLayout;
import net.wespot.pim.R;

import android.support.v4.app.FragmentActivity;

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
public class DrawerActionBarBaseFragActivity extends FragmentActivity {

    private static final String TAG = "DrawerActionBarBaseFragActivity";
    private ActionBarHelper mActionBarHelper;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer = (RelativeLayout) findViewById(R.id.start_drawer);

        mDrawerLayout.setDrawerListener(new DrawerListener());
        mDrawerLayout.setDrawerShadow(R.drawable.actionbar_shadow, GravityCompat.START);

        // ActionBarDrawerToggle provides convenient helpers for tying together the
        // prescribed interactions between a top-level sliding drawer and the action bar.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);

        mActionBarHelper = createActionBarHelper();
        mActionBarHelper.init();


//        FragmentManager fmanager = getSupportFragmentManager();
//        Fragment fragment = fmanager.findFragmentById(R.id.map);
//        SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
//        GoogleMap map = supportmapfragment.getMap();
//
//        LatLng sydney = new LatLng(-33.867, 151.206);
//
//        //        map.setMyLocationEnabled(true);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
//
//        map.addMarker(new MarkerOptions()
//                .title("Sydney")
//                .snippet("The most populous city in Australia.")
//                .position(sydney));


//        MapFragment a = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
//
//        GoogleMap map = a.getMap();
//
//        LatLng sydney = new LatLng(-33.867, 151.206);
//
//        //        map.setMyLocationEnabled(true);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
//
//        map.addMarker(new MarkerOptions()
//                .title("Sydney")
//                .snippet("The most populous city in Australia.")
//                .position(sydney));

    }

    /**
     * Create a drawerlistener.
     */
    private class DrawerListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerOpened(View drawerView) {
            mDrawerToggle.onDrawerOpened(drawerView);
            mActionBarHelper.onDrawerOpened();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            mDrawerToggle.onDrawerClosed(drawerView);
            mActionBarHelper.onDrawerClosed();
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            mDrawerToggle.onDrawerStateChanged(newState);
        }
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

    public ActionBarHelper getmActionBarHelper() {
        return mActionBarHelper;
    }

    public void setmActionBarHelper(ActionBarHelper mActionBarHelper) {
        this.mActionBarHelper = mActionBarHelper;
    }

    public DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }

    public void setmDrawerLayout(DrawerLayout mDrawerLayout) {
        this.mDrawerLayout = mDrawerLayout;
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
            mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
