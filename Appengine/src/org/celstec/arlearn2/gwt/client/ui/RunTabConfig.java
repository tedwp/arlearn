package org.celstec.arlearn2.gwt.client.ui;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.control.Authentication;
import org.celstec.arlearn2.gwt.client.network.ChannelClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.run.RunClient;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverEvent;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverHandler;

public class RunTabConfig  extends Canvas {
	private RunTab runTab;
	private NfcNotificationHandler pongHandler = new NfcNotificationHandler();


	private DynamicForm form;
	private CheckboxItem openRun;
	private TextItem nfcTag;
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public RunTabConfig(RunTab rt) {
		this.runTab = rt;
		initForm();
		initNotifications();
		this.addChild(form);
	}
	
	public void refreshSources() {
		RunClient.getInstance().getItemsForRun(runTab.getRunId(), new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				JSONObject bean = jsonValue.isObject();
				if (bean != null && bean.containsKey("runConfig")) {
					JSONObject configJson = bean.get("runConfig").isObject();
					if (configJson.containsKey("selfRegistration"))
						form.setValue("openRun", configJson.get("selfRegistration").isBoolean().booleanValue());
					if (configJson.containsKey("nfcTag")){
						form.setValue("nfcTag", configJson.get("nfcTag").isString().stringValue());
					}
						
				}
			}
		});		
	}
		
	private void initNotifications() {
		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.notification.Pong", pongHandler);
	}

	private void initForm() {
		form = new DynamicForm();
		form.setNumCols(3);
		HeaderItem header = new HeaderItem();
		header.setDefaultValue(constants.runOptions());
		
		openRun = new CheckboxItem();
		openRun.setName("openRun");
		openRun.setTitle(constants.openRun());
		openRun.setShowTitle(false);
		openRun.setRedrawOnChange(true);
		openRun.setEndRow(false);
		openRun.setStartRow(false);
		openRun.setColSpan(3);  
		openRun.addItemHoverHandler(new ItemHoverHandler() {
			
			@Override
			public void onItemHover(ItemHoverEvent event) {
			 openRun.setPrompt(constants.openRunHover());
			}
		});

		FormItemIfFunction openFormFunction = new FormItemIfFunction() {
			public boolean execute(FormItem item, Object value, DynamicForm form) {
				if (form.getValue("openRun") == null)
					return false;
				return form.getValue("openRun").equals(Boolean.TRUE);
			}
		};

		
		nfcTag = new TextItem("nfcTag");
		nfcTag.setColSpan(1);
		nfcTag.setShowTitle(false);
		nfcTag.setSelectOnFocus(true);
		nfcTag.setWrapTitle(false);
		nfcTag.setShowIfCondition(openFormFunction);
		nfcTag.addItemHoverHandler(new ItemHoverHandler() {
			
			@Override
			public void onItemHover(ItemHoverEvent event) {
			 String prompt = "<img src='http://qr.kaywa.com/img.php?s=8&d="+form.getValueAsString("nfcTag")+"'/>";
		        nfcTag.setPrompt(prompt);
			}
		});
		// nfcItem.setDisabled(true);
		
		ButtonItem nfcScan = new ButtonItem("scan", "scan-i18");
		nfcScan.setStartRow(false);
		nfcScan.setEndRow(false);
		nfcScan.setColSpan(1);
		nfcScan.setShowIfCondition(openFormFunction);
		nfcScan.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				sendNfcScanRequest();				
			}
		});


		ButtonItem qrbutton = new ButtonItem("scan", "qr-i18");
		qrbutton.setStartRow(false);
		qrbutton.setColSpan(1);
		qrbutton.setStartRow(false);
		qrbutton.setEndRow(false);
		qrbutton.setShowIfCondition(openFormFunction);

		
		ButtonItem button = new ButtonItem("submit", constants.submit());
		// button.setStartRow(true);
		button.setWidth(80);
		button.setStartRow(false);
		button.setEndRow(false);
		button.setColSpan(4);
		button.setAlign(Alignment.CENTER);
		button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				update();
			}
		});
		
		form.setFields(header, openRun, nfcTag,nfcScan,qrbutton, button);
		
	}
	
	private void sendNfcScanRequest() {
		String account = Authentication.getInstance().getCurrentUser();
		ChannelClient.getInstance().pingRequest(new JsonCallback() {
			public void onReceived() {
			};
		}, account, account, 2, "none");
	}
	
	public class NfcNotificationHandler implements NotificationHandler {
		@Override
		public void onNotification(final JSONObject bean) {
				form.setValue("nfcTag", bean.get("response").isString().stringValue());
		}
	}
	
	private void update() {
		RunClient.getInstance().getItemsForRun(runTab.getRunId(), new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				updateConfig(jsonValue.isObject());
				updateRun(jsonValue.isObject());
			}
			
		});		
	}
	
	private void updateConfig(JSONObject bean) {
		if (!bean.containsKey("runConfig")) {
			bean.put("runConfig", new JSONObject());
		}
		JSONObject configJson = bean.get("runConfig").isObject();
		if (form.getValue("openRun") != null) {
			boolean openRunBoolean = (Boolean)form.getValue("openRun");
			configJson.put("selfRegistration", JSONBoolean.getInstance(openRunBoolean));
			if (form.getValue("nfcTag") != null && openRunBoolean) {
				configJson.put("nfcTag", new JSONString(form.getValueAsString("nfcTag")));
			} else {
				configJson.put("nfcTag", null);
			}
		}

		
		
	};
	
	private void updateRun(JSONObject object) {
		RunClient.getInstance().updateRun(runTab.getRunId(), object, null);
		
	}
}
