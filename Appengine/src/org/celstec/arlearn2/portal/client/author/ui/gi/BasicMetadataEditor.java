package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;

import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

public class BasicMetadataEditor extends VLayout {
	DynamicForm form = new DynamicForm();
	protected RichTextEditor richTextEditor;
	protected TextAreaItem textAreaItem;
	protected boolean showRichtText = true;
	
	public BasicMetadataEditor(boolean showTitle, boolean showDescription) {
		createTextArea();
		createRichTextArea();
		createForm();

		LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(10);
		addMember(vSpacer);
		
		addMember(richTextEditor);
	}
	
	protected void createForm() {
		final TextItem titleText = new TextItem(GeneralItemModel.NAME_FIELD, "Title");

		form.setFields(titleText, textAreaItem);
		form.setWidth100();
		
		addMember(form);
	}

	protected void createTextArea() {
		textAreaItem = new TextAreaItem(GeneralItemModel.RICH_TEXT_FIELD, "TextArea");  
        textAreaItem.setShowTitle(false);
        textAreaItem.setColSpan(3);
        textAreaItem.setWidth("100%");
        textAreaItem.setShowIfCondition(formIf);
	}
	
	protected void createRichTextArea() {
		richTextEditor = new RichTextEditor();
		richTextEditor.setHeight100();

		richTextEditor.setWidth100();
		richTextEditor.setBorder("1px  grey");
//		richTextEditor.setBorder("1px dashed blue");  
		richTextEditor.setControlGroups(new String[] { "fontControls",
				"formatControls", "styleControls" });
	}
	
	public void toggleHtml() {
		showRichtText = !showRichtText;
		if (showRichtText) {
			richTextEditor.setVisibility(Visibility.INHERIT);
			richTextEditor.setValue(textAreaItem.getValueAsString());

		} else {
			richTextEditor.setVisibility(Visibility.HIDDEN);
			textAreaItem.setValue(richTextEditor.getValue());
		}
		form.redraw();
	}
	
	FormItemIfFunction formIf = new FormItemIfFunction() {
		public boolean execute(FormItem item, Object value, DynamicForm form) {
			return !showRichtText;
		}

	};
	
	public void saveToBean(GeneralItem ni) {
		ni.getJsonRep().put(GeneralItemModel.NAME_FIELD, new JSONString(form.getValueAsString(GeneralItemModel.NAME_FIELD)));
		if (showRichtText) {
			ni.getJsonRep().put(GeneralItemModel.RICH_TEXT_FIELD, new JSONString(richTextEditor.getValue()));
			
		} else {
			ni.getJsonRep().put(GeneralItemModel.RICH_TEXT_FIELD, new JSONString(form.getValueAsString(GeneralItemModel.RICH_TEXT_FIELD)));
		}
	}

	public void loadGeneralItem(GeneralItem gi) {
		form.setValue(GeneralItemModel.NAME_FIELD, gi.getString(GeneralItemModel.NAME_FIELD));
		form.setValue(GeneralItemModel.RICH_TEXT_FIELD, gi.getString(GeneralItemModel.RICH_TEXT_FIELD));
		richTextEditor.setValue(gi.getString(GeneralItemModel.RICH_TEXT_FIELD));
		
	}

}
