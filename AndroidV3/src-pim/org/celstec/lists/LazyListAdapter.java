package org.celstec.lists;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import de.greenrobot.dao.query.LazyList;
import org.celstec.dao.gen.InquiryLocalObject;

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
public abstract class LazyListAdapter <T extends InquiryLocalObject> extends BaseAdapter {


    protected boolean dataValid;
    protected LazyList<T> lazyList;
    protected Context context;

    public LazyListAdapter(Context context, LazyList<T> lazyList) {
        this.lazyList = lazyList;
        this.dataValid = lazyList != null;
        this.context = context;
    }
    /**
     * Returns the list.
     * @return the list.
     */
    public LazyList<T> getLazyList() {
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

