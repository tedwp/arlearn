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
