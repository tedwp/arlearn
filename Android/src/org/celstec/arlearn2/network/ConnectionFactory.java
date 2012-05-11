package org.celstec.arlearn2.network;

public class ConnectionFactory {
	
	
	public static HttpConnection instance;
	
	public static HttpConnection getConnection() {
		if (instance == null) {
			instance = new DesktopConnection();
		}
		return instance;
	}
}
