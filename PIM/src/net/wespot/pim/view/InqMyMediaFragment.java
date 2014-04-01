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
import org.celstec.dao.gen.GameLocalObject;
import org.celstec.dao.gen.GeneralItemLocalObject;
import org.celstec.dao.gen.InquiryLocalObject;

/**
 * Fragment to display responses from a Data Collection Task (General Item)
 */
public class InqMyMediaFragment extends _ActBar_FragmentActivity {

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
        data_collection_tasks.setOnItemClickListener(new onListDataCollectionTasksClick());
        data_collection_tasks.setAdapter(datAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_data_collection, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.menu_data_collection:
//                Toast.makeText(this, "Display options for capture data", Toast.LENGTH_SHORT).show();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class onListDataCollectionTasksClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getApplicationContext(), ImageDetailActivity.class);
            intent.putExtra("DataCollectionTask", datAdapter.getItem(i).getId());
            intent.putExtra("DataCollectionTaskGeneralItemId", generalItemId);
            intent.putExtra(ImageDetailActivity.EXTRA_IMAGE, i);
            startActivity(intent);
        }
    }
}
