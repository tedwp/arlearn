package org.celstec.arlearn2.gwt.client.ui.modal;

import java.util.Date;

import org.celstec.arlearn2.gwt.client.network.ActionClient;
import org.celstec.arlearn2.gwt.client.network.ActionsCallback;
import org.celstec.arlearn2.gwt.client.network.run.RunDataSource;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class UserActionsWindow extends Window {
	
	private long runId;
	
	public UserActionsWindow(final String email, String name, long runId) {
		this.runId = runId;
		 Label label = new Label(  
	                "<b>Severity 1</b> - Critical problem<br/>System is unavailable in production or is corrupting data, and the error severely impacts the user's operations.<br/><br/>"  
	                        + "<b>Severity 2</b> - Major problem<br/>An important function of the system is not available in production, and the user's operations are restricted.<br/><br/>"  
	                        + "<b>Severity 3</b> - Minor problem<br/>Inability to use a function of the system occurs, but it does not seriously affect the user's operations.");  
	        label.setWidth100();  
	        label.setHeight100();  
	        label.setPadding(5);  
	        label.setValign(VerticalAlignment.TOP);  
	  
	        setAutoSize(true);  
	        setTitle(name);  
	        setWidth(300);  
//	        setHeight(200);  
	        setLeft(0);  
	        setCanDragReposition(true);  
	        setCanDragResize(true);  
//	        addItem(label);  
	        
	    	ListGrid listGrid = new ListGrid();
	    	listGrid.setShowAllRecords(true);
	    	listGrid.setWidth("100%");
			listGrid.setHeight("*");
			final MyDataSource dataSource= new MyDataSource();
			listGrid.setDataSource(dataSource);
			addItem(listGrid);
			listGrid.fetchData();
			
			
			ActionClient.getInstance().getActions(runId, new ActionsCallback() {

				@Override
				public void onError() {

				}

				@Override
				public void onActionsReady() {
					for (int i = 0; i < actionsSize(); i++) {
						final ListGridRecord rec = new ListGridRecord();

						rec.setAttribute("account", getUserEmail(i));
						Date now = new Date(getTimeStamp(i));
						
						rec.setAttribute("id", DateTimeFormat.getLongDateTimeFormat().format(now));
						rec.setAttribute("action", getAction(i));
						if (email.equals(getUserEmail(i))) dataSource.addData(rec);
					}
				}
			});
			

	}
	
	public class MyDataSource extends DataSource {
		
		public MyDataSource() {
			
			init();
//			for (int i = 0; i<50; i++){
//				final ListGridRecord rec = new ListGridRecord();
//
//				rec.setAttribute("account", "ac "+i);
//				rec.setAttribute("id", i);
//				addData(rec);
//			}
			


		}
		private void init() {
			DataSourceTextField itemIdField = new DataSourceTextField("id");

			DataSourceTextField accountField = new DataSourceTextField("account");
			DataSourceTextField actionField = new DataSourceTextField("action");
			accountField.setPrimaryKey(true);
			setFields(itemIdField, accountField, actionField);

			setClientOnly(true);
		}
	}

}
