package net.wespot.pim.utils.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import net.wespot.pim.R;
import net.wespot.pim.controller.WrapperActivity;

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
public class ButtonEntryDelegator {

    private static final String TAG = "ButtonEntryDelegator";
    private static ButtonEntryDelegator instance;
    private FragmentActivity act;

    public static ButtonEntryDelegator getInstance(FragmentActivity activity) {

        if (instance == null) {
            instance = new ButtonEntryDelegator();
        }

        instance.act = activity;

        return instance;
    }

    /**
     * @param view body of the button
     * @param name string name used on the string.xml
     * @param option
     */
    public static void _button_list(View view, int id, String name, Class destination, Integer option, String option_name, Object... arg) {

        // arg [0] option [1] notification [2] icon ...

        ButtonEntry button_fragment = (ButtonEntry) instance.act.getSupportFragmentManager().findFragmentById(id);
        button_fragment.setName(name);

        if (arg.length != 0){
            button_fragment.setNotification((String)arg[0]);
        }

        view = (View) instance.act.findViewById(id);

        if(option != null){
            view.setOnClickListener(new _extended_intent(destination, option, option_name));
        }else{
            Log.e(TAG, "Not implemented yet.");
        }
    }

    private static class _extended_intent implements View.OnClickListener {

        private Object[] opt;
        private Class dest;

        public _extended_intent(Class destination, Object... option) {
            opt=option;
            dest=destination;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(instance.act.getApplicationContext(), dest);
            if (opt.length !=0){
                intent.putExtra(opt[1].toString(), (Integer) opt[0]);
            }
            instance.act.startActivity(intent);
        }
    }


}
