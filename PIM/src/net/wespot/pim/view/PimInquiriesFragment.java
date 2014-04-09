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
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ListView;
import android.widget.Toast;
import net.wespot.pim.R;
import net.wespot.pim.controller.Adapters.InquiryLazyListAdapter;
import net.wespot.pim.controller.InquiryActivity;
import net.wespot.pim.controller.InquiryPhasesActivity;
import net.wespot.pim.utils.layout.ButtonEntryDelegator;
import net.wespot.pim.utils.layout._ActBar_FragmentActivity;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.listadapter.ListItemClickInterface;
import org.celstec.dao.gen.InquiryLocalObject;

/**
 * A fragment that launches other parts of the demo application.
 */
public class PimInquiriesFragment extends _ActBar_FragmentActivity  implements ListItemClickInterface<InquiryLocalObject> {

    private static final String TAG = "PimInquiriesFragment";
    private InquiryLazyListAdapter adapterInq;

    private ListView inquiries;
    private View new_inquiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            INQ.init(this);
            INQ.accounts.syncMyAccountDetails();
            INQ.inquiry.syncInquiries();
        }

        setContentView(R.layout.fragment_inquiries);

        inquiries = (ListView) findViewById(R.id.list_inquiries);
        new_inquiry = (View) findViewById(R.id.inquiries_new_inquiry);

        setTitle(R.string.common_title);

        adapterInq =  new InquiryLazyListAdapter(this);
        inquiries.setAdapter(adapterInq);
        adapterInq.setOnListItemClickCallback(this);

        // This is needed to set the class
        ButtonEntryDelegator button_manager = ButtonEntryDelegator.getInstance(this);

        // Creation of the links
        new_inquiry =button_manager._button_list(R.id.inquiries_new_inquiry, getString(R.string.inquiry_title_new), R.drawable.ic_add_inquiry, InquiryActivity.class, true);
        new_inquiry.setOnClickListener(new OnClickNewInquiry());

        setTitle(R.string.common_title);

    }

    @Override
    public void onDestroy() {
        adapterInq.close();
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

    private class OnClickNewInquiry implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.e(TAG, "New inquiry");
            INQ.inquiry.setCurrentInquiry(null);
            Intent intent = new Intent(getApplicationContext(), InquiryActivity.class);
            startActivity(intent);
        }
    }

}