package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class GameDataSource extends GenericDataSource {

	public static GameDataSource instance;
	
	public static GameDataSource getInstance() {
		if (instance == null)
			instance = new GameDataSource();
		return instance;
	}
	
	private GameDataSource() {
		super();
		setDataSourceModel(new GameModel(this));
	}
	
	public GenericClient getHttpClient() {
		return GameClient.getInstance();
	}

	public void loadDataFromWeb() {
		((GameClient) getHttpClient()).getGamesAccess(getLastSyncDate(), new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()){
			public void onJsonObjectReceived(final JSONObject jsonObject) {
				((GameClient) getHttpClient()).getGame((long)jsonObject.get("gameId").isNumber().doubleValue(), new JsonCallback(){
					public void onJsonReceived(JSONValue jsonValue) {
						jsonValue.isObject().put("accessRights", jsonObject.get("accessRights"));
						callSuper(jsonValue.isObject());
					}
				});		
			}
			private void callSuper(JSONObject jsonObject) {
				super.onJsonObjectReceived(jsonObject);
			}
		});
	}

	protected String getBeanType() {
		return "gamesAccess";
	}

	@Override
	public void processNotification(JSONObject bean) {
		loadDataFromWeb();
	}
	
	
}
