package org.celstec.arlearn2.gwt.client.control;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
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
		titleField.setRequired(true);
		
		
		setFields(itemIdField,titleField, gameIdField);
		setClientOnly(true);

//		ListGridRecord rec = new ListGridRecord();
//		rec.setAttribute("itemId", "307");
//		rec.setAttribute("name", "Planning A");
//		addData(rec);
//		rec = new ListGridRecord();
//		rec.setAttribute("itemId", "406");
//		rec.setAttribute("name", "Planning B");
//		addData(rec);
//		rec = new ListGridRecord();
//		rec.setAttribute("itemId", "506");
//		rec.setAttribute("name", "Planning C");
//		addData(rec);
//		
//		 rec = new ListGridRecord();
//		rec.setAttribute("itemId", "315");
//		rec.setAttribute("name", "Call from hostage taker A");
//		addData(rec);
//		
//		rec = new ListGridRecord();
//		rec.setAttribute("itemId", "320");
//		rec.setAttribute("name", "HIM team has arrived");
//		addData(rec);
	}
	public static TriggerDataSource getInstance() {
		if (instance == null) instance = new TriggerDataSource();
		return instance;
	}
	
	public void processGameConfig(long gameId, JSONObject gameConfig) {	
		if (gameConfig == null) return;
		if (gameConfig.isObject() ==null) return;
		JSONArray array = gameConfig.get("manualItems").isArray();
		for (int i = 0; i< array.size();i++) {
			final ListGridRecord rec = new ListGridRecord();
			int itemId = (int) array.get(i).isObject().get("id").isNumber().doubleValue();
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
	}
}
