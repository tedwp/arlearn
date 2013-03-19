package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public abstract class DataSourceModel {

	protected final int STRING_DATA_TYPE = 1;
	protected final int INTEGER_DATA_TYPE = 2;
	protected final int STRING_AR_DATA_TYPE = 3;
	protected final int BOOLEAN_DATA_TYPE = 4;
	protected final int LONG_DATA_TYPE = 5;
	protected final int DOUBLE_DATA_TYPE = 6;

	protected String pkAttribute;
	protected DataSourceAdapter dataSourceAdapter;

	private List<Integer> typeList = new ArrayList<Integer>();
	private List<String> attributeList = new ArrayList<String>();
	private ArrayList<DerivedFieldTask> derivedTaskList = new ArrayList<DerivedFieldTask>();


	public DataSourceModel(DataSourceAdapter dataSourceAdapter) {
		this.dataSourceAdapter = dataSourceAdapter;
		initFields();
		registerForNotifications();
	}
	
	protected abstract void initFields();

	protected abstract void registerForNotifications();

	protected void addField(int dataType, String attributeName, boolean primaryKey, boolean hidden) {
		if (primaryKey)
			pkAttribute = attributeName;
		switch (dataType) {
		case STRING_DATA_TYPE:
			dataSourceAdapter.addDataSourceTextField(attributeName, hidden, primaryKey);
			typeList.add(STRING_DATA_TYPE);
			break;
		case INTEGER_DATA_TYPE:
			dataSourceAdapter.addDataSourceIntegerField(attributeName, hidden, primaryKey);
			typeList.add(INTEGER_DATA_TYPE);
			break;
		case LONG_DATA_TYPE:
			dataSourceAdapter.addDataSourceIntegerField(attributeName, hidden, primaryKey);
			typeList.add(LONG_DATA_TYPE);
			break;
		case DOUBLE_DATA_TYPE:
			dataSourceAdapter.addDataSourceDoubleField(attributeName, hidden, primaryKey);
			typeList.add(DOUBLE_DATA_TYPE);
			break;
		case STRING_AR_DATA_TYPE:
			dataSourceAdapter.addDataSourceTextField(attributeName, hidden, primaryKey);
			typeList.add(STRING_AR_DATA_TYPE);
			break;
		case BOOLEAN_DATA_TYPE:
			dataSourceAdapter.addDataSourceBooleanField(attributeName, hidden, primaryKey);
			typeList.add(BOOLEAN_DATA_TYPE);
			break;
		default:
			break;
		}
		attributeList.add(attributeName);
	}
	
	public void addDerivedField(DerivedFieldTask task, boolean primaryKey, boolean hidden) {
		derivedTaskList.add(task);
		String attribute = task.getTargetFieldName();
		addField(task.getType(), attribute, primaryKey, hidden);
		attributeList.remove(attribute);
	}

	public void addJsonObject(JSONObject object) {
		AbstractRecord record = createRecord(object);
		record.setCorrespondingJsonObject(object);
		dataSourceAdapter.saveRecord(record);
	}
	
	public void removeObject(Object id) {
		dataSourceAdapter.removeRecord(id);
	}
	
	protected AbstractRecord createRecord(JSONObject object) {
		AbstractRecord record = dataSourceAdapter.createRecord();
		record.setCorrespondingJsonObject(object);
		for (int i = 0; i <attributeList.size(); i++) {
			String attribute = attributeList.get(i);
			if (object.containsKey(attribute)) {
				processAttributeValue(record, attribute, object.get(attribute), typeList.get(i));
			}
		}
		for (int i = 0; i <derivedTaskList.size(); i++) {
			DerivedFieldTask task = derivedTaskList.get(i);
			task.setJsonSource(object);
			record.setAttribute(task.getTargetFieldName(), task.process());
		}
		System.out.println("record "+record);
		return record;
	}
	
	private void processAttributeValue(AbstractRecord record, String attribute, JSONValue value, int type) {
		switch (type) {
		case STRING_DATA_TYPE:
			record.setAttribute(attribute, value.isString().stringValue());
			break;
		case INTEGER_DATA_TYPE:
			record.setAttribute(attribute, value.isNumber().doubleValue());
			break;
		case BOOLEAN_DATA_TYPE:
			record.setAttribute(attribute, value.isBoolean().booleanValue());
			break;
		case LONG_DATA_TYPE:
			record.setAttribute(attribute, value.isNumber().doubleValue());
			break;
		case DOUBLE_DATA_TYPE:
			record.setAttribute(attribute, value.isNumber().doubleValue());
		}
	}

}
