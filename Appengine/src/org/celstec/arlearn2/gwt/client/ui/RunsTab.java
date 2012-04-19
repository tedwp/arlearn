package org.celstec.arlearn2.gwt.client.ui;

import org.celstec.arlearn2.gwt.client.Authoring;
//import org.celstec.arlearn2.gwt.client.control.GameDataSource;
import org.celstec.arlearn2.gwt.client.control.Authentication;
import org.celstec.arlearn2.gwt.client.control.ReadyCallback;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.game.GameDataSource;
import org.celstec.arlearn2.gwt.client.network.run.RunClient;
import org.celstec.arlearn2.gwt.client.network.run.RunDataSource;

import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
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
		titleRun.setTitle("Run name");
		titleRun.setSelectOnFocus(true);
		titleRun.setWrapTitle(false);
		titleRun.setWidth(200);

		final SelectItem item = new SelectItem("itemID");
		item.setTitle("Game");
		item.setOptionDataSource(GameDataSource.getInstance());
		item.setDisplayField("title");
		item.setPickListWidth(300);
		item.setValueField("gameId");
		item.setWidth(200);

		ListGridField titleGameField = new ListGridField("title");
		ListGridField creatorGameField = new ListGridField("creator");

		item.setPickListFields(titleGameField, creatorGameField);

		ButtonItem button = new ButtonItem("submit", "Create");
		// button.setStartRow(true);
		button.setWidth(80);
		button.setStartRow(false);
		button.setEndRow(false);
		button.setColSpan(4);
		button.setAlign(Alignment.CENTER);

		final DynamicForm form = getForm("Create new run", titleRun, item,
				new RowSpacerItem(), button);
		form.setWidth(300);

		button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				RunClient.getInstance().createRun(form.getValue("itemID"),
						form.getValueAsString("titleRun"), new JsonCallback());
				titleRun.setValue("");
				item.setValue("");
			}
		});
		return form;
	}

	private DynamicForm getExistingRunForm() {
		final SelectItem item = new SelectItem("gameId");
		item.setTitle("Game");
		item.setOptionDataSource(GameDataSource.getInstance());
		item.setDisplayField("title");
		item.setPickListWidth(300);
		item.setValueField("gameId");
		item.setWidth(200);

		ListGridField titleGameField = new ListGridField("title");
		ListGridField creatorGameField = new ListGridField("creator");

		item.setPickListFields(titleGameField, creatorGameField);

		final UploadItem fileItem = new UploadItem("uploadRun");
		fileItem.setTitle("Upload Run");
		fileItem.setWidth(200);
		fileItem.setWrapTitle(false);

		HiddenItem authItem = new HiddenItem("auth");
		authItem.setValue("auth="
				+ Authentication.getInstance().getAuthenticationToken());

		ButtonItem button = new ButtonItem("submit", "Upload");
		// button.setStartRow(true);
		button.setWidth(80);
		button.setStartRow(false);
		button.setEndRow(false);
		button.setColSpan(4);
		button.setAlign(Alignment.CENTER);

		final DynamicForm form = getForm("Upload run", fileItem, item,
				new RowSpacerItem(), button, authItem);
		form.setWidth(300);
		form.setEncoding(Encoding.MULTIPART);

		button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				// gameIdItem.setValue(form.getValue("itemID"));
				form.setCanSubmit(true);
				form.submit(new DSCallback() {

					@Override
					public void execute(DSResponse response, Object rawData,
							DSRequest request) {
						System.out.println("in callback");

					}
				});
				item.setValue("");
			}
		});

		form.setTarget("hidden_frame");
		form.setAction("/uploadGame/fileUpload.html");
		return form;
	}

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

		listGrid = new GenericListGrid(true, true) {
			protected void deleteItem(ListGridRecord rollOverRecord) {
				RunsTab.this.deleteRun(rollOverRecord
						.getAttributeAsInt("runId"));
			}
			protected void editItem(ListGridRecord rollOverRecord) {
				RunsTab.this.editRun(rollOverRecord);
			}
		};
		listGrid.setShowRollOverCanvas(true);

		listGrid.setWidth(500);
		listGrid.setHeight(224);
		listGrid.setShowAllRecords(true);
		listGrid.setDataSource(RunDataSource.getInstance());

		ListGridField titleGameField = new ListGridField("title", "Run title");
		ListGridField creatorGameField = new ListGridField("owner", "Owner");

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
		RunTab tab = new RunTab(
				"Run: " + record.getAttribute("title"),
				Long.parseLong(record.getAttribute("runId")),
				Long.parseLong(record.getAttribute("gameId")));
		Authoring.addTab(tab);		
	}

	private void deleteRun(int runId) {
		RunDataSource.getInstance().delete(runId);
	}
}
