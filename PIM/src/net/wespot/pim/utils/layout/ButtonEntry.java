package net.wespot.pim.utils.layout;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        private TextView name;
        private TextView notification;
        private ImageView icon;

        public ButtonEntry() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.entry_main_list, container, false);
        }

        @Override

        public void onViewCreated(View view, Bundle savedInstanceState) {

            name = (TextView) view.findViewById(R.id.name_entry_list);
            notification = (TextView) view.findViewById(R.id.notificationText);
            icon = (ImageView) view.findViewById(R.id.inquiry_entry_icon);

            super.onViewCreated(view, savedInstanceState);
        }

        public CharSequence getName() {
            return name.getText();
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public CharSequence getNotification() {
            return notification.getText();
        }

        public void setNotification(CharSequence notif) {
            notification.setVisibility(View.VISIBLE);
            this.notification.setText(notif);
        }

        public Drawable getIcon() {
            return icon.getDrawable();
        }

        public void setIcon(Drawable a) {
            this.icon.setImageDrawable(a);
        }

    }
