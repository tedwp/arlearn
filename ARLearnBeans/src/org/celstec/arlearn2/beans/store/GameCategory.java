package org.celstec.arlearn2.beans.store;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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
public class GameCategory extends Bean {

    private Long id;
    private Long gameId;
    private Long categoryId;
    private Boolean deleted;

    public static BeanDeserializer deserializer = new BeanDeserializer(){

        @Override
        public GameCategory toBean(JSONObject object) {
            GameCategory bean = new GameCategory();
            try {
                initBean(object, bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            GameCategory bean = (GameCategory) genericBean;
            if (object.has("id")) bean.setId(object.getLong("id"));
            if (object.has("gameId")) bean.setGameId(object.getLong("gameId"));
            if (object.has("categoryId")) bean.setCategoryId(object.getLong("categoryId"));
            if (object.has("deleted")) bean.setDeleted(object.getBoolean("categoryId"));
        }
    };

    public static BeanSerializer serializer = new BeanSerializer () {

        @Override
        public JSONObject toJSON(Object bean) {
            GameCategory gameCategoryBean = (GameCategory) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (gameCategoryBean.getId() != null) returnObject.put("id", gameCategoryBean.getId());
                if (gameCategoryBean.getGameId() != null) returnObject.put("gameId", gameCategoryBean.getGameId());
                if (gameCategoryBean.getCategoryId() != null) returnObject.put("categoryId", gameCategoryBean.getCategoryId());
                if (gameCategoryBean.getDeleted() != null) returnObject.put("deleted", gameCategoryBean.getDeleted());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
