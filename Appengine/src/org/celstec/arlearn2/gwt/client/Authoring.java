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

import org.celstec.arlearn2.gwt.client.control.Authentication;
import org.celstec.arlearn2.gwt.client.ui.ActionTab;
import org.celstec.arlearn2.gwt.client.ui.GamesTab;
import org.celstec.arlearn2.gwt.client.ui.LoginTab;
import org.celstec.arlearn2.gwt.client.ui.RunsTab;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Authoring implements EntryPoint {
	private Button clickMeButton;
	
	private static TabSet topTabSet;
	private static Tab loginTab;
	private static Tab gamesTab;
	private static Tab runsTab;
	
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get("container");

		topTabSet = new TabSet();
		topTabSet.setSize("100%", "100%");
		topTabSet.setTabBarPosition(Side.TOP);
		topTabSet.setTabBarAlign(Side.LEFT);
        
		gamesTab =new GamesTab();
		runsTab = new RunsTab();
		loginTab = new LoginTab();
        topTabSet.addTab(gamesTab);  
        topTabSet.addTab(runsTab);  
        topTabSet.addTab(loginTab);  
//        topTabSet.addTab(new ActionTab());  
        
        if (!Authentication.getInstance().isAuthenticated()) {
        	topTabSet.selectTab(loginTab);
        }
		VLayout vLayout = new VLayout();  
        vLayout.setMembersMargin(15); 
               
        vLayout.addMember(topTabSet);  
        vLayout.setSize("1000", "600");
       
        rootPanel.add(vLayout);		
	}
	
	public static void addTab(Tab tab) {
		topTabSet.addTab(tab); 
	}

	public static void disableTabs() {
		for (Tab t: topTabSet.getTabs()) {
			if (t == loginTab) {
				
			} else if (t == runsTab) {
				t.setDisabled(true);
				
			} else if (t == gamesTab) {
				t.setDisabled(true);
				
			} else topTabSet.removeTab(t);
		}
		
	}

	public static void enableTabs() {
		runsTab.setDisabled(false);
		gamesTab.setDisabled(false);
		
	}
}
