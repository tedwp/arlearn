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

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import net.wespot.pim.R;
import net.wespot.pim.utils.layout.ActionBarFragmentActivity;
import net.wespot.pim.view.PimBadgesFragmentList;
import net.wespot.pim.view.PimInquiriesFragment;

public class WrapperActivity extends ActionBarFragmentActivity{

    private static final String TAG = "InquiryActivity";
    public static final String OPTION = "option";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        if (extras != null){
            switch (extras.getInt(OPTION)){
                case 0:
                    setContentView(R.layout.wrapper);

                    fragmentTransaction = fragmentManager.beginTransaction();
                    PimInquiriesFragment fragment = new PimInquiriesFragment();
                    fragmentTransaction.add(R.id.drawer_layout, fragment);
                    fragmentTransaction.commit();

                    break;
                case 1:
                    setContentView(R.layout.screen_profile);

                    break;
                case 2:
                    setContentView(R.layout.wrapper);
                    fragmentTransaction = fragmentManager.beginTransaction();
                    PimBadgesFragmentList badgesFragment = new PimBadgesFragmentList();
                    fragmentTransaction.add(R.id.drawer_layout, badgesFragment);
                    fragmentTransaction.commit();
                    break;
                default:
                    setContentView(R.layout.screen_friends);

                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.e(TAG, "Back pressed");

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
}
