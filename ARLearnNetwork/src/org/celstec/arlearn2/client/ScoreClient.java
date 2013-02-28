/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
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
			entry = EntityUtils.toString(response.getEntity(), "utf-8");
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
