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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;
import net.wespot.pim.R;
import net.wespot.pim.controller.Adapters.DataCollectionLazyListAdapter;
import net.wespot.pim.controller.Adapters.InquiryLazyListAdapter;
import net.wespot.pim.utils.Constants;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.client.GameClient;
import org.celstec.dao.gen.GameLocalObject;
import org.celstec.dao.gen.InquiryLocalObject;

/**
 * A dummy fragment representing a section of the app, but that simply displays dummy text.
 */
public class InqDataTaskFragment extends Fragment {

    private ListView data_collection_tasks;
    private InquiryLocalObject inquiry;

    private DataCollectionLazyListAdapter datAdapter;

    public InqDataTaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        INQ.runs.syncRunsParticipate();
        INQ.games.syncGame(INQ.inquiry.getCurrentInquiry().getRunLocalObject().getGameId());
        INQ.games.syncGamesParticipate();

        // if game is created proceed
        // if not wait till is created



        GameLocalObject gameObject = INQ.inquiry.getCurrentInquiry().getRunLocalObject().getGameLocalObject();


        INQ.generalItems.syncGeneralItems(gameObject);

        View rootView = inflater.inflate(R.layout.fragment_data_collection_task, container, false);

        data_collection_tasks = (ListView) rootView.findViewById(R.id.data_collection_tasks);
        datAdapter =  new DataCollectionLazyListAdapter(this.getActivity());
        data_collection_tasks.setAdapter(datAdapter);

        return rootView;
    }

//    private void onEventAsync(SyncGameContributors sge) {
//
//    }

}
