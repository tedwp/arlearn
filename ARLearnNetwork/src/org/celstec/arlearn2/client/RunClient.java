package org.celstec.arlearn2.client;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
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
	
	public Run getRun(long runId, String token) {
		return (Run) executeGet(getUrlPrefix()+"/runId/"+runId, token, Run.class);
	}
	
	public RunList getRuns (String token) {
		HttpResponse response = conn.executeGET(getUrlPrefix(), token, "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(response.getEntity(), "utf-8");
			Object jsonObject = jsonDeserialise(entry, RunList.class);
			if (jsonObject instanceof String) {
				System.err.println("token "+token);
				System.err.println("urlPrefix "+getUrlPrefix());
				System.err.println("jsonObject is string not runlist: "+jsonObject.toString());
			}
			return (RunList) jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			RunList rl = new RunList();
			rl.setError("exception "+e.getMessage());
			return rl;
		}
		
	}
	
	public RunList getRunsParticipate (String token) {
		return (RunList)  executeGet(getUrlPrefix()+"/participate", token, RunList.class);
	}
	
	public RunList getRunsParticipate (String token, Long from) {
		return (RunList)  executeGet(getUrlPrefix()+"/participate?from="+from, token, RunList.class);
	}
	
	public Config getConfig(String  token, Long runId) {
		HttpResponse response = conn.executeGET(getUrlPrefix()+"/config/runId/"+runId, token, "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(response.getEntity(), "utf-8");
			Object jsonObject = jsonDeserialise(entry, Config.class);
			
			return (Config) jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			Config c = new Config();
			c.setError("exception "+e.getMessage());
			return c;
		}
	}
	
	public Run selfRegister(String token, String tagId) {
		return (Run)  executeGet(getUrlPrefix()+"/selfRegister/tagId/"+tagId, token, Run.class);

	}
	
	public Run createRun(String token, Run run) {
		HttpResponse response = ConnectionFactory.getConnection().executePOST(getUrlPrefix(), token, "application/json", toJson(run), "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(response.getEntity(), "utf-8");
			return (Run) jsonDeserialise(entry, Run.class);
		} catch (Exception e) {
			e.printStackTrace();
			Run r = new Run();
			r.setError("exception "+e.getMessage());
			return r;
		}
	}


}
