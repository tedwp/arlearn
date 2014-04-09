package org.celstec.arlearn2.android;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.listadapter.ListItemClickInterface;
import org.celstec.arlearn2.android.listadapter.impl.CategoryGamesLazyListAdapter;
import org.celstec.arlearn2.android.listadapter.impl.SearchResultsLazyListAdapter;
import org.celstec.arlearn2.beans.game.Game;

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
public class SearchFragment  extends SherlockListFragment implements ListItemClickInterface<Game> {

    private SearchResultsLazyListAdapter adapter;
    private LinearLayout searchBarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ARL.games.syncMyGames();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.store_search, container, false);
        adapter = new SearchResultsLazyListAdapter(getActivity());
        adapter.setOnListItemClickCallback(this);
        ListView lv = (ListView) v.findViewById(android.R.id.list);

        View header = inflater.inflate(R.layout.store_search_bar, null);
        lv.addHeaderView(header);

        View searchButton = header.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new SearchButton());

        setListAdapter(adapter);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        TextView tv = new TextView(getActivity());
//        tv.setText("Hello");

    }

    @Override
    public void onListItemClick(View v, int position, Game game) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Bundle args = new Bundle();

        GameFragment frag = new GameFragment(game);
        frag.setArguments(args);
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.right_pane, frag).addToBackStack(null).commit();
    }

    @Override
    public boolean setOnLongClickListener(View v, int position, Game object) {
        return false;
    }

    private class SearchButton implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ARL.games.search("game");
        }
    }
}
