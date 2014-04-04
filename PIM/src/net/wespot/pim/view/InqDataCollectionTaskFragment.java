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
import daoBase.DaoConfiguration;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import net.wespot.pim.R;
import net.wespot.pim.controller.Adapters.ResponsesLazyListAdapter;
import net.wespot.pim.controller.ImageDetailActivity;
import net.wespot.pim.utils.layout._ActBar_FragmentActivity;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.dataCollection.DataCollectionManager;
import org.celstec.arlearn2.android.dataCollection.PictureManager;
import org.celstec.arlearn2.android.dataCollection.VideoManager;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.events.ResponseEvent;
import org.celstec.arlearn2.android.listadapter.ListItemClickInterface;
import org.celstec.dao.gen.GeneralItemLocalObject;
import org.celstec.dao.gen.InquiryLocalObject;
import org.celstec.dao.gen.ResponseLocalObject;
import org.celstec.dao.gen.ResponseLocalObjectDao;

import java.io.File;

/**
 * Fragment to display responses from a Data Collection Task (General Item)
 */
public class InqDataCollectionTaskFragment extends _ActBar_FragmentActivity implements ListItemClickInterface<ResponseLocalObject> {

    private static final String TAG = "InqDataCollectionTaskFragment";
    private ListView data_collection_tasks_items;
    private InquiryLocalObject inquiry;
    private long generalItemId;

    private ResponsesLazyListAdapter datAdapter;
    private GeneralItemLocalObject genObject;
    private PictureManager man_pic = new PictureManager(this);
    private VideoManager man_vid = new VideoManager(this);
    private File bitmapFile;

    ResponseLocalObject response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            INQ.init(this);
            INQ.accounts.syncMyAccountDetails();
            INQ.inquiry.setCurrentInquiry(DaoConfiguration.getInstance().getInquiryLocalObjectDao().load(savedInstanceState.getLong("currentInquiry")));
        }

        setContentView(R.layout.fragment_data_collection_task);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            Log.e(TAG,extras.getLong("DataCollectionTask")+" testing");

            generalItemId = extras.getLong("DataCollectionTask");

            genObject = DaoConfiguration.getInstance().getGeneralItemLocalObjectDao().load(generalItemId);
            genObject.getResponses();

            data_collection_tasks_items = (ListView) findViewById(R.id.data_collection_tasks_items);
            //TODO change to the other constructor
            datAdapter =  new ResponsesLazyListAdapter(this, generalItemId);
//            datAdapter =  new ResponsesLazyListAdapter(this);

            datAdapter.setOnListItemClickCallback(this);
//            data_collection_tasks_items.setOnItemClickListener(new onListDataCollectionTasksClick());
            data_collection_tasks_items.setAdapter(datAdapter);

            getActionBar().setTitle(getResources().getString(R.string.actionbar_list_data_collection_task));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            case R.id.menu_data_collection_image:
//                Toast.makeText(this, "Display options for capture data", Toast.LENGTH_SHORT).show();
                man_pic.setRunId(INQ.inquiry.getCurrentInquiry().getRunId());
                man_pic.setGeneralItem(genObject);
                man_pic.takeDataSample();
                break;
            case R.id.menu_data_collection_video:
//                Toast.makeText(this, "Display options for capture data", Toast.LENGTH_SHORT).show();
                man_vid.setRunId(INQ.inquiry.getCurrentInquiry().getRunId());
                man_vid.setGeneralItem(genObject);
                man_vid.takeDataSample();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(View v, int position, ResponseLocalObject object) {
        Intent intent = new Intent(getApplicationContext(), ImageDetailActivity.class);
        intent.putExtra("DataCollectionTask", object.getId());
        intent.putExtra("DataCollectionTaskGeneralItemId", generalItemId);
        intent.putExtra(ImageDetailActivity.EXTRA_IMAGE, position);
        startActivity(intent);
    }

    @Override
    public boolean setOnLongClickListener(View v, int position, ResponseLocalObject object) {
        return false;
    }

//    private class onListDataCollectionTasksClick implements android.widget.AdapterView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            Intent intent = new Intent(getApplicationContext(), ImageDetailActivity.class);
//            intent.putExtra("DataCollectionTask", datAdapter.getItem(i).getId());
//            intent.putExtra("DataCollectionTaskGeneralItemId", generalItemId);
//            intent.putExtra(ImageDetailActivity.EXTRA_IMAGE, i);
//            startActivity(intent);
//        }
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

//        public static final int PICTURE_RESULT = 1;
//        public static final int AUDIO_RESULT = 2;
//        public static final int VIDEO_RESULT = 3;
//        public static final int TEXT_RESULT = 4;

        switch (requestCode){
            case DataCollectionManager.PICTURE_RESULT:
                man_pic.onActivityResult(requestCode, resultCode, data);
                break;
            case DataCollectionManager.AUDIO_RESULT:

                break;
            case DataCollectionManager.VIDEO_RESULT:
                man_vid.onActivityResult(requestCode, resultCode, data);
                break;
            case DataCollectionManager.TEXT_RESULT:

                break;
        }

        INQ.responses.syncResponses(INQ.inquiry.getCurrentInquiry().getRunLocalObject().getId());
    }

}
