package org.celstec.arlearn2.gwt.client.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

import org.celstec.arlearn2.gwt.client.control.Authentication;
import org.celstec.arlearn2.gwt.client.network.ChannelClient;
import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.run.RunClient;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;
import org.celstec.arlearn2.gwt.client.ui.DatabaseTab.PongHandler;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

public class WebServicesTab  extends MasterDetailTab {

	private VLayout rightCanvas;
	private Canvas detailCanvas;
	private String[] columns;
	private TextAreaItem messageItem;
	
	public WebServicesTab() {
		super("WS tab");
		
		setMasterCanvas(getSelectDatabase());
		setDetailCanvas(initDetailCanvas());
	}
	
	public VStack getSelectDatabase() {
		VStack returnStack = new VStack();
		returnStack.setBorder("1px solid gray");
		returnStack.setWidth(300);

		final DynamicForm selectForm = new DynamicForm();

		HeaderItem header = new HeaderItem();
		header.setDefaultValue("Web service selection");

		SelectItem selectType = new SelectItem();
		selectType.setName("selectGi");
		selectType.setTitle("select database table");
		selectType.setValueMap(getItemValues());
		selectType.setWrapTitle(false);

		selectType.addChangeHandler(selectTypeChangeHandler);

		TextItem ti = new TextItem();
		ti.setName("path");
		ti.setTitle("type url path");
		
		ButtonItem bi = new ButtonItem();
		bi.setTitle("submit path");
		bi.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String value = (String) selectForm.getValue("path");
				PathClient pc = new PathClient();
				pc.getPath(value);
			}
		});
		
		
		selectForm.setFields(header, selectType, ti, bi);
		returnStack.addMember(selectForm);
		
		final DynamicForm postForm = new DynamicForm();
		
		HeaderItem headerPost = new HeaderItem();
		headerPost.setDefaultValue("Web service post");

		
		TextItem postTi = new TextItem();
		postTi.setName("path");
		postTi.setTitle("type url path");
		
//		TextItem postTiPay = new TextItem();
//		postTiPay.setName("payload");
//		postTiPay.setTitle("post payload");
		
		ButtonItem postBi = new ButtonItem();
		postBi.setTitle("post to path");
		postBi.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String value = (String) postForm.getValue("path");
				PathClient pc = new PathClient();
				pc.postPath(value, messageItem.getValueAsString());
			}
		});
		
		postForm.setFields(headerPost, postTi, postBi);
		

		returnStack.addMember(postForm);
		return returnStack;
	}

	public LinkedHashMap<String, String> getItemValues() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("participateRun", "participateRun");
		return valueMap;
	}
	
	private ChangeHandler selectTypeChangeHandler = new ChangeHandler() {
		public void onChange(ChangeEvent event) {
			String selectedItem = (String) event.getValue();
			if ("participateRun".equals(selectedItem)){
				RunClient.getInstance().runsParticapte(new JsonCallback(){
					public void onJsonReceived(JSONValue jsonValue) {

						initGrid(jsonValue.isObject().get("runs").isArray());
					}

				});
			}
//			ChannelClient.getInstance().ping(new JsonCallback() {
//				public void onReceived() {
//				};
//			}, Authentication.getInstance().getCurrentUser(), Authentication.getInstance().getCurrentUser(), selectedItem);
			event.getForm().resetValues();
		}
	};
	
	private VLayout initDetailCanvas() {
		rightCanvas = new VLayout();
		rightCanvas.setHeight100();
		rightCanvas.addMember(getListGeneralItemsCanvas());
		return rightCanvas;
	}

	public Canvas getListGeneralItemsCanvas() {
		
		 final DynamicForm form = new DynamicForm();  
	       
	        form.setIsGroup(true);  
	        form.setWidth100();
	        form.setHeight100();  
	        form.setNumCols(2);  
	        form.setColWidths(60, "*");  
	        //form.setBorder("1px solid blue");  
	        form.setPadding(5);  
	        form.setCanDragResize(true);  
	        form.setResizeFrom("R");  
	          
	       
	  
	        messageItem = new TextAreaItem();  
	        messageItem.setShowTitle(false);  
	        messageItem.setLength(5000);  
	        messageItem.setColSpan(2);  
	        messageItem.setWidth("*");  
	        messageItem.setHeight("*");  
	  
	        form.setFields(messageItem);  
	          
	        form.draw();  
	        detailCanvas = form;
		return form;
	}
	private void initGrid(JSONArray rowsArray) {
		
		rightCanvas.removeMember(detailCanvas);
		detailCanvas = new Canvas();
		
//			String responseString = bean.get("response").isString().stringValue();
//			JSONObject responseObject = JSONParser.parseStrict(responseString).isObject();
//			JSONArray columnArray = responseObject.get("columns").isArray();
//			JSONArray rowsArray = responseObject.get("rows").isArray();
			ListGrid listGrid = new ListGrid();
			listGrid.setShowRollOverCanvas(true);
			listGrid.setShowAllRecords(true);
			HashSet<String> columnsSet = new HashSet<String>();
			for (int i = 0; i < rowsArray.size(); i++) {
				JSONObject jo = rowsArray.get(i).isObject();
				for (String key: jo.keySet()) {
					columnsSet.add(key);
				}
			}
			ArrayList<String> columns = new ArrayList<String>();
			for (String c: columnsSet) {
				columns.add(c);
			}
			
			ListGridField[] fields = new ListGridField[columns.size()];
			
			DataSourceField[] dataSourceFields = new DataSourceField[columns.size()];

			for (int i = 0; i < columns.size(); i++) {
				String fieldName = columns.get(i);
				fields[i] = new ListGridField(fieldName, fieldName);
				dataSourceFields[i] = new DataSourceTextField(fieldName);
			}
			listGrid.setFields(fields);
			listGrid.setPadding(5);
			listGrid.setWidth("100%");
			listGrid.setHeight("100%");

			listGrid.setShowFilterEditor(true);
			listGrid.setFilterOnKeypress(true);

			DataSource ds = new DataSource();
			ds.setClientOnly(true);
			ds.setFields(dataSourceFields);
			for (int j = 0; j < rowsArray.size(); j++) {
				JSONObject row = rowsArray.get(j).isObject();
				final ListGridRecord rec = new ListGridRecord();
				for (int i = 0; i < columns.size(); i++) {
					String fieldName = columns.get(i);
					if (row.containsKey(fieldName))
						if (row.get(fieldName).isString() != null) rec.setAttribute(fieldName, row.get(fieldName).isString().stringValue());
						if (row.get(fieldName).isNumber() != null) rec.setAttribute(fieldName, row.get(fieldName).isNumber().toString());
						if (row.get(fieldName).isBoolean() != null) rec.setAttribute(fieldName, row.get(fieldName).isBoolean().toString());
				}
				ds.addData(rec);

			}
			listGrid.setDataSource(ds);

			listGrid.fetchData();

//			listGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
//				public void onCellDoubleClick(CellDoubleClickEvent event) {
//					WebServicesTab.this.openWindow(event.getRecord());
//				}
//			});

			detailCanvas.addChild(listGrid);
			rightCanvas.addMember(detailCanvas);
		

	}
	
	public class PathClient  extends GenericClient {
		
		JsonCallback dummyCb =  new JsonCallback() {
			public void onJsonReceived(JSONValue jsonValue) {
				messageItem.setValue(jsonValue.toString());
			}
			
			public void onError(){
				
			}

		};
		public void getPath(String path) {
			invokeJsonGET(path, dummyCb);
		}
		
		public void getPath(String path, JsonCallback jc) {
			invokeJsonGET(path, jc);
		}
		
		public void postPath(String path, String postMessage) {
			invokeJsonPOST(path, postMessage, dummyCb);
		}
		
		public String getUrl() {
			return super.getUrl() + "";
		}
		
	}

}
