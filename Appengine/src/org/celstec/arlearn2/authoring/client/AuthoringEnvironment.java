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
package org.celstec.arlearn2.authoring.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;

public class AuthoringEnvironment implements EntryPoint {

	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get();
		final TabSet topTabSet = new TabSet();
		topTabSet.setSize("100%", "100%");
		topTabSet.setTabBarPosition(Side.TOP);
		topTabSet.setTabBarAlign(Side.LEFT);
		
		VLayout vLayout = new VLayout();  
        vLayout.setMembersMargin(15);  
        vLayout.addMember(topTabSet);  
        vLayout.setSize("100%", "100%");
          
//        vLayout.draw();  
        rootPanel.add(vLayout);
	}

}
