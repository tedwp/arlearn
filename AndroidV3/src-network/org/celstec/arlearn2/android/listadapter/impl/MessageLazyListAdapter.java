package org.celstec.arlearn2.android.listadapter.impl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.listadapter.AbstractMessagesLazyListAdapter;
import org.celstec.dao.gen.MessageLocalObject;

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
public class MessageLazyListAdapter extends AbstractMessagesLazyListAdapter {

    public MessageLazyListAdapter(Context context) {
        super(context);
    }

    @Override
    public View newView(Context context, MessageLocalObject item, ViewGroup parent) {
        if (item == null) return null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.list_game_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, MessageLocalObject item) {
        TextView firstLineView =(TextView) view.findViewById(R.id.firstLine);
        firstLineView.setText(item.getSubject());
        TextView secondLineView =(TextView) view.findViewById(R.id.secondLine);
        secondLineView.setText(item.getBody() );
    }


    @Override
    public long getItemId(int position) {
        if (dataValid && lazyList != null) {
            MessageLocalObject item = lazyList.get(position);
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
