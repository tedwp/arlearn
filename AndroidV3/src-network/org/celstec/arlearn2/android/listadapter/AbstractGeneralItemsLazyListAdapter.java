package org.celstec.arlearn2.android.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import daoBase.DaoConfiguration;
import de.greenrobot.dao.query.QueryBuilder;

import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.events.GeneralItemEvent;
import org.celstec.dao.gen.GeneralItemLocalObject;
import org.celstec.dao.gen.GeneralItemLocalObjectDao;

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
public abstract class AbstractGeneralItemsLazyListAdapter extends LazyListAdapter<GeneralItemLocalObject> {

    private QueryBuilder qb;
    private AbstractGeneralItemsLazyListAdapter adapterInq;

    public AbstractGeneralItemsLazyListAdapter(Context context) {
        super(context);
        GeneralItemLocalObjectDao dao = DaoConfiguration.getInstance().getGeneralItemLocalObjectDao();
        qb = dao.queryBuilder().orderAsc(GeneralItemLocalObjectDao.Properties.LastModificationDate);
        ARL.eventBus.register(this);
        setLazyList(qb.listLazy());
    }

    public AbstractGeneralItemsLazyListAdapter(Context context, long gameId) {
        super(context);
        GeneralItemLocalObjectDao dao = DaoConfiguration.getInstance().getGeneralItemLocalObjectDao();
        qb = dao.queryBuilder()
                .where(GeneralItemLocalObjectDao.Properties.GameId.eq(gameId))
                .orderAsc(GeneralItemLocalObjectDao.Properties.LastModificationDate);
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
}
