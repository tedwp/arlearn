package org.celstec.arlearn2.gwt.client.ui;

import java.util.HashSet;

import org.celstec.arlearn2.gwt.client.Authoring;
import org.celstec.arlearn2.gwt.client.AuthoringConstants;
//import org.celstec.arlearn2.gwt.client.control.GameDataSource;
import org.celstec.arlearn2.gwt.client.control.Authentication;
import org.celstec.arlearn2.gwt.client.control.ReadyCallback;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.game.GameDataSource;
import org.celstec.arlearn2.gwt.client.network.run.RunClient;
import org.celstec.arlearn2.gwt.client.network.run.RunDataSource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.RowSpacerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class RunsTab extends GenericTab {
	private ListGrid listGrid;
	 private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public RunsTab() {
		super("Runs");
		setLeft(getLeft());
		setRight(getRunCanvas());
	}

	@Override
	protected void tabSelect() {
		RunDataSource.getInstance().loadData(new ReadyCallback() {

			@Override
			public void ready() {
				listGrid.fetchData();
			}
		});

	}

	public Canvas getLeft() {
		VLayout vLayout = new VLayout(10);
		vLayout.addMember(getNewRunForm());
		vLayout.addMember(getExistingRunForm());
		return vLayout;
	}

	private DynamicForm getNewRunForm() {
		final TextItem titleRun = new TextItem("titleRun");
		titleRun.setTitle(constants.runName());
		titleRun.setSelectOnFocus(true);
		titleRun.setWrapTitle(false);
		titleRun.setWidth(200);

		final SelectItem item = new SelectItem("itemID");
		item.setTitle(constants.game());
		item.setOptionDataSource(GameDataSource.getInstance());
		item.setDisplayField("title");
		item.setPickListWidth(300);
		item.setValueField("gameId");
		item.setWidth(200);

		ListGridField titleGameField = new ListGridField("title");
		ListGridField creatorGameField = new ListGridField("creator");

		item.setPickListFields(titleGameField, creatorGameField);

		ButtonItem button = new ButtonItem("submit", constants.create());
		// button.setStartRow(true);
		button.setWidth(80);
		button.setStartRow(false);
		button.setEndRow(false);
		button.setColSpan(4);
		button.setAlign(Alignment.CENTER);

		final DynamicForm form = getForm(constants.createNewRun(), titleRun, item,
				new RowSpacerItem(), button);
		form.setWidth(300);

		button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				RunClient.getInstance().createRun(form.getValue("itemID"),
						form.getValueAsString("titleRun"), new JsonCallback() {
							public void onJsonReceived(JSONValue jsonValue) {
								tabSelect();
							}
						});
				titleRun.setValue("");
				item.setValue("");
			}
		});
		return form;
	}

	private DynamicForm getExistingRunForm() {
		final SelectItem item = new SelectItem("gameId");
		item.setTitle(constants.game());
		item.setOptionDataSource(GameDataSource.getInstance());
		item.setDisplayField("title");
		item.setPickListWidth(300);
		item.setValueField("gameId");
		item.setWidth(200);

		ListGridField titleGameField = new ListGridField("title");
		ListGridField creatorGameField = new ListGridField("creator");

		item.setPickListFields(titleGameField, creatorGameField);

		final UploadItem fileItem = new UploadItem("uploadRun");
		fileItem.setTitle(constants.uploadRun());
		fileItem.setWidth(200);
		fileItem.setWrapTitle(false);

		HiddenItem authItem = new HiddenItem("auth");
		authItem.setValue("auth="
				+ Authentication.getInstance().getAuthenticationToken());

		ButtonItem button = new ButtonItem("submit", constants.upload());
		// button.setStartRow(true);
		button.setWidth(80);
		button.setStartRow(false);
		button.setEndRow(false);
		button.setColSpan(4);
		button.setAlign(Alignment.CENTER);

		final DynamicForm form = getForm(constants.chooseARun(), fileItem, item,
				new RowSpacerItem(), button, authItem);
		form.setWidth(300);
		form.setEncoding(Encoding.MULTIPART);

		button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				// gameIdItem.setValue(form.getValue("itemID"));
				form.setCanSubmit(true);
				form.submit();
				item.setValue("");
				pollForUpdate();
				
			}
		});

		form.setTarget("hidden_frame");
		form.setAction("/uploadGame/fileUpload.html");
		return form;
	}
	
	private void pollForUpdate() {
		RunDataSource.getInstance().fetchData(null, new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData,
					DSRequest request) {
				HashSet<String> idSet = new HashSet<String>();
				for (Record r : response.getData()) {
					idSet.add(r.getAttribute("runId"));
				}
				System.out.println("amount of records "+ idSet);
				t = new Timer() {
					private int delay = 2000;
				public void run() {
					RunDataSource.getInstance().loadData(new ReadyCallback() {
						@Override
						public void ready() {
							
							RunDataSource.getInstance().fetchData(null, new DSCallback() {

								@Override
								public void execute(DSResponse response, Object rawData,
										DSRequest request) {
									HashSet<String> idSet = new HashSet<String>();
									boolean continueTimer = true;
									for (Record r : response.getData()) {
										if (!idSet.contains(r.getAttribute("runId"))) continueTimer = false;
									}
									if (continueTimer) {
										delay = 2 * delay;
										t.schedule(delay);
										System.out.println("Timer! ");

									}
								}
							});
							
						}
					});
				}
			};
			t.schedule(1000);
			}
		});
	}

	private int amountOfRecords = 0;
	private Timer t = null;
	// private DynamicForm getForm() {
	// final DynamicForm form = new DynamicForm();
	// form.setBorder("1px solid");
	// HeaderItem header = new HeaderItem();
	// header.setDefaultValue("Create new Run");
	//
	//
	//
	// ButtonItem button = new ButtonItem("submit", "Submit");
	// // button.setStartRow(true);
	// button.setWidth(80);
	// button.setAlign(Alignment.RIGHT);
	//
	// button.addClickHandler(new
	// com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
	// public void
	// onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event)
	// {
	// RunClient.getInstance().createRun(form.getValue("itemID"),
	// form.getValueAsString("titleRun"),new JsonCallback());
	// titleRun.setValue("");
	// item.setValue("");
	// }
	// });
	// form.setFields(header, titleRun, item, button);
	// return form;
	// }

	public Canvas getRunCanvas() {

		listGrid = new GenericListGrid(true, true, true) {
			protected void deleteItem(ListGridRecord rollOverRecord) {
				RunsTab.this.deleteRun(rollOverRecord
						.getAttributeAsInt("runId"));
			}

			protected void editItem(ListGridRecord rollOverRecord) {
				RunsTab.this.editRun(rollOverRecord);
			}
			
			protected void download(ListGridRecord rollOverRecord) {
				RunsTab.this.download(rollOverRecord);
			}
		};
		listGrid.setShowRollOverCanvas(true);

		listGrid.setWidth(500);
		listGrid.setHeight(224);
		listGrid.setShowAllRecords(true);
		listGrid.setDataSource(RunDataSource.getInstance());

		ListGridField titleGameField = new ListGridField("title", constants.run());
		ListGridField creatorGameField = new ListGridField("owner", constants.ownerAccount());

		listGrid.setFields(new ListGridField[] { titleGameField,
				creatorGameField });
		listGrid.setCanResizeFields(true);

		listGrid.setPadding(5);
		listGrid.setWidth("100%");
		listGrid.setHeight("100%");
		listGrid.fetchData();

		listGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				RunsTab.this.editRun(event.getRecord());
			}
		});
		return listGrid;
	}

	protected void editRun(ListGridRecord record) {
		long runId = Long.parseLong(record.getAttribute("runId"));
		RunTab tab = new RunTab("Run: " + record.getAttribute("title"), runId,
				Long.parseLong(record.getAttribute("gameId")));
		Authoring.addTab(tab, "run:" + runId);
	}

	private void deleteRun(int runId) {
		Authoring.removeTab("run:" + runId);
		RunDataSource.getInstance().delete(runId);
	}
	
	protected void download(ListGridRecord record) {
		long runId = Long.parseLong(record.getAttribute("runId"));
		String auth = Authentication.getInstance().getAuthenticationToken();
		Window.open( "../download/run?runId="+runId+"&auth="+auth+"&type=run", "_self", "");
		
		
	}
}
