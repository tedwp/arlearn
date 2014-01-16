package org.celstec.arlearn2.android;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import org.celstec.arlearn2.android.events.FileDownloadStatus;

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
public class MainMenu extends SherlockFragment {
    private static final String TAG = "MainMenu";

    private View button1;
    private View button2;
    private View button3;
    private View button4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.main_menu, container, false);

        // TODO some styling
        ((TextView)v.findViewById(R.id.gameText)).setText("iets anders");
        button1 = v.findViewById(R.id.games);
        button2 =  v.findViewById(R.id.store);
        button3 =  v.findViewById(R.id.scan);
        button4 =  v.findViewById(R.id.nearme);

        button1.setOnClickListener(new ClickButton1());
        button2.setOnClickListener(new ClickButton2());
        button3.setOnClickListener(new ClickButton3());
        button4.setOnClickListener(new ClickButton4());

        return v;
    }


    private class ClickButton1 implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.e(TAG, "Click My Games");
        }
    }

    private class ClickButton2 implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.e(TAG, "Click Store" );
        }
    }

    private class ClickButton3 implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.e(TAG, "Click Scan Code" );
        }
    }

    private class ClickButton4 implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.e(TAG, "Click Nearby" );
        }
    }
}