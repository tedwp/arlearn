package net.wespot.pim.utils.layout;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * ****************************************************************************
 * Copyright (C) 2014 Open Universiteit Nederland
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
public class ButtonDelegator{

    private static final String TAG = "ButtonDelegator";
    private static ButtonDelegator instance;
    private static FragmentActivity act;

    public static ButtonDelegator getInstance(FragmentActivity activity) {

        if (instance == null) {
            instance = new ButtonDelegator();
        }

        act = activity;
        return instance;
    }

    public LinearLayout layoutGenerator(int margin_top) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, (int) act.getResources().getDimension(margin_top),0,0);

        LinearLayout layout = new LinearLayout(act);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(layoutParams);
        layout.setId(123);

        return layout;
    }

    public ButtonEntry buttonGenerator(LinearLayout layout, int id, String title, String notificiation, int drawable) {
        Log.e(TAG,  layout+" | "+id+" | "+title+" | "+notificiation+" | "+drawable);
        ButtonEntry button = new ButtonEntry(id, title,String.valueOf(notificiation),drawable);
        act.getSupportFragmentManager().beginTransaction().add(layout.getId(), button, button.getId()+"").commit();
        return button;
    }
}
