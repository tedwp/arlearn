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
package org.celstec.arlearn2.gwt.client.ui;


import java.util.HashMap;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.control.GeneralItemsDataSource_Old;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemRunDataSource;
import org.celstec.arlearn2.gwt.client.network.user.UsersDataSource;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.ui.modal.ChartWindow;
import org.celstec.arlearn2.gwt.client.ui.modal.OpenQuestionAnswerWindow;
import org.celstec.arlearn2.gwt.client.ui.modal.UserActionsWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;

public class RunTabGeneralItems extends ListGrid  {

	private RunTab runTab;
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);
	public GeneralItemRunDataSource datasource;
	
	public RunTabGeneralItems(RunTab rt) {
		this.runTab = rt;
		
		datasource = new GeneralItemRunDataSource(rt);
		initGrid();
		initGridData();
		initCellClickHandler();
		initActionListener();
		
	}
	
	

	public void initGrid() {
		setWidth(500);
		setHeight(224);
		setShowAllRecords(true);
		setCanResizeFields(false);
		
		setPadding(5);
		setWidth("100%");
		setHeight("100%");
		

	}
	
	private void initGridData() {
		setDataSource(datasource);
		
		ListGridField sortKeyField = new ListGridField("sortKey", "Order");
		ListGridField titleGameField = new ListGridField("name", constants.title());
		ListGridField typeField = new ListGridField("simpleName", constants.type());
		ListGridField readField = new ListGridField("read", constants.read());
		ListGridField correctField = new ListGridField("correct", constants.correct());
		ListGridField answerField = new ListGridField("answer", constants.answer());
		ListGridField accountField = new ListGridField("account", constants.account());
		ListGridField rolesField = new ListGridField("roles", constants.roles());

		setFields(new ListGridField[] { sortKeyField, titleGameField, typeField, rolesField, readField, correctField, answerField });
		setSortField("sortKey");
		setAutoFetchData(true);

	}
	
	private void initCellClickHandler() {
		addCellDoubleClickHandler(new CellDoubleClickHandler() {
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				if (typeEquals(GeneralItemRunDataSource.NARRATORITEM, event) || typeEquals(GeneralItemRunDataSource.AUDIOOBJECT, event)||typeEquals(GeneralItemRunDataSource.VIDEOOBJECT, event)) {
					OpenQuestionAnswerWindow window = new OpenQuestionAnswerWindow(runTab.getRunId(), event.getRecord().getAttributeAsLong("id"));
					window.show();
				}
				if (typeEquals(GeneralItemRunDataSource.MULTIPLECHOICE, event) ) {
					ChartWindow cw = new ChartWindow(runTab.getGameId(), runTab.getRunId(), event.getRecord().getAttributeAsLong("id"));
					cw.show();
				}
			}
			
			private boolean typeEquals(String type, CellDoubleClickEvent event ) {
				return type.equals(event.getRecord().getAttributeAsString("type"));
			}
		});
	}
	
	
	private void initActionListener() {
		// TODO Auto-generated method stub
		
	}
	
	public void filterGrid(String email, String teamId) {
		if (email != null) {
			HashMap<String, String> values = new HashMap<String, String>();
			values.put("account", email);
			values.put("runId", ""+runTab.getRunId());
			values.put("teamId", teamId);
			datasource.loadDataGame(runTab.getGameId(), values);
		}
			
//		GeneralItemRunDataSource.getInstance().loadItemsGame(runTab.getGameId(), runTab.getRunId(), email, null);
		Criteria crit = new Criteria();
		if (email != null)
			crit.addCriteria("account", email);
		crit.addCriteria("runId", ""+runTab.getRunId());
		crit.addCriteria("deleted", false);
		filterData(crit);
	}


//	public NotificationHandler actionNotificationHandler = new NotificationHandler() {
//		@Override
//		public void onNotification(JSONObject bean) {
//			if (
//					bean.containsKey("runId") && 
//					bean.containsKey("userEmail") && 
//					bean.containsKey("action") && 
//					bean.containsKey("generalItemId") && 
//					bean.get("action").isString().stringValue().equals("read")
//					) {
//				
//				datasource.updateAction(
//						(long) bean.get("generalItemId").isNumber().doubleValue(), 
//						(long) bean.get("runId").isNumber().doubleValue(), 
//						bean.get("userEmail").isString().stringValue(), null);
//				for (UserActionsWindow u: runTab.users.uaws){
//					u.update(bean);
//				}
//			}
//			
//		}
//	};
	
	
	
	public NotificationHandler responseNotificationHandler = new NotificationHandler() {
		@Override
		public void onNotification(JSONObject bean) {
			if (
					bean.containsKey("runId") && 
					bean.containsKey("userEmail") && 
					bean.containsKey("responseValue") && 
					bean.containsKey("generalItemId") 
					) {
//				if (cw != null) cw.update(bean);
				//TODO let this datasource listen for updates from ResponseDatasource
				datasource.updateResponse(
						(long) bean.get("generalItemId").isNumber().doubleValue(), 
						(long) bean.get("runId").isNumber().doubleValue(), 
						bean.get("userEmail").isString().stringValue(), 
						bean.get("responseValue").isString().stringValue(),
						null);
			}
		
		}
	};
	
}
