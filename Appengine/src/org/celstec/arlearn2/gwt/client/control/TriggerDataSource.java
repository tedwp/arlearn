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
package org.celstec.arlearn2.gwt.client.control;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;


public class TriggerDataSource extends DataSource{
	
	public static TriggerDataSource instance;
	
	private TriggerDataSource() {
		DataSourceIntegerField itemIdField = new DataSourceIntegerField("itemId");
		itemIdField.setHidden(true);
		itemIdField.setRequired(true);
		itemIdField.setPrimaryKey(true);
		
		DataSourceTextField titleField = new DataSourceTextField("name");
		titleField.setRequired(true);
		
		
		
		DataSourceIntegerField gameIdField = new DataSourceIntegerField("gameId");
		
		DataSourceBooleanField autoLaunchField = new DataSourceBooleanField("autoLaunch");
		
		setFields(itemIdField,titleField, gameIdField, autoLaunchField);
		setClientOnly(true);
	}
	public static TriggerDataSource getInstance() {
		if (instance == null) instance = new TriggerDataSource();
		return instance;
	}
	
	public void processGameConfig(long gameId, JSONObject gameConfig) {	
		if (gameConfig == null) return;
		if (gameConfig.isObject() ==null) return;
		JSONArray array = null;
		if (gameConfig.get("manualItems") == null) {
			array = new JSONArray();
		} else {
			array = gameConfig.get("manualItems").isArray();	
		}
		
		try {
		for (int i = 0; i< array.size();i++) {
			final ListGridRecord rec = new ListGridRecord();
			int itemId = (int) array.get(i).isObject().get("id").isNumber().doubleValue();
			if (array.get(i).isObject().containsKey("autoLaunch")) {
				rec.setAttribute("autoLaunch", array.get(i).isObject().get("autoLaunch").isBoolean().booleanValue());
			}
			rec.setAttribute("itemId", itemId);
			rec.setAttribute("gameId", (int) gameId);
			rec.setAttribute("name", array.get(i).isObject().get("name").isString().stringValue());
			Criteria crit = new Criteria();
			crit.addCriteria("itemId", itemId);
			fetchData(crit, new DSCallback() {
				
				@Override
				public void execute(DSResponse response, Object rawData, DSRequest request) {
					if (response.getData().length == 0) {
						addData(rec);
					} else {
						updateData(rec);
					}
				}
			});
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
public void deleteData (Criteria crit) {
		
		fetchData(crit, new DSCallback() {
			@Override
			public void execute(DSResponse response,
					Object rawData, DSRequest request) {
				Record[] records = response.getData();
				for (Record record : records) {
					removeData(record);
				}
			}
		});
	}
	
}
