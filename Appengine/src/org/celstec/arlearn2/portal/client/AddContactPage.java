package org.celstec.arlearn2.portal.client;

import org.celstec.arlearn2.gwtcommonlib.client.network.CollaborationClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window.Location;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VStack;

public class AddContactPage {

	public void loadPage() {
		final String addContactToken = Location.getParameter("id");
		CollaborationClient.getInstance().getContactDetails(addContactToken, new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				buildPage(jsonValue.isObject(), addContactToken);
			}
		});
	}
	
	public void buildPage(JSONObject contactJson, final String addContactToken) {
		
		final Window winModal = new Window();  
        winModal.setWidth(360);  
        winModal.setHeight(140);  
        winModal.setTitle("Add contact");  
        winModal.setShowMinimizeButton(false);  
        winModal.setIsModal(true);  
        winModal.setShowModalMask(true);  
        winModal.centerInPage();  
        
        winModal.addCloseClickHandler(new CloseClickHandler() {  
           

			@Override
			public void onCloseClick(CloseClickEvent event) {
				// TODO Auto-generated method stub
				
			}  
        });  

        VStack hStack = new VStack();
        hStack.setHeight(100);
        HTMLPane paneLink = new HTMLPane();
        paneLink.setHeight(75);
        String displayString = "<img style=\"float:left;margin:0 5px 0 0;\" height=\"50\" src=\""+contactJson.get("picture").isString().stringValue()+"\"/>Do you want to add "+contactJson.get("name").isString().stringValue()+ " as a contact?";
        paneLink.setContents(displayString);  
        hStack.addMember(paneLink);
        winModal.addItem(hStack);
        final IButton addContactButton = new IButton("Add Contact");    
        addContactButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addContact(addContactToken);
			}
		});  
        final IButton ignoreButton = new IButton("Ignore");    
        ignoreButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ignore();
			}
		});
        LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(5);
        HStack horStack = new HStack();
        horStack.setAlign(Alignment.CENTER);
        horStack.addMember(addContactButton);
        horStack.addMember(vSpacer);
        horStack.addMember(ignoreButton);
        horStack.setHeight(20);

        hStack.addMember(horStack);
        winModal.show();
		
	}
	
	public void addContact(final String addContactToken) {
		CollaborationClient.getInstance().confirmAddContact(addContactToken, new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				com.google.gwt.user.client.Window.open("/portal.html", "_self", "");
			}
		});
	}
	
	public void ignore() {
		com.google.gwt.user.client.Window.open("/portal.html", "_self", "");

	}

}
