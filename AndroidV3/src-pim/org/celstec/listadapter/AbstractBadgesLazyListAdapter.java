package org.celstec.listadapter;

import android.content.Context;
import daoBase.DaoConfiguration;
import de.greenrobot.dao.query.QueryBuilder;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.listadapter.LazyListAdapter;
import org.celstec.events.BadgeEvent;
import org.celstec.dao.gen.BadgeLocalObject;
import org.celstec.dao.gen.BadgeLocalObjectDao;

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
public abstract class AbstractBadgesLazyListAdapter extends LazyListAdapter<BadgeLocalObject> {

    private QueryBuilder qb;

    public AbstractBadgesLazyListAdapter(Context context) {
        super(context);
        BadgeLocalObjectDao dao = DaoConfiguration.getInstance().getSession().getBadgeLocalObjectDao();
        qb = dao.queryBuilder().orderAsc(BadgeLocalObjectDao.Properties.Title);
        ARL.eventBus.register(this);
        setLazyList(qb.listLazy());
    }


    public void onEventMainThread(BadgeEvent event) {
        if (lazyList != null) lazyList.close();
        setLazyList(qb.listLazy());
        notifyDataSetChanged();
    }

    public void close() {
        if (lazyList != null)lazyList.close();
        ARL.eventBus.unregister(this);
    }

    @Override
    public long getItemId(int position) {
        if (dataValid && lazyList != null) {
            BadgeLocalObject item = lazyList.get(position);
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