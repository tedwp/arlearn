package org.celstec.arlearn2.android.listadapter.impl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import daoBase.DaoConfiguration;
import de.greenrobot.dao.query.LazyList;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.events.SearchResultList;
import org.celstec.arlearn2.android.listadapter.ListAdapter;
import org.celstec.arlearn2.android.viewWrappers.GameRowBig;
import org.celstec.arlearn2.android.viewWrappers.GameRowSmall;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.dao.gen.AccountLocalObject;
import org.celstec.dao.gen.GameContributorLocalObject;
import org.celstec.dao.gen.GameLocalObject;

import java.util.ArrayList;
import java.util.List;

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
public class SearchResultsLazyListAdapter extends ListAdapter<Game> {

    private SearchResultsLazyListAdapter adapter;
    private List<Game> lazyList = new ArrayList<Game>();

    public SearchResultsLazyListAdapter(Context context) {
        super(context);

        ARL.eventBus.register(this);
        setLazyList(lazyList);
    }

    public void onEventMainThread(SearchResultList event) {
        lazyList = event.getGamesList().getGames();
        setLazyList(lazyList);
        notifyDataSetChanged();
    }

    public void close() {
        ARL.eventBus.unregister(this);
    }

    @Override
    public View newView(Context context, Game item, ViewGroup parent) {
        if (item == null) return null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return inflater.inflate(R.layout.store_game_list_entry_small, parent, false);    }

    @Override
    public void bindView(View view, Context context, Game item) {
        GameRowSmall big = new GameRowSmall((LinearLayout) view);
        big.setGameTitle(item.getTitle());
    }

    @Override
    public long getItemId(int position) {
        if (dataValid && lazyList != null) {
            Game item = lazyList.get(position);
            if (item != null) {
                return item.getGameId();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
