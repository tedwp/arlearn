package org.celstec.arlearn2.android;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import org.celstec.arlearn2.android.viewWrappers.GameRowBig;

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

    private View searchButton;
    private View categoryButton;

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

        searchButton = v.findViewById(R.id.search);
        searchButton.setOnClickListener(new SearchButton());

        categoryButton = v.findViewById(R.id.category);
        categoryButton.setOnClickListener(new CategoryButton());

        LinearLayout layout = (LinearLayout) v.findViewById(R.id.storeLinearLayout);

        GameRowBig big3 = new GameRowBig(inflater, layout);
        big3.setGameTitle("Record game");
        big3.setGameCategory("Music");
        big3.setGameDescription("A location based game where the goal is to take as many pictures of music as possible.");

        GameRowBig big1 = new GameRowBig(inflater, layout);
        big1.setGameTitle("Get The picture");
        big1.setGameCategory("Photography");
        big1.setGameDescription("A location based game where the goal is to take as many pictures  as possible.");

        GameRowBig big2 = new GameRowBig(inflater, layout);
        big2.setGameTitle("Shop-a-holic");
        big2.setGameCategory("Shopping");
        big2.setGameDescription("A game that woman can play while their husbands are attending a football game");

        return v;
    }


    private class SearchButton implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.e(TAG, "Click Search");
            FragmentManager fm = getActivity().getSupportFragmentManager();
            Bundle args = new Bundle();

            SearchFragment frag = new SearchFragment();
            frag.setArguments(args);
            FragmentTransaction ft = fm.beginTransaction();

//            ft.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
            ft.replace(R.id.right_pane, frag).addToBackStack(null).commit();
        }
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
