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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import daoBase.DaoConfiguration;
import net.wespot.pim.R;
import net.wespot.pim.controller.Adapters.DataCollectionLazyListAdapter;
import net.wespot.pim.controller.Adapters.ResponsesLazyListAdapter;
import net.wespot.pim.controller.ImageDetailActivity;
import net.wespot.pim.utils.layout._ActBar_FragmentActivity;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.listadapter.ListItemClickInterface;
import org.celstec.dao.gen.BadgeLocalObject;
import org.celstec.dao.gen.GameLocalObject;
import org.celstec.dao.gen.GeneralItemLocalObject;
import org.celstec.dao.gen.InquiryLocalObject;

/**
 * Fragment to display responses from a Data Collection Task (General Item)
 */
public class InqMyMediaFragment extends _ActBar_FragmentActivity implements ListItemClickInterface<GeneralItemLocalObject> {

    private static final String TAG = "InqDataCollectionTaskFragment";
    private ListView data_collection_tasks;
    private InquiryLocalObject inquiry;
    private long generalItemId;

    private DataCollectionLazyListAdapter datAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_data_collection);

        INQ.games.syncGamesParticipate();
        INQ.runs.syncRunsParticipate();

        data_collection_tasks = (ListView) findViewById(R.id.data_collection_tasks);
        datAdapter =  new DataCollectionLazyListAdapter(getApplicationContext());
        datAdapter.setOnListItemClickCallback(this);
        data_collection_tasks.setAdapter(datAdapter);
    }


    @Override
    public void onListItemClick(View v, int position, GeneralItemLocalObject object) {
        Intent intent = new Intent(getApplicationContext(), InqMyMediaDataCollectionTaskFragment.class);
        intent.putExtra("DataCollectionTask", object.getId());
        startActivity(intent);
    }

    @Override
    public boolean setOnLongClickListener(View v, int position, GeneralItemLocalObject object) {
        return false;
    }
}
