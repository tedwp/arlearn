package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RunAccess extends Bean{
	
	private String account;
	private Integer accessRights;
	private Long runId;
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getAccessRights() {
		return accessRights;
	}

	public void setAccessRights(Integer accessRights) {
		this.accessRights = accessRights;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public static BeanDeserializer deserializer = new BeanDeserializer(){

		@Override
		public RunAccess toBean(JSONObject object) {
			RunAccess bean = new RunAccess();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			RunAccess bean = (RunAccess) genericBean;
			if (object.has("account")) bean.setAccount(object.getString("account"));
			if (object.has("accessRights")) bean.setAccessRights(object.getInt("accessRights"));
			if (object.has("runId")) bean.setRunId(object.getLong("runId"));
		}
	};
	
	public static BeanSerializer serializer = new BeanSerializer () {

		@Override
		public JSONObject toJSON(Object bean) {
			RunAccess teamBean = (RunAccess) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (teamBean.getAccount() != null) returnObject.put("account", teamBean.getAccount());
				if (teamBean.getAccessRights() != null) returnObject.put("accessRights", teamBean.getAccessRights());
				if (teamBean.getRunId() != null) returnObject.put("runId", teamBean.getRunId());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};
}

