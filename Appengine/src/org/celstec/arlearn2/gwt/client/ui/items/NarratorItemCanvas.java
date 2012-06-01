package org.celstec.arlearn2.gwt.client.ui.items;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.ui.modal.RichTextWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
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
	private RichTextEditor richTextEditor;
	private CheckboxItem openQuestionCBItem ;
	private CheckboxItem openQuestionWithImageCBItem ;
	private CheckboxItem openQuestionWithAudioCBItem ;
	private RadioGroupItem richTextToggle;
	private TextAreaItem plainTextField;
	
	private Label questionLabel;
	protected HTMLPane questionHtmlLabel;
	private IButton questionEditButton = new IButton();
	
	protected DynamicForm form1 = new DynamicForm();
	protected DynamicForm form2 = new DynamicForm();
	
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);


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
		form2.setFields(radiusItem, latItem, lngItem, dependencyField, roleGrid, openQuestionCBItem, openQuestionWithAudioCBItem, openQuestionWithImageCBItem);
		addField(form2,radiusItem, latItem, lngItem, dependencyField, roleGrid, openQuestionCBItem, openQuestionWithAudioCBItem, openQuestionWithImageCBItem);
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
		
		questionHtmlLabel.setPadding(3);
		questionHtmlLabel.setHeight(80);
		questionHtmlLabel.setShowEdges(true);  
		
		questionEditButton.setTitle("edit");
		questionEditButton.addClickHandler(new ClickHandler() {
			
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
		});

	}
	
	private void createOpenQuestionComponent() {
		// TODO Auto-generated method stub
		openQuestionCBItem = new CheckboxItem();
		openQuestionCBItem.setName("isOpenQuestion");
		openQuestionCBItem.setTitle(constants.openQuestion());
		openQuestionCBItem.setRedrawOnChange(true);

		openQuestionWithImageCBItem = new CheckboxItem();
		openQuestionWithImageCBItem.setName("openQuestionWithImage");
		openQuestionWithImageCBItem.setTitle(constants.answerWithPicture());
		openQuestionWithImageCBItem.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {  
            	System.out.println("form value"+form.getValue("isOpenQuestion"));
            	if (form.getValue("isOpenQuestion") == null) return false;
                return form.getValue("isOpenQuestion").equals(Boolean.TRUE);  
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
	
//	@Override
//	public List<FormItem> addSpecificFields() {
//		
//		
//		richTextToggle = new RadioGroupItem();
//		richTextToggle.setTitle("Text type");
//		richTextToggle.setName("richTextToggle");
//		richTextToggle.setValueMap("Html", "Plain text");
//		richTextToggle.setValue("Html");
//		richTextToggle.setRedrawOnChange(true);
//		array.add(richTextToggle);
//		
//		
//        
//		richTextToggle.addChangedHandler(new ChangedHandler() {
//			
//			@Override
//			public void onChanged(ChangedEvent event) {
//				System.out.println(event.getValue());
//				if (event.getValue().equals("Html")) {
//					richTextEditor.setVisibility(Visibility.VISIBLE);
//					
////					plainTextField.hide();
//				} else {
//					richTextEditor.setVisibility(Visibility.HIDDEN);
////					plainTextField.setVisible(true);
////					plainTextField.show();
//				}
//			}
//		});
//		
//		plainTextField = new TextAreaItem();  
//        plainTextField.setTitle("Plain Text");
//        plainTextField.setName("plainText");
//        plainTextField.setShowIfCondition(new FormItemIfFunction() {  
//            public boolean execute(FormItem item, Object value, DynamicForm form) {  
//                return form.getValue("richTextToggle").equals("Plain text");  
//            }
//
//        });  
////        plainTextField.setVisible(false);
////        plainTextField.hide();
//        
//        array.add(plainTextField);
//        
//		return array;
//	}
	
	public JSONObject generateObject() {
		JSONObject result = super.generateObject();
		result.put("type", new JSONString("org.celstec.arlearn2.beans.generalItem.NarratorItem"));
		result.put("richText", new JSONString(questionHtmlLabel.getContents()));

		if (form2.getValue("isOpenQuestion") != null && (Boolean) form2.getValue("isOpenQuestion")) {
			JSONObject openQuestion = new JSONObject();
			openQuestion.put("withPicture", JSONBoolean.getInstance( form2.getValue("openQuestionWithImage")==null?false:(Boolean) form2.getValue("openQuestionWithImage")) );
			openQuestion.put("withAudio", JSONBoolean.getInstance( form2.getValue("openQuestionWithAudio")==null?false:(Boolean) form2.getValue("openQuestionWithAudio")) );
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
		}
		form1.redraw();
		form2.redraw();
		
	}
	
	public boolean validateForm() {
		return form1.validate() && form2.validate() ;
	}

}
