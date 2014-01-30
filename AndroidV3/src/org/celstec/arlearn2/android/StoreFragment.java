package org.celstec.arlearn2.android;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;

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
public class StoreFragment extends SherlockFragment {
    private static final String TAG = "Store";

    private View categoryButton;
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

        final View v = inflater.inflate(R.layout.store, container, false);

        categoryButton = v.findViewById(R.id.category);
        categoryButton.setOnClickListener(new CategoryButton());


        return v;
    }


    private class CategoryButton implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.e(TAG, "Click Store");
//            startActivity(new Intent(MainMenu.this.getActivity(), StoreActivity.class));

            FragmentManager fm = getActivity().getSupportFragmentManager();
            Bundle args = new Bundle();

            CategoryFragment frag = new CategoryFragment();
            frag.setArguments(args);
            fm.beginTransaction().replace(R.id.right_pane, frag).addToBackStack(null).commit();
        }
    }

}
