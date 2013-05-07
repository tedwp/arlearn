package org.celstec.arlearn2.portal.client.author.ui.tabs;

import java.util.HashMap;
import java.util.Map;

import org.celstec.arlearn2.portal.client.author.ui.game.GamesTab;
import org.celstec.arlearn2.portal.client.author.ui.gi.GeneralItemsTab;
import org.celstec.arlearn2.portal.client.author.ui.run.RunsTab;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.CloseClickHandler;
import com.smartgwt.client.widgets.tab.events.TabCloseClickEvent;

public class TabManager {
	
	private TabSet topTabSet;
	private HashMap<String, String> tabHashMap = new HashMap<String, String>();
	/**
	 * 
	 */
	private Tab gamesTab;
	private Tab runsTab;
	
	private VLayout drawableWidget;
	
	public TabManager() {
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
		
		
	}
	
	public void setAccountInformation(String name, String picture) {
//		Label account =new Label(name);
//		final Img myImage = new Img(picture, 48, 48);  
//
//		 HTMLFlow htmlFlow = new HTMLFlow();  
//	        htmlFlow.setWidth(230);  
//	        htmlFlow.setStyleName("exampleTextBlock");  
//	        String contents = "<div><b>stefaan<b><img height=\"50\" src=\""+picture+"\" /></div>";  
//	        htmlFlow.setContents(contents);  
//        
//        topTabSet.setTabBarControls(TabBarControls.TAB_SCROLLER, TabBarControls.TAB_PICKER, htmlFlow);  

	}
	
	public Widget getDrawableWidget() {
		drawableWidget = new VLayout();  
		drawableWidget.setMembersMargin(15); 
               
		drawableWidget.addMember(topTabSet);  
		drawableWidget.setWidth100();
		drawableWidget.setHeight100();
//		drawableWidget.setSize("828", "600");
		return drawableWidget;
	}

	public void addTab(GamesTab gamesTab) {
		this.gamesTab = gamesTab;
        topTabSet.addTab(gamesTab);  

	}
	
	public void addTab(RunsTab runsTab) {
		this.runsTab = runsTab;
        topTabSet.addTab(runsTab);  

	}

	public void addTab(GeneralItemsTab giTab) {
		topTabSet.addTab(giTab);
		topTabSet.selectTab(giTab);
	}
}
