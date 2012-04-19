package org.celstec.arlearn2.gwt.client.network.game;

import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.GenericDataSource;

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
	}
	
	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, "gameId", true, true);
		addField(STRING_DATA_TYPE, "title", false, true);
		addField(STRING_DATA_TYPE, "creator", false, true);
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

}
