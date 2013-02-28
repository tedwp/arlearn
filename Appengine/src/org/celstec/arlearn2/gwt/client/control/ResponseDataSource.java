/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.gwt.client.control;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

@Deprecated
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
