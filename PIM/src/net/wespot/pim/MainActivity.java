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
public class MainActivity extends MainActionBarFragmentActivity /*implements ActionBar.TabListener*/ {
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

//        _entry_list_temp button_inq_frag = (_entry_list_temp) getSupportFragmentManager().findFragmentById(R.id.main_myinquiries_link);
//        button_inq_frag.setName(getString(R.string.wrapper_myinquiry));
//        //TODO hardcode number of notificatons
//        button_inq_frag.setNotification("112");
//        my_inquiries = findViewById(R.id.main_myinquiries_link);
//        my_inquiries.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e(TAG, "my inquiries");
//                Intent intent = new Intent(getApplicationContext(), WrapperActivity.class);
//                intent.putExtra(WrapperActivity.OPTION, 0);
//                startActivity(intent);
//            }
//        });

//        _entry_list_temp button_media_frag = (_entry_list_temp) getSupportFragmentManager().findFragmentById(R.id.main_mymedia_link);
//        button_media_frag.setName(getString(R.string.wrapper_mymedia));
//        //TODO hardcode number of notificatons
//        button_media_frag.setNotification("36");
//        my_media = findViewById(R.id.main_mymedia_link);
//        my_media.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e(TAG, "my media content");
//                Intent intent = new Intent(getApplicationContext(), WrapperActivity.class);
//                intent.putExtra(WrapperActivity.OPTION, 1);
//                startActivity(intent);
//            }
//        });
//
//        _entry_list_temp button_prof_frag = (_entry_list_temp) getSupportFragmentManager().findFragmentById(R.id.main_profile_link);
//        button_prof_frag.setName(getString(R.string.wrapper_profile));
//        profile = findViewById(R.id.main_profile_link);
//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e(TAG, "my profile");
//                Intent intent = new Intent(getApplicationContext(), WrapperActivity.class);
//                intent.putExtra(WrapperActivity.OPTION, 2);
//                startActivity(intent);
//            }
//        });

//        _entry_list_temp button_badges_frag = (_entry_list_temp) getSupportFragmentManager().findFragmentById(R.id.main_badges_link);
//        button_badges_frag.setName(getString(R.string.wrapper_badges));
//        badges = findViewById(R.id.main_badges_link);
//        badges.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e(TAG, "my badges");
//                Intent intent = new Intent(getApplicationContext(), WrapperActivity.class);
//                intent.putExtra(WrapperActivity.OPTION, 3);
//                startActivity(intent);
//            }
//        });



//        _entry_list_temp button_friends_frag = (_entry_list_temp) getSupportFragmentManager().findFragmentById(R.id.main_friends_link);
//        button_friends_frag.setName(getString(R.string.wrapper_friends));
//        friends = (View) findViewById(R.id.main_friends_link);
//        friends.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e(TAG, "my friends");
//                Intent intent = new Intent(getApplicationContext(), WrapperActivity.class);
//                intent.putExtra(WrapperActivity.OPTION, 4);
//                startActivity(intent);
//            }
//        });
    }

//    private static void _button_list(MainActivity mainActivity, View view, int id, int name, Integer option, Class destination, String... arg) {
//        // arg [0]: notification [1] icon....
//
//        _entry_list_temp button_fragment = (_entry_list_temp) mainActivity.getSupportFragmentManager().findFragmentById(id);
//        button_fragment.setName(mainActivity.getString(name));
//
//        if (arg.length != 0){
//            button_fragment.setNotification(arg[0]);
//        }
//
//        view = (View) mainActivity.findViewById(id);
//        view.setOnClickListener(mainActivity.new _extended_intent(destination, option));
//    }
//
//
//    private class _extended_intent implements View.OnClickListener {
//
//        private Object[] opt;
//        private Class dest;
//
//        public _extended_intent(Class destination, Object... option) {
//             opt=option;
//             dest=destination;
//        }
//
//        @Override
//        public void onClick(View view) {
//            Intent intent = new Intent(getApplicationContext(), dest);
//            if (opt.length !=0){
//                intent.putExtra(WrapperActivity.OPTION, (Integer) opt[0]);
//            }
//            startActivity(intent);
//        }
//    }

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
