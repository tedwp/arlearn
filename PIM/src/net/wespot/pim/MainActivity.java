package net.wespot.pim;

import android.annotation.TargetApi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.*;
import android.widget.*;
import daoBase.DaoConfiguration;
import net.wespot.pim.utils.layout.ButtonDelegator;
import net.wespot.pim.utils.layout.MainActionBarFragmentActivity;
import net.wespot.pim.utils.layout.ViewItemClickInterface;
import net.wespot.pim.view.*;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.events.InquiryEvent;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends MainActionBarFragmentActivity implements ViewItemClickInterface{
    private static final String TAG = "MainActivity";
    private static final int MY_INQUIRIES = 12345;
    private static final int MY_MEDIA = 12346;
    private static final int PROFILE = 12347;
    private static final int BADGES = 12348;
    private static final int FRIENDS = 12349;
    private int number_inquiries;

    private ButtonDelegator man;

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

        LinearLayout listMainMenu = (LinearLayout) findViewById(R.id.content);

        ButtonDelegator buttonDelegator =  ButtonDelegator.getInstance(this);

        LinearLayout layout = buttonDelegator.layoutGenerator(R.dimen.mainscreen_margintop_first);
        buttonDelegator.buttonGenerator(layout, MY_INQUIRIES, getResources().getString(R.string.wrapper_myinquiry),
                String.valueOf(number_inquiries + ""), R.drawable.ic_inquiries).setOnListItemClickCallback(this);
        buttonDelegator.buttonGenerator(layout, MY_MEDIA, getResources().getString(R.string.wrapper_mymedia),
                String.valueOf(""), R.drawable.ic_mymedia).setOnListItemClickCallback(this);
        buttonDelegator.buttonGenerator(layout, PROFILE, getResources().getString(R.string.wrapper_profile),
                String.valueOf(""), R.drawable.ic_profile).setOnListItemClickCallback(this);
        buttonDelegator.buttonGenerator(layout, BADGES, getResources().getString(R.string.wrapper_badges),
                String.valueOf(""), R.drawable.ic_badges).setOnListItemClickCallback(this);
        buttonDelegator.buttonGenerator(layout, FRIENDS, getResources().getString(R.string.wrapper_friends),
                String.valueOf(""), R.drawable.ic_friends).setOnListItemClickCallback(this);

        listMainMenu.addView(layout);

    }

    @Override
    protected void onResume() {
        super.onResume();
        number_inquiries = DaoConfiguration.getInstance().getInquiryLocalObjectDao().loadAll().size();
    }

    private void onEventBackgroundThread(InquiryEvent inquiryObject){
        number_inquiries = DaoConfiguration.getInstance().getInquiryLocalObjectDao().loadAll().size();
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

    @Override
    public void onListItemClick(View v, int id) {
        switch (id){
            case MY_INQUIRIES:
                Intent intent_inquiries = new Intent(getApplicationContext(), PimInquiriesFragment.class);
                startActivity(intent_inquiries);
                break;
            case MY_MEDIA:
                Intent intent_media = new Intent(getApplicationContext(), InqMyMediaFragment.class);
                startActivity(intent_media);
                break;
            case PROFILE:
                Intent intent_profile = new Intent(getApplicationContext(), PimProfileFragment.class);
                startActivity(intent_profile);
                break;
            case BADGES:
                Intent intent_badges = new Intent(getApplicationContext(), PimBadgesFragment.class);
                startActivity(intent_badges);
                break;
            case FRIENDS:
                Intent intent_friends = new Intent(getApplicationContext(), PimFriendsFragment.class);
                startActivity(intent_friends);
                break;
        }
    }

    @Override
    public boolean setOnLongClickListener(View v) {
        return false;
    }
}
