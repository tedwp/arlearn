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

import java.util.LinkedHashMap;

import org.celstec.arlearn2.gwt.client.control.Authentication;
import org.celstec.arlearn2.gwt.client.network.ChannelClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.smartgwt.client.widgets.Window;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

public class DatabaseTab extends MasterDetailTab {

	public DatabaseTab() {
		super("Database tab");

		setMasterCanvas(getSelectDatabase());
		setDetailCanvas(initDetailCanvas());

		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.notification.Pong", new PongHandler());

	}

	public VStack getSelectDatabase() {
		VStack returnStack = new VStack();
		returnStack.setBorder("1px solid gray");
		returnStack.setWidth(300);

		DynamicForm selectForm = new DynamicForm();

		HeaderItem header = new HeaderItem();
		header.setDefaultValue("Web Service selection");

		SelectItem selectType = new SelectItem();
		selectType.setName("selectGi");
		selectType.setTitle("select web service");
		selectType.setValueMap(getItemValues());
		selectType.setWrapTitle(false);

		selectType.addChangeHandler(selectTypeChangeHandler);

		selectForm.setFields(header, selectType);
		returnStack.addMember(selectForm);
		return returnStack;
	}

	public LinkedHashMap<String, String> getItemValues() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		
		valueMap.put("mediaCache", "MediaItemCache");
		valueMap.put("mediaCacheGeneralItems", "mediaCacheGeneralItems");
		valueMap.put("mediaCacheUpload", "mediaCacheUpload");
		valueMap.put("game", "Game");
		valueMap.put("run", "Run");
		valueMap.put("generalItems", "GeneralItems");
		valueMap.put("generalItemsVisibility", "GeneralItemsVisibility");
		valueMap.put("myLocations", "Locations");
		valueMap.put("myResponses", "Responses");
		valueMap.put("myActions", "Actions");
		return valueMap;
	}

	private ChangeHandler selectTypeChangeHandler = new ChangeHandler() {
		public void onChange(ChangeEvent event) {
			String selectedItem = (String) event.getValue();
			ChannelClient.getInstance().pingRequest(new JsonCallback() {
				public void onReceived() {
				};
			}, Authentication.getInstance().getCurrentUser(), Authentication.getInstance().getCurrentUser(), 1, selectedItem);
			event.getForm().resetValues();
		}
	};

	private VLayout initDetailCanvas() {
		rightCanvas = new VLayout();
		rightCanvas.setHeight100();
		rightCanvas.addMember(getListGeneralItemsCanvas());
		return rightCanvas;
	}

	private VLayout rightCanvas;
	private Canvas detailCanvas;
	private String[] columns;

	// private ListGrid listGrid ;
	public Canvas getListGeneralItemsCanvas() {
		detailCanvas = new Canvas();

		return detailCanvas;
	}

	private void initGrid(final JSONObject bean) {

		rightCanvas.removeMember(detailCanvas);
		detailCanvas = new Canvas();
		if (bean.containsKey("response")) {
			String responseString = bean.get("response").isString().stringValue();
			if (responseString.trim().startsWith("{")) {
			JSONObject responseObject = JSONParser.parseStrict(responseString).isObject();
			if (responseObject.containsKey("columns")) {
			JSONArray columnArray = responseObject.get("columns").isArray();
			JSONArray rowsArray = responseObject.get("rows").isArray();
			ListGrid listGrid = new ListGrid();
			listGrid.setShowRollOverCanvas(true);
			listGrid.setShowAllRecords(true);

			ListGridField[] fields = new ListGridField[columnArray.size()];
			columns = new String[columnArray.size()];
			DataSourceField[] dataSourceFields = new DataSourceField[columnArray.size()];

			for (int i = 0; i < columnArray.size(); i++) {
				String fieldName = columnArray.get(i).isString().stringValue();
				fields[i] = new ListGridField(fieldName, fieldName);
				columns[i] = fieldName;
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
				for (int i = 0; i < columns.length; i++) {
					String fieldName = columns[i];
					if (row.containsKey(fieldName))
						rec.setAttribute(fieldName, row.get(fieldName).isString().stringValue());
				}
				ds.addData(rec);

			}
			listGrid.setDataSource(ds);

			listGrid.fetchData();

			listGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
				public void onCellDoubleClick(CellDoubleClickEvent event) {
					DatabaseTab.this.openWindow(event.getRecord());
				}
			});

			detailCanvas.addChild(listGrid);
			rightCanvas.addMember(detailCanvas);
			}
			}
		}

	}

	public class PongHandler implements NotificationHandler {
		@Override
		public void onNotification(final JSONObject bean) {
			Criteria crit = new Criteria();
			// Window.alert("bean Value"+bean.get("response"));
			initGrid(bean);

		}
	}

	protected void openWindow(final ListGridRecord record) {
		Window w = new Window();
		w.setWidth(400);
		w.setHeight(500);
		final DynamicForm form = new DynamicForm();
		form.setHeight100();
		form.setWidth100();
		form.setPadding(5);

		FormItem[] items = new FormItem[columns.length];
		for (int i = 0; i < columns.length; i++) {
			TextItem userName = new TextItem();
			userName.setName(columns[i]);
			userName.setTitle(columns[i]);
			userName.setWrapTitle(false);
			userName.setValue(record.getAttribute(columns[i]));
			items[i] = userName;
		}

		form.setFields(items);
		w.addItem(form);
		w.show();

	}

}
