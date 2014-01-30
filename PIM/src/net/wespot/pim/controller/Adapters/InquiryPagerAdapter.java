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
import net.wespot.pim.view.*;
import org.celstec.arlearn.delegators.INQ;

/**
 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
 * representing an object in the collection.
 */
public class InquiryPagerAdapter extends FragmentPagerAdapter {

    public static final String INQUIRY_ID = "inquiry_id";

    public InquiryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        Fragment frag = new DemoObjectFragment();
        Bundle args = new Bundle();

        switch (i) {
            case 0:
                frag = new InqDescriptionFragment();
//                    args.putLong(INQUIRY_ID, inquiry_id); // Our object is just an integer :-P
//                    frag.setArguments(args);

                return frag;
            case 1:
                frag =  new InqHypothesisFragment();
                return frag;
            case 2:
                // TODO check with Stefaan if can I manually bound runs to an inquiry on the database
                if (INQ.inquiry.getCurrentInquiry().getRunLocalObject() != null){
                    frag = new InqDataFragment();
                    return frag;
                }
            case 3:
                frag =  new InqMapFragment();
                return frag;
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
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "Wonder moment";
            case 1:
                return "Description";
            case 2:
                if (INQ.inquiry.getCurrentInquiry().getRunLocalObject() != null){
                    return "Collect data";
                }
            case 3:
                return "Map";
            case 4:
                return "Analysis";
            default:
                return "Section " + (position + 1);
        }
    }
}