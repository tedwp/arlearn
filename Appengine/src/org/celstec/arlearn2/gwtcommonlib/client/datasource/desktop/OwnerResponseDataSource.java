package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonResumptionListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.ResponseModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.TeamModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.UserModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.UserClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.response.ResponseClient;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.Record;

public class OwnerResponseDataSource extends GenericDataSource {

	public static OwnerResponseDataSource instance;

	public static OwnerResponseDataSource getInstance() {
		if (instance == null)
			instance = new OwnerResponseDataSource();
		return instance;
	}

	private OwnerResponseDataSource() {
		super();
		setDataSourceModel(new ResponseModel(this));
	}

	public GenericClient getHttpClient() {
		return ResponseClient.getInstance();
	}

	@Override
	public void loadDataFromWeb() {
	}

	public void loadDataFromWeb(final long runId) {
		JsonResumptionListCallback callback = new JsonResumptionListCallback(getBeanType(), this.getDataSourceModel(), 0l) {

			@Override
			public void nextCall() {
				((ResponseClient) getHttpClient()).getResponses(runId, OwnerResponseDataSource.this.lastSyncDate, resumptionToken, this);

			}

		};
		((ResponseClient) getHttpClient()).getResponses(runId, OwnerResponseDataSource.this.lastSyncDate, null, callback);
	}

	protected String getBeanType() {
		return "responses";
	}

	@Override
	public void processNotification(JSONObject bean) {
		loadDataFromWeb((long) bean.get("runId").isNumber().doubleValue());
	}

	@Override
	public void saveRecord(final AbstractRecord record) {
		Record gwtRecord = (Record) record;
		long runId = gwtRecord.getAttributeAsLong(RunModel.RUNID_FIELD);
		String userEmail = gwtRecord.getAttributeAsString(ResponseModel.USEREMAIL_FIELD);
		setTeamId(runId, userEmail, record);
		super.saveRecord(record);
	}

	private void setTeamId(final long runId, final String userEmail, final AbstractRecord record) {
		Record userRecord = UserDataSource.getInstance().getRecord(UserModel.createKey(runId, userEmail));
		if (userRecord != null) {
			record.setAttribute(TeamModel.TEAMID_FIELD, userRecord.getAttribute(TeamModel.TEAMID_FIELD));
			record.setAttribute(ResponseModel.ROLE_VALUE_FIELD, userRecord.getAttribute(UserModel.ROLES_FIELD));
		} else {
			// User is not cached, so fetch it directly from web service
			UserClient.getInstance().getUsers(runId, new JsonObjectListCallback("users", null) {
				public void onJsonObjectReceived(JSONObject jsonObject) {
					System.out.println("user rec " +userEmail+ " "+ jsonObject);
					if (jsonObject.get("email").isString().stringValue().equals(userEmail)) {
						if (jsonObject.containsKey(TeamModel.TEAMID_FIELD)) {
							record.setAttribute(TeamModel.TEAMID_FIELD, jsonObject.get(TeamModel.TEAMID_FIELD).isString().stringValue());
						}
						if (jsonObject.containsKey("roles")) {
							JSONArray array = jsonObject.get("roles").isArray();
							String[] arrayString = new String[array.size()];
							for (int i = 0; i < array.size(); i++) {
								arrayString[i] = array.get(i).isString().stringValue();
							}
							record.setAttribute(ResponseModel.ROLE_VALUE_FIELD, arrayString);
						}
					}
				}
			});
		}
	}
}
