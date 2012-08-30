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
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class NarratorItemCanvas extends GeneralItemCanvas {
	private CheckboxItem openQuestionCBItem ;
	private CheckboxItem openQuestionWithImageCBItem ;
	private CheckboxItem openQuestionWithVideoCBItem ;
	private CheckboxItem openQuestionWithAudioCBItem ;
	
	private Label questionLabel;
	protected HTMLPane questionHtmlLabel;
	private IButton questionEditButton = new IButton();
	
	protected DynamicForm form1 = new DynamicForm();
	protected DynamicForm form2 = new DynamicForm();

	public NarratorItemCanvas(String roles[]) {
		super(roles);
		initComponents();
		doLayout();
	}
	
	protected void initComponents() {
		super.initComponents();
		createQuestionHtmlComponent();
		createOpenQuestionComponent();
	}

	protected void doLayout() {
		doLayoutForm1();
		this.addMember(questionLabel);
		this.addMember(questionHtmlLabel);
		
		HLayout buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.RIGHT);
		buttonLayout.addMember(questionEditButton);
		buttonLayout.setHeight(20);

		this.addMember(buttonLayout);

		this.addMember(form2);
		form2.setFields(latItem, lngItem, sortItem,  roleGrid, isSimpleDependency, selectAction, selectScope, selectRole, selectGeneralItem, isAutolaunch, openQuestionCBItem, openQuestionWithAudioCBItem, openQuestionWithImageCBItem, openQuestionWithVideoCBItem);
		addField(form2, latItem, lngItem, sortItem, roleGrid, isSimpleDependency, selectAction, selectScope, selectRole, selectGeneralItem, isAutolaunch, openQuestionCBItem, openQuestionWithAudioCBItem, openQuestionWithImageCBItem, openQuestionWithVideoCBItem);
	}
	
	protected void doLayoutForm1() {
		this.addMember(form1);
		form1.setFields(idItem, gameIdItem, nameItem);
		addField(form1,idItem, gameIdItem, nameItem);
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
	
	
	private void createOpenQuestionComponent() {
		openQuestionCBItem = new CheckboxItem();
		openQuestionCBItem.setName("isOpenQuestion");
		openQuestionCBItem.setTitle(constants.openQuestion());
		openQuestionCBItem.setRedrawOnChange(true);

		openQuestionWithImageCBItem = new CheckboxItem();
		openQuestionWithImageCBItem.setName("openQuestionWithImage");
		openQuestionWithImageCBItem.setTitle(constants.answerWithPicture());
		openQuestionWithImageCBItem.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {  
            	if (form.getValue("isOpenQuestion") == null) return false;
                return form.getValue("isOpenQuestion").equals(Boolean.TRUE);  
            }

        });  
		openQuestionWithImageCBItem.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if ((Boolean)form2.getValue("openQuestionWithImage")) form2.setValue("openQuestionWithVideo", false);	
			}
		});
		
		openQuestionWithVideoCBItem = new CheckboxItem();
		openQuestionWithVideoCBItem.setName("openQuestionWithVideo");
		openQuestionWithVideoCBItem.setTitle(constants.answerWithVideo());
		openQuestionWithVideoCBItem.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {  
            	if (form.getValue("isOpenQuestion") == null) return false;
                return form.getValue("isOpenQuestion").equals(Boolean.TRUE);  
            }

        });  
		openQuestionWithVideoCBItem.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if ((Boolean)form2.getValue("openQuestionWithVideo")) form2.setValue("openQuestionWithImage", false);	
			}
		});
		
		openQuestionWithAudioCBItem = new CheckboxItem();
		openQuestionWithAudioCBItem.setName("openQuestionWithAudio");
		openQuestionWithAudioCBItem.setTitle(constants.answerWithAudio());
		openQuestionWithAudioCBItem.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {
            	if (form.getValue("isOpenQuestion") == null) return false;
                return form.getValue("isOpenQuestion").equals(Boolean.TRUE);  
            }

        });  
	}
	
	
	public JSONObject generateObject() {
		JSONObject result = super.generateObject();
		result.put("type", new JSONString("org.celstec.arlearn2.beans.generalItem.NarratorItem"));
		result.put("richText", new JSONString(questionHtmlLabel.getContents()));
		result.put("description", new JSONString(StringUtil.unescapeHTML(questionHtmlLabel.getContents())));
		if (form2.getValue("isOpenQuestion") != null && (Boolean) form2.getValue("isOpenQuestion")) {
			JSONObject openQuestion = new JSONObject();
			openQuestion.put("withPicture", JSONBoolean.getInstance( form2.getValue("openQuestionWithImage")==null?false:(Boolean) form2.getValue("openQuestionWithImage")) );
			openQuestion.put("withAudio", JSONBoolean.getInstance( form2.getValue("openQuestionWithAudio")==null?false:(Boolean) form2.getValue("openQuestionWithAudio")) );
			openQuestion.put("withVideo", JSONBoolean.getInstance( form2.getValue("openQuestionWithVideo")==null?false:(Boolean) form2.getValue("openQuestionWithVideo")) );
			result.put("openQuestion", openQuestion);
		}
		
		return result;
	}
	
	public void setItemValues(JSONValue jsonValue) {
		super.setItemValues(jsonValue);
		JSONObject o = jsonValue.isObject();
		if (o == null) return;
		if (o.get("richText") != null) {
			questionHtmlLabel.setContents(o.get("richText").isString().stringValue());
			questionHtmlLabel.redraw();
			
		}

		
		if (o.get("openQuestion") != null) {
			form2.setValue("isOpenQuestion", true);
			JSONObject openQuestion =o.get("openQuestion").isObject();
			form2.setValue("openQuestionWithAudio", openQuestion.get("withAudio").isBoolean().booleanValue());
			form2.setValue("openQuestionWithImage", openQuestion.get("withPicture").isBoolean().booleanValue());
			boolean hasVideo = false;
			if (openQuestion.containsKey("withVideo")) hasVideo= openQuestion.get("withVideo").isBoolean().booleanValue();
			form2.setValue("openQuestionWithVideo", hasVideo);
		}
		form1.redraw();
		form2.redraw();
		
	}
	
	public boolean validateForm() {
		return form1.validate() && form2.validate() ;
	}

}
