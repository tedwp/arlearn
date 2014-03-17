package org.celstec.arlearn2.android.listadapter;

import android.content.Context;
import daoBase.DaoConfiguration;
import de.greenrobot.dao.query.QueryBuilder;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.events.ResponseEvent;
import org.celstec.dao.gen.ResponseLocalObject;
import org.celstec.dao.gen.ResponseLocalObjectDao;

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
public abstract class AbstractResponsesLazyListAdapter extends LazyListAdapter<ResponseLocalObject> {

    private QueryBuilder qb;

    public AbstractResponsesLazyListAdapter(Context context) {
        super(context);
        ResponseLocalObjectDao dao = DaoConfiguration.getInstance().getResponseLocalObjectDao();
        qb = dao.queryBuilder().orderAsc(ResponseLocalObjectDao.Properties.TimeStamp);
        ARL.eventBus.register(this);
        setLazyList(qb.listLazy());
    }

    public AbstractResponsesLazyListAdapter(Context context, long generalItemId) {
        super(context);
        ResponseLocalObjectDao dao = DaoConfiguration.getInstance().getResponseLocalObjectDao();
        qb = dao.queryBuilder()
                .orderAsc(ResponseLocalObjectDao.Properties.TimeStamp)
                .where(ResponseLocalObjectDao.Properties.GeneralItem.eq(generalItemId));
        ARL.eventBus.register(this);
        setLazyList(qb.listLazy());
    }

    public void onEventMainThread(ResponseEvent event) {
        if (lazyList != null) lazyList.close();
        setLazyList(qb.listLazy());
        notifyDataSetChanged();
    }

    public void close() {
        if (lazyList != null)lazyList.close();
        ARL.eventBus.unregister(this);
    }
}