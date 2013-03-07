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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
	
	public RunList getRunsByTag (String token, String tag) {
		try {
			tag = URLEncoder.encode(tag, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return (RunList)  executeGet(getUrlPrefix()+"/tagId/"+tag, token, RunList.class);
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
		try {
			tagId = URLEncoder.encode(tagId, "UTF-8");
			return (Run)  executeGet(getUrlPrefix()+"/selfRegister/tagId/"+tagId, token, Run.class);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null; //UTF8 should be supported so we don't get here

	}
	
	public Run selfRegister(String token, Long runId) {
		return (Run)  executeGet(getUrlPrefix()+"/selfRegister/runId/"+runId, token, Run.class);

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
