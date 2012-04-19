package org.celstec.arlearn2.gwt.client.network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.celstec.arlearn2.gwt.client.control.ReadyCallback;
import org.celstec.arlearn2.gwt.client.control.TriggerDataSource;

import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public abstract class GenericDataSource extends DataSource {
	protected final int STRING_DATA_TYPE = 1;
	protected final int INTEGER_DATA_TYPE = 2;

	private List<DataSourceField> fieldList = new ArrayList<DataSourceField>();
	private List<String> attributeList = new ArrayList<String>();
	private List<Integer> typeList = new ArrayList<Integer>();
	
	private String pkAttribute;
	private HashSet<String> pkMap = new HashSet<String>();
	private ArrayList<DerivedFieldTask> derivedTaskList = new ArrayList<DerivedFieldTask>();
	private ArrayList<Boolean> derivedTaskPrimaryKeyList = new ArrayList<Boolean>();
	private ArrayList<Boolean> derivedTaskHiddenList = new ArrayList<Boolean>();


	protected GenericDataSource() {
		initFields();
		setFields(fieldList.toArray(new DataSourceField[0]));
		setClientOnly(true);
	}
	
	protected void addField(int dataType, String attributeName, boolean primaryKey, boolean hidden) {
		DataSourceField field = null;
		if (primaryKey) pkAttribute = attributeName;
		switch (dataType) {
		case STRING_DATA_TYPE:
			field = new DataSourceTextField(attributeName);
			typeList.add(STRING_DATA_TYPE);
			break;
		case INTEGER_DATA_TYPE:
			field = new DataSourceIntegerField(attributeName);
			typeList.add(INTEGER_DATA_TYPE);
			break;
		default:
			break;
		}
		field.setHidden(hidden);
		field.setPrimaryKey(primaryKey);
		fieldList.add(field);
		attributeList.add(attributeName);
	}
	
	protected void addDerivedField(DerivedFieldTask task, boolean primaryKey, boolean hidden) {
		derivedTaskList.add(task);
		String attribute =  task.getTargetFieldName();
		addField(INTEGER_DATA_TYPE,attribute, primaryKey, hidden);
		attributeList.remove(attribute);
	}
	
	protected void processDerivedFields(ListGridRecord rec) {
		for (DerivedFieldTask task: derivedTaskList) {
			String[] sources = task.getSourceFieldName();
			String[] values = new String[sources.length];
			for (int i = 0; i < sources.length; i++) {
				values[i] = rec.getAttribute(sources[i]);
			}
			rec.setAttribute(task.getTargetFieldName(), task.processValue(values));
		}
		
	}
	
	protected abstract void initFields();
	protected abstract GenericClient getHttpClient();
	protected abstract String getBeanType();

	//TODO delete items that were not in the list
	public void loadData(ReadyCallback rc){
		callback.setReadyCallback(rc);
		getHttpClient().getItems(callback);
	}
	
	public void loadDataRun(long runId) {
		getHttpClient().getItemsForRun(runId, callback);
	}
	
	public void loadDataGame(long gameId) {
		getHttpClient().getItemsForGame(gameId, callback);
	}
	
	
	
	private JsonCallback callback = new JsonCallback(getBeanType()) {

		@Override
		public void onError() {}
		
		
		
		@Override
		public void onReceived() {
			HashSet<String> deleteSet = (HashSet<String>) pkMap.clone();
			for (int i = 0; i< size(); i++) {
				final ListGridRecord rec = new ListGridRecord();
				Criteria searchCrit = null;
				for (int j = 0; j< attributeList.size(); j++) {
					String attributeName = attributeList.get(j);
					switch (typeList.get(j)) {
					case STRING_DATA_TYPE:
						String value = getAttributeString(i, attributeName);
						rec.setAttribute(attributeName, value);
						if (pkAttribute != null && pkAttribute.equals(attributeName)) searchCrit = new Criteria(pkAttribute, value);
						break;
					case INTEGER_DATA_TYPE:
						int intValue = getAttributeInteger(i, attributeName);
						rec.setAttribute(attributeName, intValue);
						if (pkAttribute != null && pkAttribute.equals(attributeName)) {
							searchCrit = new Criteria();
							searchCrit.addCriteria(pkAttribute, intValue);
						}
					}

				}
				if ("gameId".equals(pkAttribute)){
					TriggerDataSource.getInstance().processGameConfig(rec.getAttributeAsInt("gameId"), getGameConfig(i)) ;

				}
				GenericDataSource.this.processDerivedFields(rec);
				String id = ""+rec.getAttribute(pkAttribute);
				if (pkMap.contains(id)) {
					deleteSet.remove(id);
					updateData(rec);
				} else {
					addData(rec);
					pkMap.add(id);
				}
			}
			for (String id: deleteSet) {
				System.out.println("delete "+id);
				final ListGridRecord rec = new ListGridRecord();
				rec.setAttribute(pkAttribute, id);
				removeData(rec);
				pkMap.remove(id);
			}
			if (rc != null) {
				rc.ready();
			}
			
		}
		
		
		
	};

	
}
