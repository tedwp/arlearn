package net.wespot.pim;

import android.annotation.TargetApi;

import android.os.Build;
import android.os.Bundle;

import android.view.*;
import android.widget.*;
import daoBase.DaoConfiguration;
import net.wespot.pim.utils.layout.ButtonEntryDelegator;
import net.wespot.pim.utils.layout.MainActionBarFragmentActivity;
import net.wespot.pim.view.*;


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

        man._button_list(R.id.main_badges_link, getResources().getString(R.string.wrapper_badges), R.drawable.ic_badges, PimBadgesFragmentList.class, false);

        man._button_list(R.id.main_friends_link, getResources().getString(R.string.wrapper_friends),R.drawable.ic_friends, PimFriendsFragment.class, false);
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
                Toast.makeText(this, "Tapped logout", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
