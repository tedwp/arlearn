package com.actionbarsherlock.app;

import static com.actionbarsherlock.app.SherlockFragmentActivity.DEBUG;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.actionbarsherlock.internal.view.menu.MenuItemMule;
import com.actionbarsherlock.internal.view.menu.MenuMule;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class SherlockFragment extends Fragment {
    private static final String TAG = "SherlockFragment";

    private SherlockFragmentActivity mActivity;

    public SherlockFragmentActivity getSherlockActivity() {
        return mActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        if (!(activity instanceof SherlockFragmentActivity)) {
            throw new IllegalStateException(TAG + " must be attached to a SherlockFragmentActivity.");
        }
        mActivity = (SherlockFragmentActivity)activity;

        super.onAttach(activity);
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, android.view.MenuInflater inflater) {
        if (DEBUG) Log.d(TAG, "[onCreateOptionsMenu] menu: " + menu + ", inflater: " + inflater);

        if (menu instanceof MenuMule) {
            MenuMule mule = (MenuMule)menu;
            mule.mDispatchShow = true;
            onCreateOptionsMenu(mule.unwrap(), mActivity.getSupportMenuInflater());
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Nothing to see here.
    }

    @Override
    public final void onPrepareOptionsMenu(android.view.Menu menu) {
        if (DEBUG) Log.d(TAG, "[onPrepareOptionsMenu] menu: " + menu);

        if (menu instanceof MenuMule) {
            MenuMule mule = (MenuMule)menu;
            mule.mDispatchShow = true;
            onPrepareOptionsMenu(mule.unwrap());
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {
        //Nothing to see here.
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (DEBUG) Log.d(TAG, "[onOptionsItemSelected] item: " + item);

        if (item instanceof MenuItemMule) {
            return onOptionsItemSelected(((MenuItemMule)item).unwrap());
        }
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //Nothing to see here.
        return false;
    }
}
