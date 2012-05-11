package org.celstec.arlearn2.client;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.network.ConnectionFactory;

public class RunClient extends GenericClient{

	private static RunClient instance;

	private RunClient() {
		super("/myRuns");
	}
	
	public static RunClient getRunClient() {
		if (instance == null) {
			instance = new RunClient();
		}
		return instance;
	}
	
	public RunList getRuns (String token) {
		HttpResponse response = conn.executeGET(getUrlPrefix(), token, "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(response.getEntity());
			Object jsonObject = jsonDeserialise(entry, RunList.class);
			return (RunList) jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			RunList rl = new RunList();
			rl.setError("exception "+e.getMessage());
			return rl;
		}
	}
	
	public Run getRun(long runId, String token) {
		HttpResponse response = conn.executeGET(getUrlPrefix()+"/runId/"+runId, token, "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(response.getEntity());
			Object jsonObject = jsonDeserialise(entry, Run.class);
			return (Run) jsonObject;
		} catch (Exception e) {
			Run rl = new Run();
			rl.setError("exception "+e.getMessage());
			return rl;
		}
	}
	
	public RunList getRunsParticipate (String token) {
		HttpResponse response = conn.executeGET(getUrlPrefix()+"/participate", token, "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(response.getEntity());
			Object jsonObject = jsonDeserialise(entry, RunList.class);
			return (RunList) jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			RunList rl = new RunList();
			rl.setError("exception "+e.getMessage());
			return rl;
		}
		
	}
	
	public Config getConfig(String  token, Long runId) {
		HttpResponse response = conn.executeGET(getUrlPrefix()+"/config/runId/"+runId, token, "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(response.getEntity());
			Object jsonObject = jsonDeserialise(entry, Config.class);
			
			return (Config) jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			Config c = new Config();
			c.setError("exception "+e.getMessage());
			return c;
		}
	}
	
	public Run createRun(String token, Run run) {
		HttpResponse response = ConnectionFactory.getConnection().executePOST(getUrlPrefix(), token, "application/json", toJson(run), "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(response.getEntity());
			return (Run) jsonDeserialise(entry, Run.class);
		} catch (Exception e) {
			e.printStackTrace();
			Run r = new Run();
			r.setError("exception "+e.getMessage());
			return r;
		}
	}


}