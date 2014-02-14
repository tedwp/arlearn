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

        Fragment frag;
        Bundle args = new Bundle();

        switch (i) {
            case 0:
                frag = new InqHypothesisFragment();
                return frag;
            case 1:
                frag =  new InqPlanFragment();
                return frag;
            case 2:
                if (INQ.inquiry.getCurrentInquiry().getRunLocalObject() != null){
                    frag = new InqDataFragment();
                    return frag;
                }else{
                    frag = new InqNoDataFragment();
                    return frag;
                }
            case 3:
                frag =  new InqAnalyseFragment();
                return frag;
            case 4:
                frag =  new InqDiscussFragment();
                return frag;
            case 5:
                frag =  new InqCommunicateFragment();
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
}