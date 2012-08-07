package org.celstec.arlearn2.client;

import org.celstec.arlearn2.beans.run.Run;

public class ChannelClient extends GenericClient{
	
	private static ChannelClient instance;

	private ChannelClient() {
		super("/channelAPI");
	}

	public static ChannelClient getChannelClient() {
		if (instance == null) {
			instance = new ChannelClient();
		}
		return instance;
	}
	
	public void pong(long runId, String token, String from, String to, int requestType, String response, long origTimeStamp) {
		executePost(getUrlPrefix()+"/pong/"+from+"/"+to+"/"+requestType+"/"+origTimeStamp, token, response, null);
	}

}
