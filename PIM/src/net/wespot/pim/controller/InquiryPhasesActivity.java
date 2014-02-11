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

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import net.wespot.pim.R;
import net.wespot.pim.controller.Adapters.PhasesLazyListAdapter;
import net.wespot.pim.utils.Constants;
import net.wespot.pim.utils.layout.DrawerActionBarBaseFragActivity;
import org.celstec.arlearn.delegators.INQ;

public class InquiryPhasesActivity extends DrawerActionBarBaseFragActivity {

    private static final String TAG = "InquiryActivity";
    private ListView list_phases;
    private PhasesLazyListAdapter phasesListAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.phases);

        phasesListAdapter = new PhasesLazyListAdapter(this, Constants.INQUIRY_PHASES_LIST);

        list_phases = (ListView) findViewById(R.id.list_phases);
        list_phases.setAdapter(phasesListAdapter);

        list_phases.setOnItemClickListener(new OnItemClickPhasesList());

        getActionBar().setTitle(INQ.inquiry.getCurrentInquiry().getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class OnItemClickPhasesList implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getApplicationContext(), InquiryActivity.class);
            intent.putExtra(InquiryActivity.PHASE, i);
            startActivity(intent);
        }
    }
}
