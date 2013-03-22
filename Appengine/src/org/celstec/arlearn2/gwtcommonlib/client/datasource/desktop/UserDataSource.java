package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.UserModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.UserClient;

public class UserDataSource extends GenericDataSource {

	public static UserDataSource instance;

	public static UserDataSource getInstance() {
		if (instance == null)
			instance = new UserDataSource();
		return instance;
	}
	
	private UserDataSource() {
		super();
		setDataSourceModel(new UserModel(this));
	}
	
	public GenericClient getHttpClient() {
		return UserClient.getInstance();
	}
	
	@Override
	public void loadDataFromWeb() {
		// TODO Auto-generated method stub
		
	}
	
	public void loadDataFromWeb(long runId) {
		((UserClient) getHttpClient()).getUsers(runId, new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()));
	}

	@Override
	public void removeRecord(Object id) {
		
	}

	protected String getBeanType() {
		return "users";
	}
}
