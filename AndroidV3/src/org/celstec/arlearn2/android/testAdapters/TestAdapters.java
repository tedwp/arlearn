package org.celstec.arlearn2.android.testAdapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import daoBase.DaoConfiguration;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.events.FileDownloadStatus;
import org.celstec.arlearn2.client.GenericClient;
import org.celstec.arlearn2.client.InquiryClient;
import org.celstec.dao.gen.ActionLocalObject;
import org.celstec.dao.gen.InquiryLocalObject;

import java.text.DateFormat;
import java.util.Date;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
public class TestAdapters extends Activity {
    private String authToken = "d324aa9b75782d9b7b76372a1f9439bd";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_adapters);
        INQ.init(this);
        ARL.properties.setAuthToken(authToken);
        ARL.properties.setFullId("5:stefaan.ternier");
//        ARL.properties.setAccount(0l);
        ARL.accounts.syncMyAccountDetails();

//        DaoConfiguration daoConfiguration= DaoConfiguration.getInstance(this);
        findViewById(R.id.myGamesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestAdapters.this, MyGames.class));
            }
        });

        findViewById(R.id.allAccountsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestAdapters.this, AllAccounts.class));
            }
        });
        findViewById(R.id.allContributors).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestAdapters.this, ContributingElements.class));
            }
        });
        findViewById(R.id.myRuns).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestAdapters.this, MyRuns.class));
            }
        });
        findViewById(R.id.generalItems).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestAdapters.this, GeneralItems.class));
            }
        });

        findViewById(R.id.dataCollection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestAdapters.this, DataCollectionTestActivity.class));
            }
        });
        findViewById(R.id.dependencies).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestAdapters.this, Dependencies.class));
            }
        });
        findViewById(R.id.showThreads).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestAdapters.this, ThreadsActivity.class));
            }
        });
        ARL.eventBus.register(this);
        ARL.store.synCategories();
        ARL.store.syncGamesForCategory(3l);

        ActionLocalObject alo = new ActionLocalObject();
//        alo.setId(1l);
        alo.setAction("read");
        alo.setRunId(19806001l);
        alo.setGeneralItem(19966001l);
        alo.setTime(1230l);
        alo.setAccount(5);
        DaoConfiguration.getInstance().getActionDependencyLocalObjectDao().insertOrReplace(alo);
//for (int i =0; i<=100;i ++) {
//        alo = new ActionLocalObject();
//        alo.setAction("read");
//        alo.setRunId(19806001l);
//        alo.setGeneralItem(19766002l);
//        alo.setTime(1239l+i);
//        alo.setAccount(5);
//        DaoConfiguration.getInstance().getActionDependencyLocalObjectDao().insertOrReplace(alo);
//}
//    for (int i =0; i<=100;i ++) {
//        GameLocalObject glo = new GameLocalObject();
//        glo.setTitle("a game "+i);
//        glo.setDeleted(false);
//        DaoConfiguration.getInstance().getGameLocalObjectDao().insertOrReplace(glo);
////        DaoConfiguration.getInstance().getSession().getInquiryLocalObjectDao().insertOrReplace()
//    }



//        BadgesDelegator.getInstance().syncBadges(2, "116743449349920850150");
//


        INQ.inquiry.syncInquiries();
        INQ.inquiry.syncHypothesis(151l);
//        InquiryLocalObject object;
//        InquiryLocalObject ilo = INQ.inquiry.getInquiryLocalObject(28845l);
//        INQ.inquiry.setCurrentInquiry(ilo);
//        INQ.inquiry.syncDataCollectionTasks();
//        System.out.println(ilo);

//        InquiryLocalObject iObject = new InquiryLocalObject();
//        iObject.setDescription("Stefaans post");
//        iObject.setTitle("a title");
//        CreateInquiryObject createInquiryObject = new CreateInquiryObject();
//        createInquiryObject.inquiry = iObject;

//        String key = ARL.config.getProperty("elgg_api_key");
//        ARL.eventBus.post(createInquiryObject);
//
//        key = GenericClient.urlPrefix;

//        iObject.setIsSynchronized(false);
//        DaoConfiguration.getInstance().getSession().getInquiryLocalObjectDao().insert(iObject);
//        INQ.inquiry.syncInquiries();


//        Log.e("ARLearn", "inq loaded"+iObject.getTitle());
//        for (BadgeLocalObject badge: iObject.getBadges()) {
//            Log.e("ARLearn", "badge"+badge.getTitle());
//        }

    }
    private class CreateInquiryObject {
        public InquiryLocalObject inquiry;

    }

    private void onEventBackgroundThread(CreateInquiryObject inquiryObject){

//        InquiryClient.getInquiryClient().createInquiry(inquiryObject.inquiry, INQ.accounts.getLoggedInAccount(), InquiryClient.VIS_PUBLIC, InquiryClient.OPEN_MEMBERSHIP);



    }

    public void onEventMainThread(FileDownloadStatus event) {
        TextView editText = (TextView) findViewById(R.id.downloadStatus);
        if (event.getStatus() == FileDownloadStatus.FINISHED) {
            editText.setText("---");
        } else {
            editText.setText(event.getBytesDownloaded()+":"+event.getAmountOfBytes()+":"+event.getFileName());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(counterTask);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(counterTask, 300);
    }

    public void onDestroy(){
        super.onDestroy();
        ARL.eventBus.unregister(this);
    }

    private Handler mHandler = new Handler();

    protected Runnable counterTask = new Runnable() {
        public void run() {
            long serverTime = ARL.time.getServerTime();
            long localTime = System.currentTimeMillis();

            ((TextView) findViewById(R.id.localTimeView)).setText("localTime: "+ DateFormat.getDateTimeInstance().format(new Date(localTime)));

            ((TextView) findViewById(R.id.serverTimeView)).setText("serverTime: " + DateFormat.getDateTimeInstance().format(new Date(serverTime)) + " " + (serverTime - localTime));
            mHandler.postDelayed(counterTask, 100);
        }
    };
}
