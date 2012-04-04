package org.celstec.arlearn2.fusion;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import com.google.gdata.client.ClientLoginAccountType;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.Service.GDataRequest;
import com.google.gdata.client.Service.GDataRequest.RequestType;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;

public class FusionClient {

	private static final String SERVICE_URL = "https://www.google.com/fusiontables/api/query";
	private static final Pattern CSV_VALUE_PATTERN = Pattern
			.compile("([^,\\r\\n\"]*|\"(([^\"]*\"\")*[^\"]*)\")(,|\\r?\\n)");
	private GoogleService service;

	public FusionClient(String email, String password)
			throws AuthenticationException {
		service = new GoogleService("fusiontables", "ARLearn2");
		service.setUserCredentials(email, password,
				ClientLoginAccountType.GOOGLE);
	}

	public FusionClient(String authToken) throws AuthenticationException {
		service = new GoogleService("fusiontables", "fusiontables.ApiExample");
		service.setUserToken(authToken);
	}

	public CSV runSelect(String selectQuery) throws IOException,
			ServiceException {
		return getQuery("?sql="+ URLEncoder.encode(selectQuery, "UTF-8"));
	}
	
	public CSV showTables() throws IOException, ServiceException {
		return getQuery("?sql="+URLEncoder.encode("SHOW TABLES", "UTF-8"));
	}
	
	private CSV getQuery(String query) throws IOException,
			ServiceException {
		URL url = new URL(SERVICE_URL + query);
		GDataRequest request = service.getRequestFactory().getRequest(
				RequestType.QUERY, url, ContentType.TEXT_PLAIN);

		request.execute();
		return new CSV(request.getResponseStream(), "UTF-8");
	}
	
	public CSV runCreate(String createString) throws IOException,
			ServiceException {
		URL url = new URL(SERVICE_URL);
		GDataRequest request = service.getRequestFactory().getRequest(
				RequestType.INSERT, url,
				new ContentType("application/x-www-form-urlencoded"));
		OutputStreamWriter writer = new OutputStreamWriter(
				request.getRequestStream());
		writer.append("sql=" + URLEncoder.encode(createString, "UTF-8"));
		writer.flush();

		request.execute();
		return new CSV(request.getResponseStream(), "UTF-8");
	}
	
	public void runDelete(int tableId, String where) throws IOException, ServiceException {
		CSV res = runSelect("SELECT rowid FROM "+tableId+ " "+where);
		for (int i = 0 ; i<res.rowSize(); i++){
			runCreate("DELETE FROM "+tableId+ " WHERE ROWID = '"+res.getValue(i, 0)+"' ");
		}
	}
	
	public void runDelete(long tableId, String where) throws IOException, ServiceException {
		CSV res = runSelect("SELECT rowid FROM "+tableId+ " "+where);
		for (int i = 0 ; i<res.rowSize(); i++){
			runCreate("DELETE FROM "+tableId+ " WHERE ROWID = '"+res.getValue(i, 0)+"' ");
		}
	}
	
	public static void main(String[] args) throws IOException, ServiceException {
		FusionClient fc = new FusionClient("");
		
//	    CSV csv = fc.runSelect("select * from 615277 ");
//	    System.out.println(csv.getColumnName(1));
//	    System.out.println(csv.columnSize());
//	    System.out.println(csv.rowSize());
//	    System.out.println(csv.getValue(0, 0));
//	    System.out.println(csv.getValue(0, 1));
//	    System.out.println(csv.getValue(0, 2));
//		
//		 csv = fc.showTables();
//		System.out.println(csv.columnSize());
//	    System.out.println(csv.rowSize());
//	    System.out.println(csv.getValue(0, 1));
//	    System.out.println(csv.containsValue(csv.getColumnIndex("name"), "testes"));
//	    System.out.println(csv.containsValue(csv.getColumnIndex("table id"), "testes"));
		
		CSV csv = fc.runCreate("CREATE TABLE SaleInformation (customer: NUMBER, memo: STRING, 'sale location': LOCATION)");
		System.out.println(csv.columnSize());
	    System.out.println(csv.rowSize());
	    System.out.println(csv.getValue(0, 0));
	}
}
