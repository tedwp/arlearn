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
import android.view.View;
import android.widget.ListView;
import net.wespot.pim.R;
import net.wespot.pim.controller.Adapters.InquiryLazyListAdapter;
import net.wespot.pim.utils.layout._ActBar_FragmentActivity;
import net.wespot.pim.utils.layout.ButtonEntryDelegator;

/**
 * A fragment that launches other parts of the demo application.
 */
public class PimFriendsFragment extends _ActBar_FragmentActivity {

    private static final String TAG = "PimInquiriesFragment";
    private InquiryLazyListAdapter adapterInq;

    private ListView friends;
    private View add_friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_friends);

        // This is needed to set the class
        ButtonEntryDelegator man = ButtonEntryDelegator.getInstance(this);

        // Creation of the links
        add_friend = man._button_list(R.id.friends_add_friend, getResources().getString(R.string.friends_friend_new), R.drawable.ic_invite_friend, null, false);

        friends = (ListView) findViewById(R.id.list_friends);
        add_friend = (View) findViewById(R.id.friends_add_friend);

        setTitle(R.string.common_title);

        adapterInq =  new InquiryLazyListAdapter(this);
        friends.setAdapter(adapterInq);
    }

    @Override
    public void onDestroy() {
        adapterInq.close();
        super.onDestroy();
    }
}