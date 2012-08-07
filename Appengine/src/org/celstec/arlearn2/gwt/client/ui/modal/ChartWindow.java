package org.celstec.arlearn2.gwt.client.ui.modal;

import java.util.HashMap;

import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.ResponseClient;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemsClient;

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
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.DragResizeStopEvent;
import com.smartgwt.client.widgets.events.DragResizeStopHandler;

public class ChartWindow extends Window {

	private VerticalPanel mapContainerPanel;
	public JSONObject generalItem;
	boolean chartloaded = false;
	long runId;
	private DataTable data = null;
	private PieChart  pie = null;
	private PieOptions options;
	private HashMap<String, Integer> answerMapping = new HashMap<String, Integer>();

	
	public ChartWindow(long gameId, long runId, long itemId) {
		super();
		this.runId = runId;
		GeneralItemsClient.getInstance().getGeneralItem(gameId, itemId, new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				
				generalItem = jsonValue.isObject();
				createPieChart();
			}
		});
		
		setWidth("400");
		setHeight("240");
		centerInPage();
		setShowResizer(true);
		setCanDragResize(true);
		addDragResizeStopHandler(new DragResizeStopHandler() {
			
			@Override
			public void onDragResizeStop(DragResizeStopEvent event) {
				if (pie != null) pie.draw(data, createOptions(event.getX()-ChartWindow.this.getAbsoluteLeft()-20, event.getY()-ChartWindow.this.getAbsoluteTop()-20));
				
			}
		});

		mapContainerPanel = new VerticalPanel();
		mapContainerPanel.setHeight("100%");
		mapContainerPanel.setWidth("100%");
		addItem(mapContainerPanel);

		Runnable onLoadCallback = new Runnable() {
			public void run() {
				chartloaded = true;
				createPieChart();
			}
		};
		VisualizationUtils.loadVisualizationApi(onLoadCallback, PieChart.PACKAGE);

	}
	
	public void createPieChart() {
		if (chartloaded && generalItem != null) {
			data = createTable();
			options = createOptions();
			

			ResponseClient.getInstance().getItemsForRun(runId, new JsonCallback(){
				public void onJsonReceived(JSONValue jsonValue) {
					JSONObject answer = jsonValue.isObject();
					JSONArray responses = answer.get("responses").isArray();
					for (int i = 0; i < responses.size(); i++) {
						JSONObject responseObject = responses.get(i).isObject();
						if ( responseObject.get("generalItemId").isNumber().doubleValue() == generalItem.get("id").isNumber().doubleValue()) {
							String answerValue = responses.get(i).isObject().get("responseValue").isString().stringValue();

							answerValue = JSONParser.parseStrict(answerValue).isObject().get("answer").isString().stringValue();
							data.setValue(answerMapping.get(answerValue), 1, data.getValueInt(answerMapping.get(answerValue), 1) + 1);
						}
					}
					pie = new PieChart(data, options);
					mapContainerPanel.add(pie.asWidget());
					

				}
			});
		}
	}
	
	public void update(JSONObject bean) {
		if ( bean.get("generalItemId").isNumber().doubleValue() == generalItem.get("id").isNumber().doubleValue()) 

		if (data != null && pie != null) {
			String answerValue = bean.get("responseValue").isString().stringValue();
			answerValue = JSONParser.parseStrict(answerValue).isObject().get("answer").isString().stringValue();
			data.setValue(answerMapping.get(answerValue), 1, data.getValueInt(answerMapping.get(answerValue), 1)+1);
			pie.draw(data, options);
		}
		
	}
	

	private DataTable createTable() {
		DataTable data = DataTable.create();
		JSONArray answers = generalItem.get("answers").isArray();
		data.addColumn(ColumnType.STRING, "Task");
		data.addColumn(ColumnType.NUMBER, "Hours per Day");
		data.addRows(answers.size());
		for (int i = 0; i< answers.size(); i++) {
			String answer = answers.get(i).isObject().get("answer").isString().stringValue();
			answerMapping.put(answer, i);
			data.setValue(i, 0, answer);
			data.setValue(i, 1, 0);
			
		}	
		return data;
	}

	
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
//		options.setTitle(title);
		return options;
	}

	
}
