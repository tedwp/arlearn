package org.celstec.arlearn2.mobileclient.client.common.datasource.mobile;

import java.util.HashMap;
import java.util.ListIterator;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.DataSourceAdapter;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.DataSourceModel;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.mobile.client.data.DataSource;
import com.smartgwt.mobile.client.data.DataSourceField;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.data.RecordList;

public abstract class GenericDataSource extends RecordList implements DataSourceAdapter {

	private DataSourceModel model; 
	private AddDataCallback dataCallback;
	
	protected static long lastSyncDate = 0;
	protected HashMap<Object, AbstractRecord> recordMap = new HashMap<Object, AbstractRecord>();
	
	protected GenericDataSource() {
//		initFields();
//		setFields(fieldList.toArray(new DataSourceField[0]));
	}
	
	@Override
	public void setDataSourceModel(DataSourceModel model) {
		this.model = model;
	}
	
	@Override
	public DataSourceModel getDataSourceModel() {
		return model;
	}
	
	@Override
	public void addDataSourceTextField(String attributeName, boolean hidden, boolean primaryKey) {
		addField(new DataSourceField(attributeName), hidden, primaryKey);
	}
	
	@Override
	public void addDataSourceIntegerField(String attributeName, boolean hidden, boolean primaryKey) {
		addField(new DataSourceField(attributeName), hidden, primaryKey);
	}
	
	@Override
	public void addDataSourceDoubleField(String attributeName, boolean hidden, boolean primaryKey) {
		addField(new DataSourceField(attributeName), hidden, primaryKey);
	}

	@Override
	public void addDataSourceBooleanField(String attributeName, boolean hidden, boolean primaryKey) {
		addField(new DataSourceField(attributeName), hidden, primaryKey);
	}
	
	

	@Override
	public void addDataSourceEnumField(String attributeName, boolean hidden, boolean primaryKey) {
		addField(new DataSourceField(attributeName), hidden, primaryKey);
	}
	
	private void addField(DataSourceField field, boolean hidden, boolean primaryKey) {
		field.setHidden(hidden);
		field.setPrimaryKey(primaryKey);
//		addField(field);
	}
	
	public void addJsonObject(JSONObject object) {
		model.addJsonObject(object);
	}
	
	@Override
	public AbstractRecord createRecord() {
		final MyAbstractRecord rec = new MyAbstractRecord();
		return rec;
	}

	@Override
	public void saveRecord(AbstractRecord record) {
		MyAbstractRecord rec = (MyAbstractRecord) record;
		ListIterator<Record> li = listIterator();
		while (li.hasNext()) {
			Record next = li.next();
			if (rec.getAttributeAsInt("_id").equals(next.getAttributeAsInt("_id"))) {
				li.remove();
			}
		}
		trimToSize();
		if (dataCallback !=null) dataCallback.recordAdded((MyAbstractRecord) record);
		add((MyAbstractRecord) record);
		
		Object key = model.getPrimaryKey(record);
		if (key != null) recordMap.put(key, (MyAbstractRecord) record);
	}
	

	
	public void setAddDataCallback(AddDataCallback callback) {
		this.dataCallback = callback;
	}
	
	public void setServerTime(long serverTime) {
		lastSyncDate = serverTime;
	}
	
	protected long getLastSyncDate() {
		return lastSyncDate;
	}
	
	public class MyAbstractRecord extends Record implements AbstractRecord {
		
		private JSONObject json;
		
		@Override
		public void setCorrespondingJsonObject(JSONObject object) {
			this.json = object;
		}

		@Override
		public JSONObject getCorrespondingJsonObject() {
			return json;
		}

		@Override
		public void setAttribute(String property, String[] value) {
			super.setAttribute(property, value);
		}
		
	}
	
	public interface AddDataCallback {
		public void recordAdded(MyAbstractRecord record);
	}
	
	@Override
	public void removeRecord(AbstractRecord record) {
		Record toRemove = getRecord(model.getPrimaryKey(record));
		if (toRemove != null) remove(toRemove);
	}
	
	protected Record getRecord(Object id) {
		return (Record) recordMap.get(id);
	}
	
}
