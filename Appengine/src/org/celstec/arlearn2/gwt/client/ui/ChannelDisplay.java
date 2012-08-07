package org.celstec.arlearn2.gwt.client.ui;

import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;

import com.google.gwt.json.client.JSONObject;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.CellHoverEvent;
import com.smartgwt.client.widgets.grid.events.CellHoverHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

public class ChannelDisplay extends MasterDetailTab {
	
	private DisplayNotificationHandler pongHandler = new DisplayNotificationHandler();
	private ListGrid notificationsGrid;
	private DataSource datasource;
	private RichTextEditor richTextEditor;
	

	public ChannelDisplay() {
		super("Channel");
		
		
		initNotifications();
		initDataSource();
		initGrid();
		setMasterCanvas(master());
		setDetailCanvas(detail());
	}
	
	private VLayout master() {
		VLayout returnStack = new VLayout();
		
		returnStack.setHeight100();
		returnStack.setBorder("1px solid gray");
		returnStack.setWidth(500);
		
		returnStack.addMember(notificationsGrid);
		return returnStack;
	}
	
	private VLayout detail() {
		VLayout rightCanvas = new VLayout();
		rightCanvas.setHeight100();
		richTextEditor = new RichTextEditor();
		richTextEditor.setHeight(300);
//		richTextEditor.setWidth(440);
		richTextEditor.setWidth100();
//		richTextEditor.setShowEdges(true);
		richTextEditor.setBorder("1px solid grey");

		richTextEditor.setControlGroups(new String[] { "fontControls",
				"formatControls", "styleControls" });
		rightCanvas.addMember(richTextEditor);
		return rightCanvas;
	}
	
	private void initNotifications() {
		NotificationSubscriber.getInstance().addNotificationHandler("all",pongHandler);		
	}
	
	private void initDataSource() {
		datasource = new DataSource();
		datasource.setClientOnly(true);
		DataSourceTextField timestampField = new DataSourceTextField("timestamp");
		DataSourceTextField typeField = new DataSourceTextField("type");
		datasource.setFields(timestampField, typeField);
	}
	private void initGrid() {
		notificationsGrid = new ListGrid();
		notificationsGrid.setHeight100();

		notificationsGrid.setDataSource(datasource);
		notificationsGrid.setCanEdit(true);
		ListGridField imageField = new ListGridField("timestamp", "timestamp");
		ListGridField audioField = new ListGridField("type", "type");

		notificationsGrid.setFields(new ListGridField[] { imageField, audioField });
		notificationsGrid.setSortField("sortKey");
		notificationsGrid.fetchData();

		notificationsGrid.addCellHoverHandler(new CellHoverHandler() {
			
			@Override
			public void onCellHover(CellHoverEvent event) {
				// TODO Auto-generated method stub
				updateRecord(event.getRecord());
				
			}
		});
		notificationsGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
System.out.println("selection changed "+event.getSelectedRecord());
if (event.getSelectedRecord()!= null) updateRecord(event.getSelectedRecord());
//				// TODO Auto-generated method stub
//				
			}
		});
		notificationsGrid.addCellClickHandler(new CellClickHandler() {

			@Override
			public void onCellClick(CellClickEvent event) {
				updateRecord(event.getRecord());
			}
		});
		notificationsGrid.setShowFilterEditor(true);

	}
	
	private void updateRecord(ListGridRecord record) {

		richTextEditor.setValue(record.getAttribute("payload"));
	}
	
	public class DisplayNotificationHandler implements NotificationHandler {
		@Override
		public void onNotification(final JSONObject bean) {
			final ListGridRecord rec = new ListGridRecord();
			if (bean.containsKey("type")) rec.setAttribute("type", bean.get("type").isString().stringValue());
			if (bean.containsKey("timestamp")) rec.setAttribute("timestamp", ""+((long)bean.get("timestamp").isNumber().doubleValue()));
			
			rec.setAttribute("payload", ""+bean.toString());
			datasource.addData(rec);

			
		}
	}
}
