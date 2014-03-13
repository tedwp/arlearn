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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import net.wespot.pim.R;
import net.wespot.pim.controller.Adapters.InquiryLazyListAdapter;
import net.wespot.pim.controller.InquiryActivity;
import net.wespot.pim.controller.InquiryPhasesActivity;
import net.wespot.pim.controller.WrapperActivity;
import net.wespot.pim.utils.layout.ButtonEntry;
import net.wespot.pim.utils.layout.ButtonEntryDelegator;
import org.celstec.arlearn.delegators.INQ;

/**
 * A fragment that launches other parts of the demo application.
 */
public class PimInquiriesFragment extends Fragment {

    private static final String TAG = "PimInquiriesFragment";
    private InquiryLazyListAdapter adapterInq;

    private ListView inquiries;
    private View new_inquiry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_inquiries, container, false);

//        // This is needed to set the class
//        ButtonEntryDelegator man = ButtonEntryDelegator.getInstance(getActivity());
//
//        // Creation of the links
//        man._button_list(new_inquiry, R.id.inquiries_new_inquiry, getResources().getString(R.string.inquiry_title_new), WrapperActivity.class, null, null);


        ButtonEntry button_new_inquiry = (ButtonEntry) getActivity().getSupportFragmentManager().findFragmentById(R.id.inquiries_new_inquiry);
        button_new_inquiry.setName(getString(R.string.inquiry_title_new));


        inquiries = (ListView) rootview.findViewById(R.id.list_inquiries);
        new_inquiry = (View) rootview.findViewById(R.id.inquiries_new_inquiry);

        getActivity().setTitle(R.string.inquiry_title);

        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

//        INQ.inquiry.syncInquiries();

//        ARL.runs.syncRunsParticipate();
//        ARL.responses.syncResponses(19806001l);

        adapterInq =  new InquiryLazyListAdapter(this.getActivity());
        inquiries.setAdapter(adapterInq);
        inquiries.setOnItemClickListener(new onListInquiryClick());

        new_inquiry.setOnClickListener(new OnClickNewInquiry());
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
                Toast.makeText(getActivity(), "Save new inquiry initialized", Toast.LENGTH_SHORT).show();
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

    private class onListInquiryClick implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // This links to the PhasesActivities but then go back to the InquiryActivity.class, as it has been done for the
            // new inquiry. Then in new inquiry depends on the INQ.inquiry object we go to create a new one o visualise the
            // current inquiry
            Intent intent = new Intent(getActivity(), InquiryPhasesActivity.class);
            INQ.inquiry.setCurrentInquiry(INQ.inquiry.getInquiryLocalObject(adapterInq.getItemId(i)));
            startActivity(intent);
        }
    }

    private class OnClickNewInquiry implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.e(TAG, "New inquiry");
            INQ.inquiry.setCurrentInquiry(null);
            Intent intent = new Intent(getActivity(), InquiryActivity.class);
            startActivity(intent);
        }
    }
}