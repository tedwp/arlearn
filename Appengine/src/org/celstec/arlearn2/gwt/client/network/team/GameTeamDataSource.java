package org.celstec.arlearn2.gwt.client.network.team;

import java.util.HashSet;

import org.celstec.arlearn2.gwt.client.control.TriggerDataSource;
import org.celstec.arlearn2.gwt.client.network.DerivedFieldTask;
import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.GenericDataSource;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemsClient;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class GameTeamDataSource extends GenericDataSource {
	
	public static GameTeamDataSource instance;

	public static GameTeamDataSource getInstance() {
		if (instance == null)
			instance = new GameTeamDataSource();
		return instance;
	}

	private GameTeamDataSource() {
		super();
	}

	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, "id", false, true);
		addField(STRING_DATA_TYPE, "name", false, false);
		addDerivedField(new DerivedFieldTask() {
			
			@Override
			public String processValue(String... value) {
				if (value == null || "".equals(value)) return "";
				String result = value[0];
				for (int i = 1; i < value.length; i++) {
					result += ":"+value[i];
				}
				return result;
			}
			
			@Override
			public String getTargetFieldName() {
				return "pk";
			}
			
			@Override
			public String[] getSourceFieldName() {
				return new String[] {"id", "name"};
			}
			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}
			@Override
			public void setJsonSource(JSONObject jsonObject) {}
			
		}, true, false);
	}

	@Override
	protected GenericClient getHttpClient() {
		return GeneralItemsClient.getInstance();
	}

	@Override
	protected String getBeanType() {
		return "generalItems";
	}
	
	protected JsonCallback getCallBack(){
		return callback;
	}
	
	private JsonCallback callback = new JsonCallback(getBeanType()) {

		@Override
		public void onError() {}
		
		
		
		@Override
		public void onReceived() {
			//TODO do something here
		}
		
		
		
	};

}
