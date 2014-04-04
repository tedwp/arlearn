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
import net.wespot.pim.SplashActivity;
import net.wespot.pim.controller.Adapters.DataCollectionLazyListAdapter;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.events.GameEvent;
import org.celstec.arlearn2.android.events.RunEvent;
import org.celstec.arlearn2.android.listadapter.ListItemClickInterface;
import org.celstec.dao.gen.GameLocalObject;
import org.celstec.dao.gen.GeneralItemLocalObject;
import org.celstec.dao.gen.InquiryLocalObject;

/**
 * Fragment to display Data Collection Task (General Item) from an Inquiry (Game)
 */
public class InqDataCollectionFragment extends Fragment implements ListItemClickInterface<GeneralItemLocalObject> {

    private static final String TAG = "InqDataCollectionFragment";
    private ListView data_collection_tasks;
    private InquiryLocalObject inquiry;

    private DataCollectionLazyListAdapter datAdapter;

    public InqDataCollectionFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        if (savedInstanceState != null) {
            INQ.init(getActivity());
            INQ.accounts.syncMyAccountDetails();
            INQ.inquiry.setCurrentInquiry(DaoConfiguration.getInstance().getInquiryLocalObjectDao().load(savedInstanceState.getLong("currentInquiry")));
        }

        INQ.inquiry.syncDataCollectionTasks();

        View rootView = inflater.inflate(R.layout.fragment_data_collection, container, false);

        if(INQ.inquiry.getCurrentInquiry().getRunLocalObject()!=null){
            if (INQ.inquiry.getCurrentInquiry().getRunLocalObject().getGameLocalObject()!=null){
                GameLocalObject gameLocalObject = DaoConfiguration.getInstance().getGameLocalObjectDao().load(INQ.inquiry.getCurrentInquiry().getRunLocalObject().getGameLocalObject().getId());

                if (gameLocalObject.getGeneralItems().size() != 0){
                    INQ.responses.syncResponses(INQ.inquiry.getCurrentInquiry().getRunLocalObject().getId());
                    data_collection_tasks = (ListView) rootView.findViewById(R.id.data_collection_tasks);
                    datAdapter =  new DataCollectionLazyListAdapter(this.getActivity(),gameLocalObject.getId());
//                    data_collection_tasks.setOnItemClickListener(new onListDataCollectionTasksClick());
                    datAdapter.setOnListItemClickCallback(this);
                    data_collection_tasks.setAdapter(datAdapter);
                }else{
                    Log.e(TAG, "There are no data collection tasks for this inquiry");
                }
            }else{
                Log.e(TAG, "There is no game for this run.");
            }
        }else{
            Log.e(TAG, "Data collection task are not enabled on this inquiry");
        }
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

    @Override
    public void onListItemClick(View v, int position, GeneralItemLocalObject object) {
        Intent intent = new Intent(getActivity(), InqDataCollectionTaskFragment.class);
        intent.putExtra("DataCollectionTask", object.getId());
        startActivity(intent);
    }

    @Override
    public boolean setOnLongClickListener(View v, int position, GeneralItemLocalObject object) {
        return false;
    }

//    private class onListDataCollectionTasksClick implements android.widget.AdapterView.OnItemClickListener {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//    }
}
