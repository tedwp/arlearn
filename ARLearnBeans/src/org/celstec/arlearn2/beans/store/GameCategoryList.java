package org.celstec.arlearn2.beans.store;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
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
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
public class GameCategoryList extends Bean {

    public static String GameCategoryType = "org.celstec.arlearn2.beans.store.GameCategory";

    private List<GameCategory> gameCategoryList = new ArrayList<GameCategory>();

    public List<GameCategory> getGameCategoryList() {
        return gameCategoryList;
    }

    public void setGameCategoryList(List<GameCategory> gameCategoryList) {
        this.gameCategoryList = gameCategoryList;
    }

    public void addGameCategory(GameCategory gameCategory) {
        this.gameCategoryList.add(gameCategory);
    }

    public static BeanSerializer serializer = new BeanSerializer() {

        @Override
        public JSONObject toJSON(Object bean) {
            GameCategoryList categoryList = (GameCategoryList) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (categoryList.getGameCategoryList() != null)
                    returnObject.put("gameCategoryList", ListSerializer.toJSON(categoryList.getGameCategoryList()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };

    public static BeanDeserializer deserializer = new BeanDeserializer() {

        @Override
        public GameCategoryList toBean(JSONObject object) {
            GameCategoryList al = new GameCategoryList();
            try {
                initBean(object, al);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return al;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            GameCategoryList categoryList = (GameCategoryList) genericBean;
            if (object.has("gameCategoryList"))
                categoryList.setGameCategoryList(ListDeserializer.toBean(object.getJSONArray("gameCategoryList"), GameCategory.class));
        }
    };
}
