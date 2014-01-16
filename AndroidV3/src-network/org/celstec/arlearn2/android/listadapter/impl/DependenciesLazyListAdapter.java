package org.celstec.arlearn2.android.listadapter.impl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import daoBase.DaoConfiguration;
import de.greenrobot.dao.query.QueryBuilder;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.events.GeneralItemEvent;
import org.celstec.arlearn2.android.listadapter.LazyListAdapter;
import org.celstec.dao.gen.DependencyLocalObject;
import org.celstec.dao.gen.DependencyLocalObjectDao;

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
public class DependenciesLazyListAdapter extends LazyListAdapter<DependencyLocalObject> {

    private QueryBuilder qb;
    private GeneralItemsLazyListAdapter adapterInq;

    public DependenciesLazyListAdapter(Context context) {
        super(context);
        DependencyLocalObjectDao dao = DaoConfiguration.getInstance().getDependencyLocalObjectDao();
        qb = dao.queryBuilder().orderAsc(DependencyLocalObjectDao.Properties.Id);
        ARL.eventBus.register(this);
        setLazyList(qb.listLazy());
    }

    public void onEventMainThread(GeneralItemEvent event) {
        if (lazyList != null) lazyList.close();
        setLazyList(qb.listLazy());
        notifyDataSetChanged();
    }

    public void close() {
        if (lazyList != null)lazyList.close();
        ARL.eventBus.unregister(this);
    }

    @Override
    public View newView(Context context, DependencyLocalObject item, ViewGroup parent) {
        if (item == null) return null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.list_game_row, parent, false);

    }
    @Override
    public void bindView(View view, Context context,  DependencyLocalObject item) {
        TextView firstLineView =(TextView) view.findViewById(R.id.firstLine);
        firstLineView.setText(item.getClass().toString()+item.toString() );
        TextView secondLineView =(TextView) view.findViewById(R.id.secondLine);
    }


    @Override
    public long getItemId(int position) {
        if (dataValid && lazyList != null) {
            DependencyLocalObject item = lazyList.get(position);
            if (item != null) {
                return item.getId();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

}

