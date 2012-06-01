package org.celstec.arlearn2.gwt.client.ui.items;

import java.util.ArrayList;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.ui.modal.RichTextWindow;


import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
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
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverEvent;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.HStack;

public class MultiplechoiceCanvas extends GeneralItemCanvas{
	private RadioGroupItem richTextToggle;
	private TextAreaItem plainTextField;
	private Label questionLabel;
	private HTMLPane questionHtmlLabel;
	private IButton questionEditButton = new IButton();
	
	private TextItem ti1 = new TextItem();
	private CheckboxItem cb1 = new CheckboxItem();
	private TextItem ti2 = new TextItem();
	private CheckboxItem cb2 = new CheckboxItem();
	private TextItem ti3 = new TextItem();
	private CheckboxItem cb3 = new CheckboxItem();
	private TextItem ti4 = new TextItem();
	private CheckboxItem cb4 = new CheckboxItem();
	private TextItem ti5 = new TextItem();
	private CheckboxItem cb5 = new CheckboxItem();
	
	private HLayout buttonLayout;
	
	protected DynamicForm form1 = new DynamicForm();
	protected DynamicForm form2 = new DynamicForm();

	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	
	public MultiplechoiceCanvas(String roles[]) {
		super(roles);
		initComponents();
		doLayout();
	}
	
	protected void initComponents() {
		super.initComponents();
		createQuestionHtmlComponent();
		createAnswer(ti1, cb1, "answertext1", "answercb1", constants.answer()+ " 1", null);
		createAnswer(ti2, cb2, "answertext2", "answercb2", constants.answer()+ " 2", "answertext1");
		createAnswer(ti3, cb3, "answertext3", "answercb3", constants.answer()+ " 3", "answertext2");
		createAnswer(ti4, cb4, "answertext4", "answercb4", constants.answer()+ " 4", "answertext3");
		createAnswer(ti5, cb5, "answertext5", "answercb5", constants.answer()+ " 5", "answertext4");
	}

	private void doLayout() {
		this.addMember(form1);
		form1.setFields(idItem, gameIdItem, nameItem, radiusItem, latItem, lngItem, dependencyField, roleGrid);
		addField(form1, idItem, gameIdItem, nameItem, radiusItem, latItem, lngItem, dependencyField, roleGrid);
		this.addMember(questionLabel);
		this.addMember(questionHtmlLabel);
		
		buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.RIGHT);
		buttonLayout.addMember(questionEditButton);
		buttonLayout.setHeight(20);

		this.addMember(buttonLayout);

		this.addMember(form2);
		form2.setFields(ti1,cb1, ti2, cb2, ti3, cb3, ti4, cb4, ti5, cb5);
		addField(form2, ti1,cb1, ti2, cb2, ti3, cb3, ti4, cb4, ti5, cb5);
		form2.setNumCols(3);
		form2.setColWidths(10, "*", 10);
	}
	
	
	
//	public void markForDestroy() {
//		super.markForDestroy();
//		this.removeChild(form1);
//		form1.markForDestroy();
//		this.removeChild(form2);
//		form1.markForDestroy();
//		this.removeChild(questionLabel);
//		questionLabel.markForDestroy();
//		this.removeChild(questionHtmlLabel);
//		questionHtmlLabel.markForDestroy();
//		this.removeChild(buttonLayout);
//		buttonLayout.markForDestroy();
//	}
	
	
	private void createQuestionHtmlComponent() {
		questionLabel = new Label(constants.question());
		questionLabel.setHeight(10);

		
		questionHtmlLabel = new HTMLPane();  
		questionHtmlLabel.setStyleName("htmlsection");  

		   
		questionHtmlLabel.setContents(constants.formulateYourQuestionHere());
		questionHtmlLabel.setAlign(Alignment.LEFT);
		
		questionHtmlLabel.setPadding(3);
		questionHtmlLabel.setHeight(80);
		questionHtmlLabel.setShowEdges(true);  

		questionEditButton.setTitle(constants.edit());
		questionEditButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				RichTextWindow rtw = new RichTextWindow(questionHtmlLabel.getContents(), constants.editMultipleChoiceQuestion(), new RichTextWindow.HtmlSaver() {

					@Override
					public void htmlReady(String html) {
						questionHtmlLabel.setContents(html);						
					}
					
				});
				rtw.show();
			}
		});

	}
//	@Override
//	public ArrayList<FormItem>  addSpecificFields() {
//		ArrayList<FormItem> array = new ArrayList<FormItem>();
//
////		final TextItem nameItem = new TextItem("question");
////		nameItem.addItemHoverHandler(new ItemHoverHandler() {  
////	            public void onItemHover(ItemHoverEvent event) {  
////	                nameItem.setPrompt("Formulate your multiplechoice question here");  
////	            }  
////	        });  
////		nameItem.setTitle("Question");
////		nameItem.setStartRow(true);
////		nameItem.setWidth("*");
////		nameItem.setColSpan(4);
////		nameItem.setSelectOnFocus(true);
////		nameItem.setWrapTitle(false);
////		array.add(nameItem);
//		
//		richTextToggle = new RadioGroupItem();
//		richTextToggle.setTitle("Text type");
//		richTextToggle.setName("richTextToggle");
//		richTextToggle.setValueMap("Html", "Plain text");
//		richTextToggle.setValue("Html");
//		richTextToggle.setRedrawOnChange(true);
//		richTextToggle.addItemHoverHandler(new ItemHoverHandler() {  
//            public void onItemHover(ItemHoverEvent event) {  
//            	richTextToggle.setPrompt("Select how to author the multiplechoice.");  
//            }  
//        });  
//		array.add(richTextToggle);
//		
////		form.setNumCols(3);
////		form.setColWidths(10, "*", 10);
////		form.setWidth("100%");
////		form.setResizeFrom("R");
//		
//		
//		 
//		
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
//				} else {
//					richTextEditor.setVisibility(Visibility.HIDDEN);
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
//		return array;
//	}

	private void createAnswer(TextItem ansTextItem1, CheckboxItem ansItem1, String textName, String cbName, String textTitle, String showIf) {
		ansTextItem1.setName(textName);
		ansTextItem1.setRedrawOnChange(true);

		ansTextItem1.setTitle(textTitle);
		ansTextItem1.setWrapTitle(false);
		
		ansTextItem1.setStartRow(true);
		
		ansItem1.setName(cbName);
		ansItem1.setStartRow(false);
		ansItem1.setTitle("Correct");

		ansItem1.setShowTitle(false);
		ansItem1.setColSpan(1);
		
		if (showIf != null) {
			FormItemIfFunction fiFunction = new FormFunction(showIf);
			ansTextItem1.setShowIfCondition(fiFunction);
			ansItem1.setShowIfCondition(fiFunction);
		}
//		array.add(ansTextItem1);
//		array.add(ansItem1);
	}
	
//	//TODO delete
//	public Canvas getNonFormPart() {
//		 final HTMLFlow htmlFlow = new HTMLFlow();  
//		 htmlFlow.setContents("<b>this is html</b> this also");
//		 htmlFlow.setWidth100();
//		richTextEditor = new RichTextEditor();
//		richTextEditor.setHeight(155);
//		richTextEditor.setWidth100();
////		richTextEditor.setOverflow(Overflow.HIDDEN);
////		richTextEditor.setCanDragResize(true);
//		richTextEditor.setShowEdges(true);
//		richTextEditor.setControlGroups(new String[] { "fontControls",
//				"formatControls", "styleControls" });
//		return htmlFlow;
//	}
	
	public class FormFunction implements FormItemIfFunction {
		private String name;
		
		public FormFunction(String name) {
			this.name = name;
		}
        public boolean execute(FormItem item, Object value, DynamicForm form) {  
        	if (form.getValueAsString(name) == null) return false;
            return !form.getValueAsString(name).trim().equals("");  
        }

    }
	
	public JSONObject generateObject() {
		JSONObject result = super.generateObject();
		result.put("type", new JSONString("org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest"));
		result.put("richText", new JSONString(questionHtmlLabel.getContents()));
//		if ("Html".equals(form.getValue("richTextToggle"))) {
//			if (richTextEditor.getValue()!= null && !richTextEditor.getValue().equals("")) {
//				result.put("richText", new JSONString(richTextEditor.getValue()));
//			}
//		} else {
//			if (form.getValue("plainText")!= null && !form.getValue("plainText").equals("")) {
//				result.put("text", new JSONString(form.getValueAsString("plainText")));
//			}
//		}
		JSONArray ansArray = new JSONArray();
		result.put("answers", ansArray);
		generateAnswer(ansArray, "answertext1", "answercb1", 0);
		generateAnswer(ansArray, "answertext2", "answercb2", 1);
		generateAnswer(ansArray, "answertext3", "answercb3", 2);
		generateAnswer(ansArray, "answertext4", "answercb4", 3);
		generateAnswer(ansArray, "answertext5", "answercb5", 4);
		
		return result;
	}
	
	private void generateAnswer(JSONArray ansArray, String textId, String cbId, int i) {
		if (form2.getValue(textId) != null) {
			JSONObject answer = new JSONObject();
			answer.put("answer",  new JSONString(form2.getValueAsString(textId)) );
			answer.put("isCorrect", JSONBoolean.getInstance( form2.getValue(cbId)==null?false:(Boolean) form2.getValue(cbId)) );
			ansArray.set(i, answer);
		}
	}
	
	public void setItemValues(JSONValue jsonValue) {
		super.setItemValues(jsonValue);
		JSONObject o = jsonValue.isObject();
		if (o == null) return;
//		
		if (o.get("richText") != null) {
			questionHtmlLabel.setContents(o.get("richText").isString().stringValue());
		}
//		
		if (o.get("answers") != null) {
			JSONArray array = o.get("answers").isArray();
			System.out.println("array size"+ o.get("answers"));
			for (int  i = 0; i< array.size(); i++) {
				JSONObject ansObject = array.get(i).isObject();
				form2.setValue("answertext"+(i+1), "Html");
				System.out.println("setting value for "+"answertext"+(i+1) + " to " + ansObject.get("answer").isString().stringValue());
				if (ansObject.get("isCorrect") != null) form2.setValue("answercb"+(i+1), ansObject.get("isCorrect").isBoolean().booleanValue());
				if (ansObject.get("answer") != null) form2.setValue("answertext"+(i+1), ansObject.get("answer").isString().stringValue());
//				if (ansObject.get("id") != null);
			}
			form2.redraw();
		}

		
		
		
	}
	
	public boolean validateForm() {
		return form1.validate() && form2.validate() ;
	}
}
