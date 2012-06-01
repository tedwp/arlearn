package org.celstec.arlearn2.gwt.client.network;

public class ChannelClient extends GenericClient {

	private static ChannelClient instance;
	private ChannelClient() {
	}
	
	public static ChannelClient getInstance() {
		if (instance == null) instance = new ChannelClient();
		return instance;
	}
	
	public void getToken(final JsonCallback jcb) {
		invokeJsonGET("/token", jcb);
	}
	
	public String getUrl() {
		return super.getUrl() + "channelAPI";
	}
}
