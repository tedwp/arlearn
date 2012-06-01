package org.celstec.arlearn2.gwt.client.ui;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
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
