package org.celstec.arlearn2.gwt.client.ui;

import org.celstec.arlearn2.gwt.client.control.GeneralItemsDataSource;
import org.celstec.arlearn2.gwt.client.control.ReadyCallback;
import org.celstec.arlearn2.gwt.client.control.TriggerDataSource;
//import org.celstec.arlearn2.gwt.client.control.UsersDataSource;
import org.celstec.arlearn2.gwt.client.network.ActionClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.team.TeamsDataSource;
import org.celstec.arlearn2.gwt.client.network.user.UsersDataSource;

import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
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

public class RunTab extends Tab {
	private ListGrid teamGrid;
	private ListGrid userGrid;
	private ListGrid giGrid;
	private ListGrid actionGrid;

	private long runId;
	private long gameId;

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
				Criteria crit = new Criteria();
//				Integer critInt = new Integer((int) runId);
				crit.addCriteria("gameId", (int) gameId);
				actionGrid.filterData(crit);
				
				TeamsDataSource.getInstance().loadDataRun(runId);
//						new ReadyCallback() {
//
//							@Override
//							public void ready() {
//								teamGrid.fetchData();
//								Criteria crit = new Criteria();
//								Integer critInt = new Integer((int) runId);
//								crit.addCriteria("runId", critInt);
//								teamGrid.filterData(crit);
//							}
//						});
				UsersDataSource.getInstance().loadDataRun(runId);
//				.loadUsers(runId,
//						new ReadyCallback() {
//
//							@Override
//							public void ready() {
//								userGrid.fetchData();
//								 Criteria crit = new Criteria();
//								 Integer critInt = new Integer((int) runId);
//								 crit.addCriteria("runId", critInt);
//								 userGrid.filterData(crit);
//							}
//						});

			}
		});
	}

	private Canvas getSectionStack() {

		sectionStack = new SectionStack();
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setWidth(300);

		IButton expandButton = new IButton("Expand Players");

		SectionStackSection section1 = new SectionStackSection("Teams");
		section1.setExpanded(true);
		section1.addItem(getTeams());

		sectionStack.addSection(section1);

		final SectionStackSection section2 = new SectionStackSection("Players");
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
		teamGrid = new ListGrid();
		teamGrid.setWidth("100%");
		teamGrid.setHeight(240);
		teamGrid.setShowAllRecords(true);

		teamGrid.setDataSource(TeamsDataSource.getInstance());

		ListGridField nameField = new ListGridField("name", "Team name");
		teamGrid.setFields(new ListGridField[] { nameField });

		canvas.addChild(teamGrid);

		// IButton editButton = new IButton("New team");
		// editButton.setTop(250);
		// editButton.addClickHandler(new ClickHandler() {
		// public void onClick(ClickEvent event) {
		// teamGrid.startEditingNew();
		//
		// }
		// });
		//
		// canvas.addChild(editButton);
		teamGrid.addCellClickHandler(new CellClickHandler() {

			@Override
			public void onCellClick(CellClickEvent event) {
				sectionStack.expandSection(1);
				Criteria crit = new Criteria();
				Integer critInt = new Integer((int) runId);
				crit.addCriteria("runId", critInt);
				crit.addCriteria("teamId",
						event.getRecord().getAttribute("teamId"));
				userGrid.filterData(crit);
			}
		});
		// Criteria crit = new Criteria();
		// crit.addCriteria("runId", (int) runId);
		teamGrid.fetchData();
		teamGrid.filterData(new Criteria("runId", "" + runId));
		// teamGrid.filterData(new Criteria("name", "Team A"));
		return canvas;
	}

	public Canvas getUsers() {
		Canvas canvas = new Canvas();
		userGrid = new ListGrid();
		userGrid.setWidth("100%");
		userGrid.setHeight(240);

		userGrid.setDataSource(UsersDataSource.getInstance());
		ListGridField nameField = new ListGridField("name", "User name");
		ListGridField emailField = new ListGridField("email", "Email");
		userGrid.setFields(new ListGridField[] {nameField, emailField });
		userGrid.fetchData();
		canvas.addChild(userGrid);

		userGrid.addCellClickHandler(new CellClickHandler() {

			@Override
			public void onCellClick(final CellClickEvent event) {
				GeneralItemsDataSource.getInstance().loadItemsGame(gameId,
						runId, event.getRecord().getAttribute("email"),
						new ReadyCallback() {

							@Override
							public void ready() {
								giGrid.fetchData();
								Criteria crit = new Criteria();
								crit.addCriteria("account", event.getRecord()
										.getAttribute("email"));
								crit.addCriteria("runId", Integer
										.parseInt(event.getRecord()
												.getAttribute("runId")));
								giGrid.filterData(crit);

							}
						});
			}
		});
		// IButton editButton = new IButton("New player");
		// editButton.setTop(250);
		// editButton.addClickHandler(new ClickHandler() {
		// public void onClick(ClickEvent event) {
		// userGrid.startEditingNew();
		// }
		// });
		//
		// canvas.addChild(editButton);

		return canvas;
	}

	public VLayout getRightSide() {
		VLayout layout = new VLayout();
		layout.addMember(getRunCanvas());
		layout.addMember(getActionTriggerCanvas());
		return layout;
	}

	public Canvas getRunCanvas() {

		giGrid = new ListGrid();
		// {
		//
		// protected Canvas getExpansionComponent(final ListGridRecord record) {
		//
		// return getSubCanvans(record);
		// }
		// };
		// giGrid.setCanExpandRecords(true);

		giGrid.setWidth(500);
		giGrid.setHeight(224);
		giGrid.setShowAllRecords(true);
		giGrid.setDataSource(GeneralItemsDataSource.getInstance());

		// ListGridField pkField = new ListGridField("pk", "Pk");
		ListGridField titleGameField = new ListGridField("name", "Title");
		ListGridField typeField = new ListGridField("type", "Type");
		ListGridField readField = new ListGridField("read", "Read");
		ListGridField correctField = new ListGridField("correct", "Correct");
		ListGridField answerField = new ListGridField("answer", "Answer");
		ListGridField accountField = new ListGridField("account", "Account");

		giGrid.setFields(new ListGridField[] { accountField, titleGameField,
				typeField, readField, correctField, answerField });
		giGrid.setCanResizeFields(true);

		giGrid.setPadding(5);
		giGrid.setWidth("100%");
		giGrid.setHeight("100%");
		giGrid.fetchData();
		return giGrid;
	}

	public Canvas getActionTriggerCanvas() {
		Canvas canvas = new Canvas();
		actionGrid = new ListGrid();
		actionGrid.setWidth("100%");
		actionGrid.setHeight(150);
//		ListGridField gameField = new ListGridField("gameId", "Game Id");
		ListGridField itemIdField = new ListGridField("itemId", "Item Id");
		ListGridField nameField = new ListGridField("name", "Item name");
		actionGrid.setFields(new ListGridField[] { itemIdField, nameField });
		actionGrid.setDataSource(TriggerDataSource.getInstance());

//		actionGrid.setPadding(5);
//		actionGrid.setWidth("100%");
//		actionGrid.setHeight("100%");
		actionGrid.fetchData();
        canvas.addChild(actionGrid);  

		IButton sendTriggers = new IButton("Send");
		sendTriggers.setLeft(0);
		sendTriggers.setTop(160);
		sendTriggers.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				for (ListGridRecord userRec: userGrid.getSelectedRecords()){
					System.out.println(userRec.getAttribute("email"));

					for (ListGridRecord actionRec: actionGrid.getSelectedRecords()){

						System.out.println(actionRec.getAttribute("itemId"));
						ActionClient.getInstance().notify(runId, userRec.getAttribute("email"), actionRec.getAttribute("itemId"), new JsonCallback() {
							
							@Override
							public void onJsonReceived(JSONValue jsonValue) {
								System.out.println("time for deselection");
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
		canvas.addChild(sendTriggers);
		return canvas;

	}
}
