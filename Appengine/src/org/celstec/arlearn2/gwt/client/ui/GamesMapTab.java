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
package org.celstec.arlearn2.gwt.client.ui;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
//import com.google.gwt.maps.client.InfoWindowContent;
//import com.google.gwt.maps.client.MapWidget;
//import com.google.gwt.maps.client.Maps;
//import com.google.gwt.maps.client.control.LargeMapControl;
//import com.google.gwt.maps.client.geom.LatLng;
//import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
public class GamesMapTab  extends Tab { 

	private Canvas c;
	public GamesMapTab(String name,  VerticalPanel vPanel2) {
		super(name);
//		HLayout navLayout = new HLayout();
//		navLayout.setMembersMargin(10);
		 c = new Canvas();
	  	  c.setWidth(500);
	  	  c.setHeight100();
	  	  c.setBorder("1px");
	  	  c.setBackgroundColor("#22AAFF");
//			navLayout.addMember(new IButton());	
//			navLayout.addMember(c);	
			c.addChild(vPanel2);
		    // Add the map to the HTML host page
		    this.setPane(c);
		    c.draw();
		    
		this.setPane(c);
	}
	
	
	

}
