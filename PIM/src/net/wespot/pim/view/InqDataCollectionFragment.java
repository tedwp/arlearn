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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import daoBase.DaoConfiguration;
import net.wespot.pim.R;
import net.wespot.pim.controller.Adapters.DataCollectionLazyListAdapter;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.events.GameEvent;
import org.celstec.arlearn2.android.events.RunEvent;
import org.celstec.dao.gen.GameLocalObject;
import org.celstec.dao.gen.InquiryLocalObject;

/**
 * Fragment to display Data Collection Task (General Item) from an Inquiry (Game)
 */
public class InqDataCollectionFragment extends Fragment {

    private static final String TAG = "InqDataCollectionFragment";
    private ListView data_collection_tasks;
    private InquiryLocalObject inquiry;

    private DataCollectionLazyListAdapter datAdapter;

    public InqDataCollectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);


//        INQ.runs.syncRunsParticipate();
//        INQ.games.syncGame(INQ.inquiry.getCurrentInquiry().getRunLocalObject().getGameId());
//        INQ.games.syncGamesParticipate();

        // if game is created proceed
        // if not wait till is created

        // Avoiding NULL exceptions when resuming the PIM
        if (INQ.inquiry == null){
            INQ.init(getActivity());
            INQ.inquiry.syncInquiries();
            Log.e(TAG, "recover INQ.inquiry is needed.");
        }


//        GameLocalObject gameObject = INQ.inquiry.getCurrentInquiry().getRunLocalObject().getGameLocalObject();
//
//        INQ.generalItems.syncGeneralItems(gameObject);

        INQ.inquiry.syncDataCollectionTasks();


//        GameLocalObject gameLocalObject = DaoConfiguration.getInstance().getGameLocalObjectDao().load(INQ.inquiry.getCurrentInquiry().getRunLocalObject().getGameLocalObject().getId());
//
//        gameLocalObject.getGeneralItems();

        View rootView = inflater.inflate(R.layout.fragment_data_collection, container, false);

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

    private class onListDataCollectionTasksClick implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), InqDataCollectionTaskFragment.class);
            intent.putExtra("DataCollectionTask", datAdapter.getItem(i).getId());
            startActivity(intent);
        }
    }
}
