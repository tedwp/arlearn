package org.celstec.arlearn2.gwt.client.ui.items;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

public class NarratorItemCanvas extends GeneralItemCanvas {
	private RichTextEditor richTextEditor;

	public NarratorItemCanvas() {
		super();
		setHeight(100);
	}

	private CheckboxItem openQuestionCBItem ;
	private CheckboxItem openQuestionWithImageCBItem ;
	private CheckboxItem openQuestionWithAudioCBItem ;
	private RadioGroupItem richTextToggle;
	private TextAreaItem plainTextField;
	@Override
	public List<FormItem> addSpecificFields() {
		ArrayList<FormItem> array = new ArrayList<FormItem>();
		openQuestionCBItem = new CheckboxItem();
		openQuestionCBItem.setName("isOpenQuestion");
		openQuestionCBItem.setTitle("Open Question");
		openQuestionCBItem.setRedrawOnChange(true);

		array.add(openQuestionCBItem);

		openQuestionWithImageCBItem = new CheckboxItem();
		openQuestionWithImageCBItem.setName("openQuestionWithImage");
		openQuestionWithImageCBItem.setTitle("Answer with picture");
		openQuestionWithImageCBItem.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {  
            	System.out.println("form value"+form.getValue("isOpenQuestion"));
            	if (form.getValue("isOpenQuestion") == null) return false;
                return form.getValue("isOpenQuestion").equals(Boolean.TRUE);  
            }

        });  
		array.add(openQuestionWithImageCBItem);
		
		openQuestionWithAudioCBItem = new CheckboxItem();
		openQuestionWithAudioCBItem.setName("openQuestionWithAudio");
		openQuestionWithAudioCBItem.setTitle("Answer with audio");
		openQuestionWithAudioCBItem.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {
            	if (form.getValue("isOpenQuestion") == null) return false;
                return form.getValue("isOpenQuestion").equals(Boolean.TRUE);  
            }

        });  
		array.add(openQuestionWithAudioCBItem);
		
		richTextToggle = new RadioGroupItem();
		richTextToggle.setTitle("Text type");
		richTextToggle.setName("richTextToggle");
		richTextToggle.setValueMap("Html", "Plain text");
		richTextToggle.setValue("Html");
		richTextToggle.setRedrawOnChange(true);
		array.add(richTextToggle);
		
		
        
		richTextToggle.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				System.out.println(event.getValue());
				if (event.getValue().equals("Html")) {
					richTextEditor.setVisibility(Visibility.VISIBLE);
					
//					plainTextField.hide();
				} else {
					richTextEditor.setVisibility(Visibility.HIDDEN);
//					plainTextField.setVisible(true);
//					plainTextField.show();
				}
			}
		});
		
		plainTextField = new TextAreaItem();  
        plainTextField.setTitle("Plain Text");
        plainTextField.setName("plainText");
        plainTextField.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {  
                return form.getValue("richTextToggle").equals("Plain text");  
            }

        });  
//        plainTextField.setVisible(false);
//        plainTextField.hide();
        
        array.add(plainTextField);
        
		return array;
	}

	@Override
	public Canvas getNonFormPart() {
		richTextEditor = new RichTextEditor();
		richTextEditor.setHeight(155);
		// richTextEditor.setWidth("100%");
		richTextEditor.setOverflow(Overflow.HIDDEN);
		richTextEditor.setCanDragResize(true);
		richTextEditor.setShowEdges(true);
		richTextEditor.setControlGroups(new String[] { "fontControls",
				"formatControls", "styleControls" });
		return richTextEditor;
	}
	
	public JSONObject generateObject() {
		JSONObject result = super.generateObject();
		result.put("type", new JSONString("org.celstec.arlearn2.beans.generalItem.NarratorItem"));
//		result.put("isOpenQuestion", JSONBoolean.getInstance((Boolean) form.getValue("isOpenQuestion")) );
		if ("Html".equals(form.getValue("richTextToggle"))) {
			if (richTextEditor.getValue()!= null && !richTextEditor.getValue().equals("")) {
				result.put("richText", new JSONString(richTextEditor.getValue()));
			}
		} else {
			if (form.getValue("plainText")!= null && !form.getValue("plainText").equals("")) {
				result.put("text", new JSONString(form.getValueAsString("plainText")));
			}
		}
		if ((Boolean) form.getValue("isOpenQuestion")) {
			JSONObject openQuestion = new JSONObject();//openQuestionWithImage
			openQuestion.put("withPicture", JSONBoolean.getInstance( form.getValue("openQuestionWithImage")==null?true:(Boolean) form.getValue("openQuestionWithImage")) );
			openQuestion.put("withAudio", JSONBoolean.getInstance( form.getValue("openQuestionWithAudio")==null?true:(Boolean) form.getValue("openQuestionWithAudio")) );
			result.put("openQuestion", openQuestion);
		}
		
		return result;
	}
	
	public void setItemValues(JSONValue jsonValue) {
		super.setItemValues(jsonValue);
		JSONObject o = jsonValue.isObject();
		if (o == null) return;
		
		if (o.get("richText") != null) {
			form.setValue("richTextToggle", "Html");
			richTextEditor.setValue(o.get("richText").isString().stringValue());
		}
		
		if (o.get("openQuestion") != null) {
			form.setValue("isOpenQuestion", true);
			JSONObject openQuestion =o.get("openQuestion").isObject();
			form.setValue("openQuestionWithAudio", openQuestion.get("withAudio").isBoolean().booleanValue());
			form.setValue("openQuestionWithImage", openQuestion.get("withPicture").isBoolean().booleanValue());
		}
		
		
	}

}
