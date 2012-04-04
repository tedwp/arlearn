package org.celstec.arlearn2.local;

import java.io.File;
import java.util.Properties;
import java.util.StringTokenizer;


public class LoadData {
	
	public static String root = "/Users/str/frequent/mobileLearning/ARLearn2GoogleProject/ARLearn2Libary/curl/florence/2011/arlearn1/";
	public static String authToken = "";

	
	public static void main(String[] args) {
		System.out.println("test");
		java.util.Properties p = PropertiesUtil.getProperties(new File(root+"props.txt"));
		authToken = p.getProperty("authtoken");
		processProperties(p);
	}

	private static void processProperties(Properties p) {
		StringTokenizer st = new StringTokenizer(p.getProperty("allitems"), ",");
		while (st.hasMoreTokens()) {
		  processGenItem(st.nextToken(), p);
		}

	}

	private static void processGenItem(String id, Properties p) {
		String account = p.getProperty(id+".account");
		String htmlFile = p.getProperty(id+".htmlFile");
		String title = p.getProperty(id+".title");
		String dependsOnAction = p.getProperty(id+".dependsOn.action");
		String dependsOnScope = p.getProperty(id+".dependsOn.scope");
		System.out.println(account+ " "+authToken);
		System.out.println(account+ " "+htmlFile);
		
		publishGenItem(account, htmlFile, title, dependsOnAction, dependsOnScope);
	}

	private static void publishGenItem(String account, String htmlFile, String title, String dependsOnAction, String dependsOnScope) {
		
		
	}
}
