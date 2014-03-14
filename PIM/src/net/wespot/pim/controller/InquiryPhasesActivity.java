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
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import net.wespot.pim.R;
import net.wespot.pim.utils.Constants;
import net.wespot.pim.utils.images.BitmapWorkerTask;
import net.wespot.pim.utils.layout.ActionBarFragmentActivity;
import net.wespot.pim.utils.layout.ButtonEntryDelegator;
import org.celstec.arlearn.delegators.INQ;

public class InquiryPhasesActivity extends ActionBarFragmentActivity {

    private static final String TAG = "InquiryActivity";
    private ListView list_phases;
    private TextView inquiry_description_title;
    private ImageView inquiry_description_image;

//    private _EntryListTemp button_description;
    private View button_descriptionView;
//    private _EntryListTemp button_hypothesis;
    private View button_hypothesisView;

//    private _EntryListTemp button_plan;
    private View button_planView;
//    private _EntryListTemp button_data;
    private View button_dataView;
//    private _EntryListTemp button_analyse;
    private View button_analyseView;
//    private _EntryListTemp button_discuss;
    private View button_discussView;
//    private _EntryListTemp button_communicate;
    private View button_communicateView;
    private View button_friendsView;
//    private PhasesLazyListAdapter phasesListAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_phases);

//        phasesListAdapter = new PhasesLazyListAdapter(this, Constants.INQUIRY_PHASES_LIST);

        inquiry_description_title = (TextView) findViewById(R.id.list_phases_title);
        inquiry_description_image = (ImageView) findViewById(R.id.list_phases_image);
//        list_phases = (ListView) findViewById(R.id.list_phases);
//        list_phases.setAdapter(phasesListAdapter);
//        list_phases.setOnItemClickListener(new OnItemClickPhasesList());


        // This is needed to set the class
        ButtonEntryDelegator button_manager = ButtonEntryDelegator.getInstance(this);

        // Creation of the links
        button_manager._button_list(button_descriptionView, R.id.list_phases_description, Constants.INQUIRY_PHASES_LIST.get(0), InquiryActivity.class, 0, InquiryActivity.PHASE);

        button_manager._button_list(button_hypothesisView, R.id.list_phases_hypothesis, Constants.INQUIRY_PHASES_LIST.get(1), InquiryActivity.class, 1, InquiryActivity.PHASE);

        button_manager._button_list(button_planView, R.id.list_phases_plan, Constants.INQUIRY_PHASES_LIST.get(2), InquiryActivity.class, 2, InquiryActivity.PHASE);

        button_manager._button_list(button_dataView, R.id.list_phases_data, Constants.INQUIRY_PHASES_LIST.get(3), InquiryActivity.class, 3, InquiryActivity.PHASE, "112");

        button_manager._button_list(button_analyseView, R.id.list_phases_analyse, Constants.INQUIRY_PHASES_LIST.get(4), InquiryActivity.class, 4, InquiryActivity.PHASE);

        button_manager._button_list(button_discussView, R.id.list_phases_discuss, Constants.INQUIRY_PHASES_LIST.get(5), InquiryActivity.class, 5, InquiryActivity.PHASE);

        button_manager._button_list(button_communicateView, R.id.list_phases_communicate, Constants.INQUIRY_PHASES_LIST.get(6), InquiryActivity.class, 6, InquiryActivity.PHASE);

        button_manager._button_list(button_friendsView, R.id.invites_friends_to_inquiry, getString(R.string.phases_invite_new_friend), null, null, null);


        getActionBar().setTitle(R.string.actionbar_inquiry_list);

        if (INQ.inquiry.getCurrentInquiry().getIcon() != null){
            BitmapWorkerTask task = new BitmapWorkerTask(inquiry_description_image);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, INQ.inquiry.getCurrentInquiry().getIcon());
        }
        else{
            inquiry_description_image.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.foto_perfil_croped));
        }

        inquiry_description_title.setText(INQ.inquiry.getCurrentInquiry().getTitle());
//        inquiry_description_title.loadData(INQ.inquiry.getCurrentInquiry().getDescription(), Constants.MIME_TYPE, Constants.ENCONDING);
//        inquiry_description_title.setBackgroundColor(0x00000000);

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

//    private class OnItemClickPhasesList implements AdapterView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            Intent intent = new Intent(getApplicationContext(), InquiryActivity.class);
//            intent.putExtra(InquiryActivity.PHASE, i);
//            startActivity(intent);
//        }
//    }

    private class OnClickNewInquiry implements View.OnClickListener {

        private int phase;

        public OnClickNewInquiry(int i) {
                 phase = i;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), InquiryActivity.class);
            intent.putExtra(InquiryActivity.PHASE, phase);
            startActivity(intent);
        }
    }
}
