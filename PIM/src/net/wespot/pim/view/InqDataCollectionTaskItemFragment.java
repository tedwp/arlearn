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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import net.wespot.pim.R;
import net.wespot.pim.controller.Adapters.DataCollectionLazyListAdapter;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.events.GameEvent;
import org.celstec.arlearn2.android.events.RunEvent;
import org.celstec.dao.gen.GameLocalObject;
import org.celstec.dao.gen.InquiryLocalObject;

/**
 * A dummy fragment representing a section of the app, but that simply displays dummy text.
 */
public class InqDataCollectionTaskItemFragment extends Fragment {

    private static final String TAG = "InqDataCollectionTaskItemFragment";
    private ListView data_collection_tasks;
    private InquiryLocalObject inquiry;

    private DataCollectionLazyListAdapter datAdapter;

    public InqDataCollectionTaskItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        INQ.inquiry.syncDataCollectionTasks();

//        INQ.runs.syncRunsParticipate();
//        INQ.games.syncGame(INQ.inquiry.getCurrentInquiry().getRunLocalObject().getGameId());
//        INQ.games.syncGamesParticipate();

        // if game is created proceed
        // if not wait till is created

        GameLocalObject gameObject = INQ.inquiry.getCurrentInquiry().getRunLocalObject().getGameLocalObject();

        INQ.generalItems.syncGeneralItems(gameObject);

        View rootView = inflater.inflate(R.layout.fragment_data_collection_task, container, false);

        data_collection_tasks = (ListView) rootView.findViewById(R.id.data_collection_tasks);
        datAdapter =  new DataCollectionLazyListAdapter(this.getActivity());
        data_collection_tasks.setOnItemClickListener(new onListDataCollectionTasksClick());
        data_collection_tasks.setAdapter(datAdapter);

        return rootView;
    }

    private void onEventAsync(RunEvent g) {
        // Run synchronized
        Log.e(TAG, "Runs loaded OK");
    }

    private void onEventAsync(GameEvent g) {
        // Game synchronized
        Log.e(TAG, "Games loaded OK");
    }

    private class onListDataCollectionTasksClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = fragmentManager.beginTransaction();
            PimInquiriesFragment fragment = new PimInquiriesFragment();
            fragmentTransaction.add(R.id.content, fragment);
            fragmentTransaction.commit();
        }
    }
}
