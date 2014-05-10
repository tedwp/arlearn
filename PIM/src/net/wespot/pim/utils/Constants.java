package net.wespot.pim.utils;

import net.wespot.pim.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class Constants {

    public static final int SIMULATED_REFRESH_LENGTH = 1000;
    public static final String MIME_TYPE = "text/html";
    public static final String URL_LOGIN_NAME = "url";
    public static final String TYPE_LOGIN = "type";
    public static final int GOOGLE = 1;
    public static final int WESPOT = 0;
    public static final int LINKEDIN = 2;
    public static final int FACEBOOK = 3;
    public static final String URL_LOGIN = "http://wespot-arlearn.appspot.com/MobileLogin.html";
    public static final String ENCONDING = null;

    // If we want to change the order of the screen this must be done here and also in the rest of lists
    // Only appear the first three
    public static final int  ID_DESCRIPTION = 0;
    public static final int  ID_QUESTION = 1;
    public static final int  ID_DATA = 2;
    public static final int  ID_HYPOTHESIS = 3;
    public static final int  ID_PLAN = 4;
    public static final int  ID_ANALYSIS = 5;
    public static final int  ID_DISCUSS = 6;
    public static final int  ID_COMMUNICATE = 7;

    public static final List<Integer> INQUIRY_PHASES_LIST = Arrays.asList(
            R.string.inquiry_title_description,
            R.string.inquiry_title_question,
            R.string.inquiry_title_data,
            R.string.inquiry_title_hypothesis,
            R.string.inquiry_title_plan,
            R.string.inquiry_title_analyse,
            R.string.inquiry_title_discuss,
            R.string.inquiry_title_communicate
    );

    public static final List<Integer> INQUIRY_ICON_PHASES_LIST = Arrays.asList(
            R.drawable.ic_description,
            R.drawable.ic_placeholder,
            R.drawable.ic_data,
            R.drawable.ic_hypothesis,
            R.drawable.ic_plan,
            R.drawable.ic_analyze,
            R.drawable.ic_discuss,
            R.drawable.ic_communicate
    );

    public static final List<Integer> INQUIRY_ID_PHASES_LIST = Arrays.asList(
            ID_DESCRIPTION,
            ID_QUESTION,
            ID_DATA,
            ID_HYPOTHESIS,
            ID_PLAN,
            ID_ANALYSIS,
            ID_DISCUSS,
            ID_COMMUNICATE
    );
}

