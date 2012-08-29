package org.celstec.arlearn2.gwt.client.ui.modal;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.response.ResponseClient;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class OpenQuestionAnswerWindow extends Window {

	private long runId;
	private long itemId;

	private ListGrid answerGrid;
	// private HTMLPane htmlPane;
	private HTMLFlow htmlFlow = new HTMLFlow();

	private DataSource datasource;

	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public OpenQuestionAnswerWindow(long runId, long itemId) {
		setWidth(500);
		setHeight(600);

		setTitle(constants.answer());
		this.runId = runId;
		this.itemId = itemId;

		setIsModal(true);
		setShowModalMask(true);
		setShowResizer(true);
		setCanDragResize(true);
		centerInPage();

		answerGrid = new ListGrid();
		htmlFlow.setOverflow(Overflow.AUTO);
		htmlFlow.setPadding(10);
		htmlFlow.setContents("");
		htmlFlow.setHeight(400);

		initDataSource();
		initGrid();
		initGui();
		addCloseClickHandler(new CloseClickHandler() {
			
			@Override
			public void onCloseClick(CloseClientEvent event) {
				htmlFlow.setContents("");
				htmlFlow.destroy();
				answerGrid.destroy();
				datasource.destroy();
				OpenQuestionAnswerWindow.this.destroy();
				
			}
		});

	}

	private void initGui() {
		SectionStack sectionStack = new SectionStack();
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);

		
		SectionStackSection section1 = new SectionStackSection("html pane");
		section1.setExpanded(true);
		section1.addItem(htmlFlow);

		sectionStack.addSection(section1);

		SectionStackSection section2 = new SectionStackSection("grid");
		section2.setExpanded(true);
		section2.addItem(answerGrid);
		sectionStack.addSection(section2);
		addItem(sectionStack);
	}

	private void initDataSource() {
		datasource = new DataSource();
		datasource.setClientOnly(true);
		DataSourceTextField imageField = new DataSourceTextField("image");
		DataSourceTextField audioField = new DataSourceTextField("audio");
		DataSourceTextField videoField = new DataSourceTextField("video");
		datasource.setFields(imageField, audioField,videoField);

		ResponseClient.getInstance().getResponses(runId, itemId, new JsonCallback() {
			public void onJsonReceived(JSONValue jsonValue) {
				JSONObject answer = jsonValue.isObject();
				JSONArray responses = answer.get("responses").isArray();
				for (int i = 0; i < responses.size(); i++) {
					JSONObject responseObject = responses.get(i).isObject();
					String answerValue = responses.get(i).isObject().get("responseValue").isString().stringValue();
					JSONObject answerJson = JSONParser.parseLenient(answerValue).isObject();

					final ListGridRecord rec = new ListGridRecord();
					String imageUrl = "";
					if (answerJson.containsKey("imageUrl"))
						imageUrl = answerJson.get("imageUrl").isString().stringValue();
					rec.setAttribute("image", imageUrl);

					String audioUrl = "";
					if (answerJson.containsKey("audioUrl"))
						audioUrl = answerJson.get("audioUrl").isString().stringValue();
					rec.setAttribute("audio", audioUrl);
					
					String videoUrl = "";
					if (answerJson.containsKey("videoUrl"))
						videoUrl = answerJson.get("videoUrl").isString().stringValue();
					rec.setAttribute("video", videoUrl);
					
					
					if (i == 0) {
						displayAnswer(imageUrl, audioUrl, videoUrl);
					}

					datasource.addData(rec);
				}
			}
		});

	}

	private void initGrid() {
		answerGrid.setDataSource(datasource);
		answerGrid.setCanEdit(true);
		ListGridField imageField = new ListGridField("image", "image");
		ListGridField audioField = new ListGridField("audio", "audio");
		ListGridField videoField = new ListGridField("video", "video");

		answerGrid.setFields(new ListGridField[] { imageField, audioField, videoField });
		answerGrid.setSortField("sortKey");
		answerGrid.fetchData();

		answerGrid.addCellClickHandler(new CellClickHandler() {

			@Override
			public void onCellClick(CellClickEvent event) {
				ListGridRecord record = event.getRecord();
				displayAnswer(record.getAttribute("image"), record.getAttribute("audio"), record.getAttribute("video"));
			}
		});
	}

	private void displayAnswer(String image, String audio, String video) {
		String html = "";
		if (audio != null && !"".equals(audio)) {
			html += "<center><embed src=\"" + audio + "\" autostart=\"false\" loop=\"false\" width=\"350\" height=\"50\"></center>";
		}
		if (video != null && !"".equals(video)) {
			html += "<center><embed src=\"" + video + "\" autostart=\"false\" loop=\"false\" height=\"400\" ></center>";
		}
		if (image != null && !"".equals(image)) {
			html += "<center><img align='center' width=\"350\" src=\"" + image + "\"/></center>";
		}
		
		htmlFlow.setContents(html);

		// addItem(htmlPane);
	}

}
