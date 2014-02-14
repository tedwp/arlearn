package net.wespot.pim.controller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import net.wespot.pim.R;

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
 * Contributors: Angel Suarez
 * ****************************************************************************
 */
public class PhasesLazyListAdapter extends BaseAdapter {

    protected boolean dataValid;
    protected List<String> list;
    protected Context context;

    public PhasesLazyListAdapter(Context context, List<String> list) {
        this.list = list;
        this.dataValid = list != null;
        this.context = context;
    }

    /**
     * @see android.widget.ListAdapter#getCount()
     */
    @Override
    public int getCount() {
        if (dataValid && list != null) {
            return list.size();
        } else {
            return 0;
        }
    }



    @Override
    public boolean hasStableIds() {
        return true;
    }
    /**
     * @see android.widget.ListAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        if (!dataValid) {
            throw new IllegalStateException("this should only be called when lazylist is populated");
        }

        String item = list.get(position);
        if (item == null) {
            throw new IllegalStateException("Item at position " + position + " is null");
        }

        View v;
        if (convertView == null) {
            v = newView(context, item, parent);
        } else {
            v = convertView;
        }
        bindView(v, context, item);
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if (dataValid) {
            String item = list.get(position);
            View v;
            if (convertView == null) {
                v = newView(context, item, parent);
            } else {
                v = convertView;
            }
            bindView(v, context, item);
            return v;
        } else {
            return null;
        }
    }


    public View newView(Context context,  String item, ViewGroup parent) {
        if (item == null) return null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.entry_inquiry_list, parent, false);

    }

    public void bindView(View view, Context context,  String item) {
        TextView firstLineView =(TextView) view.findViewById(R.id.name_entry_list);
        firstLineView.setText(item);
    }

    @Override
    public Object getItem(int i) {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
