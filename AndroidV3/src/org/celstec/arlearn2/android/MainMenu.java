package org.celstec.arlearn2.android;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.delegators.ARL;

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
    private String authToken = "ya29.1.AADtN_V345QcK9W1bqSBWH98rK1pb2fl9KDsfvzhtDlOBkERiDXuuvRMlT9_6zg";

    private View button1;
    private View storeButton;
    private View button3;
    private View button4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        INQ.init(getActivity());
        ARL.properties.setAuthToken(authToken);
        ARL.properties.setFullId("2:116757187626671489073");

        ARL.accounts.syncMyAccountDetails();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.main_menu, container, false);

        button1 = v.findViewById(R.id.games);
        storeButton =  v.findViewById(R.id.store);
        button3 =  v.findViewById(R.id.scan);
        button4 =  v.findViewById(R.id.nearme);

        button1.setOnClickListener(new ClickButton1());
        storeButton.setOnClickListener(new ClickButton2());
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
//            startActivity(new Intent(MainMenu.this.getActivity(), StoreActivity.class));

                FragmentManager fm = getActivity().getSupportFragmentManager();
                Bundle args = new Bundle();

                StoreFragment frag = new StoreFragment();
                frag.setArguments(args);
                fm.beginTransaction().replace(R.id.right_pane, frag).addToBackStack(null).commit();
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
