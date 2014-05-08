package net.wespot.pim.view;

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
 * Contributors: Angel Suarez
 * ****************************************************************************
 */

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import net.wespot.pim.R;
import net.wespot.pim.controller.Adapters.InquiryLazyListAdapter;
import net.wespot.pim.controller.InquiryActivity;
import net.wespot.pim.controller.InquiryActivityBack;
import net.wespot.pim.controller.InquiryPhasesActivity;
import net.wespot.pim.utils.layout.ButtonDelegator;
import net.wespot.pim.utils.layout.ButtonEntry;
import net.wespot.pim.utils.layout.ViewItemClickInterface;
import net.wespot.pim.utils.layout._ActBar_FragmentActivity;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.listadapter.ListItemClickInterface;
import org.celstec.dao.gen.InquiryLocalObject;
import org.celstec.events.InquiryEvent;

/**
 * A fragment that launches other parts of the demo application.
 */
public class PimInquiriesFragment extends _ActBar_FragmentActivity  implements ListItemClickInterface<InquiryLocalObject>, ViewItemClickInterface {

    private static final String TAG = "PimInquiriesFragment";
    private InquiryLazyListAdapter adapterInq;

    private ListView inquiries;
    private View new_inquiry;
    private static final int NEW_INQUIRY = 12350;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARL.eventBus.register(this);
        if (savedInstanceState != null) {
            INQ.init(this);
            INQ.accounts.syncMyAccountDetails();
        }

        INQ.inquiry.syncInquiries();

        setContentView(R.layout.fragment_inquiries);

        inquiries = (ListView) findViewById(R.id.list_inquiries);

        adapterInq =  new InquiryLazyListAdapter(this);
        inquiries.setAdapter(adapterInq);
        adapterInq.setOnListItemClickCallback(this);

        LinearLayout new_inquiry = (LinearLayout) findViewById(R.id.inquiries_new_inquiry);
        ButtonDelegator buttonDelegator =  ButtonDelegator.getInstance(this);

        LinearLayout layout = buttonDelegator.layoutGenerator(R.dimen.mainscreen_margintop_zero);
        buttonDelegator.buttonGenerator(layout, NEW_INQUIRY, getResources().getString(R.string.inquiry_title_new), String.valueOf(""), R.drawable.ic_add_inquiry).setOnListItemClickCallback(this);
        new_inquiry.addView(layout);

        setTitle(R.string.common_title);
    }

    private void onEventAsync(InquiryEvent inquiryObject){
//        Toast.makeText(getApplicationContext(), "Loaded: "+DaoConfiguration.getInstance().getInquiryLocalObjectDao().loadAll().size()+" inquiries",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        adapterInq.close();
        ARL.eventBus.unregister(this);
        super.onDestroy();
    }



    @Override
    public void onListItemClick(View v, int position, InquiryLocalObject object) {
        Intent intent = new Intent(getApplicationContext(), InquiryPhasesActivity.class);
        INQ.inquiry.setCurrentInquiry(object);
        startActivity(intent);
    }

    @Override
    public boolean setOnLongClickListener(View v, int position, InquiryLocalObject object) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_inquiry, menu);

        menu.setGroupVisible(R.id.actions_general, true);
        menu.setGroupVisible(R.id.actions_wonder_moment, false);
        menu.setGroupVisible(R.id.actions_data_collection, false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
                INQ.inquiry.syncInquiries();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(View v, int id) {
        INQ.inquiry.setCurrentInquiry(null);
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            intent = new Intent(getApplicationContext(), InquiryActivity.class);
        }else{
            intent = new Intent(getApplicationContext(), InquiryActivityBack.class);
        }
        startActivity(intent);
    }

    @Override
    public boolean setOnLongClickListener(View v) {
        return false;
    }

//    private class OnClickNewInquiry implements View.OnClickListener {
//        @Override
//        public void onClick(View view) {
//            Log.e(TAG, "New inquiry");
//            INQ.inquiry.setCurrentInquiry(null);
//            Intent intent;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//                intent = new Intent(getApplicationContext(), InquiryActivity.class);
//            }else{
//                intent = new Intent(getApplicationContext(), InquiryActivityBack.class);
//            }
//            startActivity(intent);
//        }
//    }

}