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
import android.view.*;
import android.widget.ListView;
import android.widget.Toast;
import net.wespot.pim.controller.InquiryActivity;
import net.wespot.pim.R;
import net.wespot.pim.controller.Adapters.InquiryLazyListAdapter;
import net.wespot.pim.utils.layout.RefreshListFragment;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.delegators.ARL;

/**
 * A fragment that launches other parts of the demo application.
 */
public class PimInquiriesFragment extends RefreshListFragment {

    private InquiryLazyListAdapter adapterInq;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        INQ.init(getActivity());
        INQ.inquiry.syncInquiries();
        INQ.inquiry.syncHypothesis(151l);

        ARL.init(getActivity());
        ARL.properties.setAuthToken("ya29.1.AADtN_Wk3DnTkoP7u1l-BxvWjDeqVgQF6HCjj13GYi9xLk-SUXbdVQ4nPn7hiamhwgzskw");
        ARL.properties.setFullId("2:117769871710404943583");
        ARL.responses.syncResponses(19806001l);


        adapterInq =  new InquiryLazyListAdapter(this.getActivity());
        setListAdapter(adapterInq);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.setGroupVisible(R.id.actions_inquiries_list, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Toast.makeText(getActivity(), "New inquiry initialized", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), InquiryActivity.class);
                // TODO  change this for a new InquiryLocalObject or provide a way to create that on the API
                INQ.inquiry.setCurrentInquiry(null);
//                intent.putExtra(InquiryActivity.INQUIRY_ID, 0);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        adapterInq.close();
        super.onDestroy();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), InquiryActivity.class);
        INQ.inquiry.setCurrentInquiry(INQ.inquiry.getInquiryLocalObject(adapterInq.getItemId(position)));
//        intent.putExtra(InquiryActivity.INQUIRY_ID, adapterInq.getItemId(position));
        startActivity(intent);
    }
}