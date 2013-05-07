package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class RunDataSource extends GenericDataSource {

	public static RunDataSource instance;

	public static RunDataSource getInstance() {
		if (instance == null)
			instance = new RunDataSource();
		return instance;
	}
	
	private RunDataSource() {
		super();
		setDataSourceModel(new RunModel(this));
	}
	
	public GenericClient getHttpClient() {
		return RunClient.getInstance();
	}
	
	public void loadDataFromWeb() {
		((RunClient) getHttpClient()).myRuns(new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()){
			public void onJsonObjectReceived(final JSONObject jsonObject) {
				GameClient.getInstance().getGame((long)jsonObject.get(RunModel.GAMEID_FIELD).isNumber().doubleValue(), new JsonCallback(){
					public void onJsonReceived(JSONValue jsonValue) {
						jsonObject.put(RunModel.GAME_TITLE_FIELD, jsonValue.isObject().get(GameModel.GAME_TITLE_FIELD));
						callSuper(jsonObject);
					}
				});		
			}
			private void callSuper(JSONObject jsonObject) {
				super.onJsonObjectReceived(jsonObject);
			}
		});
//		((RunClient) getHttpClient()).runsParticipate(getLastSyncDate(), new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()));
	}

	protected String getBeanType() {
		return "runs";
	}
	
	@Override
	public void processNotification(JSONObject bean) {
		loadDataFromWeb();
	}
	
}
