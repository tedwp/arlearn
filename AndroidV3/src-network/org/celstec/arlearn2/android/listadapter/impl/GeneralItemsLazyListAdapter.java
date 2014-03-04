package org.celstec.arlearn2.android.listadapter.impl;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import daoBase.DaoConfiguration;
import de.greenrobot.dao.query.QueryBuilder;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.events.GeneralItemEvent;
import org.celstec.arlearn2.android.listadapter.AbstractGeneralItemsLazyListAdapter;
import org.celstec.arlearn2.android.listadapter.LazyListAdapter;
import org.celstec.dao.gen.*;

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
public class GeneralItemsLazyListAdapter  extends AbstractGeneralItemsLazyListAdapter {

    public GeneralItemsLazyListAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindView(View view, Context context,  GeneralItemLocalObject item) {
        TextView firstLineView =(TextView) view.findViewById(R.id.firstLine);
        firstLineView.setText(item.getTitle());
        TextView secondLineView =(TextView) view.findViewById(R.id.secondLine);
        String description = item.getDescription()==null?"":item.getDescription();
        if (item.getDependencyLocalObject() != null) {
            description += "dep "+(item.getDependencyLocalObject()).toString();
            long time = System.currentTimeMillis();
            description += "sat "+ item.getDependencyLocalObject().satisfiedAt(DaoConfiguration.getInstance().getRunLocalObjectDao().load(19806001l));
            Log.e("ARLearn", "sat time "+(System.currentTimeMillis()-time));
        }
//        for (GeneralItemMediaLocalObject media: item.getGeneralItemMedia()){
//            description += " fr "+media.getLocalId()+":"+media.getMd5Hash();
//        }
        secondLineView.setText(description + " id " +item.getId() );
    }


    @Override
    public long getItemId(int position) {
        if (dataValid && lazyList != null) {
            GeneralItemLocalObject item = lazyList.get(position);
            if (item != null) {
                return item.getId();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public View newView(Context context, GeneralItemLocalObject item, ViewGroup parent) {
        if (item == null) return null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.list_game_row, parent, false);

    }

}

