package org.celstec.arlearn2.portal.client.toolbar;

import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.AccountClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ToolBar extends ToolStrip{
	protected ToolStripButton profileButton;
	public ToolBar(){
		createProfileButton();
		setWidth100();
		addFill();
		addButton(profileButton);
	}
	
	private void createProfileButton() {
		profileButton = new ToolStripButton();  
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
	}
}
