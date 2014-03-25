package org.celstec.arlearn2.android.testAdapters;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.listadapter.ListItemClickInterface;
import org.celstec.arlearn2.android.listadapter.impl.ThreadsLazyListAdapter;
import org.celstec.dao.gen.ThreadLocalObject;

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
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
public class ThreadsActivity extends ListActivity implements ListItemClickInterface<ThreadLocalObject>{
    private ThreadsLazyListAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_list);
        ARL.threads.syncThreads(5908010983489536l);
        adapter = new ThreadsLazyListAdapter(this);
        adapter.setOnListItemClickCallback(this);
        setListAdapter(adapter);
    }



    public void onDestroy(){
        adapter.close();
        super.onDestroy();
    }

    @Override
    public void onListItemClick(View v, int position, ThreadLocalObject object) {


        Intent i = null;
        i = new Intent(ThreadsActivity.this, MessagesActivity.class);
        i.putExtra("threadId", object.getId());
        this.startActivity(i);

    }

    @Override
    public boolean setOnLongClickListener(View v, int position, ThreadLocalObject object) {
        return false;
    }
}
