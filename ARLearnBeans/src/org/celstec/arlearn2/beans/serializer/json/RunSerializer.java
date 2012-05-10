package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.run.Run;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RunSerializer extends RunBeanSerialiser {
	
	@Override
	public JSONObject toJSON(Object bean) {
		Run runBean = (Run) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (runBean.getGameId() != null) returnObject.put("gameId", runBean.getGameId());
			if (runBean.getStartTime() != null) returnObject.put("startTime", runBean.getStartTime());
			if (runBean.getServerCreationTime() != null) returnObject.put("serverCreationTime", runBean.getServerCreationTime());
			if (runBean.getTitle() != null) returnObject.put("title", runBean.getTitle());
			if (runBean.getOwner() != null) returnObject.put("owner", runBean.getOwner());
			if (runBean.getGame() != null) returnObject.put("game", JsonBeanSerialiser.serialiseToJson(runBean.getGame()));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
