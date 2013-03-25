package org.celstec.arlearn2.mobileclient.client;

import org.celstec.arlearn2.gwtcommonlib.client.network.LoginCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.LoginClient;

import com.smartgwt.client.util.Offline;
import com.smartgwt.mobile.client.internal.util.AnimationUtil;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.types.FormStyle;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.fields.ButtonItem;
import com.smartgwt.mobile.client.widgets.form.fields.EmailItem;
import com.smartgwt.mobile.client.widgets.form.fields.PhoneItem;
import com.smartgwt.mobile.client.widgets.form.fields.PasswordItem;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.mobile.client.widgets.layout.HLayout;
import com.smartgwt.mobile.client.widgets.layout.VLayout;

public class LoginPanel extends ScrollablePanel implements LoginCallback {

	DynamicForm dynamicForm;
	PhoneItem phoneItem;
	EmailItem emailItem;
	PasswordItem passwordItem;
	ButtonItem button;
	Panel output = new Panel();

	public LoginPanel(String title) {
		super(title);
		this.setWidth("100%");
		output.setClassName("sc-rounded-panel");
		output.getElement().getStyle().setProperty("textAlign", "center");
		output.getElement().getStyle().setOpacity(0.0);
		HLayout panelWrapper = new HLayout();
		panelWrapper.setLayoutMargin(20);
		panelWrapper.setPaddingAsLayoutMargin(true);
		panelWrapper.setMembersMargin(20);
		panelWrapper.addMember(output);

		dynamicForm = new DynamicForm();
		dynamicForm.setFormStyle(FormStyle.STYLE2);

		phoneItem = new PhoneItem("phone", "Phone Number", "Enter phone number");
		phoneItem.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (phoneItem.getValue() != null) {
					output.setContents("Phone input is " + phoneItem.getValue() + " " + (phoneItem.validate() ? "VALID" : "INVALID"));
					AnimationUtil.fadeTransition(output, true);
				}
			}
		});
		emailItem = new EmailItem("email", "Email", "Enter email");
		emailItem.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (emailItem.getValue() != null) {
					output.setContents("Email input is " + emailItem.getValue() + " " + (emailItem.validate() ? "VALID" : "INVALID"));
					AnimationUtil.fadeTransition(output, true);
				}
			}
		});
		passwordItem = new PasswordItem("password", "Password", "Enter Password");

		dynamicForm.setFields(emailItem, passwordItem);
		Button button = new Button("Submit");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				LoginClient lc = new LoginClient(emailItem.getValueAsString(), passwordItem.getValueAsString());
				Offline.put("account", emailItem.getValueAsString());
				Offline.put("normalizedAccount", normalizeEmail(emailItem.getValueAsString()));
				lc.authenticate(LoginPanel.this);
			}
		});
		VLayout vlayout = new VLayout();
		vlayout.setWidth("100%");
		vlayout.setAlign(Alignment.CENTER);
		vlayout.addMember(dynamicForm);
		vlayout.addMember(button);

		addMember(vlayout);
	}

	public static String normalizeEmail(String mail) {
		if (mail == null) return null;
		int posAt = mail.indexOf("@");
		if (posAt != -1) {
			mail = mail.substring(0, posAt);
		}
		return mail.replace(".", "").toLowerCase();
	}
	
	@Override
	public void onAuthenticationTokenReceived(String string) {
		Offline.put("auth", string);
		MobileClient.navStack.pop();
	}

	@Override
	public void onError() {
		System.out.println("error"); //TODO
	}
}
