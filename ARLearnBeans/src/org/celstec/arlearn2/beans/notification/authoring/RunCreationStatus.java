package org.celstec.arlearn2.beans.notification.authoring;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RunCreationStatus extends Bean{

	public final static int RUN_CREATED = 0;
	public final static int FIRST_TEAM_CREATED = 1;
	public final static int FIRST_USER_CREATED = 2;
	
	private Long runId;
	private Long gameId;
	private Integer status;
	
	public Long getRunId() {
		return runId;
	}
	public void setRunId(Long runId) {
		this.runId = runId;
	}
	public Long getGameId() {
		return gameId;
	}
	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Override
	public boolean equals(Object obj) {
		RunCreationStatus other = (RunCreationStatus ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getGameId(), other.getGameId()) && 
			nullSafeEquals(getRunId(), other.getRunId()) && 
			nullSafeEquals(getStatus(), other.getStatus()); 

	}
	
	public static class RunCreationStatusDeserializer extends BeanDeserializer{

		@Override
		public RunCreationStatus toBean(JSONObject object) {
			RunCreationStatus bean = new RunCreationStatus();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			RunCreationStatus bean = (RunCreationStatus) genericBean;
			if (object.has("runId")) bean.setRunId(object.getLong("runId"));
			if (object.has("gameId")) bean.setGameId(object.getLong("gameId"));
			if (object.has("status")) bean.setStatus(object.getInt("status"));
		}

	}
	
	public static class GameCreationStatusSerializer extends BeanSerializer{

		
		@Override
		public JSONObject toJSON(Object bean) {
			RunCreationStatus statusBean = (RunCreationStatus) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (statusBean.getRunId() != null) returnObject.put("runId", statusBean.getRunId());
				if (statusBean.getGameId() != null) returnObject.put("gameId", statusBean.getGameId());
				if (statusBean.getStatus() != null) returnObject.put("status", statusBean.getStatus());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}

	}


}
