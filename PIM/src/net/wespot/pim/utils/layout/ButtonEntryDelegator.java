package net.wespot.pim.utils.layout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

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
     * @param id identificator
     * @param name string name used on the string.xml
     * @param icon icon for the link
     * @param destination end of the intent
     * @param options true => option within the intent; false => no intent options
     * @param arg string arguments to add in the link, such us notifications or icons.
     */
    public static View _button_list(int id, String name, int icon, Class destination, boolean options, String... arg) {

        // arg [0] option [1] notification [2] icon ...

        View view;
        ButtonEntry button_fragment = (ButtonEntry) instance.act.getSupportFragmentManager().findFragmentById(id);
        button_fragment.setName(name);
        button_fragment.setIcon(instance.act.getResources().getDrawable(icon));


        if (arg.length != 0){
            button_fragment.setNotification((String)arg[0]);
        }

        view = (View) instance.act.findViewById(id);

        if(!options){

                view.setOnClickListener(new _extended_intent(destination));

        }else{
            Log.d(TAG, "Need to provide and setOnClickListener");
        }
        return view;
    }

    private static class _extended_intent implements View.OnClickListener {
        private Class dest;

        public _extended_intent(Class destination) {
            dest=destination;
        }

        @Override
        public void onClick(View view) {
            if (dest!=null){
                Intent intent = new Intent(instance.act.getApplicationContext(), dest);
                instance.act.startActivity(intent);
            }else{
                Toast.makeText(instance.act,"Not implemented yet. A destination is needed", 10).show();
            }
        }
    }
}
