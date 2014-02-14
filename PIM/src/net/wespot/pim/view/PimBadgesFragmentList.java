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
import daoBase.DaoConfiguration;
import de.greenrobot.dao.query.LazyList;
import net.wespot.pim.controller.Adapters.BadgesLazyListAdapter;
import net.wespot.pim.utils.layout.RefreshListFragment;
import org.celstec.arlearn.delegators.INQ;

/**
 * A fragment that launches other parts of the demo application.
 */
public class PimBadgesFragmentList extends RefreshListFragment {

    //TODO don't hardcode credentials
    private String accountLocalId = "116743449349920850150";
    private int accountType = 2;

    LazyList lazyList;
    private BadgesLazyListAdapter adapterInq;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        INQ.init(getActivity());
        INQ.badges.syncBadges(accountType,accountLocalId);

        DaoConfiguration daoConfiguration= DaoConfiguration.getInstance(this.getActivity());
        adapterInq =  new BadgesLazyListAdapter(this.getActivity());

        setListAdapter(adapterInq);
    }

    @Override
    public void onDestroy() {
        adapterInq.close();
        super.onDestroy();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }

}