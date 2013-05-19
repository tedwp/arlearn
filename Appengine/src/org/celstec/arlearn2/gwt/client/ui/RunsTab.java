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

import org.celstec.arlearn2.gwt.client.Authoring;
import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.control.Authentication;
import org.celstec.arlearn2.gwt.client.control.ReadyCallback;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.game.GameDataSource;
import org.celstec.arlearn2.gwt.client.network.run.RunClient;
import org.celstec.arlearn2.gwt.client.network.run.RunDataSource;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Progressbar;
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

public class RunsTab extends GenericTab implements NotificationHandler {
	private ListGrid listGrid;
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);
	private Progressbar hBar2;

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

		hBar2 = new Progressbar();
		hBar2.setVertical(false);
		hBar2.setHeight(24);
		hBar2.setWidth("*");
		vLayout.addMember(hBar2);
		hBar2.setVisibility(Visibility.HIDDEN);

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

		final DynamicForm form = getForm(constants.createNewRun(), titleRun, item, new RowSpacerItem(), button);
		form.setWidth(300);

		button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				RunClient.getInstance().createRun(form.getValue("itemID"), form.getValueAsString("titleRun"), new JsonCallback() {
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
		authItem.setValue("auth=" + Authentication.getInstance().getAuthenticationToken());

		ButtonItem button = new ButtonItem("submit", constants.upload());
		// button.setStartRow(true);
		button.setWidth(80);
		button.setStartRow(false);
		button.setEndRow(false);
		button.setColSpan(4);
		button.setAlign(Alignment.CENTER);

		final DynamicForm form = getForm(constants.chooseARun(), fileItem, item, new RowSpacerItem(), button, authItem);
		form.setWidth(300);
		form.setEncoding(Encoding.MULTIPART);

		button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				form.setCanSubmit(true);
				form.submit();
				item.setValue("");
				NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.notification.authoring.RunCreationStatus", RunsTab.this);
				hBar2.setPercentDone(0);
				hBar2.setVisibility(Visibility.VISIBLE);
			}
		});

		form.setTarget("hidden_frame");
		form.setAction("/uploadGame/fileUpload.html");
		return form;
	}

	
//	private Timer t = null;

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
		listGrid = new GenericListGrid(true, true, true, false, false) {
			protected void deleteItem(ListGridRecord rollOverRecord) {
				RunsTab.this.deleteRun(rollOverRecord.getAttributeAsInt("runId"), rollOverRecord.getAttributeAsString("title"));
			}

			protected void editItem(ListGridRecord rollOverRecord) {
				RunsTab.this.editRun(rollOverRecord);
			}

			protected void download(ListGridRecord rollOverRecord) {
				RunsTab.this.download(rollOverRecord);
			}

			protected void mapItem(ListGridRecord rollOverRecord) {
				RunsTab.this.map(rollOverRecord);
			}
		};

		listGrid.setShowRollOverCanvas(true);

		listGrid.setWidth(500);
		listGrid.setHeight(224);
		listGrid.setShowAllRecords(true);
		listGrid.setDataSource(RunDataSource.getInstance());

		ListGridField titleGameField = new ListGridField("title", constants.run());
		ListGridField creatorGameField = new ListGridField("owner", constants.ownerAccount());

		listGrid.setFields(new ListGridField[] { titleGameField, creatorGameField });
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
		Criteria crit = new Criteria();
		crit.addCriteria("deleted", false);
		listGrid.filterData(crit);
		return listGrid;
	}

	protected void editRun(ListGridRecord record) {
		long runId = Long.parseLong(record.getAttribute("runId"));
		RunTab tab = new RunTab("Run: " + record.getAttribute("title"), runId, Long.parseLong(record.getAttribute("gameId")));
		Authoring.addTab(tab, "run:" + runId);
	}

	protected void map(ListGridRecord record) {
		long runId = Long.parseLong(record.getAttribute("runId"));
//		RunMapTab tab = new RunMapTab("Run: " + record.getAttribute("title"), runId, Long.parseLong(record.getAttribute("gameId")));
//		Authoring.addTab(tab, "runmap:" + runId);
	}

	private void deleteRun(final int runId, String name) {
		SC.ask(constants.confirmDeleteUser().replace("***", name), new BooleanCallback() {
			public void execute(Boolean value) {
				if (value != null && value) {
					Authoring.removeTab("run:" + runId);
					RunDataSource.getInstance().delete(runId);
				}
			}
		});
		
	}

	protected void download(ListGridRecord record) {
		long runId = Long.parseLong(record.getAttribute("runId"));
		String auth = Authentication.getInstance().getAuthenticationToken();
		Window.open("../download/run?runId=" + runId + "&auth=" + auth + "&type=run", "_self", "");

	}

	@Override
	public void onNotification(JSONObject bean) {
		int status = (int) bean.get("status").isNumber().doubleValue();

		hBar2.setPercentDone((int) (100/3*(status+1)));  
		if (status == 100) {
			hBar2.setVisibility(Visibility.HIDDEN);
			tabSelect();
		}
	}
}
