package net.wespot.pim.controller.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import net.wespot.pim.view.DemoObjectFragment;
import net.wespot.pim.view.InqCreateInquiryFragment;

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
public class NewInquiryPagerAdapter extends FragmentStatePagerAdapter {

    public static final String INQUIRY_ID = "inquiry_id";


    public NewInquiryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        Fragment frag = new DemoObjectFragment();
        Bundle args = new Bundle();

        switch (i) {
            case 0:
                // The first section of the app is the most interesting -- it offers
                // a launchpad into the other demonstrations in this example application.
                return new InqCreateInquiryFragment();
            case 1:
                // The first section of the app is the most interesting -- it offers
                // a launchpad into the other demonstrations in this example application.

            case 2:
                // The first section of the app is the most interesting -- it offers
                // a launchpad into the other demonstrations in this example application.
//                    return new _InqDataFragment();
//                case 3:
//                    // The first section of the app is the most interesting -- it offers
//                    // a launchpad into the other demonstrations in this example application.
////                    return new InterpretationFragment();
//                case 4:
//                    // The first section of the app is the most interesting -- it offers
//                    // a launchpad into the other demonstrations in this example application.
////                    return new CommunicationFragment();
            default:
                // The other sections of the app are dummy placeholders.
                frag = new DemoObjectFragment();
                args.putInt(INQUIRY_ID, i + 1); // Our object is just an integer :-P
                frag.setArguments(args);
                return frag;
        }
    }

    @Override
    public int getCount() {
        // For this contrived example, we have a 100-object collection.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}

