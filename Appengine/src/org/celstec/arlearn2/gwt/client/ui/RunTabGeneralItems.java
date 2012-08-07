package org.celstec.arlearn2.gwt.client.ui;


import java.util.HashMap;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.control.GeneralItemsDataSource_Old;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemRunDataSource;
import org.celstec.arlearn2.gwt.client.network.user.UsersDataSource;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.ui.modal.ChartWindow;
import org.celstec.arlearn2.gwt.client.ui.modal.OpenQuestionAnswerWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;

public class RunTabGeneralItems extends ListGrid  {

	private RunTab runTab;
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);
	private GeneralItemRunDataSource datasource;
	
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
		fetchData();
	}
	
	private void initCellClickHandler() {
		addCellDoubleClickHandler(new CellDoubleClickHandler() {
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				if (typeEquals(GeneralItemRunDataSource.NARRATORITEM, event) || typeEquals(GeneralItemRunDataSource.AUDIOOBJECT, event)||typeEquals(GeneralItemRunDataSource.VIDEOOBJECT, event)) {
//					String html = event.getRecord().getAttributeAsString("answer");
					OpenQuestionAnswerWindow window = new OpenQuestionAnswerWindow(runTab.getRunId(), event.getRecord().getAttributeAsLong("id"));
					window.show();
				}
				if (typeEquals(GeneralItemRunDataSource.MULTIPLECHOICE, event) ) {
					if (cw != null) cw.destroy();
					cw = new ChartWindow(runTab.getGameId(), runTab.getRunId(), event.getRecord().getAttributeAsLong("id"));
					cw.show();
				}
			}
			
			private boolean typeEquals(String type, CellDoubleClickEvent event ) {
				return type.equals(event.getRecord().getAttributeAsString("type"));
//				{"imageUrl":"http:\/\/ar-learn.appspot.com\/\/uploadService\/60020\/arlearn2\/image-1929634920.jpg"}
			}
		});
	}
	
	
	private ChartWindow cw;
	
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


	public NotificationHandler actionNotificationHandler = new NotificationHandler() {
		@Override
		public void onNotification(JSONObject bean) {
			if (
					bean.containsKey("runId") && 
					bean.containsKey("userEmail") && 
					bean.containsKey("action") && 
					bean.containsKey("generalItemId") && 
					bean.get("action").isString().stringValue().equals("read")
					) {
				
				datasource.updateAction(
						(long) bean.get("generalItemId").isNumber().doubleValue(), 
						(long) bean.get("runId").isNumber().doubleValue(), 
						bean.get("userEmail").isString().stringValue(), null);
			}
			
		}
	};
	
	public NotificationHandler responseNotificationHandler = new NotificationHandler() {
		@Override
		public void onNotification(JSONObject bean) {
			if (
					bean.containsKey("runId") && 
					bean.containsKey("userEmail") && 
					bean.containsKey("responseValue") && 
					bean.containsKey("generalItemId") 
					) {
				if (cw != null) cw.update(bean);
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
