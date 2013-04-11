package org.celstec.arlearn2.resultDisplay.client;

import org.celstec.arlearn2.gwtcommonlib.client.auth.Authentication;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunRoleModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.TeamModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.UserModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.OwnerResponseDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.QueryGameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunRolesDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.TeamDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.UserDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;
import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationSubscriber;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;  
import com.smartgwt.client.widgets.form.fields.events.ClickHandler; 

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tile.TileGrid;

public class ResultDisplayOld implements EntryPoint {

	private Grid tileGrid = null;
	private List listGrid = null;
	private Mixed columnTreeGrid = null;

	private static final String password = "arl3arn123";
	private static final long runId = 3l;

	private SlideShow slide;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		TeamDataSource.getInstance().loadDataFromWeb(runId);
		UserDataSource.getInstance().loadDataFromWeb(runId);
//		 showResultDisplay();
//		showGames();
		 showResponses();
//		 showRuns();
//		 showTeams();
//		 showGeneralItems();
//		search();
	}

	private void showResultDisplayOld() {
		if (!Authentication.getInstance().isAuthenticated()) {
			Authentication.getInstance().userCredentialsReceived("arlearn1", password);
		} else {
			System.out.println(Authentication.getInstance().getAuthenticationToken());
		}
		OwnerResponseDataSource.getInstance().loadDataFromWeb(runId);

		tileGrid = Grid.getInstance();
		listGrid = List.getInstance();
		columnTreeGrid = Mixed.getInstance();

		tileGrid.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				showPreview(tileGrid);
			}
		});

		listGrid.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				showPreview(tileGrid);
			}
		});

		ResultDisplayLayout main = new ResultDisplayLayout(tileGrid, listGrid, columnTreeGrid);
		main.draw();
	}

	private void showPreview(final TileGrid tileGrid) {
		slide = SlideShow.getInstance(tileGrid);
		slide.show();
	}

	/**
	 * Legacy code -----------
	 * */

	private VLayout vLayout;

	public void showResponses() {
		RootPanel rootPanel = RootPanel.get("container");
		vLayout = new VLayout();
		vLayout.setMembersMargin(15);
		if (!Authentication.getInstance().isAuthenticated()) {
			Authentication.getInstance().userCredentialsReceived("arlearn1", password);
		} else {
			System.out.println(Authentication.getInstance().getAuthenticationToken());
		}
//		UserDataSource.getInstance().loadDataFromWeb(runId);
		OwnerResponseDataSource.getInstance().loadDataFromWeb(runId);

		// if (!Authentication.getInstance().isAuthenticated()) {
		// vLayout.addMember(new Label("not auth"));
		// } else {
		// NotificationSubscriber not = NotificationSubscriber.getInstance();
		//
		// }
		vLayout.setWidth100();
		vLayout.setHeight100();
		// vLayout.setSize("1000", "600");

		ListGrid responsesGrid = new ListGrid();

		responsesGrid.setDataSource(OwnerResponseDataSource.getInstance());
		
		ListGridField titleGameField = new ListGridField("responseValue", "responseValue");
		ListGridField creatorGameField = new ListGridField("userEmail", "email");
		ListGridField imageUrlField = new ListGridField("imageUrl", "imgae ");
		ListGridField roleField = new ListGridField("role", "read");
		ListGridField readField = new ListGridField("read", "read");
		ListGridField teamIdField = new ListGridField("teamId", "teamId");

		responsesGrid.setFields(new ListGridField[] { titleGameField, creatorGameField, imageUrlField, readField, roleField, teamIdField });
		responsesGrid.setCanResizeFields(true);

		responsesGrid.fetchData();
		vLayout.addMember(responsesGrid);
		
		ListGrid UserlistGrid = new ListGrid();

		UserlistGrid.setDataSource(UserDataSource.getInstance());
		UserDataSource.getInstance().loadDataFromWeb(118l);
		ListGridField userEmailField = new ListGridField(UserModel.EMAIL_FIELD, "email");
		ListGridField userTeamField = new ListGridField(TeamModel.TEAMID_FIELD, "teamId");
		ListGridField rolesField = new ListGridField(UserModel.ROLES_FIELD, "roles");
		

		UserlistGrid.setFields(new ListGridField[] { userEmailField, userTeamField, rolesField });
		UserlistGrid.setCanResizeFields(true);

		UserlistGrid.fetchData();
		vLayout.addMember(UserlistGrid);
		
		ListGrid listGrid = new ListGrid();

		listGrid.setDataSource(RunRolesDataSource.getInstance());
		ListGridField rolerRunIdField = new ListGridField(RunModel.RUNID_FIELD, "run id");
		ListGridField RoleNameField = new ListGridField(RunRoleModel.ROLE_FIELD, "role");
		

		listGrid.setFields(new ListGridField[] { rolerRunIdField, RoleNameField });
		listGrid.setCanResizeFields(true);

		listGrid.fetchData();
		vLayout.addMember(listGrid);
		
		rootPanel.add(vLayout);
	}

	public void showRuns() {
		RootPanel rootPanel = RootPanel.get("container");
		// vLayout = new VLayout();
		// vLayout.setMembersMargin(15);
		if (!Authentication.getInstance().isAuthenticated()) {
			Authentication.getInstance().userCredentialsReceived("arlearn1", password);
		} else {
			System.out.println(Authentication.getInstance().getAuthenticationToken());
		}

		// vLayout.setWidth100();
		// vLayout.setHeight100();

		ListGrid listGrid = new ListGrid();

		listGrid.setDataSource(RunDataSource.getInstance());
		RunDataSource.getInstance().loadDataFromWeb();
		ListGridField titleGameField = new ListGridField("title", "title");
		ListGridField creatorGameField = new ListGridField("owner", "owner");

		listGrid.setFields(new ListGridField[] { titleGameField, creatorGameField });
		listGrid.setCanResizeFields(true);

		listGrid.fetchData();
		// vLayout.addMember(listGrid);
		rootPanel.add(listGrid);
	}

	public void showGames() {
		RootPanel rootPanel = RootPanel.get("container");
		if (!Authentication.getInstance().isAuthenticated()) {
			Authentication.getInstance().userCredentialsReceived("arlearn1", password);
		} else {
			System.out.println(Authentication.getInstance().getAuthenticationToken());
			NotificationSubscriber.getInstance();

			ListGrid listGrid = new ListGrid();

			listGrid.setDataSource(GameDataSource.getInstance());
			GameDataSource.getInstance().loadDataFromWeb();
			ListGridField titleGameField = new ListGridField("title", "title");

			listGrid.setFields(new ListGridField[] { titleGameField });
			listGrid.setCanResizeFields(true);
			listGrid.setWidth100();
			listGrid.setHeight100();
			listGrid.fetchData();
			rootPanel.add(listGrid);
		}

	}
	
	public void showTeams() {
		RootPanel rootPanel = RootPanel.get("container");
		if (!Authentication.getInstance().isAuthenticated()) {
			Authentication.getInstance().userCredentialsReceived("arlearn1", password);
		} else {
			System.out.println(Authentication.getInstance().getAuthenticationToken());
			NotificationSubscriber.getInstance();

			ListGrid listGrid = new ListGrid();

			listGrid.setDataSource(TeamDataSource.getInstance());
			TeamDataSource.getInstance().loadDataFromWeb(runId);
			ListGridField titleGameField = new ListGridField(TeamModel.NAME_FIELD, TeamModel.NAME_FIELD);
			ListGridField titlIdField = new ListGridField(TeamModel.TEAMID_FIELD, TeamModel.TEAMID_FIELD);

			listGrid.setFields(new ListGridField[] { titleGameField, titlIdField });
			listGrid.setCanResizeFields(true);
			listGrid.setWidth100();
			listGrid.setHeight100();
			listGrid.fetchData();
			rootPanel.add(listGrid);
		}

	}
	
	public void showGeneralItems() {
		RootPanel rootPanel = RootPanel.get("container");
		if (!Authentication.getInstance().isAuthenticated()) {
			Authentication.getInstance().userCredentialsReceived("arlearn1", password);
		} else {
			NotificationSubscriber.getInstance();

			ListGrid listGrid = new ListGrid();

			RunClient.getInstance().getItemsForRun(runId, new JsonCallback(){
				public void onJsonReceived(JSONValue jsonValue) {
					GeneralItemDataSource.getInstance().loadDataFromWeb((long) jsonValue.isObject().get("gameId").isNumber().doubleValue());
			}
				
			});
			listGrid.setDataSource(GeneralItemDataSource.getInstance());
			GeneralItemDataSource.getInstance().loadDataFromWeb(1l);
			ListGridField idField = new ListGridField(GeneralItemModel.ID_FIELD, "identifier");
			ListGridField nameField = new ListGridField(GeneralItemModel.NAME_FIELD, TeamModel.TEAMID_FIELD);

			listGrid.setFields(new ListGridField[] { idField, nameField });
			listGrid.setCanResizeFields(true);
			listGrid.setWidth100();
			listGrid.setHeight100();
			listGrid.fetchData();
			rootPanel.add(listGrid);
		}
	}
	
//	public void search() {
//		RootPanel rootPanel = RootPanel.get("container");
//		if (!Authentication.getInstance().isAuthenticated()) {
//			Authentication.getInstance().userCredentialsReceived("arlearn1", password);
//		} else {
//			NotificationSubscriber.getInstance();
//
//			ListGrid listGrid = new ListGrid();
//			final QueryGameDataSource qgds = new QueryGameDataSource();
//			
//			listGrid.setDataSource(qgds);
////			qgds.search("test");
//			ListGridField idField = new ListGridField(GameModel.GAMEID_FIELD, "identifier");
//			ListGridField nameField = new ListGridField(GameModel.GAME_TITLE_FIELD, TeamModel.TEAMID_FIELD);
//
//			listGrid.setFields(new ListGridField[] { idField, nameField });
//			listGrid.setCanResizeFields(true);
//			listGrid.setWidth100();
//			listGrid.setHeight100();
//			listGrid.fetchData();
////			rootPanel.add(listGrid);
//			
//			final SearchForm form = new SearchForm();  
//	        form.setTop(50);  
//	        form.setNumCols(3);  
//	        final TextItem query = new TextItem();  
//	        query.setName("query");  
//	        query.setTitle("Query");  
//	        query.setDefaultValue("snowboarding");  
//	  
//	        query.addChangedHandler(new ChangedHandler() {
//				
//				@Override
//				public void onChanged(ChangedEvent event) {
//	    			qgds.search(query.getValueAsString());
//					
//				}
//			});
//	        
//	        ButtonItem button = new ButtonItem();  
//	        button.setTitle("Search");  
//	        button.setStartRow(false);  
//	        button.addClickHandler(new ClickHandler() {  
//	            public void onClick(ClickEvent event) {  
//	    			qgds.search(query.getValueAsString());
//
//	            }  
//	        });  
//	  
//	        form.setItems(query, button);  
//	        vLayout = new VLayout();
//			vLayout.setMembersMargin(15);
//			vLayout.setWidth100();
//			vLayout.setHeight100();
//			vLayout.addMember(form);
//			vLayout.addMember(listGrid);
//			
//			rootPanel.add(vLayout);
//		}
//	}
}
