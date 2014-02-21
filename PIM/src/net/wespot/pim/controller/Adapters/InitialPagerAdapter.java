package net.wespot.pim.controller.Adapters;

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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import net.wespot.pim.view.DemoObjectFragment;
import net.wespot.pim.view.SplashImagesFragment;

/**
 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
 * representing an object in the collection.
 */
public class InitialPagerAdapter extends FragmentPagerAdapter {

    public static final String INQUIRY_ID = "inquiry_id";

    public InitialPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        Fragment frag = new DemoObjectFragment();
        Bundle args = new Bundle();

        switch (i) {
            default:
                // The other sections of the app are dummy placeholders.
                frag = new SplashImagesFragment();
                args.putInt(INQUIRY_ID, i); // Our object is just an integer :-P
                frag.setArguments(args);
                return frag;
        }
    }

    @Override
    public int getCount() {
        // For this contrived example, we have a 100-object collection.
        return 3;
    }
}