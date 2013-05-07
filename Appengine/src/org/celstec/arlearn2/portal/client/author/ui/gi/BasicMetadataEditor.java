package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.objects.NarratorItem;

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
	private RichTextEditor richTextEditor;
	private TextAreaItem textAreaItem;
	private boolean showRichtText = true;
	public BasicMetadataEditor(boolean showTitle, boolean showDescription) {

		final TextItem titleText = new TextItem(GeneralItemModel.NAME_FIELD, "Title");
		
		textAreaItem = new TextAreaItem(GeneralItemModel.RICH_TEXT_FIELD, "TextArea");  
        textAreaItem.setShowTitle(false);
        textAreaItem.setColSpan(3);
        textAreaItem.setWidth("100%");
        textAreaItem.setShowIfCondition(formIf);
       

        form.setBorder("1px dashed blue");  
//        textAreaItem.setValue(html);
        
		form.setFields(titleText, textAreaItem);
		form.setWidth100();
//		form.setHeight100();
		
		addMember(form);
		LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(10);
		addMember(vSpacer);
		
		richTextEditor = new RichTextEditor();
//		richTextEditor.setValue(html);
		richTextEditor.setHeight100();

		richTextEditor.setWidth100();
//		richTextEditor.setShowEdges(true);
//		richTextEditor.setBorder("1px  grey");
		richTextEditor.setBorder("1px dashed blue");  


		richTextEditor.setControlGroups(new String[] { "fontControls",
				"formatControls", "styleControls" });
		
		addMember(richTextEditor);
	}
	
	public void toggleHtml() {
		showRichtText = !showRichtText;
		if (showRichtText) {
			richTextEditor.setVisibility(Visibility.VISIBLE);
		} else {
			richTextEditor.setVisibility(Visibility.HIDDEN);
		}
		form.redraw();
	}
	
	FormItemIfFunction formIf = new FormItemIfFunction() {
		public boolean execute(FormItem item, Object value, DynamicForm form) {
			return !showRichtText;
		}

	};
	
	public void saveToBean(NarratorItem ni) {
		ni.getJsonRep().put(GeneralItemModel.NAME_FIELD, new JSONString(form.getValueAsString(GeneralItemModel.NAME_FIELD)));
		if (showRichtText) {
			ni.getJsonRep().put(GeneralItemModel.RICH_TEXT_FIELD, new JSONString(richTextEditor.getValue()));
			
		} else {
			ni.getJsonRep().put(GeneralItemModel.RICH_TEXT_FIELD, new JSONString(form.getValueAsString(GeneralItemModel.RICH_TEXT_FIELD)));
		}
	}

}
