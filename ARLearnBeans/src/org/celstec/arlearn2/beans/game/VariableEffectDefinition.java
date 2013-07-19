package org.celstec.arlearn2.beans.game;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
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
public class VariableEffectDefinition extends Bean{
    private Long id;
    private Long gameId;
    private String name;
    private String effectType;
    private Long effectValue;
    private Integer effectCount;
    private Dependency dependsOn;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEffectType() {
        return effectType;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public Long getEffectValue() {
        return effectValue;
    }

    public void setEffectValue(Long effectValue) {
        this.effectValue = effectValue;
    }

    public Integer getEffectCount() {
        return effectCount;
    }

    public void setEffectCount(Integer effectCount) {
        this.effectCount = effectCount;
    }

    public Dependency getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(Dependency dependsOn) {
        this.dependsOn = dependsOn;
    }
    public static BeanDeserializer deserializer = new BeanDeserializer(){

        @Override
        public VariableEffectDefinition toBean(JSONObject object) {
            VariableEffectDefinition bean = new VariableEffectDefinition();
            try {
                initBean(object, bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            VariableEffectDefinition bean = (VariableEffectDefinition) genericBean;
            if (object.has("id")) bean.setId(object.getLong("id"));
            if (object.has("name")) bean.setName(object.getString("name"));
            if (object.has("effectType")) bean.setEffectType(object.getString("effectType"));
            if (object.has("gameId")) bean.setGameId(object.getLong("gameId"));
            if (object.has("effectValue")) bean.setEffectValue(object.getLong("effectValue"));
            if (object.has("effectCount")) bean.setEffectCount(object.getInt("effectCount"));
            if (object.has("dependsOn")) bean.setDependsOn((Dependency) JsonBeanDeserializer.deserialize(Dependency.class, object.getJSONObject("dependsOn")));


        }
    };

    public static BeanSerializer serializer = new BeanSerializer () {

        @Override
        public JSONObject toJSON(Object bean) {
            VariableEffectDefinition variableEffectDefinition = (VariableEffectDefinition) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (variableEffectDefinition.getId() != null) returnObject.put("id", variableEffectDefinition.getId());
                if (variableEffectDefinition.getName() != null) returnObject.put("name", variableEffectDefinition.getName());
                if (variableEffectDefinition.getEffectType() != null) returnObject.put("effectType", variableEffectDefinition.getEffectType());
                if (variableEffectDefinition.getGameId() != null) returnObject.put("gameId", variableEffectDefinition.getGameId());
                if (variableEffectDefinition.getEffectValue() != null) returnObject.put("effectValue", variableEffectDefinition.getEffectValue());
                if (variableEffectDefinition.getEffectCount() != null) returnObject.put("effectCount", variableEffectDefinition.getEffectCount());
                if (variableEffectDefinition.getDependsOn() != null) returnObject.put("dependsOn", JsonBeanSerialiser.serialiseToJson(variableEffectDefinition.getDependsOn()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };
}
