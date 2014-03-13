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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ListView;
import net.wespot.pim.R;
import net.wespot.pim.controller.Adapters.InquiryLazyListAdapter;
import net.wespot.pim.controller.WrapperActivity;
import net.wespot.pim.utils.layout.ButtonEntryDelegator;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.delegators.ARL;

/**
 * A fragment that launches other parts of the demo application.
 */
public class PimFriendsFragment extends Fragment {

    private static final String TAG = "PimInquiriesFragment";
    private InquiryLazyListAdapter adapterInq;

    private ListView friends;
    private View add_friend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_friends, container, false);

        // This is needed to set the class
        ButtonEntryDelegator man = ButtonEntryDelegator.getInstance(getActivity());

        // Creation of the links
        man._button_list(add_friend, R.id.friends_add_friend, getResources().getString(R.string.friends_friend_new), null, null, null);


//        _EntryListTemp button_new_inquiry = (_EntryListTemp) getActivity().getSupportFragmentManager().findFragmentById(R.id.friends_add_friend);
//        button_new_inquiry.setName(getString(R.string.friends_friend_new));

        friends = (ListView) rootview.findViewById(R.id.list_friends);
        add_friend = (View) rootview.findViewById(R.id.friends_add_friend);

        getActivity().setTitle(R.string.inquiry_title);

        return rootview;
    }

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
        friends.setAdapter(adapterInq);
//        friends.setOnItemClickListener(new onListInquiryClick());
//
//        add_friend.setOnClickListener(new OnClickNewInquiry());
    }




    @Override
    public void onDestroy() {
        adapterInq.close();
        super.onDestroy();
    }


}