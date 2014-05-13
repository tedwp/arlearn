package net.wespot.pim.utils.layout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.wespot.pim.R;

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

public class ButtonEntry extends Fragment {

    private ViewItemClickInterface callback;
    private String name;
    private String notification;
    private int icon;
    private int id;

    public ButtonEntry() {
    }

    public ButtonEntry(int id, String name, String notification, int icon) {
        this.id = id;
        this.name = name;
        this.notification = notification;
        this.icon = icon;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        LinearLayout linearLayout = new LinearLayout(getActivity());
//
//        TextView nam = new TextView(getActivity());
//        TextView noti = new TextView(getActivity());
//
//        nam.setText(this.name);
//        noti.setText(this.notification);
//
//        if (icon != 0){
//            ImageView ico = new ImageView(getActivity());
//            ico.setImageDrawable(getResources().getDrawable(icon));
//        }
//
//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (callback != null) callback.onListItemClick(view, id);
//            }
//        });
//
//        container.addView(linearLayout);
//
//        return container;


        View view = inflater.inflate(R.layout.entry_main_list, container, false);
        ((TextView) view.findViewById(R.id.name_entry_list)).setText(name);
        ((TextView) view.findViewById(R.id.notificationText)).setText(notification);

        if (icon != 0){
            ((ImageView) view.findViewById(R.id.inquiry_entry_icon)).setImageDrawable(getResources().getDrawable(icon));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) callback.onListItemClick(view, id);
            }
        });

        return  view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    public void setOnListItemClickCallback(ViewItemClickInterface callback) {
        this.callback = callback;
    }
}
