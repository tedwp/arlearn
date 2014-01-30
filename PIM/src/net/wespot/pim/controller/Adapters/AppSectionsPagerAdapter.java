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
import net.wespot.pim.DemoFragment;
import net.wespot.pim.view.PimBadgesFragment;
import net.wespot.pim.view.PimInquiriesFragment;
import net.wespot.pim.view.PimProfileFragment;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
 * sections of the app.
 */
public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

    private static final int NUMBER_OF_MAIN_SCREENS = 4;

    public AppSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new PimProfileFragment();
            case 1:
                return new PimInquiriesFragment();
            case 2:
                return new PimBadgesFragment();
            default:
                // The other sections of the app are dummy placeholders.
                Fragment defFragment = new DemoFragment();
                Bundle defArgs = new Bundle();
                defArgs.putInt(DemoFragment.ARG_SECTION_NUMBER, i + 1);
                defFragment.setArguments(defArgs);
                return defFragment;
        }
    }

    @Override
    public int getCount() {
        return NUMBER_OF_MAIN_SCREENS;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "Profile";
            case 1:
                return "Inquiries";
            case 2:
                return "Badges";
            case 3:
                return "Friends";
            default:
                return "Section " + (position + 1);
        }
    }
}
