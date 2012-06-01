package org.celstec.arlearn2.gwt.client.control;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class ResponseDataSource extends DataSource{

public static ResponseDataSource instance;
	
	private ResponseDataSource() {
		DataSourceIntegerField rundIdField = new DataSourceIntegerField("runId");
		rundIdField.setHidden(true);
		
		DataSourceIntegerField generalItemIdField = new DataSourceIntegerField("generalItemId");
		generalItemIdField.setHidden(true);
		
		DataSourceTextField responseValueField = new DataSourceTextField("responseValue");
		responseValueField.setRequired(true);
		
		DataSourceTextField ownerField = new DataSourceTextField("userEmail");
		ownerField.setRequired(true);
		
		setFields(rundIdField, generalItemIdField, responseValueField,ownerField);

		setClientOnly(true);
	}
	
	public static ResponseDataSource getInstance() {
		if (instance == null) instance = new ResponseDataSource();
		return instance;
	}
	
//	public void loadResponses(final long runId, final String account, final ResponseCallback rc) {
//		ResponseClient.getInstance().getResponses(runId, account, new ResponseCallback() {
//			
//			@Override
//			public void onError() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onResponsesReady() {
//				for (int i = 0; i < responsesSize();i++) {
//					
//				}
//				
//			}
//		});
//	}
}
