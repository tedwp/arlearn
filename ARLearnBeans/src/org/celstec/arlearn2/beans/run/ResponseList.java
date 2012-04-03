package org.celstec.arlearn2.beans.run;

import java.util.ArrayList;
import java.util.List;

public class ResponseList  extends RunBean{
	
	public static String responsesType = "org.celstec.arlearn2.beans.run.Response";
	
	private List<Response> responses = new ArrayList<Response>();

	public ResponseList() {

	}
	public List<Response> getResponses() {
		return responses;
	}

	public void setResponses(List<Response> responses) {
		this.responses = responses;
	}
	public void addResponse(Response r) {
		this.responses.add(r);
	}
}
