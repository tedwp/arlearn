package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.ContactModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.ResponseModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunRoleModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.UserModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.CollaborationClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.UserClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
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
		((UserClient) getHttpClient()).getUsers(runId, new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()){
			public void onJsonObjectReceived(final JSONObject jsonObject) {
				CollaborationClient.getInstance().getContact(jsonObject.get(UserModel.EMAIL_FIELD).isString().stringValue(), new JsonCallback(){
					public void onJsonReceived(JSONValue jsonValue) {
						jsonObject.put(UserModel.NAME_FIELD, jsonValue.isObject().get(ContactModel.NAME_FIELD));
						jsonObject.put(UserModel.EMAIL_FIELD, jsonValue.isObject().get(ContactModel.EMAIL_FIELD));
						jsonObject.put(UserModel.PICTURE_FIELD, jsonValue.isObject().get(ContactModel.PICTURE_FIELD));
						callSuper(jsonObject);
					}
				});		
			}
			private void callSuper(JSONObject jsonObject) {
				super.onJsonObjectReceived(jsonObject);
			}
		});
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
