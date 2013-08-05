package org.celstec.arlearn2.beans.game;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class VariableDefinition extends Bean{
	
	private String name;
    private Long minValue;
    private Long maxValue;
    private Long startValue;
    private Integer scope;

    private Long gameId;

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public Long getMinValue() {
        return minValue;
    }

    public void setMinValue(Long minValue) {
        this.minValue = minValue;
    }

    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
    }

    public Long getStartValue() {
        return startValue;
    }

    public void setStartValue(Long startValue) {
        this.startValue = startValue;
    }

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public static BeanDeserializer deserializer = new BeanDeserializer(){

		@Override
		public VariableDefinition toBean(JSONObject object) {
			VariableDefinition bean = new VariableDefinition();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			VariableDefinition bean = (VariableDefinition) genericBean;
			if (object.has("name")) bean.setName(object.getString("name"));
            if (object.has("minValue")) bean.setMinValue(object.getLong("minValue"));
            if (object.has("maxValue")) bean.setMaxValue(object.getLong("maxValue"));
            if (object.has("startValue")) bean.setStartValue(object.getLong("startValue"));
            if (object.has("scope")) bean.setScope(object.getInt("scope"));
            if (object.has("gameId")) bean.setGameId(object.getLong("gameId"));
			
		}
	};
	
	public static BeanSerializer serializer = new BeanSerializer () {

		@Override
		public JSONObject toJSON(Object bean) {
			VariableDefinition teamBean = (VariableDefinition) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (teamBean.getName() != null) returnObject.put("name", teamBean.getName());
                if (teamBean.getMaxValue() != null) returnObject.put("maxValue", teamBean.getMaxValue());
                if (teamBean.getMinValue() != null) returnObject.put("minValue", teamBean.getMinValue());
                if (teamBean.getStartValue() != null) returnObject.put("startValue", teamBean.getStartValue());
                if (teamBean.getScope() != null) returnObject.put("scope", teamBean.getScope());
                if (teamBean.getGameId() != null) returnObject.put("gameId", teamBean.getGameId());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};
}
