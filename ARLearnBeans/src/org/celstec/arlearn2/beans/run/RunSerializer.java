package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.beans.serializer.json.RunBeanSerialiser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RunSerializer extends RunBeanSerialiser {
	public static final BeanSerializer runConfigSerializer = new BeanSerializer() {
		@Override
		public JSONObject toJSON(Object bean) {
			RunConfig runBean = (RunConfig) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (runBean.getSelfRegistration() != null) returnObject.put("selfRegistration", runBean.getSelfRegistration());
				if (runBean.getNfcTag() != null) returnObject.put("nfcTag", runBean.getNfcTag());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};
	
	
	@Override
	public JSONObject toJSON(Object bean) {
		Run runBean = (Run) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (runBean.getGameId() != null) returnObject.put("gameId", runBean.getGameId());
			if (runBean.getStartTime() != null) returnObject.put("startTime", runBean.getStartTime());
			if (runBean.getServerCreationTime() != null) returnObject.put("serverCreationTime", runBean.getServerCreationTime());
			if (runBean.getLastModificationDate() != null) returnObject.put("lastModificationDate", runBean.getLastModificationDate());
			if (runBean.getTitle() != null) returnObject.put("title", runBean.getTitle());
			if (runBean.getOwner() != null) returnObject.put("owner", runBean.getOwner());
			if (runBean.getGame() != null) returnObject.put("game", JsonBeanSerialiser.serialiseToJson(runBean.getGame()));
			if (runBean.getRunConfig() != null) returnObject.put("runConfig", JsonBeanSerialiser.serialiseToJson(runBean.getRunConfig(), runConfigSerializer));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
