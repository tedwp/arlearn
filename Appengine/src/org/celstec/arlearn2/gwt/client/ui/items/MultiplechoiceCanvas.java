package org.celstec.arlearn2.gwt.client.ui.items;

import java.util.ArrayList;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.control.Authentication;
import org.celstec.arlearn2.gwt.client.network.ChannelClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;
import org.celstec.arlearn2.gwt.client.ui.ChannelDisplay.DisplayNotificationHandler;
import org.celstec.arlearn2.gwt.client.ui.modal.RichTextWindow;
import org.omg.CORBA.NVList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.util.StringUtil;
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
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.HStack;

public class MultiplechoiceCanvas extends GeneralItemCanvas {
	private DisplayNotificationHandler pongHandler = new DisplayNotificationHandler();

	private CheckboxItem nfcEnabledCBItem;
	private String currentSelectedNfcTag = null;
	private Label questionLabel;
	private HTMLPane questionHtmlLabel;
	private IButton questionEditButton = new IButton();

	private TextItem ti1 = new TextItem();
	private TextItem nfc1 = new TextItem();
	private CheckboxItem cb1 = new CheckboxItem();
	private HiddenItem id1 = new HiddenItem("answerid1");

	private TextItem ti2 = new TextItem();
	private TextItem nfc2 = new TextItem();
	private CheckboxItem cb2 = new CheckboxItem();
	private HiddenItem id2 = new HiddenItem("answerid2");
	
	private TextItem ti3 = new TextItem();
	private TextItem nfc3 = new TextItem();
	private CheckboxItem cb3 = new CheckboxItem();
	private HiddenItem id3 = new HiddenItem("answerid3");
	
	private TextItem ti4 = new TextItem();
	private TextItem nfc4 = new TextItem();
	private CheckboxItem cb4 = new CheckboxItem();
	private HiddenItem id4 = new HiddenItem("answerid4");
	
	private TextItem ti5 = new TextItem();
	private TextItem nfc5 = new TextItem();
	private CheckboxItem cb5 = new CheckboxItem();
	private HiddenItem id5 = new HiddenItem("answerid5");
	
	private HLayout buttonLayout;

	protected DynamicForm form1 = new DynamicForm();
	protected DynamicForm form2 = new DynamicForm();

	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public MultiplechoiceCanvas(String roles[]) {
		super(roles);
		initComponents();
		doLayout();
		initNotifications();
	}

	private void initNotifications() {
		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.notification.Pong", pongHandler);
	}

	protected void initComponents() {
		super.initComponents();
		createNfcEnabledComponent();
		createQuestionHtmlComponent();
		createAnswer(ti1, cb1, nfc1, "answertext1", "answercb1", constants.answer() + " 1", null);
		createAnswer(ti2, cb2, nfc2, "answertext2", "answercb2", constants.answer() + " 2", "answertext1");
		createAnswer(ti3, cb3, nfc3, "answertext3", "answercb3", constants.answer() + " 3", "answertext2");
		createAnswer(ti4, cb4, nfc4, "answertext4", "answercb4", constants.answer() + " 4", "answertext3");
		createAnswer(ti5, cb5, nfc5, "answertext5", "answercb5", constants.answer() + " 5", "answertext4");
	}

	private void doLayout() {
		this.addMember(form1);
		form1.setFields(idItem, gameIdItem, nameItem, latItem, lngItem, sortItem,  roleGrid);
		addField(form1, idItem, gameIdItem, nameItem, latItem, lngItem, sortItem,  roleGrid);
		this.addMember(questionLabel);
		this.addMember(questionHtmlLabel);

		buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.RIGHT);
		buttonLayout.addMember(questionEditButton);
		buttonLayout.setHeight(20);

		this.addMember(buttonLayout);

		this.addMember(form2);
		isDisapperOnDependency.setStartRow(true);
		form2.setFields(isSimpleDependency, selectGeneralItem, selectAction, selectRole, selectScope,   isDisapperOnDependency,  disAppearOnGeneralItem, disAppearOnAction, disAppearOnRole, disAppearOnScope, showCountDownCb, disTime,isAutolaunch, nfcEnabledCBItem, 
				ti1, cb1, nfc1, id1, 
				ti2, cb2, nfc2, id2,
				ti3, cb3, nfc3, id3,
				ti4, cb4, nfc4, id4,
				ti5, cb5, nfc5, id5);
		addField(form2,isSimpleDependency, selectGeneralItem, selectAction, selectRole, selectScope,   isDisapperOnDependency,  disAppearOnGeneralItem, disAppearOnAction, disAppearOnRole, disAppearOnScope, showCountDownCb, disTime, isAutolaunch, nfcEnabledCBItem, 
				ti1, cb1, nfc1, id1, 
				ti2, cb2, nfc2, id2,
				ti3, cb3, nfc3, id3,
				ti4, cb4, nfc4, id4,
				ti5, cb5, nfc5, id5);
		form2.setNumCols(4);
		form2.setColWidths(10, "*", 10);
	}

	// public void markForDestroy() {
	// super.markForDestroy();
	// this.removeChild(form1);
	// form1.markForDestroy();
	// this.removeChild(form2);
	// form1.markForDestroy();
	// this.removeChild(questionLabel);
	// questionLabel.markForDestroy();
	// this.removeChild(questionHtmlLabel);
	// questionHtmlLabel.markForDestroy();
	// this.removeChild(buttonLayout);
	// buttonLayout.markForDestroy();
	// }

	private void createNfcEnabledComponent() {
		nfcEnabledCBItem = new CheckboxItem();
		nfcEnabledCBItem.setName("isNfcEnabled");
		nfcEnabledCBItem.setTitle(constants.isNfcEnabled());
		nfcEnabledCBItem.setRedrawOnChange(true);
		nfcEnabledCBItem.setStartRow(true);
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

	private void createAnswer(TextItem ansTextItem1, CheckboxItem ansItem1, TextItem nfcItem, String textName, String cbName, String textTitle, final String showIf) {
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

		if (nfcItem != null) {
			final String itemName = cbName + "nfc";
			nfcItem.setColSpan(1);
			nfcItem.setName(itemName);
			nfcItem.setShowTitle(false);
			nfcItem.setShowIfCondition(new FormItemIfFunction() {
				public boolean execute(FormItem item, Object value, DynamicForm form) {
					if (form.getValue("isNfcEnabled") == null)
						return false;
					if (showIf == null)
						return form.getValue("isNfcEnabled").equals(Boolean.TRUE);
					if (form.getValueAsString(showIf) == null)
						return false;
					return form.getValue("isNfcEnabled").equals(Boolean.TRUE)&&!form.getValueAsString(showIf).trim().equals("");
				}

			});
			// nfcItem.setDisabled(true);
			nfcItem.setValue("click to update tag");
			nfcItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					currentSelectedNfcTag = itemName;
					sendNfcScanRequest();

				}
			});
		}

		if (showIf != null) {
			FormItemIfFunction fiFunction = new FormFunction(showIf);
			ansTextItem1.setShowIfCondition(fiFunction);
			ansItem1.setShowIfCondition(fiFunction);
		}
		// array.add(ansTextItem1);
		// array.add(ansItem1);
	}

	private void sendNfcScanRequest() {
		String account = Authentication.getInstance().getCurrentUser();
		ChannelClient.getInstance().pingRequest(new JsonCallback() {
			public void onReceived() {
			};
		}, account, account, 2, "none");
	}

	// //TODO delete
	// public Canvas getNonFormPart() {
	// final HTMLFlow htmlFlow = new HTMLFlow();
	// htmlFlow.setContents("<b>this is html</b> this also");
	// htmlFlow.setWidth100();
	// richTextEditor = new RichTextEditor();
	// richTextEditor.setHeight(155);
	// richTextEditor.setWidth100();
	// // richTextEditor.setOverflow(Overflow.HIDDEN);
	// // richTextEditor.setCanDragResize(true);
	// richTextEditor.setShowEdges(true);
	// richTextEditor.setControlGroups(new String[] { "fontControls",
	// "formatControls", "styleControls" });
	// return htmlFlow;
	// }

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

	public JSONObject generateObject() {
		JSONObject result = super.generateObject();
		result.put("type", new JSONString("org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest"));
		result.put("richText", new JSONString(questionHtmlLabel.getContents()));
		result.put("description", new JSONString(StringUtil.unescapeHTML(questionHtmlLabel.getContents())));


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
			answer.put("answer", new JSONString(form2.getValueAsString(textId)));
			if (form2.getValue("isNfcEnabled") != null && ((Boolean) form2.getValue("isNfcEnabled")))
				answer.put("nfcTag", new JSONString(form2.getValueAsString(cbId + "nfc")));
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
		if (o.get("richText") != null) {
			questionHtmlLabel.setContents(o.get("richText").isString().stringValue());
		}
		if (o.get("answers") != null) {
			JSONArray array = o.get("answers").isArray();
			for (int i = 0; i < array.size(); i++) {
				JSONObject ansObject = array.get(i).isObject();
				form2.setValue("answertext" + (i + 1), "Html");
				if (ansObject.get("isCorrect") != null)
					form2.setValue("answercb" + (i + 1), ansObject.get("isCorrect").isBoolean().booleanValue());
				if (ansObject.get("nfcTag") != null) {
					form2.setValue("isNfcEnabled", true);
					form2.setValue("answercb" + (i + 1) + "nfc", ansObject.get("nfcTag").isString().stringValue());
				}
				if (ansObject.get("id") != null) {
					form2.setValue("answerid"+ (i + 1), ansObject.get("id").isString().stringValue());
				}
				if (ansObject.get("answer") != null)
					form2.setValue("answertext" + (i + 1), ansObject.get("answer").isString().stringValue());
				// if (ansObject.get("id") != null);
			}
			form2.redraw();
		}

	}

	public boolean validateForm() {
		return form1.validate() && form2.validate();
	}

	public class DisplayNotificationHandler implements NotificationHandler {
		@Override
		public void onNotification(final JSONObject bean) {
			if (currentSelectedNfcTag != null) {
				String response = bean.get("response").isString().stringValue();
				if (response != null && !"".equals(response.trim())) form2.setValue(currentSelectedNfcTag, response);
			}
				
		}
	}
	
	/*
	File: Math.uuid.js
	Version: 1.3
	Change History:
	  v1.0 - first release
	  v1.1 - less code and 2x performance boost (by minimizing calls to Math.random())
	  v1.2 - Add support for generating non-standard uuids of arbitrary length
	  v1.3 - Fixed IE7 bug (can't use []'s to access string chars.  Thanks, Brian R.)
	  v1.4 - Changed method to be "Math.uuid". Added support for radix argument.  Use module pattern for better encapsulation.

	Latest version:   http://www.broofa.com/Tools/Math.uuid.js
	Information:      http://www.broofa.com/blog/?p=151
	Contact:          robert@broofa.com
	----
	Copyright (c) 2008, Robert Kieffer
	All rights reserved.

	Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

	    * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
	    * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
	    * Neither the name of Robert Kieffer nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
	*/

	public static class UUID {
		private static final char[] CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray(); 
		/**
		 * Generate a random uuid of the specified length. Example: uuid(15) returns
		 * "VcydxgltxrVZSTV"
		 * 
		 * @param len
		 *            the desired number of characters
		 */
		public static String uuid(int len) {
			return uuid(len, CHARS.length);
		}
		/**
		 * Generate a random uuid of the specified length, and radix. Examples:
		 * <ul>
		 * <li>uuid(8, 2) returns "01001010" (8 character ID, base=2)
		 * <li>uuid(8, 10) returns "47473046" (8 character ID, base=10)
		 * <li>uuid(8, 16) returns "098F4D35" (8 character ID, base=16)
		 * </ul>
		 * 
		 * @param len
		 *            the desired number of characters
		 * @param radix
		 *            the number of allowable values for each character (must be <=
		 *            62)
		 */
		public static String uuid(int len, int radix) {
			if (radix > CHARS.length) {
				throw new IllegalArgumentException();
			}
			char[] uuid = new char[len];
			// Compact form
			for (int i = 0; i < len; i++) {
				uuid[i] = CHARS[(int)(Math.random()*radix)];
			}
			return new String(uuid);
		}
		/**
		 * Generate a RFC4122, version 4 ID. Example:
		 * "92329D39-6F5C-4520-ABFC-AAB64544E172"
		 */
		public static String uuid() {
			char[] uuid = new char[36];
			int r;

			// rfc4122 requires these characters
			uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
			uuid[14] = '4';

			// Fill in random data.  At i==19 set the high bits of clock sequence as
			// per rfc4122, sec. 4.1.5
			for (int i = 0; i < 36; i++) {
				if (uuid[i] == 0) {
					r = (int) (Math.random()*16);
					uuid[i] = CHARS[(i == 19) ? (r & 0x3) | 0x8 : r & 0xf];
				}
			}
			return new String(uuid);
		}
	}
}
