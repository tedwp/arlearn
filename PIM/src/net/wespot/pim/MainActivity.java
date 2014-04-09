package net.wespot.pim;

import android.annotation.TargetApi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.*;
import android.widget.*;
import daoBase.DaoConfiguration;
import net.wespot.pim.utils.layout.ButtonEntryDelegator;
import net.wespot.pim.utils.layout.MainActionBarFragmentActivity;
import net.wespot.pim.view.*;
import org.celstec.arlearn.delegators.INQ;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends MainActionBarFragmentActivity {
    private static final String TAG = "MainActivity";

    /**
     * Called when the activity is first created.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This is needed to set the class
        ButtonEntryDelegator man = ButtonEntryDelegator.getInstance(this);

        // Creation of the links
        man._button_list(R.id.main_myinquiries_link, getResources().getString(R.string.wrapper_myinquiry), R.drawable.ic_inquiries, PimInquiriesFragment.class, false,  DaoConfiguration.getInstance().getSession().getInquiryLocalObjectDao().loadAll().size()+"");

        man._button_list(R.id.main_mymedia_link, getResources().getString(R.string.wrapper_mymedia),R.drawable.ic_mymedia,  InqMyMediaFragment.class, false);

        man._button_list(R.id.main_profile_link, getResources().getString(R.string.wrapper_profile),R.drawable.ic_profile, PimProfileFragment.class, false);

        man._button_list(R.id.main_badges_link, getResources().getString(R.string.wrapper_badges), R.drawable.ic_badges, PimBadgesFragment.class, false);

//        man._button_list(R.id.main_friends_link, getResources().getString(R.string.wrapper_friends),R.drawable.ic_friends, PimFriendsFragment.class, false);
        man._button_list(R.id.main_friends_link, getResources().getString(R.string.wrapper_friends),R.drawable.ic_friends, null, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_default, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:

                Log.e(TAG, "Before logout: "+INQ.properties.isAuthenticated());
                INQ.properties.disAuthenticate();
                INQ.properties.setAccount(0l);

                Intent myIntent = new Intent(this, SplashActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
                startActivity(myIntent);
                finish();

                Log.e(TAG, "After logout: "+INQ.properties.isAuthenticated());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
