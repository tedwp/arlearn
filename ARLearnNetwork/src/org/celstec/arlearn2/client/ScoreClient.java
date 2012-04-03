package org.celstec.arlearn2.client;

import java.lang.reflect.InvocationTargetException;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.game.ScoreDefinition;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.UserScore;
import org.celstec.arlearn2.network.ConnectionFactory;

public class ScoreClient extends GenericClient{

	private static ScoreClient instance;

	private ScoreClient() {
		super("/scoring");
	}
	
	public static ScoreClient getScoreClient() {
		if (instance == null) {
			instance = new ScoreClient();
		}
		return instance;
	}
	
	public UserScore getScore (String token, Long runId) {
		HttpResponse response = conn.executeGET(getUrlPrefix()+"/runId/"+runId, token, "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(response.getEntity());
			return (UserScore) jsonDeserialise(entry, UserScore.class);
		} catch (Exception e) {
			e.printStackTrace();
			UserScore usError = new UserScore();
			usError.setError("exception "+e.getMessage());
			return usError;
		}	
	}
	
	public ScoreDefinition createScoreDefinition(String token, ScoreDefinition sd) {
		return (ScoreDefinition) executePost(getUrlPrefix(), token, sd, ScoreDefinition.class);
	}
	
	
}
