package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;

import com.google.gwt.json.client.JSONObject;

public class GeneralItemDataSource extends GenericDataSource {
	
	public static GeneralItemDataSource instance;

	public static GeneralItemDataSource getInstance() {
		if (instance == null)
			instance = new GeneralItemDataSource();
		return instance;
	}
	
	private GeneralItemDataSource() {
		super();
		setDataSourceModel(new GeneralItemModel(this));
	}
	
	public GeneralItemsClient getHttpClient() {
		return GeneralItemsClient.getInstance();
	}
	
	@Override
	public void loadDataFromWeb() {
	}

	
	public void loadDataFromWeb(long gameId) {
		getHttpClient().getGeneralItemsGame(gameId, getLastSyncDate(), new JsonObjectListCallback(getBeanType(),  this.getDataSourceModel()));
	}

	protected String getBeanType() {
		return "generalItems";
	}
	
	@Override
	public void processNotification(JSONObject bean) {
		loadDataFromWeb((long) bean.get("gameId").isNumber().doubleValue());
	}

}
