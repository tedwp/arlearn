package org.celstec.arlearn2.gwt.client.ui.items;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.ui.items.MultiplechoiceCanvas.FormFunction;
import org.celstec.arlearn2.gwt.client.ui.items.MultiplechoiceCanvas.UUID;
import org.celstec.arlearn2.gwt.client.ui.modal.RichTextWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.util.StringUtil;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverEvent;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class SingleChoiceImageCanvas extends GeneralItemCanvas{

	private Label questionLabel;
	private HTMLPane questionHtmlLabel;
	private IButton questionEditButton = new IButton();

	protected CheckboxItem isAudioQuestion;
	protected TextItem audioUrl;
	
	private TextItem ti1 = new TextItem();
	private TextItem audio1 = new TextItem();
	private CheckboxItem cb1 = new CheckboxItem();
	private HiddenItem id1 = new HiddenItem("answerid1");

	private TextItem ti2 = new TextItem();
	private TextItem audio2 = new TextItem();
	private CheckboxItem cb2 = new CheckboxItem();
	private HiddenItem id2 = new HiddenItem("answerid2");
	
	private TextItem ti3 = new TextItem();
	private TextItem audio3 = new TextItem();
	private CheckboxItem cb3 = new CheckboxItem();
	private HiddenItem id3 = new HiddenItem("answerid3");
	
	private TextItem ti4 = new TextItem();
	private TextItem audio4 = new TextItem();
	private CheckboxItem cb4 = new CheckboxItem();
	private HiddenItem id4 = new HiddenItem("answerid4");
	
	private TextItem ti5 = new TextItem();
	private TextItem audio5 = new TextItem();
	private CheckboxItem cb5 = new CheckboxItem();
	private HiddenItem id5 = new HiddenItem("answerid5");
	
	private HLayout buttonLayout;

	protected DynamicForm form1 = new DynamicForm();
	protected DynamicForm form2 = new DynamicForm();
	
	private final String AUDIO_QUESTION = "audioQuestion";
	private final String IS_AUDIO_QUESTION = "isAudioQuestion";


	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public SingleChoiceImageCanvas(String[] roles) {
		super(roles);
		initComponents();
		doLayout();
	}
	
	protected void initComponents() {
		super.initComponents();
		createQuestionHtmlComponent();
		createAudioFeedTextItem();
		createisAudioQuestionComponent();
		createAnswer(ti1, cb1, audio1, "answertext1", "answercb1", constants.answer() + " 1", null);
		createAnswer(ti2, cb2, audio2, "answertext2", "answercb2", constants.answer() + " 2", "answertext1");
		createAnswer(ti3, cb3, audio3, "answertext3", "answercb3", constants.answer() + " 3", "answertext2");
		createAnswer(ti4, cb4, audio4, "answertext4", "answercb4", constants.answer() + " 4", "answertext3");
		createAnswer(ti5, cb5, audio5, "answertext5", "answercb5", constants.answer() + " 5", "answertext4");
	}
	
	private void doLayout() {
		this.addMember(form1);
		form1.setFields(idItem, gameIdItem, nameItem, latItem, lngItem, sortItem,  roleGrid, isAudioQuestion, audioUrl);
		addField(form1, idItem, gameIdItem, nameItem, latItem, lngItem, sortItem,  roleGrid, isAudioQuestion, audioUrl);
		this.addMember(questionLabel);
		this.addMember(questionHtmlLabel);

		buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.RIGHT);
		buttonLayout.addMember(questionEditButton);
		buttonLayout.setHeight(20);

		this.addMember(buttonLayout);

		this.addMember(form2);
		isDisapperOnDependency.setStartRow(true);
		form2.setFields(isSimpleDependency, selectGeneralItem, selectAction, selectActionString, selectRole, selectScope,   isDisapperOnDependency,  disAppearOnGeneralItem, disAppearOnAction, disAppearOnRole, disAppearOnScope, showCountDownCb, disTime,isAutolaunch,  
				ti1, cb1, audio1, id1, 
				ti2, cb2, audio2, id2,
				ti3, cb3, audio3, id3,
				ti4, cb4, audio4, id4,
				ti5, cb5, audio5, id5);
		addField(form2,isSimpleDependency, selectGeneralItem, selectAction, selectActionString, selectRole, selectScope,   isDisapperOnDependency,  disAppearOnGeneralItem, disAppearOnAction, disAppearOnRole, disAppearOnScope, showCountDownCb, disTime, isAutolaunch,  
				ti1, cb1, audio1, id1, 
				ti2, cb2, audio2, id2,
				ti3, cb3, audio3, id3,
				ti4, cb4, audio4, id4,
				ti5, cb5, audio5, id5);
		form2.setNumCols(4);
		form2.setColWidths(10, "*", 10);
	}
	
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
	
	protected void createAudioFeedTextItem() {
		audioUrl = new TextItem(AUDIO_QUESTION);
		audioUrl.setTitle("audio feed question");
		audioUrl.setSelectOnFocus(true);
		audioUrl.setWrapTitle(false);
		audioUrl.setShowIfCondition(new FormItemIfFunction() {
			
			@Override
			public boolean execute(FormItem item, Object value, DynamicForm form) {
				if (form.getValue(IS_AUDIO_QUESTION) == null) return false;
				return form.getValue(IS_AUDIO_QUESTION).equals(Boolean.TRUE);
			}
		});
	}
	
	private void createisAudioQuestionComponent() {
		isAudioQuestion = new CheckboxItem();
		isAudioQuestion.setName(IS_AUDIO_QUESTION);
		isAudioQuestion.setStartRow(true);
		isAudioQuestion.setTitle("question as audio");
		isAudioQuestion.setRedrawOnChange(true);
		isAudioQuestion.addItemHoverHandler(new ItemHoverHandler() {
			
			@Override
			public void onItemHover(ItemHoverEvent event) {
			 
				isAudioQuestion.setPrompt(constants.autoLaunchHover());
			}
		});
		isAudioQuestion.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				boolean visible = (Boolean) event.getValue();
				if (visible) {
					questionLabel.setVisibility(Visibility.HIDDEN);	
					questionHtmlLabel.setVisibility(Visibility.HIDDEN);
					questionEditButton.setVisibility(Visibility.HIDDEN);
				} else {
					questionLabel.setVisibility(Visibility.VISIBLE);
					questionHtmlLabel.setVisibility(Visibility.VISIBLE);
					questionEditButton.setVisibility(Visibility.VISIBLE);
				}
				
				
			}
		});
	}
	
	private void createAnswer(TextItem ansTextItem1, CheckboxItem ansItem1, TextItem audioItem, String textName, String cbName, String textTitle, final String showIf) {
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

		if (audioItem != null) {
			final String itemName = cbName + "nfc";
			audioItem.setColSpan(1);
			audioItem.setName(itemName);
			audioItem.setShowTitle(false);
			audioItem.setShowIfCondition(new FormItemIfFunction() {
				public boolean execute(FormItem item, Object value, DynamicForm form) {
					if (showIf == null)
						return true;
					if (form.getValueAsString(showIf) == null)
						return false;
					return !form.getValueAsString(showIf).trim().equals("");
				}

			});
			// nfcItem.setDisabled(true);
//			audioItem.setValue();
		
		}

		if (showIf != null) {
			FormItemIfFunction fiFunction = new FormFunction(showIf);
			ansTextItem1.setShowIfCondition(fiFunction);
			ansItem1.setShowIfCondition(fiFunction);
		}
	}
	public JSONObject generateObject() {
		JSONObject result = super.generateObject();
		result.put("type", new JSONString("org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest"));
		if (getValue(IS_AUDIO_QUESTION) != null && (Boolean) getValue(IS_AUDIO_QUESTION)) {
			result.put(AUDIO_QUESTION, new JSONString((String) getValue(AUDIO_QUESTION)));	

		} else {
			result.put("richText", new JSONString(questionHtmlLabel.getContents()));
			result.put("description", new JSONString(StringUtil.unescapeHTML(questionHtmlLabel.getContents())));
		}

		JSONArray ansArray = new JSONArray();
		result.put("answers", ansArray);
		generateAnswer(ansArray, "answertext1", "answercb1", 0);
		generateAnswer(ansArray, "answertext2", "answercb2", 1);
		generateAnswer(ansArray, "answertext3", "answercb3", 2);
		generateAnswer(ansArray, "answertext4", "answercb4", 3);
		generateAnswer(ansArray, "answertext5", "answercb5", 4);
		
		return result;
	}
	
	
	protected void generateAnswer(JSONArray ansArray, String textId, String cbId, int i) {
		if (form2.getValue(textId) != null) {
			JSONObject answer = new JSONObject();
			answer.put("imageUrl", new JSONString(form2.getValueAsString(textId)));
			if (form2.getValueAsString(cbId + "nfc") != null) answer.put("audioUrl", new JSONString(form2.getValueAsString(cbId + "nfc")));
			answer.put("isCorrect", JSONBoolean.getInstance(form2.getValue(cbId) == null ? false : (Boolean) form2.getValue(cbId)));

			if (form2.getValue("answerid"+(i+1)) != null) {
				answer.put("id", new JSONString(form2.getValueAsString("answerid"+(i+1))));
			} else {
				answer.put("id",  new JSONString(UUID.uuid(15)));
			}
			ansArray.set(i, answer);
		}
	}
	
	public void setItemValues(JSONValue jsonValue) {
		super.setItemValues(jsonValue);
		JSONObject o = jsonValue.isObject();
		if (o == null)
			return;
		if (o.get("audioQuestion") != null) {
			form1.setValue(IS_AUDIO_QUESTION, true);
			form1.setValue(AUDIO_QUESTION, o.get("audioQuestion").isString().stringValue());
			form1.redraw();
			questionLabel.setVisibility(Visibility.HIDDEN);	
			questionHtmlLabel.setVisibility(Visibility.HIDDEN);
			questionEditButton.setVisibility(Visibility.HIDDEN);
		}
		if (o.get("answers") != null) {
			JSONArray array = o.get("answers").isArray();
			for (int i = 0; i < array.size(); i++) {
				JSONObject ansObject = array.get(i).isObject();
				form2.setValue("answertext" + (i + 1), "Html");
				if (ansObject.get("isCorrect") != null)
					form2.setValue("answercb" + (i + 1), ansObject.get("isCorrect").isBoolean().booleanValue());
				
				if (ansObject.get("id") != null) {
					form2.setValue("answerid"+ (i + 1), ansObject.get("id").isString().stringValue());
				}
				if (ansObject.get("imageUrl") != null)
					form2.setValue("answertext" + (i + 1), ansObject.get("imageUrl").isString().stringValue());
				if (ansObject.get("audioUrl") != null)
					form2.setValue("answercb" + (i + 1) + "nfc", ansObject.get("audioUrl").isString().stringValue());
				// if (ansObject.get("id") != null);
			}
			form2.redraw();
		}

	}

	@Override
	public boolean validateForm() {
		return form1.validate() && form2.validate();
	}
	
	public class FormFunction implements FormItemIfFunction {
		private String name;

		public FormFunction(String name) {
			this.name = name;
		}

		public boolean execute(FormItem item, Object value, DynamicForm form) {
			if (form.getValueAsString(name) == null)
				return false;
			return !form.getValueAsString(name).trim().equals("");
		}

	}
}