package org.celstec.arlearn2.gwt.client.network.game;

import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.GenericDataSource;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class GameDataSource extends GenericDataSource {
	
	public static GameDataSource instance;

	public static GameDataSource getInstance() {
		if (instance == null)
			instance = new GameDataSource();
		return instance;
	}

	private GameDataSource() {
		super();
		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.notification.GameModification", gameNotitificationHandler);

	}
	
	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, "gameId", true, true);
		addField(STRING_DATA_TYPE, "title", false, true);
		addField(INTEGER_DATA_TYPE, "time", false, true);
	}
	
	protected GenericClient getHttpClient() {
		return GameClient.getInstance();
	}

	@Override
	protected String getBeanType() {
		return "games";
	}

	public void delete(long gameId) {
		ListGridRecord rec = new ListGridRecord();
		rec.setAttribute("gameId", gameId);
		removeData(rec);
	}

	public NotificationHandler gameNotitificationHandler = new GameNotificationHandler() {
		

		@Override
		public void onNewGame(JSONObject jsonObject) {
			addBean(jsonObject);
		}

		@Override
		public void onDeleteGame(long gameId) {
			delete(gameId);
			
		}
	};
}
