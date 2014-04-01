/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.wespot.pim.controller;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import net.wespot.pim.MainActivity;
import net.wespot.pim.R;
import net.wespot.pim.SplashActivity;
import net.wespot.pim.controller.Adapters.InquiryPagerAdapter;
import net.wespot.pim.controller.Adapters.NewInquiryPagerAdapter;
import net.wespot.pim.utils.layout._ActBar_FragmentActivity;
import net.wespot.pim.view.InqCreateInquiryFragment;
import org.celstec.arlearn.delegators.INQ;

public class InquiryActivity extends _ActBar_FragmentActivity implements ActionBar.TabListener{

    private static final String TAG = "InquiryActivity";
    public static final String PHASE = "num_phase";
    private int mStackLevel = 0;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments representing
     * each object in a collection. We use a {@link android.support.v4.app.FragmentStatePagerAdapter}
     * derivative, which will destroy and re-create fragments as needed, saving and restoring their
     * state in the process. This is important to conserve memory and is a best practice when
     * allowing navigation between objects in a potentially large collection.
     */
    private InquiryPagerAdapter mInquiryPagerAdapter;
    private NewInquiryPagerAdapter mNewInquiryPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    private ViewPager mViewPager;
    private long num_phase;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (INQ.inquiry.getCurrentInquiry() == null){
            Log.e(TAG, "Back pressed - New inquiry");
        }else{
            Log.e(TAG, "Back pressed - Show inquiry");
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Avoiding NULL exceptions when resuming the PIM
        if (INQ.inquiry == null ){
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
            INQ.init(this);
            INQ.accounts.syncMyAccountDetails();
            INQ.inquiry.syncInquiries();
            Log.e(TAG, "recover INQ.inquiry is needed in InquiryActivity.");
        }

        if (INQ.inquiry.getCurrentInquiry() == null){
            Log.e(TAG, "New inquiry");

            setContentView(R.layout.wrapper);
            FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction;

            fragmentTransaction = fragmentManager.beginTransaction();
            InqCreateInquiryFragment fragment = new InqCreateInquiryFragment();
            fragmentTransaction.add(R.id.content, fragment);
            fragmentTransaction.commit();
        }else{

            setContentView(R.layout.activity_inquiry);

            Log.e(TAG, "Show inquiry");

            // Create an adapter that when requested, will return a fragment representing an object in
            // the collection.
            // ViewPager and its adapters use support library fragments, so we must use
            // getSupportFragmentManager.
            mInquiryPagerAdapter = new InquiryPagerAdapter(getSupportFragmentManager());
            getmActionBarHelper().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

            // Set up the ViewPager, attaching the adapter.
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mInquiryPagerAdapter);
            mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    // When swiping between different app sections, select the corresponding tab.
                    // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                    // Tab.
//                    getmActionBarHelper().setSelectedNavigationItem(position);
                }
            });

            // For each of the sections in the app, add a tab to the action bar.
            for (int i = 0; i < mInquiryPagerAdapter.getCount(); i++) {
                // Create a tab with text corresponding to the page title defined by the adapter.
                // Also specify this Activity object, which implements the TabListener interface, as the
                // listener for when this tab is selected.
                getmActionBarHelper().addTab(
                        getmActionBarHelper().newTab()
                                .setText(mInquiryPagerAdapter.getPageTitle(i))
                                .setTabListener(this));
            }

            getActionBar().setTitle(getResources().getString(R.string.actionbar_inquiry)+" - "+INQ.inquiry.getCurrentInquiry().getTitle());

            Bundle extras = getIntent().getExtras();
            if (extras != null){
                mViewPager.setCurrentItem(extras.getInt(PHASE));
            }
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
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
//                Intent upIntent = new Intent(this, MainActivity.class);
//                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//                    // This activity is not part of the application's task, so create a new task
//                    // with a synthesized back stack.
//                    TaskStackBuilder.from(this)
//                            // If there are ancestor activities, they should be added here.
//                            .addNextIntent(upIntent)
//                            .startActivities();
//                    finish();
//                } else {
//                    // This activity is part of the application's task, so simply
//                    // navigate up to the hierarchical parent activity.
//                    NavUtils.navigateUpTo(this, upIntent);
//                }

                onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

//    void showCreateInquiryDialogFragment() {
//        DialogFragment dialog = new CreateInquiryDialogFragment();
//        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
//
//
//    }
//
//    public class CreateInquiryDialogFragment extends DialogFragment {
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the Builder class for convenient dialog construction
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setMessage(R.string.inquiry_create_title)
//                    .setPositiveButton(R.string.inquiry_create_yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            // FIRE ZE MISSILES!
//                        }
//                    })
//                    .setNegativeButton(R.string.inquiry_create_no, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            // User cancelled the dialog
//                        }
//                    });
//            // Create the AlertDialog object and return it
//            return builder.create();
//        }
//    }
}
