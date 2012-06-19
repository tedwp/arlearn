package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.RunBeanDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class UserScoreDeserializer extends RunBeanDeserializer{

	@Override
	public UserScore toBean(JSONObject object) {
		UserScore bean = new UserScore();
		try {
			initBean(object, bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		UserScore bean = (UserScore) genericBean;
		if (object.has("userScore")) bean.setUserScore(object.getLong("userScore"));
		if (object.has("teamScore")) bean.setTeamScore(object.getLong("teamScore"));
		if (object.has("allScore")) bean.setAllScore(object.getLong("allScore"));
		if (object.has("totalScore")) bean.setTotalScore(object.getLong("totalScore"));

	}
}
