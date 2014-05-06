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
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import daoBase.DaoConfiguration;
import net.wespot.pim.R;
import net.wespot.pim.utils.Constants;
import net.wespot.pim.utils.images.BitmapWorkerTask;
import net.wespot.pim.utils.layout.ButtonDelegator;
import net.wespot.pim.utils.layout.ViewItemClickInterface;
import net.wespot.pim.utils.layout._ActBar_FragmentActivity;
import org.celstec.arlearn.delegators.INQ;

public class InquiryPhasesActivity extends _ActBar_FragmentActivity implements ViewItemClickInterface {

    private static final String TAG = "InquiryActivity";
    private ListView list_phases;
    private TextView inquiry_description_title;
    private ImageView inquiry_description_image;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("currentInquiry", INQ.inquiry.getCurrentInquiry().getId());
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            INQ.init(this);
            INQ.accounts.syncMyAccountDetails();
            INQ.inquiry.setCurrentInquiry(DaoConfiguration.getInstance().getInquiryLocalObjectDao().load(savedInstanceState.getLong("currentInquiry")));
        }

        INQ.inquiry.syncDataCollectionTasks();

        setContentView(R.layout.activity_phases);

        if (!(Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1)) {
            if (!(Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)){
                getActionBar().setTitle(R.string.actionbar_inquiry_list);
            }
        }

        inquiry_description_title = (TextView) findViewById(R.id.list_phases_title);
        inquiry_description_image = (ImageView) findViewById(R.id.list_phases_image);

        if (INQ.inquiry.getCurrentInquiry().getIcon() != null){
            BitmapWorkerTask task = new BitmapWorkerTask(inquiry_description_image);

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
                task.execute(INQ.inquiry.getCurrentInquiry().getIcon());
            } else {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, INQ.inquiry.getCurrentInquiry().getIcon());
            }

        }
        else{
            inquiry_description_image.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_placeholder));
        }

        inquiry_description_title.setText(INQ.inquiry.getCurrentInquiry().getTitle());
//        inquiry_description_title.loadData(INQ.inquiry.getCurrentInquiry().getDescription(), Constants.MIME_TYPE, Constants.ENCONDING);
//        inquiry_description_title.setBackgroundColor(0x00000000);

        LinearLayout listPhasesContainer = (LinearLayout) findViewById(R.id.list_phases);
        ButtonDelegator buttonDelegator =  ButtonDelegator.getInstance(this);
        LinearLayout layout = buttonDelegator.layoutGenerator(R.dimen.mainscreen_margintop_zero);

        for (int i=0; i < Constants.INQUIRY_ID_PHASES_LIST.size(); i++){
            switch (i){
                case Constants.ID_DATA:
                    createDataCollectionButton(buttonDelegator, layout, i);
                    break;
                case Constants.ID_DESCRIPTION:
                    buttonDelegator.buttonGenerator(layout,
                            Constants.INQUIRY_ID_PHASES_LIST.get(i),
                            Constants.INQUIRY_PHASES_LIST.get(i),String.valueOf(""),
                            Constants.INQUIRY_ICON_PHASES_LIST.get(i)
                    ).setOnListItemClickCallback(this);
                    break;
                case Constants.ID_QUESTION:
                    buttonDelegator.buttonGenerator(layout,
                            Constants.INQUIRY_ID_PHASES_LIST.get(i),
                            Constants.INQUIRY_PHASES_LIST.get(i), String.valueOf(""),
                            Constants.INQUIRY_ICON_PHASES_LIST.get(i)
                    ).setOnListItemClickCallback(this);
                    break;
                default:
                    break;
            }
        }

        listPhasesContainer.addView(layout);

        LinearLayout linkAddFriends = (LinearLayout) findViewById(R.id.link_friends);
        ButtonDelegator buttonDelegatorAddFriends =  ButtonDelegator.getInstance(this);
        LinearLayout layoutAddFriends = buttonDelegatorAddFriends.layoutGenerator(R.dimen.mainscreen_margintop_zero);

        buttonDelegator.buttonGenerator(layoutAddFriends, 10, getResources().getString(R.string.phases_invite_new_friend),
                "", R.drawable.ic_invite_friend
        ).setOnListItemClickCallback(new ClickInviteFriend());

        linkAddFriends.addView(layoutAddFriends);

    }

    private void createDataCollectionButton(ButtonDelegator buttonDelegator, LinearLayout layout, int i) {
        if (INQ.inquiry.getCurrentInquiry().getRunLocalObject()!=null){
            if (INQ.inquiry.getCurrentInquiry().getRunLocalObject().getGameLocalObject()!=null){
                int numberDataCollectionTasks = DaoConfiguration.getInstance().getGameLocalObjectDao().load(INQ.inquiry.getCurrentInquiry().getRunLocalObject().getGameLocalObject().getId()).getGeneralItems().size();
                buttonDelegator.buttonGenerator(layout,
                        Constants.INQUIRY_ID_PHASES_LIST.get(i),
                        Constants.INQUIRY_PHASES_LIST.get(i),String.valueOf(numberDataCollectionTasks),
                        Constants.INQUIRY_ICON_PHASES_LIST.get(i)
                ).setOnListItemClickCallback(this);
            }else{
                buttonDelegator.buttonGenerator(layout,
                        Constants.INQUIRY_ID_PHASES_LIST.get(i),
                        Constants.INQUIRY_PHASES_LIST.get(i),String.valueOf(""),
                        Constants.INQUIRY_ICON_PHASES_LIST.get(i)
                ).setOnListItemClickCallback(new NoDataCollection());
            }
        }else{
            buttonDelegator.buttonGenerator(layout,
                    Constants.INQUIRY_ID_PHASES_LIST.get(i),
                    Constants.INQUIRY_PHASES_LIST.get(i),String.valueOf(""),
                    Constants.INQUIRY_ICON_PHASES_LIST.get(i)
            ).setOnListItemClickCallback(new ClickNoGame());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onListItemClick(View v, int id) {
        Intent intent = new Intent(getApplicationContext(), InquiryActivity.class);
        intent.putExtra(InquiryActivity.PHASE, id);
        startActivity(intent);
    }

    @Override
    public boolean setOnLongClickListener(View v) {
        return false;
    }

    private class ClickNoGame implements ViewItemClickInterface {
        @Override
        public void onListItemClick(View v, int id) {
            Toast.makeText(getApplicationContext(), "Game is not sync yet", 10).show();
        }

        @Override
        public boolean setOnLongClickListener(View v) {
            return false;
        }
    }

    private class NoDataCollection implements ViewItemClickInterface {
        @Override
        public void onListItemClick(View v, int id) {
            Toast.makeText(getApplicationContext(), "Add data collection task on IWE", 10).show();
        }

        @Override
        public boolean setOnLongClickListener(View v) {
            return false;
        }
    }

    private class ClickInviteFriend implements ViewItemClickInterface {
        @Override
        public void onListItemClick(View v, int id) {
            Toast.makeText(getApplicationContext(), "Invite friend to this inquiry", 10).show();
        }

        @Override
        public boolean setOnLongClickListener(View v) {
            return false;
        }
    }
}
