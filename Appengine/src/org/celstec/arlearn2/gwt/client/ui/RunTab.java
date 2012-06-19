package org.celstec.arlearn2.gwt.client.ui;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.control.GeneralItemsDataSource;
import org.celstec.arlearn2.gwt.client.control.ReadyCallback;
import org.celstec.arlearn2.gwt.client.control.TriggerDataSource;
//import org.celstec.arlearn2.gwt.client.control.UsersDataSource;
import org.celstec.arlearn2.gwt.client.network.ActionClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.team.TeamClient;
import org.celstec.arlearn2.gwt.client.network.team.TeamsDataSource;
import org.celstec.arlearn2.gwt.client.network.user.UserClient;
import org.celstec.arlearn2.gwt.client.network.user.UsersDataSource;
import org.celstec.arlearn2.gwt.client.ui.modal.OpenQuestionAnswerWindow;
import org.celstec.arlearn2.gwt.client.ui.modal.TeamWindow;
import org.celstec.arlearn2.gwt.client.ui.modal.UserWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;

public class RunTab extends Tab {
	private ListGrid teamGrid;
	private ListGrid userGrid;
	private ListGrid giGrid;
	private ListGrid actionGrid;

	private long runId;
	private long gameId;

	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public RunTab(String title, final long runId, final long gameId) {
		super(title);
		this.runId = runId;
		this.gameId = gameId;
		setCanClose(true);
		HLayout navLayout = new HLayout();
		navLayout.setMembersMargin(10);

		navLayout.setMembers(getSectionStack(), getRightSide());

		this.setPane(navLayout);
		addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				refreshSources();
			}
		});
	}

	public void refreshSources() {
		if (actionGrid != null) {
			Criteria crit = new Criteria();

			crit.addCriteria("gameId", (int) gameId);
			actionGrid.filterData(crit);
			TriggerDataSource.getInstance().fetchData(crit, new DSCallback() {
				
				@Override
				public void execute(DSResponse response, Object rawData, DSRequest request) {
					if (response.getData().length != 0) {
						 triggerCanvas.setVisibility(Visibility.INHERIT);

					} 
					
				}
			});
		}
		Criteria crit = new Criteria();
		crit.addCriteria("deleted", false);
		giGrid.filterData(crit);

		TeamsDataSource.getInstance().loadDataRun(runId);

		UsersDataSource.getInstance().loadDataRun(runId);
		// teamGrid.fetchData();
		// userGrid.fetchData();
	}

	private Canvas getSectionStack() {

		sectionStack = new SectionStack();
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setWidth(300);

		IButton expandButton = new IButton("Expand Players");

		SectionStackSection section1 = new SectionStackSection(constants.teams());
		section1.setExpanded(true);
		section1.addItem(getTeams());

		sectionStack.addSection(section1);

		final SectionStackSection section2 = new SectionStackSection(constants.players());
		section2.setExpanded(true);
		section2.setCanCollapse(true);
		section2.addItem(getUsers());

		sectionStack.addSection(section2);

		expandButton.setWidth(150);
		expandButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				sectionStack.expandSection(1);

			}
		});

		return sectionStack;
	}

	private SectionStack sectionStack;

	public Canvas getTeams() {
		Canvas canvas = new Canvas();
		// teamGrid = new ListGrid();
		teamGrid = new GenericListGrid(false, true) {
			protected void deleteItem(ListGridRecord rollOverRecord) {
				RunTab.this.deleteTeam(rollOverRecord.getAttributeAsString("teamId"));
			}

		};
		teamGrid.setShowRollOverCanvas(true);
		teamGrid.setWidth("100%");
		teamGrid.setHeight(245);
		teamGrid.setShowAllRecords(true);

		teamGrid.setDataSource(TeamsDataSource.getInstance());

		ListGridField nameField = new ListGridField("name", constants.teamName());
		teamGrid.setFields(new ListGridField[] { nameField });

		canvas.addChild(teamGrid);

		IButton addTeamButton = new IButton(constants.newTeam());
		// addTeamButton.setTop(245);
		addTeamButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				(new TeamWindow(runId, RunTab.this)).show();
			}
		});

		HLayout buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.addMember(addTeamButton);
		buttonLayout.setTop(245);
		buttonLayout.setHeight(20);
		buttonLayout.setWidth100();
		// buttonLayout.setBorder("2px solid blue");

		// canvas.setBorder("1px solid blue");

		canvas.addChild(buttonLayout);
		teamGrid.addCellClickHandler(new CellClickHandler() {

			@Override
			public void onCellClick(CellClickEvent event) {
				sectionStack.expandSection(1);
				filterUsers(event.getRecord().getAttribute("teamId"));
			}
		});
		// Criteria crit = new Criteria();
		// crit.addCriteria("runId", (int) runId);
		teamGrid.fetchData();
		teamGrid.filterData(new Criteria("runId", "" + runId));
		// teamGrid.filterData(new Criteria("name", "Team A"));
		return canvas;
	}

	public void filterUsers(String teamId) {
		Criteria crit = new Criteria();
		Integer critInt = new Integer((int) runId);
		crit.addCriteria("runId", critInt);
		if (teamId != null)
			crit.addCriteria("teamId", teamId);
		userGrid.filterData(crit);
	}

	protected void deleteTeam(String teamId) {
		TeamClient.getInstance().deleteTeam(teamId, new JsonCallback() {
			public void onJsonReceived(JSONValue jsonValue) {
				refreshSources();
			}
		});

	}

	public Canvas getUsers() {
		Canvas canvas = new Canvas();
		userGrid = new GenericListGrid(false, true) {
			protected void deleteItem(ListGridRecord rollOverRecord) {
				RunTab.this.deleteUser(rollOverRecord.getAttributeAsString("email"));
			}

		};
		userGrid.setShowRollOverCanvas(true);
		// userGrid = new ListGrid();
		userGrid.setWidth("100%");
		userGrid.setHeight(200);

		userGrid.setDataSource(UsersDataSource.getInstance());
		ListGridField nameField = new ListGridField("name", constants.userName());
		ListGridField emailField = new ListGridField("email", constants.email());
		ListGridField rolesField = new ListGridField("roles", constants.roles());
		userGrid.setFields(new ListGridField[] { nameField, emailField, rolesField });
		userGrid.fetchData();

		canvas.addChild(userGrid);

		IButton addUserButton = new IButton(constants.newUser());
		// addTeamButton.setTop(245);
		addUserButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				(new UserWindow(runId, gameId, RunTab.this)).show();
			}
		});

		HLayout buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.addMember(addUserButton);
		buttonLayout.setTop(200);
		buttonLayout.setHeight(20);
		buttonLayout.setWidth100();
		// buttonLayout.setBorder("2px solid blue");

		// canvas.setBorder("1px solid blue");

		canvas.addChild(buttonLayout);

		userGrid.addCellClickHandler(new CellClickHandler() {

			@Override
			public void onCellClick(final CellClickEvent event) {
				Criteria crit = new Criteria();
				crit.addCriteria("account", event.getRecord().getAttribute("email"));
				crit.addCriteria("runId", Integer.parseInt(event.getRecord().getAttribute("runId")));
				giGrid.filterData(crit);
				GeneralItemsDataSource.getInstance().loadItemsGame(gameId, runId, event.getRecord().getAttribute("email"), null);
			}
		});

		return canvas;
	}

	protected void deleteUser(String email) {
		UserClient.getInstance().deleteUser(runId, email, new JsonCallback() {
			public void onJsonReceived(JSONValue jsonValue) {
				refreshSources();
			}
		});

	}

	private Canvas triggerCanvas;
	public VLayout getRightSide() {
		VLayout layout = new VLayout();
		layout.addMember(getRunCanvas());
		triggerCanvas = getActionTriggerCanvas();
		 layout.addMember(triggerCanvas);
		 triggerCanvas.setVisibility(Visibility.HIDDEN);
		return layout;
	}

	public Canvas getRunCanvas() {

		giGrid = new ListGrid();
		giGrid.setWidth(500);
		giGrid.setHeight(224);
		giGrid.setShowAllRecords(true);
		giGrid.setDataSource(GeneralItemsDataSource.getInstance());

		// ListGridField pkField = new ListGridField("pk", "Pk");
		// ListGridField id = new ListGridField("id", constants.title());
		ListGridField titleGameField = new ListGridField("name", constants.title());
		ListGridField typeField = new ListGridField("type", constants.type());
		ListGridField readField = new ListGridField("read", constants.read());
		ListGridField correctField = new ListGridField("correct", constants.correct());
		ListGridField answerField = new ListGridField("answer", constants.answer());
		ListGridField accountField = new ListGridField("account", constants.account());

		giGrid.setFields(new ListGridField[] { accountField, titleGameField, typeField, readField, correctField, answerField });
		giGrid.setCanResizeFields(true);
		giGrid.setSortField("sortKey");

		giGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				if ("NarratorItem".equals(event.getRecord().getAttributeAsString("type")) || "AudioObject".equals(event.getRecord().getAttributeAsString("type"))) {
					String html = event.getRecord().getAttributeAsString("answer");
					// JSONObject object = new JSONObject();
					// JSONString string = new
					// JSONString("http://streetlearn.appspot.com/uploadService/414007/arlearn1/image54439871.jpg");
					// object.put("imageUrl", string);
					// string = new
					// JSONString("http://sharetec.celstec.org/arlearn/Vanessa.m4a");
					// object.put("audioUrl", string);
					// html = object.toString();
					OpenQuestionAnswerWindow window = new OpenQuestionAnswerWindow(html, "answer");
					window.show();
				}
				// sayCellEvent(countryGrid, "Double-clicked", (CountryRecord)
				// event.getRecord(), event.getColNum());
			}
		});

		giGrid.setPadding(5);
		giGrid.setWidth("100%");
		giGrid.setHeight("100%");
		giGrid.fetchData();

		return giGrid;
	}

	public Canvas getActionTriggerCanvas() {
		VLayout canvas = new VLayout();
		Label l = new Label("Send manual notifications to players");
		l.setHeight(20);
		canvas.addMember(l);
		actionGrid = new ListGrid();
		actionGrid.setWidth("100%");
		actionGrid.setHeight(150);
		// ListGridField gameField = new ListGridField("gameId", "Game Id");
		ListGridField itemIdField = new ListGridField("itemId", "Item Id");
		ListGridField nameField = new ListGridField("name", constants.itemName());
		actionGrid.setFields(new ListGridField[] { itemIdField, nameField });
		actionGrid.setDataSource(TriggerDataSource.getInstance());

		// actionGrid.setPadding(5);
		// actionGrid.setWidth("100%");
		// actionGrid.setHeight("100%");
		actionGrid.fetchData();
		canvas.addMember(actionGrid);

		IButton sendTriggers = new IButton(constants.send());
		sendTriggers.setLeft(0);
		sendTriggers.setTop(160);
		sendTriggers.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				for (ListGridRecord userRec : userGrid.getSelectedRecords()) {

					for (ListGridRecord actionRec : actionGrid.getSelectedRecords()) {

						ActionClient.getInstance().notify(runId, userRec.getAttribute("email"), actionRec.getAttribute("itemId"), new JsonCallback() {

							@Override
							public void onJsonReceived(JSONValue jsonValue) {

								userGrid.deselectAllRecords();
								actionGrid.deselectAllRecords();
							}

							@Override
							public void onError() {
								userGrid.deselectAllRecords();
								actionGrid.deselectAllRecords();
							}
						});
						userGrid.deselectAllRecords();
						actionGrid.deselectAllRecords();
					}
				}

			}
		});
		
		canvas.addMember(sendTriggers);
		return canvas;

	}
}
