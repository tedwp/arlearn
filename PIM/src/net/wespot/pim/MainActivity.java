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
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.client.InquiryClient;
import org.celstec.events.InquiryEvent;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends MainActionBarFragmentActivity {
    private static final String TAG = "MainActivity";
    private int number_inquiries;
    private ButtonEntryDelegator man;
    /**
     * Called when the activity is first created.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARL.eventBus.register(this);

        INQ.inquiry.syncInquiries();
        number_inquiries = DaoConfiguration.getInstance().getInquiryLocalObjectDao().loadAll().size();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // This is needed to set the class
        man = ButtonEntryDelegator.getInstance(this);

        number_inquiries = DaoConfiguration.getInstance().getInquiryLocalObjectDao().loadAll().size();

        // Creation of the links
        man._button_list(R.id.main_myinquiries_link, getResources().getString(R.string.wrapper_myinquiry), R.drawable.ic_inquiries, PimInquiriesFragment.class, false, number_inquiries+"" );

        man._button_list(R.id.main_mymedia_link, getResources().getString(R.string.wrapper_mymedia),R.drawable.ic_mymedia,  InqMyMediaFragment.class, false);

        man._button_list(R.id.main_profile_link, getResources().getString(R.string.wrapper_profile),R.drawable.ic_profile, PimProfileFragment.class, false);

        man._button_list(R.id.main_badges_link, getResources().getString(R.string.wrapper_badges), R.drawable.ic_badges, PimBadgesFragment.class, false);

//        man._button_list(R.id.main_friends_link, getResources().getString(R.string.wrapper_friends),R.drawable.ic_friends, PimFriendsFragment.class, false);
        man._button_list(R.id.main_friends_link, getResources().getString(R.string.wrapper_friends),R.drawable.ic_friends, null, false);
    }

    private void onEventBackgroundThread(InquiryEvent inquiryObject){
        number_inquiries = DaoConfiguration.getInstance().getInquiryLocalObjectDao().loadAll().size();
//        Log.d(TAG, "Lazy loaded: "+number_inquiries+" inquiries");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ARL.eventBus.unregister(this);
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

                Log.d(TAG, "Before logout: " + INQ.accounts.isAuthenticated());
                INQ.accounts.disAuthenticate();
                INQ.properties.setAccount(0l);

                Intent myIntent = new Intent(this, SplashActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
                startActivity(myIntent);
                finish();

                Log.d(TAG, "After logout: "+INQ.accounts.isAuthenticated());

                Toast.makeText(this,R.string.menu_logout,Toast.LENGTH_SHORT);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
