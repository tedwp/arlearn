package org.celstec.arlearn2.android.listadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
public abstract class ListAdapter <T> extends BaseAdapter {

    private ListItemClickInterface<T> callback;
    protected boolean dataValid;
    protected List<T> lazyList;
    protected Context context;

    public ListAdapter(Context context, List<T> lazyList) {
        this.lazyList = lazyList;
        this.dataValid = lazyList != null;
        this.context = context;
    }

    public ListAdapter(Context context) {
        this.dataValid = lazyList != null;
        this.context = context;
    }

    public void setLazyList(List<T> lazyList) {
        this.dataValid = lazyList != null;
        this.lazyList = lazyList;
    }
    /**
     * Returns the list.
     * @return the list.
     */
    public List<T> getLazyList() {
        return lazyList;
    }
    /**
     * @see android.widget.ListAdapter#getCount()
     */
    @Override
    public int getCount() {
        if (dataValid && lazyList != null) {
            return lazyList.size();
        } else {
            return 0;
        }
    }

    /**
     * @see android.widget.ListAdapter#getItem(int)
     */
    @Override
    public T getItem(int position) {
        if (dataValid && lazyList != null) {
            return lazyList.get(position);
        } else {
            return null;
        }
    }

    public void setOnListItemClickCallback(ListItemClickInterface<T> callback) {
        this.callback = callback;
    }



    @Override
    public boolean hasStableIds() {
        return true;
    }
    /**
     * @see android.widget.ListAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (!dataValid) {
            throw new IllegalStateException("this should only be called when lazylist is populated");
        }

        T item = lazyList.get(position);
        if (item == null) {
            throw new IllegalStateException("Item at position " + position + " is null");
        }

        View v;
        if (convertView == null) {
            v = newView(context, item, parent);
        } else {
            v = convertView;
        }
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (callback!= null) callback.onListItemClick(v, position, getItem(position));

            }
        });
        v.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (callback!= null) return callback.setOnLongClickListener(v, position, getItem(position));
                return false;
            }
        });
        bindView(v, context, item);
        return v;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if (dataValid) {
            T item = lazyList.get(position);
            View v;
            if (convertView == null) {
                v = newDropDownView(context, item, parent);
            } else {
                v = convertView;
            }
            bindView(v, context, item);
            return v;
        } else {
            return null;
        }
    }

    /**
     * Makes a new view to hold the data contained in the item.
     * @param context Interface to application's global information
     * @param item  The object that contains the data
     * @param parent The parent to which the new view is attached to
     * @return the newly created view.
     */
    public abstract View newView(Context context, T item, ViewGroup parent);
    /**
     * Makes a new drop down view to hold the data contained in the item.
     * @param context Interface to application's global information
     * @param item  The object that contains the data
     * @param parent The parent to which the new view is attached to
     * @return the newly created view.
     */
    public View newDropDownView(Context context, T item, ViewGroup parent) {
        return newView(context, item, parent);
    }
    /**
     * Bind an existing view to the data data contained in the item.
     * @param view Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param item The object that contains the data
     */
    public abstract void bindView(View view, Context context, T item);

}
