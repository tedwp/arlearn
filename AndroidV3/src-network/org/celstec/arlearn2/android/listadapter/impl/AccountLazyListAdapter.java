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
import org.celstec.arlearn2.android.events.GameEvent;
import org.celstec.arlearn2.android.listadapter.LazyListAdapter;
import org.celstec.dao.gen.AccountLocalObject;
import org.celstec.dao.gen.AccountLocalObjectDao;
import org.celstec.dao.gen.GameContributorLocalObject;

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
public class AccountLazyListAdapter  extends LazyListAdapter<AccountLocalObject> {

    private QueryBuilder qb;
    private AccountLazyListAdapter adapterInq;

    public AccountLazyListAdapter(Context context) {
        super(context);

        AccountLocalObjectDao dao = DaoConfiguration.getInstance().getAccountLocalObjectDao();

        qb = dao.queryBuilder().orderAsc(AccountLocalObjectDao.Properties.FamilyName);
        ARL.eventBus.register(this);
        setLazyList(qb.listLazy());
    }


    public void onEventMainThread(GameEvent event) {
        if (lazyList != null) lazyList.close();
        setLazyList(qb.listLazy());
        notifyDataSetChanged();
    }

    public void close() {
        if (lazyList != null)lazyList.close();
        ARL.eventBus.unregister(this);
    }

    @Override
    public View newView(Context context, AccountLocalObject item, ViewGroup parent) {
        if (item == null) return null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.list_game_row, parent, false);

    }
    @Override
    public void bindView(View view, Context context,  AccountLocalObject item) {
        TextView firstLineView =(TextView) view.findViewById(R.id.firstLine);
        firstLineView.setText(item.getName());
        TextView secondLineView =(TextView) view.findViewById(R.id.secondLine);
        String description = item.getEmail()+" "+item.getFullId()+"\n";
        for (GameContributorLocalObject contrib: item.getAccount()) {
            description += "contrib "+contrib.getType()+contrib.getGameLocalObject().getTitle()+"\n";
        }
        secondLineView.setText(description);
    }


    @Override
    public long getItemId(int position) {
        if (dataValid && lazyList != null) {
            AccountLocalObject item = lazyList.get(position);
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

