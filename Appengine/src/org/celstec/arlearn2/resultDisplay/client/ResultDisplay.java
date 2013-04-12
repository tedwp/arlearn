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
import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordDoubleClickHandler;

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
		GWT.log("runId :"+runId );
//		System.out.println(runId);
		TeamDataSource.getInstance().loadDataFromWeb(runId);
		UserDataSource.getInstance().loadDataFromWeb(runId);
		//
		RunClient.getInstance().getItemsForRun(runId, new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				GeneralItemDataSource.getInstance().loadDataFromWeb((long) jsonValue.isObject().get("gameId").isNumber().doubleValue());
			}
		});
		
		showResultDisplay();
//		showResponses();
//		showRuns();
//		mainMenu();
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
		
		tileGrid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				if (!tileGrid.getRecordList().isEmpty()) {
					showPreview(tileGrid, event.getRecord());
				}				
			}
		});
		
		listGrid.addRecordDoubleClickHandler(new com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(
					com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent event) {
				if (!tileGrid.getRecordList().isEmpty()) {
					showPreview(tileGrid, event.getRecord());
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



	private void showPreview(final TileGrid tileGrid, Record record) {
		slide = SlideShow.getInstance(tileGrid, record);
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

	public void mainMenu() {
		RootPanel rootPanel = RootPanel.get("container");
		if (!Authentication.getInstance().isAuthenticated()) {
			Authentication.getInstance().userCredentialsReceived("arlearn1", password);
		} else {
			
	        vLayout = new VLayout();
	        vLayout.setHeight100();
	        vLayout.setWidth100();
//	        vLayout.setBackgroundColor("#5DA5D7");
	        vLayout.setBackgroundColor("#03652F");
//	        vLayout.setBackgroundColor("#68A333");
	        
	        HLayout icon_login = new HLayout();
	        icon_login.setStyleName("icon_login");
	        
	        Image icon = new Image("images/icon_arlearn.png");
	        icon.setWidth("200px");
	        icon_login.addMember(icon);
	        
	        
	        VLayout buttons = new VLayout();
	        
	        /*
	        
	        IButton facebook = new IButton();
	        facebook.setSrc("images/facebook_signin.png");
	        
	        IButton twitter = new IButton();
	        twitter.setIcon("/images/twitter_signin.png");
	        
	        IButton linkedin = new IButton();
	        linkedin.setSrc("images/linkedin_signin.png");
	        */
	        
	        HTML facebook = new HTML();
	        HTML twitter = new HTML();
	        HTML linkedin = new HTML();
	        facebook.setHTML("<a href=''><img src='images/facebook_signin.png' title='Sign in with Facebook' alt='Sign in with Facebook' /></a>");
	        twitter.setHTML("<a href=''><img src='images/twitter_signin.png' title='Sign in with Twitter' alt='Sign in with Twitter' /></a>");
	        linkedin.setHTML("<a href=''><img src='images/linkedin_signin.png' title='Sign in with Linkedin' alt='Sign in with Linkedin' /></a>");
	        buttons.addMember(facebook);
	        buttons.addMember(twitter);
	        buttons.addMember(linkedin);
	        
	        icon_login.addMember(buttons);
	        
	        HTML footer = new HTML();
	        footer.setHeight("10%");
	        footer.setStyleName("footer-main-interface");
	        footer.setHTML("<p> ARLearn Project | 2013 </p>");
	        
	        vLayout.addMember(icon_login);
	        vLayout.addMember(footer);
	        
			rootPanel.add(vLayout);

		}
	}
	
	

}
