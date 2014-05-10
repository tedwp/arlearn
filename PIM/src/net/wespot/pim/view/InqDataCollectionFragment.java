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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.*;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import daoBase.DaoConfiguration;
import net.wespot.pim.R;
import net.wespot.pim.controller.Adapters.DataCollectionLazyListAdapter;
import net.wespot.pim.utils.layout.NoticeDialogFragment;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.listadapter.ListItemClickInterface;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.dao.gen.GameLocalObject;
import org.celstec.dao.gen.GeneralItemLocalObject;
import org.celstec.dao.gen.InquiryLocalObject;

/**
 * Fragment to display Data Collection Task (General Item) from an Inquiry (Game)
 */
public class InqDataCollectionFragment extends Fragment implements ListItemClickInterface<GeneralItemLocalObject>{

    private static final String TAG = "InqDataCollectionFragment";
    private static final int DIALOG_FRAGMENT = 0;
    private ListView data_collection_tasks;
    private TextView text_default;
    private TextView data_collection_tasks_title_list;
    private InquiryLocalObject inquiry;

    private DataCollectionLazyListAdapter datAdapter;

    public InqDataCollectionFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("currentInquiry", INQ.inquiry.getCurrentInquiry().getId());
        if(INQ.inquiry.getCurrentInquiry().getRunLocalObject()!=null){
            outState.putLong("currentInquiryRunLocalObject", INQ.inquiry.getCurrentInquiry().getRunLocalObject().getId());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARL.eventBus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

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
    public void onActivityCreated(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_inquiry, menu);

        menu.setGroupVisible(R.id.actions_general, false);
        menu.setGroupVisible(R.id.actions_wonder_moment, false);
        menu.setGroupVisible(R.id.actions_data_collection, true);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new_data_collection:

                showDialog();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment dialog = new NoticeDialogFragment();
        dialog.setTargetFragment(this, DIALOG_FRAGMENT);
        dialog.show(getFragmentManager().beginTransaction(), "dialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case DIALOG_FRAGMENT:

                if (resultCode == Activity.RESULT_OK) {
                    // After Ok code.
                    Log.e(TAG, "ok code");

//                    Bundle extras = data.getExtras();
//
//                    String title;
//                    Iterator a = data.getExtras().keySet().iterator();
//
//
//
//                    while (a.hasNext()){
//                        if (NoticeDialogFragment.TITLE.equals(a.next())) {
//                            title =
//                        }
//                        String p = (String) a.next();
//                    }

                    INQ.dataCollection.createDataCollectionTask(
                            INQ.inquiry.getCurrentInquiry().getRunLocalObject().getGameLocalObject().getId(),
                            "demo title" ,
                            "demo description"
                    );
                    INQ.inquiry.syncDataCollectionTasks();

                    Toast.makeText(getActivity(), getResources().getString(R.string.data_collection_dialog_creating), Toast.LENGTH_SHORT).show();
                } else if (resultCode == Activity.RESULT_CANCELED){
                    // After Cancel code.
                    Log.e(TAG, "cancel code");
                }
                break;
        }
    }

    private class CreateGeneralItem {
        public GeneralItem generalItem;
    }

    private void onEventBackgroundThread(CreateGeneralItem generalItem){
        Log.e(TAG, "Creation data collection task done ");
        INQ.inquiry.syncDataCollectionTasks();

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ARL.eventBus.unregister(this);
    }

}
