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
package org.celstec.arlearn2.gwt.client.ui.modal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.celstec.arlearn2.gwt.client.network.DatasourceUpdateHandler;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwt.client.network.response.ResponseDataSource;

import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;

import com.google.gwt.visualization.client.visualizations.corechart.PieChart;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart.PieOptions;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.DragResizeStopEvent;
import com.smartgwt.client.widgets.events.DragResizeStopHandler;

public class ChartWindow extends Window implements DatasourceUpdateHandler {

	private VerticalPanel mapContainerPanel;
	public JSONObject generalItem;
	boolean chartloaded = false;
	private long runId;
	private long gameId;
	private long itemId;
	private DataTable data = null;
	private PieChart pie = null;
	private PieOptions options;
	private HashMap<String, String> processedRecords = new HashMap<String, String>();
	// private HashMap<String, Integer> answerMapping = new HashMap<String,
	// Integer>();

	boolean pieDrawn = false;

	public ChartWindow(long gameId, long runId, long itemId) {
		super();
		this.gameId = gameId;
		this.runId = runId;
		this.itemId = itemId;
		initGui();
		processedRecords = new HashMap<String, String>();
		ResponseDataSource.getInstance().loadDataRun(runId);
		ResponseDataSource.getInstance().addNotificationHandler(this);
		Runnable onLoadCallback = new Runnable() {
			public void run() {
				chartloaded = true;
				createPieChart();
			}
		};
		VisualizationUtils.loadVisualizationApi(onLoadCallback, PieChart.PACKAGE);
		initMultipleChoiceItem();
		addCloseClickHandler(new CloseClickHandler() {


			@Override
			public void onCloseClick(CloseClickEvent event) {
				ResponseDataSource.getInstance().removeUpdateHandeler(ChartWindow.this);
				ChartWindow.this.destroy();
				
			}
		});
	}

	private void initGui() {
		setWidth("400");
		setHeight("240");
		centerInPage();
		setShowResizer(true);
		setCanDragResize(true);
		addDragResizeStopHandler(new DragResizeStopHandler() {

			@Override
			public void onDragResizeStop(DragResizeStopEvent event) {
				if (pie != null)
					pie.draw(data, createOptions(event.getX() - ChartWindow.this.getAbsoluteLeft() - 20, event.getY() - ChartWindow.this.getAbsoluteTop() - 20));

			}
		});

		mapContainerPanel = new VerticalPanel();
		mapContainerPanel.setHeight("100%");
		mapContainerPanel.setWidth("100%");
		addItem(mapContainerPanel);
	}

	private void initMultipleChoiceItem() {
		GeneralItemsClient.getInstance().getGeneralItem(gameId, itemId, new JsonCallback() {
			public void onJsonReceived(JSONValue jsonValue) {

				generalItem = jsonValue.isObject();
				createPieChart();
			}
		});
	}

	public void createPieChart() {
		if (chartloaded && generalItem != null) {
//			data = createTable();
			options = createOptions();
			updateFromDataSource();
		}
	}

	private String parseRecordForAnswer(Record record) {
		String responseValue = record.getAttribute("responseValue");
		return JSONParser.parseStrict(responseValue).isObject().get("answer").isString().stringValue();
	}

	private void buildData() {
		HashMap<String, Integer> countMap = new HashMap<String, Integer>();
		for (Map.Entry<String, String> entry : processedRecords.entrySet()) {
			String responseValue = entry.getValue();
			if (countMap.containsKey(responseValue)) {
				countMap.put(responseValue, 1 + countMap.get(responseValue));
			} else {
				countMap.put(responseValue, 1);
			}
		}
		data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Task");
		data.addColumn(ColumnType.NUMBER, "Hours per Day");
		data.addRows(countMap.size());
		int i = 0;
		for (String key : countMap.keySet()) {
			data.setValue(i, 0, key);
			data.setValue(i++, 1, countMap.get(key));
		}
	}

	@Override
	public void newRecord(Record record) {
		if (!(record.getAttributeAsLong("runId") == runId && record.getAttributeAsLong("generalItemId") == itemId)) return;
		String responseValue = parseRecordForAnswer(record);
		String pk = record.getAttribute("pk");
		if (processedRecords.containsKey(pk))
			return;
		processedRecords.put(pk, responseValue);
		if (pieDrawn) {
			buildData();
			pie.draw(data, options);
		}

	}

	private void updateFromDataSource() {
		Criteria criteria = new Criteria();
		criteria.addCriteria("runId", (int) runId);
		criteria.addCriteria("generalItemId", (int) itemId);

		ResponseDataSource.getInstance().fetchData(criteria, new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				Record[] records = response.getData();
				for (Record record : records) {
					newRecord(record);
				}
				buildData();
				pie = new PieChart(data, options);
				mapContainerPanel.add(pie.asWidget());
				pieDrawn = true;

			}
		});
	}

//	private DataTable createTable() {
//		DataTable data = DataTable.create();
//		JSONArray answers = generalItem.get("answers").isArray();
//		data.addColumn(ColumnType.STRING, "Task");
//		data.addColumn(ColumnType.NUMBER, "Hours per Day");
//		data.addRows(answers.size());
//		for (int i = 0; i < answers.size(); i++) {
//			String answer = answers.get(i).isObject().get("answer").isString().stringValue();
//			data.setValue(i, 0, answer);
//			data.setValue(i, 1, 0);
//
//		}
//		return data;
//	}

	private PieOptions createOptions() {
		return createOptions(400, 240);
	}

	private PieOptions createOptions(int width, int height) {
		String title = generalItem.get("name").isString().stringValue();
		setTitle(title);
		PieOptions options = PieOptions.create();
		options.setWidth(width);
		options.setHeight(height);
		options.set3D(true);
		return options;
	}

}
