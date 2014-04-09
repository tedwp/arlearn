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
import net.wespot.pim.controller.Adapters.BadgesLazyListAdapter;
import net.wespot.pim.controller.InquiryActivity;
import net.wespot.pim.utils.layout.ButtonEntryDelegator;
import net.wespot.pim.utils.layout._ActBar_FragmentActivity;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.listadapter.ListItemClickInterface;
import org.celstec.dao.gen.BadgeLocalObject;
import org.celstec.dao.gen.InquiryLocalObject;

/**
 * A fragment that launches other parts of the demo application.
 */
public class PimBadgesFragment extends _ActBar_FragmentActivity implements ListItemClickInterface<BadgeLocalObject> {

    private static final String TAG = "PimBadgesFragment";
    private BadgesLazyListAdapter adapterInq;

    private ListView badges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_badges);

        badges = (ListView) findViewById(R.id.list_badges);

//        setTitle(R.string.badges_title_list);
        setTitle(R.string.common_title);

        INQ.init(this);
        INQ.badges.syncBadges();

        adapterInq =  new BadgesLazyListAdapter(this);
        badges.setAdapter(adapterInq);
        adapterInq.setOnListItemClickCallback(this);

    }

    @Override
    public void onListItemClick(View v, int position, BadgeLocalObject object) {

    }

    @Override
    public boolean setOnLongClickListener(View v, int position, BadgeLocalObject object) {
        return false;
    }

    @Override
    public void onDestroy() {
        adapterInq.close();
        super.onDestroy();
    }
}