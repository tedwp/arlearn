package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;

public interface DataSourceAdapter {

	public void setDataSourceModel(DataSourceModel model);
	
	public DataSourceModel getDataSourceModel();
	
	public abstract GenericClient getHttpClient();

	public abstract void loadDataFromWeb();

	public void addDataSourceTextField(String attributeName, boolean hidden, boolean primaryKey);

	public void addDataSourceIntegerField(String attributeName, boolean hidden, boolean primaryKey);

	public void addDataSourceDoubleField(String attributeName, boolean hidden, boolean primaryKey);

	public void addDataSourceBooleanField(String attributeName, boolean hidden, boolean primaryKey);
	
	public AbstractRecord createRecord();

	public void saveRecord(AbstractRecord record);
	
	public void removeRecord(Object id);


}
