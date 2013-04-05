package org.celstec.arlearn2.resultDisplay.client;

import org.celstec.arlearn2.gwtcommonlib.client.auth.Authentication;
import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthClient;
import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthFbClient;
import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthGoogleClient;
import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthLinkedIn;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.OwnerResponseDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.TeamDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.UserDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tile.TileGrid;

public class ResultDisplay implements EntryPoint {

	private Grid tileGrid = null;
	private List listGrid = null;
	private Mixed columnTreeGrid = null;
	
	private long runId;
	
	private static int NUMBER_VERSION = 1;  // Set this parameter to 1 to use first Layout 
											// Set this parameter to 2 to use second Layout
	
	private static final String password = "";
	
	private SlideShow slide;
	
	public Anchor anchorFacebook = new Anchor("Access with Facebook");
	public Anchor anchorGoogle = new Anchor("Access with Google");
	public Anchor anchorLinkedIn = new Anchor("Access with LinkedIn");
	
	public void onModuleLoad() {
		final OauthClient client = OauthClient.checkAuthentication();
		if (client != null) {
			RootPanel.get("sendButtonContainerFacebook").add(new Anchor("you are authenticated"));
			RootPanel.get("sendButtonContainerGoogle").add(new Anchor("you are authenticated"));
			RootPanel.get("sendButtonContainerLinkedIn").add(new Anchor("you are authenticated"));
			Button b = new Button("disauthenticate");
			b.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
				
				@Override
				public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
					client.disAuthenticate();
					Window.Location.reload();					
				}
			});
			vLayout = new VLayout();
			vLayout.setMembersMargin(15);
			Label accestoken = new Label(client.getAccessToken());
			vLayout.addMember(b);
			vLayout.addMember(accestoken);
			 RootPanel.get("container").add(vLayout);
		} else {
			RootPanel.get("sendButtonContainerFacebook").add(anchorFacebook);
			RootPanel.get("sendButtonContainerGoogle").add(anchorGoogle);
			RootPanel.get("sendButtonContainerLinkedIn").add(anchorLinkedIn);
			anchorFacebook.setHref((new OauthFbClient()).getLoginRedirectURL());
			anchorGoogle.setHref((new OauthGoogleClient()).getLoginRedirectURL());
			anchorLinkedIn.setHref((new OauthLinkedIn()).getLoginRedirectURL());
			
		}

	    	  
	    	  System.out.println("AccessToken Cookie "+Cookies.getCookie("arlearn.AccessToken"));
	   
	   

	}
	
	
	
	
	
	    
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad3() {
		
		
		
		runId = Long.parseLong(com.google.gwt.user.client.Window.Location.getParameter("runId"));
		TeamDataSource.getInstance().loadDataFromWeb(runId);
		UserDataSource.getInstance().loadDataFromWeb(runId);
		//
		RunClient.getInstance().getItemsForRun(runId, new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				GeneralItemDataSource.getInstance().loadDataFromWeb((long) jsonValue.isObject().get("gameId").isNumber().doubleValue());
			}
		});
		
		showResultDisplay();
		//showResponses();
		//showRuns();
		//search();
	}

	private void showResultDisplay() {
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
				// TODO we also should take into account when grid has 
				// records list but user clicks outside the elements
				if (!tileGrid.getRecordList().isEmpty()) {
					showPreview(tileGrid);
				}				
			}
		});
		
		listGrid.addDoubleClickHandler(new DoubleClickHandler() {			
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				// TODO we also should take into account when grid has 
				// records list but user clicks outside the elements
				if (!tileGrid.getRecordList().isEmpty()) {
					showPreview(tileGrid);
				}
			}
		});
		
		switch (NUMBER_VERSION) {
		case 1:
			ResultDisplayLayout main_v1 = new ResultDisplayLayout(tileGrid, listGrid, columnTreeGrid);
			main_v1.draw();
			break;
		case 2:
			ResultDisplayLayoutSideBar main_v2 = new ResultDisplayLayoutSideBar(tileGrid, listGrid, columnTreeGrid);
			main_v2.draw();
			break;

		default:
			break;
		}

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

		ListGrid listGrid = new ListGrid();

		listGrid.setDataSource(OwnerResponseDataSource.getInstance());
		OwnerResponseDataSource.getInstance().loadDataFromWeb(runId);
		ListGridField titleGameField = new ListGridField("responseValue", "responseValue");
		ListGridField creatorGameField = new ListGridField("userEmail", "email");
		ListGridField imageUrlField = new ListGridField("imageUrl", "imgae ");
		ListGridField roleField = new ListGridField("role", "read ");
		ListGridField readField = new ListGridField("read", "read ");

		listGrid.setFields(new ListGridField[] { titleGameField, creatorGameField, imageUrlField , readField, roleField});
		listGrid.setCanResizeFields(true);

		listGrid.fetchData();
		vLayout.addMember(listGrid);
		rootPanel.add(vLayout);
	}
	
	
	public void showRuns() {
		RootPanel rootPanel = RootPanel.get("container");
//		vLayout = new VLayout();
//		vLayout.setMembersMargin(15);
		if (!Authentication.getInstance().isAuthenticated()) {
			Authentication.getInstance().userCredentialsReceived("arlearn1", password);
		} else {
			System.out.println(Authentication.getInstance().getAuthenticationToken());
		}

//		vLayout.setWidth100();
//		vLayout.setHeight100();

		ListGrid listGrid = new ListGrid();

		listGrid.setDataSource(RunDataSource.getInstance());
		RunDataSource.getInstance().loadDataFromWeb();
		ListGridField titleGameField = new ListGridField("title", "title");
		ListGridField creatorGameField = new ListGridField("owner", "owner");

		listGrid.setFields(new ListGridField[] { titleGameField, creatorGameField });
		listGrid.setCanResizeFields(true);

		listGrid.fetchData();
//		vLayout.addMember(listGrid);
		rootPanel.add(listGrid);
	}
	
//	public void search() {
//		RootPanel rootPanel = RootPanel.get("container");
//		if (!Authentication.getInstance().isAuthenticated()) {
//			Authentication.getInstance().userCredentialsReceived("arlearn1", password);
//		} else {
//			//NotificationSubscriber.getInstance();
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
//	    			//getGrid().setData(getGridData(listRecords));
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
