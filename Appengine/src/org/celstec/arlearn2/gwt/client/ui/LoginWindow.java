package org.celstec.arlearn2.gwt.client.ui;

import java.util.HashMap;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.control.Authentication;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class LoginWindow  extends Window {
	
	 private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public LoginWindow() {
		setTitle(constants.authentication());
		setIsModal(true);  
        setShowModalMask(true); 
        centerInPage();
        setWidth(360);  
        setHeight(115);
		initForm();
	}

	private DynamicForm form;
	 public void initForm() {  
		  
	        form = new DynamicForm();  
	        form.setAutoFocus(true);  
	        form.setNumCols(3);  
	        form.setWidth(300);  
	  
	        TextItem firstName = new TextItem("account");  
	        firstName.setTitle(constants.googleAccount());  
	        firstName.setSelectOnFocus(true);  
	        firstName.setWrapTitle(false);  
	        firstName.setDefaultValue("");  
	  
	        
	        PasswordItem lastName = new PasswordItem("pw");  
	        lastName.setTitle(constants.password());  
	        lastName.setDefaultValue("");  
	        lastName.setWrapTitle(false);  
	  
	        ButtonItem button = new ButtonItem("submit", constants.submit());  
//	        button.setStartRow(true);  
	        button.setWidth(80);  
	        
	        button.addClickHandler(new ClickHandler() {  
	            public void onClick(ClickEvent event) {  
	            	Authentication.getInstance().userCredentialsReceived(form.getValueAsString("account"), form.getValueAsString("pw"));
	            }  
	        });  
	        button.setStartRow(true);
	        button.setEndRow(false);
	        
//	        ButtonItem logout = new ButtonItem("logout", "Logout");  
//	        logout.setWidth(80);  
//	        
//	        logout.addClickHandler(new ClickHandler() {  
//	            public void onClick(ClickEvent event) {  
//	            	Authentication.getInstance().disAuthenticate();
//	            }  
//	        });  
//	        logout.setStartRow(false);
//	        logout.setEndRow(false);
	  
	        form.setFields(firstName, lastName, button);  
	        
	        form.setAlign(Alignment.CENTER);
	        final HLayout hLayoutAlignCenter = new HLayout();  
	        hLayoutAlignCenter.setAlign(Alignment.CENTER);
	        hLayoutAlignCenter.addMember(form);
	        addItem(hLayoutAlignCenter);
//	        this.setPane(hLayoutAlignCenter);
	    }  
	 
	 public void loginDidNotSucceed(){
		 HashMap<String, String> errors = new HashMap<String, String>();
		 errors.put("account", constants.passwordOrAccountIncorrect());
		 errors.put("pw", constants.passwordOrAccountIncorrect());
		 form.setErrors(errors, true);
	 }
}