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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import daoBase.DaoConfiguration;
import net.wespot.pim.MainActivity;
import net.wespot.pim.R;
import net.wespot.pim.SplashActivity;
import net.wespot.pim.utils.Constants;
import net.wespot.pim.utils.images.BitmapWorkerTask;
import net.wespot.pim.utils.layout.ButtonEntryDelegator;
import net.wespot.pim.utils.layout._ActBar_FragmentActivity;
import org.celstec.arlearn.delegators.INQ;

public class InquiryPhasesActivity extends _ActBar_FragmentActivity {

    private static final String TAG = "InquiryActivity";
    private ListView list_phases;
    private TextView inquiry_description_title;
    private ImageView inquiry_description_image;

    private View b_description;
    private View b_hypothesis;
    private View b_plan;
    private View b_data;
    private View b_analyse;
    private View b_discuss;
    private View b_communicate;
    private View b_add_friends;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("currentInquiry", INQ.inquiry.getCurrentInquiry().getId());
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
//            getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
            //return;
//            Long currentInquiry = savedInstanceState.getLong("currentInquiry");
//            Log.e(TAG, "savedInstanceState != null - value"+savedInstanceState.getLong("currentInquiry"));

            INQ.init(this);
            INQ.accounts.syncMyAccountDetails();
            INQ.inquiry.setCurrentInquiry(DaoConfiguration.getInstance().getInquiryLocalObjectDao().load(savedInstanceState.getLong("currentInquiry")));

        }

        setContentView(R.layout.activity_phases);

        // This is needed to set the class
        ButtonEntryDelegator button_manager = ButtonEntryDelegator.getInstance(this);

        // Creation of the links
        b_description =button_manager._button_list(R.id.list_phases_description, Constants.INQUIRY_PHASES_LIST.get(0), R.drawable.ic_description, InquiryActivity.class, true);
        b_description.setOnClickListener(new OnClick(0));

        b_hypothesis =button_manager._button_list(R.id.list_phases_hypothesis, Constants.INQUIRY_PHASES_LIST.get(1), R.drawable.ic_hypothesis, InquiryActivity.class, true);
        b_hypothesis.setOnClickListener(new OnClick(1));

        b_plan =button_manager._button_list(R.id.list_phases_plan, Constants.INQUIRY_PHASES_LIST.get(2),R.drawable.ic_plan, InquiryActivity.class, true);
        b_plan.setOnClickListener(new OnClick(2));

        if (INQ.inquiry.getCurrentInquiry().getRunLocalObject()!=null){
            if (INQ.inquiry.getCurrentInquiry().getRunLocalObject().getGameLocalObject()!=null){
                int numberDataCollectionTasks = DaoConfiguration.getInstance().getGameLocalObjectDao().load(INQ.inquiry.getCurrentInquiry().getRunLocalObject().getGameLocalObject().getId()).getGeneralItems().size();
                b_data =button_manager._button_list(R.id.list_phases_data, Constants.INQUIRY_PHASES_LIST.get(3),R.drawable.ic_data, InquiryActivity.class, true, numberDataCollectionTasks+"" );
                b_data.setOnClickListener(new OnClick(3));
            }else{
                b_data =button_manager._button_list(R.id.list_phases_data, Constants.INQUIRY_PHASES_LIST.get(3),R.drawable.ic_data, InquiryActivity.class, true, "0");
            }
        }else{
            b_data =button_manager._button_list(R.id.list_phases_data, Constants.INQUIRY_PHASES_LIST.get(3),R.drawable.ic_data, InquiryActivity.class, true, "0");
        }

        b_analyse =button_manager._button_list(R.id.list_phases_analyse, Constants.INQUIRY_PHASES_LIST.get(4), R.drawable.ic_analyze, InquiryActivity.class,true);
        b_analyse.setOnClickListener(new OnClick(4));

        b_discuss =button_manager._button_list(R.id.list_phases_discuss, Constants.INQUIRY_PHASES_LIST.get(5), R.drawable.ic_discuss, InquiryActivity.class, true);
        b_discuss.setOnClickListener(new OnClick(5));

        b_communicate =button_manager._button_list(R.id.list_phases_communicate, Constants.INQUIRY_PHASES_LIST.get(6),  R.drawable.ic_communicate, InquiryActivity.class, true);
        b_communicate.setOnClickListener(new OnClick(6));

        b_add_friends =button_manager._button_list(R.id.invites_friends_to_inquiry, getString(R.string.phases_invite_new_friend), R.drawable.ic_invite_friend, null, false);

        getActionBar().setTitle(R.string.actionbar_inquiry_list);

        inquiry_description_title = (TextView) findViewById(R.id.list_phases_title);
        inquiry_description_image = (ImageView) findViewById(R.id.list_phases_image);

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

    private class OnClick implements View.OnClickListener {

        private int phase;

        public OnClick(int i) {
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
