package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameRoleModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class GameRolesDataSource extends GenericDataSource {
	
	public static GameRolesDataSource instance;

	public static GameRolesDataSource getInstance() {
		if (instance == null)
			instance = new GameRolesDataSource();
		return instance;
	}

	private GameRolesDataSource() {
		super();
		setDataSourceModel(new GameRoleModel(this));
	}

	public GenericClient getHttpClient() {
		return null;
	}

	@Override
	public void loadDataFromWeb() {
		
	}

	@Override
	public void processNotification(JSONObject bean) {
	}

	public void addRole (long gameId, String role) {
		String pk = gameId+":"+role;
		if (getRecord(pk) == null) {
		AbstractRecord record = createRecord();
		record.setCorrespondingJsonObject(new JSONObject());
		record.getCorrespondingJsonObject().put(GameRoleModel.ROLE_PK_FIELD, new JSONString(pk));
		record.setAttribute(GameModel.GAMEID_FIELD, gameId);
		record.setAttribute(GameRoleModel.ROLE_PK_FIELD, pk);
		record.setAttribute(GameRoleModel.ROLE_FIELD, role);
		saveRecord(record);
		}
	}

	public void addRole(long gameId, String[] roles) {
		if (roles == null) return;
		for (String role: roles) {
			addRole(gameId, role);
		}
	}
}