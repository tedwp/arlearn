/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.celstec.arlearn2.gwt.client;

import java.util.HashMap;
import java.util.Map;

import org.celstec.arlearn2.delegators.notification.ChannelNotificator;
import org.celstec.arlearn2.gwt.client.control.Authentication;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;
import org.celstec.arlearn2.gwt.client.ui.GamesMapTab;
import org.celstec.arlearn2.gwt.client.ui.GamesTab;
import org.celstec.arlearn2.gwt.client.ui.LoginWindow;
import org.celstec.arlearn2.gwt.client.ui.RunsTab;

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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.widgets.IButton;
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
	
	
	
	public void setCanvas() {
		
	}
	
	private void buildUi() {
	    // Open a map centered on Cawker City, KS USA
	    LatLng cawkerCity = LatLng.newInstance(39.509, -98.434);

	    final MapWidget map = new MapWidget(cawkerCity, 2);
	    map.setSize("100%", "100%");
	    // Add some controls for the zoom level
	    map.addControl(new LargeMapControl());

	    // Add a marker
	    MarkerOptions options = MarkerOptions.newInstance();
	    options.setDraggable(true);
	    
	    Marker m = new Marker(map.getCenter(), options);

	    m.addMarkerDragEndHandler(new MarkerDragEndHandler() {
			
			@Override
			public void onDragEnd(MarkerDragEndEvent event) {
				System.out.println("dragged to "+event.getSender().getLatLng().getLatitude());
				 com.google.gwt.user.client.Window.alert("dragged to "+event.getSender().getLatLng().getLatitude());
				
			}
		});
	    map.addOverlay(m);

	    // Add an info window to highlight a point of interest
	    map.getInfoWindow().open(map.getCenter(),
	        new InfoWindowContent("World's Largest Ball of Sisal Twine"));
//	    map.setHeight("100%");
//	  	map.setWidth("600");
//	    map.addMapDragEndHandler(new MapDragEndHandler(){
//
//			@Override
//			public void onDragEnd(MapDragEndEvent event) {
////				System.out.println(event.getSender().get);
//				
//			}
//	    	
//	    });
	  	VerticalPanel vPanel2 = new VerticalPanel();  
        vPanel2.setSpacing(15);  
        vPanel2.setHeight("500px");
        vPanel2.setWidth("800px");
        
        vPanel2.add(map);
	    
	    
	  	GamesMapTab mapTab = new GamesMapTab("map", vPanel2);
        topTabSet.addTab(mapTab);
        

	  }
	
	public void onModuleLoad1() {
		RootPanel rootPanel = RootPanel.get();

		HLayout navLayout = new HLayout();
		navLayout.setMembersMargin(10);
		
		navLayout.addChild(new IButton());
		rootPanel.add(navLayout);
		
	}
	
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get("container");
		
		NotificationSubscriber not = NotificationSubscriber.getInstance();
		
		topTabSet = new TabSet();
		topTabSet.setSize("100%", "100%");
		topTabSet.setTabBarPosition(Side.TOP);
		topTabSet.setTabBarAlign(Side.LEFT);
		topTabSet.addCloseClickHandler(new CloseClickHandler() {
			
			@Override
			public void onCloseClick(TabCloseClickEvent event) {
				System.out.println("close click"+event.getTab().getID());
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
        
        Maps.loadMapsApi("AIzaSyAyNa-4zOAleXRkWRSTJLLkzyLLULS9Vxk", "2", false, new Runnable() {
		      public void run() {
		        buildUi();
		      }
		    });
        
        
  
//        topTabSet.addTab(new ActionTab());  

        
        b = new IButton(constants.login());
        if (!Authentication.getInstance().isAuthenticated()) {
    	  b.setTitle(constants.login());
        } else {
    	  b.setTitle(constants.logout());
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

        topTabSet.setTabBarControls(TabBarControls.TAB_SCROLLER, TabBarControls.TAB_PICKER, b);  
        
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
