package net.wespot.pim.utils;

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
    public static final String URL_LOGIN = "http://wespot-arlearn.appspot.com/MobileLogin.html";
    public static final String ENCONDING = null;

    public static final List<String> INQUIRY_PHASES_LIST = Arrays.asList(
            "Description",
            "Hypothesis",
            "Plan",
            "Collect data",
            "Analyse",
            "Discuss",
            "Communicate"
    );
}

