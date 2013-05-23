package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.AuthoringConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class DataCollectionEditor extends VLayout implements ExtensionEditor{

	protected DynamicForm form;
	private CheckboxItem openQuestionCBItem ;
	private CheckboxItem openQuestionWithImageCBItem ;
	private CheckboxItem openQuestionWithVideoCBItem ;
	private CheckboxItem openQuestionWithAudioCBItem ;
	private CheckboxItem openQuestionWithTextCBItem ;

	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);
	private final static String OPENQUESTIONWITHAUTDIO = "openQuestionWithAudio";
	private final static String OPENQUESTIONWITHTEXT = "openQuestionWithText";
	
	public DataCollectionEditor() {
		form = new DynamicForm();
		openQuestionCBItem = new CheckboxItem();
		openQuestionCBItem.setName("isOpenQuestion");
		openQuestionCBItem.setTitle("enable data collection");
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
				if ((Boolean)form.getValue("openQuestionWithImage")) form.setValue("openQuestionWithVideo", false);	
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
				if ((Boolean)form.getValue("openQuestionWithVideo")) form.setValue("openQuestionWithImage", false);	
			}
		});
		
		openQuestionWithAudioCBItem = new CheckboxItem();
		openQuestionWithAudioCBItem.setName(OPENQUESTIONWITHAUTDIO);
		openQuestionWithAudioCBItem.setTitle(constants.answerWithAudio());
		openQuestionWithAudioCBItem.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {
            	if (form.getValue("isOpenQuestion") == null) return false;
                return form.getValue("isOpenQuestion").equals(Boolean.TRUE);  
            }

        });
		openQuestionWithAudioCBItem.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if (form.getValue(OPENQUESTIONWITHTEXT) == null) form.setValue(OPENQUESTIONWITHTEXT, false);	

				if ((Boolean)form.getValue(OPENQUESTIONWITHTEXT)) form.setValue(OPENQUESTIONWITHTEXT, false);	
			}
		});
		
		openQuestionWithTextCBItem = new CheckboxItem();
		openQuestionWithTextCBItem.setName(OPENQUESTIONWITHTEXT);
		openQuestionWithTextCBItem.setTitle(constants.answerWithText());
		openQuestionWithTextCBItem.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {
            	if (form.getValue("isOpenQuestion") == null) return false;
                return form.getValue("isOpenQuestion").equals(Boolean.TRUE);  
            }

        });  
		openQuestionWithTextCBItem.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if (form.getValue(OPENQUESTIONWITHAUTDIO) == null) form.setValue(OPENQUESTIONWITHAUTDIO, false);	
				if ((Boolean)form.getValue(OPENQUESTIONWITHAUTDIO)) form.setValue(OPENQUESTIONWITHAUTDIO, false);	
			}
		});
		form.setWidth100();
		form.setFields(openQuestionCBItem, openQuestionWithImageCBItem, openQuestionWithVideoCBItem, openQuestionWithAudioCBItem, openQuestionWithTextCBItem);
		addMember(form);
	}
	
	public DataCollectionEditor(GeneralItem gi) {
		this();
		if (gi.getJsonRep().get("openQuestion") != null) {
			form.setValue("isOpenQuestion", true);
			JSONObject openQuestion = gi.getJsonRep().get("openQuestion").isObject();
			form.setValue(OPENQUESTIONWITHAUTDIO, openQuestion.get("withAudio").isBoolean().booleanValue());
			boolean hasText = false;
			if (openQuestion.containsKey("withText")) hasText= openQuestion.get("withText").isBoolean().booleanValue();
			form.setValue(OPENQUESTIONWITHTEXT, hasText);
			
			form.setValue("openQuestionWithImage", openQuestion.get("withPicture").isBoolean().booleanValue());
			boolean hasVideo = false;
			if (openQuestion.containsKey("withVideo")) hasVideo= openQuestion.get("withVideo").isBoolean().booleanValue();
			form.setValue("openQuestionWithVideo", hasVideo);
		}
	}


	
	@Override
	public void saveToBean(GeneralItem gi) {
		
		if (form.getValue("isOpenQuestion") != null && (Boolean) form.getValue("isOpenQuestion")) {
			JSONObject openQuestion = new JSONObject();
			openQuestion.put("withPicture", JSONBoolean.getInstance( form.getValue("openQuestionWithImage")==null?false:(Boolean) form.getValue("openQuestionWithImage")) );
			openQuestion.put("withText", JSONBoolean.getInstance( form.getValue(OPENQUESTIONWITHTEXT)==null?false:(Boolean) form.getValue(OPENQUESTIONWITHTEXT)) );
			openQuestion.put("withAudio", JSONBoolean.getInstance( form.getValue(OPENQUESTIONWITHAUTDIO)==null?false:(Boolean) form.getValue(OPENQUESTIONWITHAUTDIO)) );
			openQuestion.put("withVideo", JSONBoolean.getInstance( form.getValue("openQuestionWithVideo")==null?false:(Boolean) form.getValue("openQuestionWithVideo")) );
			gi.getJsonRep().put("openQuestion", openQuestion);
		}
	}
	
	@Override
	public boolean validate() {
		return true;
	}


}
