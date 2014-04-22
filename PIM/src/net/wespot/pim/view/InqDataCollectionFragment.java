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
import android.widget.TextView;
import android.widget.Toast;
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
    private TextView text_default;
    private TextView data_collection_tasks_title_list;
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

        View rootView = inflater.inflate(R.layout.fragment_data_collection, container, false);
        data_collection_tasks = (ListView) rootView.findViewById(R.id.data_collection_tasks);
        text_default = (TextView) rootView.findViewById(R.id.text_default);
        data_collection_tasks_title_list = (TextView) rootView.findViewById(R.id.data_collection_tasks_title_list);

        if(INQ.inquiry.getCurrentInquiry().getRunLocalObject()!=null){
            if (INQ.inquiry.getCurrentInquiry().getRunLocalObject().getGameLocalObject()!=null){
                GameLocalObject gameLocalObject = DaoConfiguration.getInstance().getGameLocalObjectDao().load(INQ.inquiry.getCurrentInquiry().getRunLocalObject().getGameLocalObject().getId());

                if (gameLocalObject.getGeneralItems().size() != 0){
                    INQ.responses.syncResponses(INQ.inquiry.getCurrentInquiry().getRunLocalObject().getId());
                    data_collection_tasks = (ListView) rootView.findViewById(R.id.data_collection_tasks);
                    datAdapter =  new DataCollectionLazyListAdapter(this.getActivity(),gameLocalObject.getId());
                    datAdapter.setOnListItemClickCallback(this);
                    data_collection_tasks.setAdapter(datAdapter);
                }else{
                    Log.e(TAG, "There are no data collection tasks for this inquiry");
                    data_collection_tasks_title_list.setVisibility(View.INVISIBLE);
                    text_default.setVisibility(View.VISIBLE);
                    text_default.setText(R.string.data_collection_task_no_created);
                }
            }else{
                Log.e(TAG, "There is no game for this run.");
            }
        }else{
            Log.e(TAG, "Data collection task are not enabled on this inquiry");
            data_collection_tasks_title_list.setVisibility(View.INVISIBLE);
            text_default.setVisibility(View.VISIBLE);
            text_default.setText(R.string.data_collection_task_no_enabled_created);

        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        INQ.inquiry.syncDataCollectionTasks();
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
}
