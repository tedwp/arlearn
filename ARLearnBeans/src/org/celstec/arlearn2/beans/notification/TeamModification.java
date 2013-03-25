package org.celstec.arlearn2.beans.notification;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class TeamModification extends NotificationBean {
	
	public final static int ALTERED = TEAM_ALTERED;

	private Integer modificationType;
	private Long runId;
	
	public TeamModification(){}

	public Integer getModificationType() {
		return modificationType;
	}

	public void setModificationType(Integer modificationType) {
		this.modificationType = modificationType;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	@Override
	public boolean equals(Object obj) {
		TeamModification other = (TeamModification ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getModificationType(), other.getModificationType()); 
	}
	
	public static BeanDeserializer deserializer = new BeanDeserializer(){

		@Override
		public TeamModification toBean(JSONObject object) {
			TeamModification bean = new TeamModification();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			TeamModification bean = (TeamModification) genericBean;
			if (object.has("modificationType")) bean.setModificationType(object.getInt("modificationType"));
			if (object.has("runId")) bean.setRunId(object.getLong("runId"));
		}
	};
	
	public static BeanSerializer serializer = new BeanSerializer () {

		@Override
		public JSONObject toJSON(Object bean) {
			TeamModification teamBean = (TeamModification) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (teamBean.getModificationType() != null) returnObject.put("modificationType", teamBean.getModificationType());
				if (teamBean.getRunId() != null) returnObject.put("runId", teamBean.getRunId());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};
}
