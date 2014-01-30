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
public class CategoryList extends Bean {

    public static String CategoryType = "org.celstec.arlearn2.beans.store.Category";

    private List<Category> categoryList = new ArrayList<Category>();

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public void addCategory(Category category) {
        this.categoryList.add(category);
    }

    public static BeanSerializer serializer = new BeanSerializer() {

        @Override
        public JSONObject toJSON(Object bean) {
            CategoryList categoryList = (CategoryList) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (categoryList.getCategoryList() != null)
                    returnObject.put("categoryList", ListSerializer.toJSON(categoryList.getCategoryList()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };

    public static BeanDeserializer deserializer = new BeanDeserializer() {

        @Override
        public CategoryList toBean(JSONObject object) {
            CategoryList al = new CategoryList();
            try {
                initBean(object, al);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return al;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            CategoryList categoryList = (CategoryList) genericBean;
            if (object.has("categoryList"))
                categoryList.setCategoryList(ListDeserializer.toBean(object.getJSONArray("categoryList"), Category.class));
        }
    };
}
