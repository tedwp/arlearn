package net.wespot.pim;

import android.annotation.TargetApi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.widget.*;
import net.wespot.pim.controller.Adapters.InquiryLazyListAdapter;
import net.wespot.pim.controller.WrapperActivity;
import net.wespot.pim.utils.layout.TempEntryFragment;
import net.wespot.pim.utils.layout.MainActionBarFragmentActivity;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends MainActionBarFragmentActivity /*implements ActionBar.TabListener*/ {
    private static final String TAG = "MainActivity";

    private View my_inquiries;
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

        TempEntryFragment button_inq_frag = (TempEntryFragment) getSupportFragmentManager().findFragmentById(R.id.main_myinquiries_link);
        button_inq_frag.setName(getString(R.string.wrapper_myinquiry));
        //TODO hardcode number of notificatons
        button_inq_frag.setNotification("112");
        my_inquiries = findViewById(R.id.main_myinquiries_link);
        my_inquiries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "my inquiries");
                Intent intent = new Intent(getApplicationContext(), WrapperActivity.class);
                intent.putExtra(WrapperActivity.OPTION, 0);
                startActivity(intent);
            }
        });

        TempEntryFragment button_prof_frag = (TempEntryFragment) getSupportFragmentManager().findFragmentById(R.id.main_profile_link);
        button_prof_frag.setName(getString(R.string.wrapper_profile));
        profile = findViewById(R.id.main_profile_link);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "my profile");
                Intent intent = new Intent(getApplicationContext(), WrapperActivity.class);
                intent.putExtra(WrapperActivity.OPTION, 1);
                startActivity(intent);
            }
        });

        TempEntryFragment button_badges_frag = (TempEntryFragment) getSupportFragmentManager().findFragmentById(R.id.main_badges_link);
        button_badges_frag.setName(getString(R.string.wrapper_badges));
        badges = findViewById(R.id.main_badges_link);
        badges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "my badges");
                Intent intent = new Intent(getApplicationContext(), WrapperActivity.class);
                intent.putExtra(WrapperActivity.OPTION, 2);
                startActivity(intent);
            }
        });

        TempEntryFragment button_friends_frag = (TempEntryFragment) getSupportFragmentManager().findFragmentById(R.id.main_friends_link);
        button_friends_frag.setName(getString(R.string.wrapper_friends));
        friends = (View) findViewById(R.id.main_friends_link);
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "my friends");
                Intent intent = new Intent(getApplicationContext(), WrapperActivity.class);
                intent.putExtra(WrapperActivity.OPTION, 3);
                startActivity(intent);
            }
        });
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
//            case android.R.id.home:
//                Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.menu_logout:
                Toast.makeText(this, "Tapped logout", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.menu_logout:
//                Toast.makeText(this, "Tapped logout", Toast.LENGTH_SHORT).show();
//                onSearchRequested();
//                break;
//            case R.id.menu_share:
//                Toast.makeText(this, "Tapped share", Toast.LENGTH_SHORT).show();
//                break;
            // add case implemented on inquiresFragment.java

        }
        return super.onOptionsItemSelected(item);
    }


}
