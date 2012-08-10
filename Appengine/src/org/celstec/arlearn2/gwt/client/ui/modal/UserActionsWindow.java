package org.celstec.arlearn2.gwt.client.ui.modal;

import java.util.Date;

import org.celstec.arlearn2.gwt.client.network.action.ActionDatasource;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.AdvancedCriteria;
import com.smartgwt.client.data.Criterion;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.OperatorId;

import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class UserActionsWindow extends Window {

	private long runId;
	private ListGrid listGrid;

	public UserActionsWindow(final String email, String name, long runId) {
		this.runId = runId;
		ActionDatasource.getInstance().loadDataRun(runId);
		setAutoSize(true);
		setTitle(name);
		setWidth(500);
//		setLeft(0);
		centerInPage();

		setCanDragReposition(true);
		setCanDragResize(true);

		listGrid = new ListGrid();
		listGrid.setShowAllRecords(true);
		listGrid.setWidth("100%");
		listGrid.setHeight("*");
		
		listGrid.setDataSource(ActionDatasource.getInstance());

		ListGridField dateField = new ListGridField("time");
		dateField.setCellFormatter(new CellFormatter() {

			@Override
			public String format(Object arg0, ListGridRecord arg1, int arg2, int arg3) {
				DateTimeFormat fmt = DateTimeFormat.getFormat("dd/MM hh:mm");
				return fmt.format(new Date(arg1.getAttributeAsLong("timestamp")));
			}
		});
		dateField.setAlign(Alignment.LEFT);
		ListGridField actionField = new ListGridField("action");
		ListGridField accountField = new ListGridField("userEmail");
		ListGridField teamField = new ListGridField("team");
		ListGridField runIdField = new ListGridField("runId");
		runIdField.setHidden(true);
		

		
		listGrid.setFields(runIdField, dateField, actionField, accountField, teamField);
		listGrid.setAutoFetchData(true);
		
		listGrid.setShowFilterEditor(true);
		listGrid.setAllowFilterExpressions(true); 
		AdvancedCriteria initialCriteria = new AdvancedCriteria(OperatorId.AND, new Criterion[]{  
                new Criterion("userEmail", OperatorId.EQUALS, email) , 
                new Criterion("runId", OperatorId.EQUALS, runId)   

        });  
		listGrid.setInitialCriteria(initialCriteria);
		
		addItem(listGrid);
		setAutoSize(true);
	}

//	public void update(JSONObject bean) {
//		ActionDatasource.getInstance().addBean(bean);
////		listGrid.fetchData();
//		
//	}
	

}
