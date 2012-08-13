package org.celstec.arlearn2.gwt.client.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.celstec.arlearn2.gwt.client.control.ReadyCallback;
import org.celstec.arlearn2.gwt.client.control.TriggerDataSource;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONArray;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public abstract class GenericDataSource extends DataSource {
	protected final int STRING_DATA_TYPE = 1;
	protected final int INTEGER_DATA_TYPE = 2;
	protected final int STRING_AR_DATA_TYPE = 3;
	protected final int BOOLEAN_DATA_TYPE = 4;
	protected final int LONG_DATA_TYPE = 5;

	private List<DataSourceField> fieldList = new ArrayList<DataSourceField>();
	private List<String> attributeList = new ArrayList<String>();
	private List<Integer> typeList = new ArrayList<Integer>();

	protected String pkAttribute;
	private HashSet<String> pkMap = new HashSet<String>();
	private ArrayList<DerivedFieldTask> derivedTaskList = new ArrayList<DerivedFieldTask>();
	private ArrayList<Boolean> derivedTaskPrimaryKeyList = new ArrayList<Boolean>();
	private ArrayList<Boolean> derivedTaskHiddenList = new ArrayList<Boolean>();

	protected GenericDataSource() {
		initFields();
		setFields(fieldList.toArray(new DataSourceField[0]));
		setClientOnly(true);
	}

	protected void addField(int dataType, String attributeName,
			boolean primaryKey, boolean hidden) {
		DataSourceField field = null;
		if (primaryKey)
			pkAttribute = attributeName;
		switch (dataType) {
		case STRING_DATA_TYPE:
			field = new DataSourceTextField(attributeName);
			typeList.add(STRING_DATA_TYPE);
			break;
		case INTEGER_DATA_TYPE:
			field = new DataSourceIntegerField(attributeName);
			typeList.add(INTEGER_DATA_TYPE);
			break;
		case LONG_DATA_TYPE:
			field = new DataSourceIntegerField(attributeName);
			typeList.add(LONG_DATA_TYPE);
			break;
		case STRING_AR_DATA_TYPE:
			field = new DataSourceTextField(attributeName);
			typeList.add(STRING_DATA_TYPE);
			break;
		case BOOLEAN_DATA_TYPE:
			field = new DataSourceBooleanField(attributeName);
			typeList.add(BOOLEAN_DATA_TYPE);
			break;
		default:
			break;
		}
		field.setHidden(hidden);
		field.setPrimaryKey(primaryKey);
		fieldList.add(field);
		attributeList.add(attributeName);
	}

	protected void addDerivedField(DerivedFieldTask task, boolean primaryKey,
			boolean hidden) {
		derivedTaskList.add(task);
		String attribute = task.getTargetFieldName();
		addField(task.getType(), attribute, primaryKey, hidden);
		attributeList.remove(attribute);
	}

	protected void processDerivedFields(ListGridRecord rec) {
		for (DerivedFieldTask task : derivedTaskList) {
			String[] sources = task.getSourceFieldName();
			String[] values = new String[sources.length];
			for (int i = 0; i < sources.length; i++) {
				values[i] = rec.getAttribute(sources[i]);
			}
			switch (task.getType()) {
			case STRING_DATA_TYPE:
				rec.setAttribute(task.getTargetFieldName(),task.processValue(values));
				break;
			case BOOLEAN_DATA_TYPE:
				rec.setAttribute(task.getTargetFieldName(),Boolean.parseBoolean(task.processValue(values)));
				break;
			default:
				break;
			}
		}

	}

	protected abstract void initFields();

	protected abstract GenericClient getHttpClient();

	protected abstract String getBeanType();

	// TODO delete items that were not in the list
	public void loadData(ReadyCallback rc) {
		getCallBack().setReadyCallback(rc);
		getHttpClient().getItems(getCallBack());
	}

	public void loadDataRun(long runId) {
		getHttpClient().getItemsForRun(runId, getCallBack(null));
	}

	public void loadDataGame(long gameId) {
		loadDataGame(gameId, null);
	}
	
	public void loadDataGame(long gameId, HashMap<String,String> values) {
		getHttpClient().getItemsForGame(gameId, getCallBack(values));
	}

	protected JsonCallback getCallBack() {
		return getCallBack(null);
	}

	public void addBean(JSONObject bean) {
		JSONObject object = new JSONObject();
		JSONArray array = new JSONArray();
		object.put(getBeanType(), array);
		array.set(array.size(), bean);
		JsonCallback callback = getCallBack(null);
		callback.onJsonReceivedNoProcessing(object);
		final ListGridRecord rec = callback.processArrayEntry(0);
		String id = "" + rec.getAttribute(pkAttribute);
		addData(rec);
		pkMap.add(id);
		
	}
	
	protected JsonCallback getCallBack(final HashMap<String,String> values) {
		return new JsonCallback(getBeanType()) {

			@Override
			public void onError() {
			}

			public void insertRecords() {
				
			}
			
			public ListGridRecord processArrayEntry(int i) {
				final ListGridRecord rec = new ListGridRecord();
				Criteria searchCrit = null;
				for (int j = 0; j < attributeList.size(); j++) {
					String attributeName = attributeList.get(j);
					switch (typeList.get(j)) {
					case STRING_DATA_TYPE:
						String value = getAttributeString(i, attributeName);
						rec.setAttribute(attributeName, value);
						if (pkAttribute != null
								&& pkAttribute.equals(attributeName))
							searchCrit = new Criteria(pkAttribute, value);
						break;
					case STRING_AR_DATA_TYPE:
						String arValue = getAttributeString(i, attributeName);
						rec.setAttribute(attributeName, arValue);
						if (pkAttribute != null
								&& pkAttribute.equals(attributeName))
							searchCrit = new Criteria(pkAttribute, arValue);
						break;
					case BOOLEAN_DATA_TYPE:
						Boolean boolValue = getAttributeBoolean(i, attributeName);
						rec.setAttribute(attributeName, boolValue);
						break;
					case INTEGER_DATA_TYPE:
						int intValue = getAttributeInteger(i, attributeName);
						rec.setAttribute(attributeName, intValue);
						if (pkAttribute != null
								&& pkAttribute.equals(attributeName)) {
							searchCrit = new Criteria();
							searchCrit.addCriteria(pkAttribute, intValue);
						}
						break;
					case LONG_DATA_TYPE:
						long longValue = getAttributeLong(i, attributeName);
						rec.setAttribute(attributeName, longValue);
						if (pkAttribute != null
								&& pkAttribute.equals(attributeName)) {
							searchCrit = new Criteria();
							searchCrit.addCriteria(pkAttribute, new Double[]{(double)longValue});
						}
						break;
					}
				}
				GenericDataSource.this.processDerivedFields(rec);
				//TODO the following is not generic and thus not elegant

				if ("gameId".equals(pkAttribute)) {
					TriggerDataSource.getInstance().processGameConfig(
							rec.getAttributeAsInt("gameId"),
							getGameConfig(i));

				}
				processRecord(rec);
				return rec;
			}
			
			@Override
			public void onReceived() {
				HashSet<String> deleteSet = (HashSet<String>) pkMap.clone();
				for (int i = 0; i < size(); i++) {
					final ListGridRecord rec = processArrayEntry(i);
					
					if (values != null) {
						for (Entry<String, String> entry: values.entrySet()) {
							rec.setAttribute(entry.getKey(), entry.getValue());
						}
					}
					
					
					String id = "" + rec.getAttribute(pkAttribute);
					
					if (!filterRecord(rec)) {
						if (pkMap.contains(id)) {
							deleteSet.remove(id);
							updateData(rec);
						} else {
							addData(rec);
							pkMap.add(id);
						}
					} 
				}
				for (String id : deleteSet) {
					final ListGridRecord rec = new ListGridRecord();
					rec.setAttribute(pkAttribute, id);
					removeData(rec);
					pkMap.remove(id);
				}
				if (rc != null) {
					rc.ready();
				}
				allRecordsUpdated(values);
			}
		};
	}
	
	
	
	protected boolean filterRecord(ListGridRecord rec) {
		return false;
	}
	
	protected void processRecord(ListGridRecord rec) {
	}
	
	protected void allRecordsUpdated(HashMap<String,String> values) {
	}
	
	public void clearAllData() {
		HashSet<String> deleteSet = (HashSet<String>) pkMap.clone();
		for (String id : deleteSet) {
			final ListGridRecord rec = new ListGridRecord();
			rec.setAttribute(pkAttribute, id);
			removeData(rec);
			pkMap.remove(id);
		}
	}

	public boolean isEmpty(){
		return pkMap.isEmpty();
	}
	
	public void setValue(String pk, final String fieldName, final String value) {
//		// updateData(rec);
		Criteria crit = new Criteria();
		crit.addCriteria("pk", pk);
		fetchData(crit, new DSCallback() {
			@Override
			public void execute(DSResponse response,
					Object rawData, DSRequest request) {
				Record[] records = response.getData();
				for (Record record : records) {
					record.setAttribute(fieldName, value);
					updateData(record);

				}
			}
		});
	}
	
	public void query(final DatasourceUpdateHandler handler, long runId) {
		Criteria crit = new Criteria();
		crit.addCriteria("runId", ""+runId);
		fetchData(crit, new DSCallback() {
			@Override
			public void execute(DSResponse response,
					Object rawData, DSRequest request) {
				Record[] records = response.getData();
				for (Record record : records) {
					System.out.println("query "+record);
					handler.newRecord(record);
				}
			}
		});
	}
	
	
	
	@Override
	public void addData(Record record) {
		super.addData(record);
		for (Iterator<DatasourceUpdateHandler> iter = updateHandlerList.iterator(); iter.hasNext();) {
			iter.next().newRecord(record);
		}
	}

	private List<DatasourceUpdateHandler> updateHandlerList =  new ArrayList<DatasourceUpdateHandler>();

	public void addNotificationHandler(DatasourceUpdateHandler handler) {
		updateHandlerList.add(handler);
	}
	
	public void removeUpdateHandeler(DatasourceUpdateHandler handler) {
		for (Iterator iter = updateHandlerList.iterator(); iter.hasNext();) {
			if (iter.next()== handler)iter.remove();
		}
	}

	public void removeAllHandlers() {
		updateHandlerList =  new ArrayList<DatasourceUpdateHandler>();
	}
	
	
}
