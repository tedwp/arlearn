package org.celstec.arlearn2.mobileclient.client;

import org.celstec.arlearn2.gwtcommonlib.client.auth.Authentication;

import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.types.LayoutPolicy;
import com.smartgwt.mobile.client.types.NavigationMode;
import com.smartgwt.mobile.client.types.PanelHeaderPosition;
import com.smartgwt.mobile.client.types.SelectionStyle;
import com.smartgwt.mobile.client.types.VerticalAlignment;
import com.smartgwt.mobile.client.widgets.Action;
import com.smartgwt.mobile.client.widgets.ActionContext;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;

import com.smartgwt.mobile.client.widgets.Img;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.icons.IconResources;

public class SplashScreen extends ScrollablePanel {

    public SplashScreen(String title) {
    	super("SplashScreen", IconResources.INSTANCE.files());
//    	setVPolicy(LayoutPolicy.FILL);
//    	setHPolicy(LayoutPolicy.FILL);
//    	setAlign(Alignment.CENTER);
//    	setAlign(VerticalAlignment.CENTER);
//    	setWidth("100%");
//    	setHeight("100%");
    	setShowPanelHeader(false);
    	setHeaderPosition(PanelHeaderPosition.BOTTOM);
//    	
    	
    	Img img = new Img("https://lh6.ggpht.com/IRzI4Uta2C0dMQTI95ybxrb-ngGP1tfG3rM7kEW-A_YkICdIVe8EGMpmAhjBcM1exg=w124");
    	img.setWidth(140);
    	img.setHeight(90);
    	
//    	img.setShowNavigation(true);
//        img.setSelectionType(SelectionStyle.SINGLE);
//        img.setNavigationMode(NavigationMode.WHOLE_RECORD);
//        img.setParentNavStack(MobileClient.navStack);
        
    	addMember(img);
    	addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (Authentication.getInstance().isAuthenticated()) {
					System.out.println(Authentication.getInstance().getAuthenticationToken());
					MobileClient.navStack.push(new RunList("Runs"));
				} else {
					MobileClient.navStack.push(new LoginPanel("login"));
				}
				
			}
		});
    	setActions(new Action[]{
                new Action("Delete") {
                    @Override
                    public void execute(ActionContext context) {
                       
                    }
                }
            });
    }

}