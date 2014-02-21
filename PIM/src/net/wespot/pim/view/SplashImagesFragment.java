package net.wespot.pim.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import net.wespot.pim.R;

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
public class SplashImagesFragment extends Fragment {

    public static final String INQUIRY_ID = "inquiry_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_splash_image, container, false);
        Bundle args = getArguments();



        switch (args.getInt(INQUIRY_ID)){
            case 0:
                ((ImageView) rootView.findViewById(R.id.image_splash_screen)).
                        setImageDrawable(getResources().getDrawable(R.drawable.screen1));
                break;
            case 1:
                ((ImageView) rootView.findViewById(R.id.image_splash_screen)).
                        setImageDrawable(getResources().getDrawable(R.drawable.screen2));
                break;
            case 2:
                ((ImageView) rootView.findViewById(R.id.image_splash_screen)).
                        setImageDrawable(getResources().getDrawable(R.drawable.screen3));
                break;
        }


//        ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                Integer.toString(args.getInt(INQUIRY_ID)));
        return rootView;
    }
}