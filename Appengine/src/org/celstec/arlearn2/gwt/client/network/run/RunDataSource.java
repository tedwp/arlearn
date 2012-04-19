package org.celstec.arlearn2.gwt.client.network.run;

import org.celstec.arlearn2.gwt.client.control.ReadyCallback;
import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.GenericDataSource;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;

import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RunDataSource extends GenericDataSource {
	
	public static RunDataSource instance;

	public static RunDataSource getInstance() {
		if (instance == null)
			instance = new RunDataSource();
		return instance;
	}

	private RunDataSource() {
		super();
	}
	
	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, "runId", true, true);
		addField(INTEGER_DATA_TYPE, "gameId", false, true);
		addField(STRING_DATA_TYPE, "title", false, true);
		addField(STRING_DATA_TYPE, "owner", false, true);
	}
	
	protected GenericClient getHttpClient() {
		return RunClient.getInstance();
	}

	@Override
	protected String getBeanType() {
		return "runs";
	}
	
	public void delete(final long runId) {
		getHttpClient().deleteItemsForRun(runId, new JsonCallback() {
			
			@Override
			public void onJsonReceived(JSONValue jsonValue) {
				
				ListGridRecord rec = new ListGridRecord();
				rec.setAttribute("runId", runId);
				removeData(rec);
//				RunDataSource.getInstance().loadRuns(new ReadyCallback() {
//					
//					@Override
//					public void ready() {
//						listGrid.fetchData();
//					}
//				});	
			}
		});
		
		
	}

}
