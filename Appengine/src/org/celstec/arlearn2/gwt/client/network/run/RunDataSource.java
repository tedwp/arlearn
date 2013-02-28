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
package org.celstec.arlearn2.gwt.client.network.run;

import org.celstec.arlearn2.gwt.client.control.ReadyCallback;
import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.GenericDataSource;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;

import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
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
		addField(BOOLEAN_DATA_TYPE, "deleted", false, true);

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
				
//				ListGridRecord rec = new ListGridRecord();
//				rec.setAttribute("runId", runId);
//				removeData(rec);
				Criteria crit = new Criteria();
				crit.addCriteria("runId", ""+runId);
				deleteData(crit);
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
	
	public void recordExists( DSCallback cb) {
		
		fetchData(null, cb);
		
	}

}
