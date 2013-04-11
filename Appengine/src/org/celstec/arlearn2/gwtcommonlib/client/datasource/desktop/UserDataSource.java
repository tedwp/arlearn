package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.ResponseModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunRoleModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.UserModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.UserClient;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.Record;

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

	protected String getBeanType() {
		return "users";
	}
	
	@Override
	public void processNotification(JSONObject bean) {
		// TODO Auto-generated method stub
		
	}
	
	public void setServerTime(long serverTime) {}
	
	@Override
	public void saveRecord(final AbstractRecord record) {
		Record gwtRecord = (Record) record;
		long runId = gwtRecord.getAttributeAsLong(RunModel.RUNID_FIELD);
		if (record.getCorrespondingJsonObject().containsKey("roles")){
			JSONArray roleArray = record.getCorrespondingJsonObject().get("roles").isArray();
			for (int i = 0; i < roleArray.size(); i++)  {
				RunRolesDataSource.getInstance().addRole(runId, roleArray.get(i).isString().stringValue());
			}
		}
		super.saveRecord(record);
	}
	
}
