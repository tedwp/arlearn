package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;

public class GameDataSource extends GenericDataSource {

	public GenericClient getHttpClient() {
		return GameClient.getInstance();
	}

	public void loadDataFromWeb() {

	}

	@Override
	public void removeRecord(Object id) {
		// TODO Auto-generated method stub

	}
}
