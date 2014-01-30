package org.celstec.arlearn2.android;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListActivity;
import daoBase.DaoConfiguration;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.events.CategoryEvent;
import org.celstec.dao.gen.CategoryLocalObject;

import java.util.HashMap;

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
public class CategoryFragment extends SherlockFragment{

    HashMap<Long, View> categoryMap = new HashMap<Long, View>();
    private     TableLayout tableLayout;
    private TableRow row = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        ARL.store.synCategories();
        ARL.eventBus.register(this);
//        ARL.store.syncGamesForCategory(2l);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ARL.eventBus.unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.store_category, container, false);
         tableLayout = (TableLayout) v.findViewById(R.id.categoryTableLayout);
//        TableRow row = null;
        int i = 0;
        for (CategoryLocalObject categoryLocalObject: ARL.store.getCategories()){
            if (i %2 == 0) {
                row = (TableRow) inflater.inflate(R.layout.store_category_row, tableLayout, false);
                tableLayout.addView(row);
            }

            RelativeLayout item1 = (RelativeLayout) inflater.inflate(R.layout.store_category_item, row, false);
            row.addView(item1);
            categoryMap.put(categoryLocalObject.getId(), item1);
            ((TextView)item1.findViewById(R.id.categoryItemText)).setText(categoryLocalObject.getCategory());
            item1.setOnClickListener(new ClickCategory(categoryLocalObject.getId()));
            i++;
        };

        return v;
    }

    private class ClickCategory implements View.OnClickListener {
        private long categoryId;

        private ClickCategory(long categoryId) {
            this.categoryId = categoryId;
        }

        @Override
        public void onClick(View view) {

            FragmentManager fm = getActivity().getSupportFragmentManager();
            Bundle args = new Bundle();

            StoreGameListFragment frag = new StoreGameListFragment(categoryId);

            fm.beginTransaction().replace(R.id.right_pane, frag).addToBackStack(null).commit();
        }
    }

    public void onEventMainThread(CategoryEvent event) {
//        LayoutInflater inflater = getLayoutInflater(null);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);

        if (!categoryMap.containsKey(event.getCategoryId())) {
            CategoryLocalObject categoryLocalObject = DaoConfiguration.getInstance().getCategoryLocalObjectDao().load(event.getCategoryId());
             if (categoryMap.size() %2 ==0) {

                 row = (TableRow) inflater.inflate(R.layout.store_category_row, tableLayout, false);
                 tableLayout.addView(row);
             }
            RelativeLayout item1 = (RelativeLayout) inflater.inflate(R.layout.store_category_item, row, false);
            row.addView(item1);
            categoryMap.put(categoryLocalObject.getId(), item1);
            ((TextView)item1.findViewById(R.id.categoryItemText)).setText(categoryLocalObject.getCategory());
        }

    }


}
