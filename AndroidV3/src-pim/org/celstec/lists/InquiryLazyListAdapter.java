package org.celstec.lists;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
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
public class InquiryLazyListAdapter extends LazyListAdapter<InquiryLocalObject>{

    public InquiryLazyListAdapter(Context context, LazyList<InquiryLocalObject> lazyList) {
        super(context, lazyList);
    }

    @Override
    public View newView(Context context, InquiryLocalObject item, ViewGroup parent) {

        return null;
    }

    @Override
    public void bindView(View view, Context context, InquiryLocalObject item) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getItemId(int i) {
        return lazyList.get(i).getId();
    }
}
