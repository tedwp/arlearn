package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.serializer.json.RunBeanSerialiser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class UserScoreSerializer extends RunBeanSerialiser {

	@Override
	public JSONObject toJSON(Object bean) {
		UserScore userScore = (UserScore) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (userScore.getUserScore() != null) returnObject.put("userScore", userScore.getUserScore());
			if (userScore.getTeamScore() != null) returnObject.put("teamScore", userScore.getTeamScore());
			if (userScore.getAllScore() != null) returnObject.put("allScore", userScore.getAllScore());
			if (userScore.getTotalScore() != null) returnObject.put("totalScore", userScore.getTotalScore());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
