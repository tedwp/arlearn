package org.celstec.arlearn2.beans.run;

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
public class VariableInstance extends Bean {

    private String account;
    private Long runId;
    private String teamId;
    private Long value;
    private Long gameId;
    private String name;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
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


    public static BeanDeserializer deserializer = new BeanDeserializer(){

        @Override
        public VariableInstance toBean(JSONObject object) {
            VariableInstance bean = new VariableInstance();
            try {
                initBean(object, bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            VariableInstance bean = (VariableInstance) genericBean;
            if (object.has("account")) bean.setAccount(object.getString("account"));
            if (object.has("runId")) bean.setRunId(object.getLong("runId"));
            if (object.has("teamId")) bean.setTeamId(object.getString("teamId"));
            if (object.has("value")) bean.setValue(object.getLong("value"));
            if (object.has("gameId")) bean.setGameId(object.getLong("gameId"));
            if (object.has("name")) bean.setName(object.getString("name"));
        }
    };

    public static BeanSerializer serializer = new BeanSerializer () {

        @Override
        public JSONObject toJSON(Object bean) {
            VariableInstance teamBean = (VariableInstance) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (teamBean.getAccount() != null) returnObject.put("account", teamBean.getAccount());
                if (teamBean.getRunId() != null) returnObject.put("runId", teamBean.getRunId());
                if (teamBean.getTeamId() != null) returnObject.put("teamId", teamBean.getTeamId());
                if (teamBean.getValue() != null) returnObject.put("value", teamBean.getValue());
                if (teamBean.getGameId() != null) returnObject.put("gameId", teamBean.getGameId());
                if (teamBean.getName() != null) returnObject.put("name", teamBean.getName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };
}
