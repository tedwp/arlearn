package org.celstec.arlearn2.beans.run;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RunAccessList extends Bean{

	public static String RunAccessType = "org.celstec.arlearn2.beans.run.RunAccess";

	private List<RunAccess> runAccess = new ArrayList<RunAccess>();

	public RunAccessList() {

	}

	private Long serverTime;

	public Long getServerTime() {
		return serverTime;
	}

	public void setServerTime(Long serverTime) {
		this.serverTime = serverTime;
	}

	public List<RunAccess> getRunAccess() {
		return runAccess;
	}

	public void setRunAccess(List<RunAccess> runAccess) {
		this.runAccess = runAccess;
	}

	public void addRunAccess(RunAccess ga) {
		runAccess.add(ga);
	}

	public static BeanSerializer serializer = new BeanSerializer() {

		@Override
		public JSONObject toJSON(Object bean) {
			RunAccessList gal = (RunAccessList) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (gal.getServerTime() != null)
					returnObject.put("serverTime", gal.getServerTime());
				if (gal.getRunAccess() != null)
					returnObject.put("runAccess", ListSerializer.toJSON(gal.getRunAccess()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};

	public static BeanDeserializer deserializer = new BeanDeserializer() {

		@Override
		public RunAccessList toBean(JSONObject object) {
			RunAccessList tl = new RunAccessList();
			try {
				initBean(object, tl);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return tl;
		}

		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			RunAccessList giList = (RunAccessList) genericBean;
			if (object.has("serverTime"))
				giList.setServerTime(object.getLong("serverTime"));
			if (object.has("runAccess"))
				giList.setRunAccess(ListDeserializer.toBean(object.getJSONArray("runAccess"), RunAccess.class));
		}
	};
}

