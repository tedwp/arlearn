package org.celstec.arlearn2.portal.client.author;

import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthClient;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.AccountClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.portal.client.author.ui.game.GamesTab;
import org.celstec.arlearn2.portal.client.author.ui.run.RunsTab;
import org.celstec.arlearn2.portal.client.author.ui.tabs.TabManager;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;

public class AuthorPage {

	ToolStrip toolStrip;
	
	public void loadPage() {
		createToolstrip();
		
//		toolStrip.draw();

		TabManager tabManager = TabManager.getInstance();
		tabManager.setAccountInformation("stefaan", "https://lh3.googleusercontent.com/-rRb8mSKLrNY/AAAAAAAAAAI/AAAAAAAAEQY/Y8BKx96IyHQ/photo.jpg");
		tabManager.addTab(new GamesTab(tabManager));
		tabManager.addTab(new RunsTab());
		LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(50);
		vSpacer.setHeight(10);
		
		VLayout vertical = new VLayout();
		vertical.setWidth100();
		vertical.setHeight100();
		vertical.addMember(toolStrip);
		vertical.addMember(vSpacer);
		vertical.addMember(tabManager.getDrawableWidget());
		
//		RootPanel.get("author").add(toolStrip);
//		RootPanel.get("author").add(vSpacer);
		RootPanel.get("author").add(vertical);

		RunDataSource.getInstance().loadDataFromWeb();
		// GameDataSource.getInstance().loadDataFromWeb();

	}
	
	private void createToolstrip() {
		toolStrip = new ToolStrip();
		toolStrip.setWidth100();

//		Menu menu = new Menu();
//		menu.setShowShadow(true);
//		menu.setShadowDepth(3);

//		MenuItem newItem = new MenuItem("New", "icons/16/document_plain_new.png", "Ctrl+N");
//		ToolStripMenuButton menuButton = new ToolStripMenuButton("File", menu);
//		menuButton.setWidth(100);
		
		final ToolStripButton profileButton = new ToolStripButton();  
		AccountClient.getInstance().accountDetails(new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				JSONObject object = jsonValue.isObject();
				profileButton.setIcon(object.get("picture").isString().stringValue());  
		        profileButton.setTitle(object.get("name").isString().stringValue());  
			}			
		});
		profileButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				SC.ask("Logout?", new BooleanCallback() {
					
					@Override
					public void execute(Boolean value) {
						if (value) {
							OauthClient.disAuthenticate();
							Window.open("/oauth.html", "_self", "");
						}
						
					}
				});
				
			}
		});
		
//		final MenuItem activateMenu = new MenuItem("Activate");  
//        activateMenu.setTitle("Logout");
//        menu.setItems(activateMenu);
        
          

//		toolStrip.addMenuButton(menuButton);
		toolStrip.addFill();
		toolStrip.addButton(profileButton);
	}

}
