package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;

import com.google.gwt.json.client.JSONObject;

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
		((GameClient) getHttpClient()).getGames(getLastSyncDate(), new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()));
	}

	protected String getBeanType() {
		return "games";
	}

	@Override
	public void processNotification(JSONObject bean) {
		loadDataFromWeb();
	}
	
	
}
