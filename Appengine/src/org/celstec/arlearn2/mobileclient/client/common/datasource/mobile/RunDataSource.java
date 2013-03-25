package org.celstec.arlearn2.mobileclient.client.common.datasource.mobile;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;
import org.celstec.arlearn2.mobileclient.client.images.ImageResources;

import com.google.gwt.json.client.JSONObject;

public class RunDataSource  extends GenericDataSource {

	public static RunDataSource instance;
	
	private long runId;

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

	public long getRunId() {
		return runId;
	}

	public void setRunId(long runId) {
		this.runId = runId;
	}

	@Override
	public void loadDataFromWeb() {
		((RunClient) getHttpClient()).runsParticipate(new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()));
	}
	
	@Override
	public void saveRecord(AbstractRecord record) {
		MyAbstractRecord rec = (MyAbstractRecord) record;
		String owner = rec.getAttribute("owner");
		if (owner != null) rec.setAttribute("info", owner);
		rec.setAttribute("icon", ImageResources.INSTANCE.listMap());
//		if (GameDataSource.getInstance().getGame(rec.getAttributeAsLong("gameId"))!= null) {
//			MyAbstractRecord gameRecord =  (MyAbstractRecord) GameDataSource.getInstance().getGame(rec.getAttributeAsLong("gameId"));
//			rec.setAttribute("mapAvailable", gameRecord.getAttributeAsBoolean("mapAvailable"));
//			if (gameRecord.getAttributeAsBoolean("mapAvailable")) {
//				rec.setAttribute("icon", ImageResources.INSTANCE.listMap());
//			} else {
//				rec.setAttribute("icon", ImageResources.INSTANCE.list());
//			}
//		}
		rec.setAttribute("_id", rec.getAttributeAsInt("runId"));
		if (!rec.getAttribute("deleted").equals("true")) 
			super.saveRecord(rec);
	}
	
	protected String getBeanType() {
		return "runs";
	}


	@Override
	public void removeRecord(AbstractRecord record) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processNotification(JSONObject bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setServerTime(long doubleValue) {
		// TODO Auto-generated method stub
		
	}

}
