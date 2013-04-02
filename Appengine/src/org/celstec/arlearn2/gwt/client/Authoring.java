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
package org.celstec.arlearn2.gwt.client;

import java.util.HashMap;
import java.util.Map;

import org.celstec.arlearn2.gwt.client.control.Authentication;
import org.celstec.arlearn2.gwt.client.network.game.GameDataSource;
import org.celstec.arlearn2.gwt.client.network.run.RunDataSource;
import org.celstec.arlearn2.gwt.client.network.team.TeamsDataSource;
import org.celstec.arlearn2.gwt.client.network.user.UsersDataSource;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;
import org.celstec.arlearn2.gwt.client.ui.DatabaseTab;
import org.celstec.arlearn2.gwt.client.ui.GameTab.RoleDataSource;
import org.celstec.arlearn2.gwt.client.ui.ChannelDisplay;
import org.celstec.arlearn2.gwt.client.ui.GamesMapTab;
import org.celstec.arlearn2.gwt.client.ui.GamesTab;
import org.celstec.arlearn2.gwt.client.ui.GeneralItemVisiblityTab;
import org.celstec.arlearn2.gwt.client.ui.LoginWindow;
import org.celstec.arlearn2.gwt.client.ui.RunsTab;
import org.celstec.arlearn2.gwt.client.ui.WebServicesTab;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MapDragEndHandler;
import com.google.gwt.maps.client.event.MarkerDragEndHandler;
import com.google.gwt.maps.client.event.MarkerDragEndHandler.MarkerDragEndEvent;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.CloseClickHandler;
import com.smartgwt.client.widgets.tab.events.TabCloseClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Authoring implements EntryPoint {
	private Button clickMeButton;
	
	private static TabSet topTabSet;
	private static HashMap<String, String> tabHashMap = new HashMap<String, String>();
	private static IButton b;
	private static Tab gamesTab;
	private static Tab runsTab;
	private static LoginWindow lt;
	
	private static AuthoringConstants constants = GWT.create(AuthoringConstants.class);
	public static boolean hidden = false;
	
	
	public void setCanvas() {
		
	}
	
	public void onModuleLoad() {
		GeoPositioner.getInstance();
		RootPanel rootPanel = RootPanel.get("container");
		hidden = "true".equals(com.google.gwt.user.client.Window.Location.getParameter("hidden"));
		NotificationSubscriber not = NotificationSubscriber.getInstance();
		
		topTabSet = new TabSet();
		topTabSet.setSize("100%", "100%");
		topTabSet.setTabBarPosition(Side.TOP);
		topTabSet.setTabBarAlign(Side.LEFT);
		topTabSet.addCloseClickHandler(new CloseClickHandler() {
			
			@Override
			public void onCloseClick(TabCloseClickEvent event) {
				String toDelete = "";
				for (Map.Entry<String, String> entry: tabHashMap.entrySet()) {
					if (entry.getValue().equals(event.getTab().getID())) toDelete =entry.getKey();
				}
				tabHashMap.remove(toDelete);
			}
		});
		
        
		gamesTab =new GamesTab();
		runsTab = new RunsTab();
        topTabSet.addTab(gamesTab);  
        topTabSet.addTab(runsTab);  
        
        if (hidden) {
			topTabSet.addTab(new DatabaseTab());
			topTabSet.addTab(new WebServicesTab());
			topTabSet.addTab(new ChannelDisplay());
			topTabSet.addTab(new GeneralItemVisiblityTab());
		}
        
        b = new IButton(constants.login());
        Label account;
        if (!Authentication.getInstance().isAuthenticated()) {
    	  b.setTitle(constants.login());
    	  account = new Label("");
        } else {
    	  b.setTitle(constants.logout());
          account = new Label(constants.account()+": "+Authentication.getInstance().getCurrentUser());
        }
        
        b.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				 if (Authentication.getInstance().isAuthenticated()) {
					 Authentication.getInstance().disAuthenticate();
					 b.setTitle(constants.login());
				 }
				lt = new LoginWindow();
				lt.show();
				
			}
		})  ;

        
        
        
        topTabSet.setTabBarControls(TabBarControls.TAB_SCROLLER, TabBarControls.TAB_PICKER, account, b);  
        
		VLayout vLayout = new VLayout();  
        vLayout.setMembersMargin(15); 
               
        vLayout.addMember(topTabSet);  
        vLayout.setSize("1000", "600");
       
        rootPanel.add(vLayout);		
	}
	
	public static void addTab(Tab tab, String id) {
		if (tabHashMap.containsKey(id)) {
			topTabSet.selectTab(tabHashMap.get(id));
		} else {
			topTabSet.addTab(tab); 
			topTabSet.selectTab(tab);
			tabHashMap.put(id, tab.getID());	
		}
		
		
	}
	
	public static void loginIncorrect() {
		if (lt != null) lt.loginDidNotSucceed();
	}

	public static void disableTabs() {
		for (Tab t: topTabSet.getTabs()) {
			if (t == runsTab) {
				t.setDisabled(true);
				
			} else if (t == gamesTab) {
				t.setDisabled(true);
				
			} else topTabSet.removeTab(t);
		}
	}
	
	public static void clearCaches() {
		GameDataSource.getInstance().clearAllData();
		RunDataSource.getInstance().clearAllData();
		TeamsDataSource.getInstance().clearAllData();
		UsersDataSource.getInstance().clearAllData();
	}

	public static void enableTabs() {
		runsTab.setDisabled(false);
		gamesTab.setDisabled(false);
		if (lt != null) lt.destroy();
		 b.setTitle(constants.logout());
		
	}

	public static void removeTab(String id) {
		if (tabHashMap.get(id) != null) {
			topTabSet.removeTab(tabHashMap.get(id));
			tabHashMap.remove(id);
		}
	}
}
