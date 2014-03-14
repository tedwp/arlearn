package net.wespot.pim;

import android.annotation.TargetApi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.*;
import android.widget.*;
import net.wespot.pim.controller.WrapperActivity;
import net.wespot.pim.utils.layout.ButtonEntryDelegator;
import net.wespot.pim.utils.layout.MainActionBarFragmentActivity;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends MainActionBarFragmentActivity {
    private static final String TAG = "MainActivity";

    private View my_inquiries;
    private View my_media;
    private View profile;
    private View badges;
    private View friends;

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
        man._button_list(my_inquiries, R.id.main_myinquiries_link, getResources().getString(R.string.wrapper_myinquiry), WrapperActivity.class, 0, WrapperActivity.OPTION, "112");

        man._button_list(my_media, R.id.main_mymedia_link, getResources().getString(R.string.wrapper_mymedia), WrapperActivity.class, 1, WrapperActivity.OPTION);

        man._button_list(profile, R.id.main_profile_link, getResources().getString(R.string.wrapper_profile), WrapperActivity.class, 2, WrapperActivity.OPTION);

        man._button_list(badges, R.id.main_badges_link, getResources().getString(R.string.wrapper_badges), WrapperActivity.class, 3, WrapperActivity.OPTION);

        man._button_list(friends, R.id.main_friends_link, getResources().getString(R.string.wrapper_friends), WrapperActivity.class, 4, WrapperActivity.OPTION);
    }

//
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
