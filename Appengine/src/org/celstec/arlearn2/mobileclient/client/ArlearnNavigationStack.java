package org.celstec.arlearn2.mobileclient.client;

//import com.smartgwt.client.widgets.HTMLPane;
import java.util.LinkedList;

import org.celstec.arlearn2.gwtcommonlib.client.auth.Authentication;

import com.smartgwt.client.util.Offline;
import com.smartgwt.mobile.client.core.Function;
import com.smartgwt.mobile.client.internal.util.AnimationUtil.Direction;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.types.IconAlign;
import com.smartgwt.mobile.client.types.VerticalAlignment;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.Action;
import com.smartgwt.mobile.client.widgets.ActionContext;
import com.smartgwt.mobile.client.widgets.Img;
import com.smartgwt.mobile.client.widgets.NavigationButton;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.icons.IconResources;
import com.smartgwt.mobile.client.widgets.layout.HLayout;
import com.smartgwt.mobile.client.widgets.layout.Layout;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.Panel;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

public class ArlearnNavigationStack extends NavStack {

	NavigationButton navButton;

	public ArlearnNavigationStack() {
		super("Widgets", IconResources.INSTANCE.files());
		if (Offline.get("auth") != null) {
			Authentication.getInstance().onAuthenticationTokenReceived((String)Offline.get("auth"));
		}
		
		final Panel splashScreen = new Panel("Welcome", IconResources.INSTANCE.files()) {
			public void onLoad() {
				System.out.println("load");
				initNavButton();
			}
		};
		final Layout layout2 = new HLayout();
		layout2.setWidth("100%");
        layout2.setHeight("120px");
        layout2.setAlign(Alignment.CENTER);
        layout2.getElement().getStyle().setProperty("marginLeft", "auto");
        layout2.getElement().getStyle().setProperty("marginRight", "auto");
        layout2.setAlign(VerticalAlignment.TOP);
        
		Img img = new Img("https://lh6.ggpht.com/IRzI4Uta2C0dMQTI95ybxrb-ngGP1tfG3rM7kEW-A_YkICdIVe8EGMpmAhjBcM1exg=w124");
		img.setWidth(280);
		img.setHeight(200);
		
		
		splashScreen.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				if (Authentication.getInstance().isAuthenticated()) {
					
					MobileClient.navStack.push(new RunList("Runs"));
				} else {
					MobileClient.navStack.push(new LoginPanel("Login"));
				}
				
			}
		});
		
		layout2.addMember(img);
		splashScreen.addMember(layout2);
		

		navButton = new NavigationButton("logout");
		navButton.setIcon(IconResources.INSTANCE.contacts(), true);
		navButton.addClickHandler(loginClickHandler);
		
		getNavigationBar().setRightButton(navButton);
		push(splashScreen);

	}
	
	public void initNavButton() {
		
		if (Authentication.getInstance().isAuthenticated()) {
			navButton.setTitle("logout");
		} else {
			navButton.setTitle("login");
		}
		
		
	}
	
	private ClickHandler loginClickHandler = new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			if (navButton.getTitle().equals("logout")) {
				Authentication.getInstance().disAuthenticate();
				Offline.remove("auth");
				initNavButton();
			} else {
				MobileClient.navStack.push(new LoginPanel("login"));
			}
		}
	};

	
	public void loadSplashScreen() {
		// push(new SplashScreen("test"));
		push(new SplashScreen("test"), Direction.DOWN);
	}
	
	private LinkedList<Panel> panels = new LinkedList<Panel>();
	
	@Override
	public void push(Panel p) {
		super.push(p);
		panels.add(p);
	}
	
	@Override
	public Panel pop() {
		panels.removeLast();
		return super.pop();
	}
	
	public void popToRunList() {
		System.out.println("is instance of runlist "+(panels.getLast() instanceof RunList));
		if (!(panels.getLast() instanceof RunList || panels.getLast() instanceof SplashScreen)) {
			pop();
			popToRunList();
		} 
			
	}
	
	
	

}
