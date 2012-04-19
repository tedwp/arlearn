package org.celstec.arlearn2.gwt.client.ui;

import org.celstec.arlearn2.gwt.client.control.Authentication;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class LoginTab  extends Tab {

	public LoginTab() {
		super("Authentication");
		setCanClose(false);
		initForm();
	}

	 public void initForm() {  
		  
	        final DynamicForm form = new DynamicForm();  
	        form.setAutoFocus(true);  
	        form.setNumCols(3);  
	        form.setWidth(300);  
	  
	        TextItem firstName = new TextItem("account");  
	        firstName.setTitle("Google account");  
	        firstName.setSelectOnFocus(true);  
	        firstName.setWrapTitle(false);  
	        firstName.setDefaultValue("");  
	  
	        
	        PasswordItem lastName = new PasswordItem("pw");  
	        lastName.setTitle("Password");  
	        lastName.setDefaultValue("");  
	        lastName.setWrapTitle(false);  
	  
	        ButtonItem button = new ButtonItem("submit", "Submit");  
//	        button.setStartRow(true);  
	        button.setWidth(80);  
	        
	        button.addClickHandler(new ClickHandler() {  
	            public void onClick(ClickEvent event) {  
	            	Authentication.getInstance().userCredentialsReceived(form.getValueAsString("account"), form.getValueAsString("pw"));
	            }  
	        });  
	        button.setStartRow(true);
	        button.setEndRow(false);
	        
	        ButtonItem logout = new ButtonItem("logout", "Logout");  
	        logout.setWidth(80);  
	        
	        logout.addClickHandler(new ClickHandler() {  
	            public void onClick(ClickEvent event) {  
	            	Authentication.getInstance().disAuthenticate();
	            }  
	        });  
	        logout.setStartRow(false);
	        logout.setEndRow(false);
	  
	        form.setFields(firstName, lastName, button, logout);  
	        
	        form.setAlign(Alignment.CENTER);
	        final HLayout hLayoutAlignCenter = new HLayout();  
	        hLayoutAlignCenter.setAlign(Alignment.CENTER);
	        hLayoutAlignCenter.addMember(form);
	        this.setPane(hLayoutAlignCenter);
	    }  
}
