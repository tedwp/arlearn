package org.celstec.arlearn2.cache;

import com.google.appengine.api.utils.SystemProperty;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.delegators.ActionRelevancyPredictor;

import java.util.HashSet;

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
public class CategoryCache extends GenericCache {

    private static CategoryCache instance;

    private static String CATEGORY_PREFIX = SystemProperty.applicationVersion.get()+"Category";

    private CategoryCache() {
    }

    public static CategoryCache getInstance() {
        if (instance == null)
            instance = new CategoryCache();
        return instance;

    }

    public void storeKeyValue(String key, String value) {
        getCache().put(CATEGORY_PREFIX+key, value);
    }

    public String getValue(String key) {
        return (String) getCache().get(CATEGORY_PREFIX+key);
    }
}
