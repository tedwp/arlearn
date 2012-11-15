package org.celstec.arlearn2.gwt.client.ui.items;

import org.celstec.arlearn2.gwt.client.ui.modal.RichTextWindow;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.StringUtil;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverEvent;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class ScanTagCanvas extends GeneralItemCanvas {

	protected DynamicForm form1 = new DynamicForm();
	protected DynamicForm form2 = new DynamicForm();

	private Label questionLabel;
	protected HTMLPane questionHtmlLabel;
	private HLayout buttonLayout;
	private IButton questionEditButton = new IButton();
	protected CheckboxItem isAutolaunchQrCode;
	

	protected final String AUTOLAUNCHQRREADER = "autoLaunchQrReader";

	
	public ScanTagCanvas(String roles[]) {
		super(roles);
		initComponents();
		doLayout();
	}
	protected void initComponents() {
		super.initComponents();
		createQuestionHtmlComponent();
		createAutoLaunchQrCodeComponent();
	}
	
	protected void doLayout() {
		doLayoutForm1();
		this.addMember(questionLabel);
		this.addMember(questionHtmlLabel);
		
		buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.RIGHT);
		buttonLayout.addMember(questionEditButton);
		buttonLayout.setHeight(20);

		this.addMember(buttonLayout);
		doLayoutForm2();
		
		isAutolaunchQrCode.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				checkVisibilityDescription();
			}
		});
	}
	
	private void checkVisibilityDescription() {
		form2.redraw();
		boolean visible = true;
    	if (form1.getValue(AUTOLAUNCHQRREADER) == null) {
    		visible = true;
    	} else {
    		visible = !form1.getValue(AUTOLAUNCHQRREADER).equals(Boolean.TRUE);
    	}
    	questionLabel.setVisible(visible);
    	buttonLayout.setVisible(visible);
    	questionHtmlLabel.setVisible(visible);
    	form2.redraw();
	}
	
	protected void doLayoutForm1() {
		this.addMember(form1);
		form1.setFields(idItem, gameIdItem, nameItem, isAutolaunchQrCode);
		addField(form1,idItem, gameIdItem, nameItem, isAutolaunchQrCode);
	}
	
	protected void doLayoutForm2() {
		this.addMember(form2);
		form2.setFields( isSimpleDependency, selectAction, selectScope, selectRole, selectGeneralItem, isAutolaunch, sortItem, roleGrid);
		addField(form2, isSimpleDependency, selectAction, selectScope, selectRole, selectGeneralItem, isAutolaunch, sortItem, roleGrid);
	}

	
	private void createQuestionHtmlComponent() {
		questionLabel = new Label(constants.description());
		questionLabel.setHeight(10);

		
		questionHtmlLabel = new HTMLPane();  
		questionHtmlLabel.setStyleName("htmlsection");  

		   
//		questionHtmlLabel.setContents("Enter your description here ...");
		questionHtmlLabel.setAlign(Alignment.LEFT);
		
		questionHtmlLabel.setPadding(2);
		questionHtmlLabel.setHeight(80);
		questionHtmlLabel.setShowEdges(true);  
		ClickHandler handler = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				RichTextWindow rtw = new RichTextWindow(questionHtmlLabel.getContents(), constants.editDescription(), new RichTextWindow.HtmlSaver() {

					@Override
					public void htmlReady(String html) {
						questionHtmlLabel.setContents(html);						
					}
					
				});
				rtw.show();
			}
		};
		questionHtmlLabel.addClickHandler(handler);
		questionHtmlLabel.addDoubleClickHandler(new DoubleClickHandler() {
			
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				RichTextWindow rtw = new RichTextWindow(questionHtmlLabel.getContents(), constants.editDescription(), new RichTextWindow.HtmlSaver() {

					@Override
					public void htmlReady(String html) {
						questionHtmlLabel.setContents(html);						
					}
					
				});
				rtw.show();
				
			}
		});
		
		
		questionEditButton.setTitle("edit");
		questionEditButton.addClickHandler(handler);

	}
	
	private void createAutoLaunchQrCodeComponent() {
		isAutolaunchQrCode = new CheckboxItem();
		isAutolaunchQrCode.setName(AUTOLAUNCHQRREADER);
		isAutolaunchQrCode.setStartRow(true);
		isAutolaunchQrCode.setTitle(constants.autoLaunchQR());
		isAutolaunchQrCode.addItemHoverHandler(new ItemHoverHandler() {
			
			@Override
			public void onItemHover(ItemHoverEvent event) {
			 
				isAutolaunchQrCode.setPrompt(constants.autoLaunchQRCodeHover());
			}
		});
	}
	
	public JSONObject generateObject() {
		JSONObject object = super.generateObject();
		object.put("type", new JSONString("org.celstec.arlearn2.beans.generalItem.ScanTag"));
		object.put("description", new JSONString(StringUtil.unescapeHTML(questionHtmlLabel.getContents())));
		object.put("richText", new JSONString(questionHtmlLabel.getContents()));
		object.put("autoLaunchQrReader", JSONBoolean.getInstance( getValue(AUTOLAUNCHQRREADER)==null?false:(Boolean) getValue(AUTOLAUNCHQRREADER)) );

		return object;
	}
	
	public void setItemValues(JSONValue jsonValue) {
		super.setItemValues(jsonValue);		
		JSONObject o = jsonValue.isObject();
		if (o == null) return;
		if (o.get("richText") != null) {
			questionHtmlLabel.setContents(o.get("richText").isString().stringValue());
			questionHtmlLabel.redraw();
			
		}
		if (o.get(AUTOLAUNCHQRREADER) != null) {
			formMapping.get(AUTOLAUNCHQRREADER).setValue(AUTOLAUNCHQRREADER, o.get(AUTOLAUNCHQRREADER).isBoolean().booleanValue());
		}
		checkVisibilityDescription();
		form1.redraw();
		form2.redraw();
		
	}
	@Override
	public boolean validateForm() {
		return true;
	}
}
