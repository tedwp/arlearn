package org.celstec.arlearn2.client;

import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.store.CategoryList;
import org.celstec.arlearn2.beans.store.GameCategoryList;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

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
public class StoreClient extends GenericClient{


    private static StoreClient instance;

    private StoreClient() {
        super("/store");
    }

    public static StoreClient getStoreClient() {
        if (instance == null) {
            instance = new StoreClient();
        }
        return instance;
    }

    public CategoryList getCategoriesByLang(String token, String lang) {
        return (CategoryList)  executeGet(getUrlPrefix()+"/categories/lang/"+lang, token, CategoryList.class);
    }

    public GameCategoryList getGameIdsForCategory(String token, Long categoryId) {
        return (GameCategoryList) executeGet(getUrlPrefix()+"/games/category/"+categoryId, token, GameCategoryList.class);
    }
}
