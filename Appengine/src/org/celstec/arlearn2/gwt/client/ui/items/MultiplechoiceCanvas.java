package org.celstec.arlearn2.gwt.client.ui.items;

import java.util.ArrayList;


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
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

public class MultiplechoiceCanvas extends GeneralItemCanvas{
	private RichTextEditor richTextEditor;
	private RadioGroupItem richTextToggle;
	private TextAreaItem plainTextField;
	
	@Override
	public ArrayList<FormItem>  addSpecificFields() {
		ArrayList<FormItem> array = new ArrayList<FormItem>();

		final TextItem nameItem = new TextItem("question");
		nameItem.setTitle("Question");
		nameItem.setStartRow(true);
		nameItem.setWidth("*");
		nameItem.setColSpan(4);
		nameItem.setSelectOnFocus(true);
		nameItem.setWrapTitle(false);
		array.add(nameItem);
		
		richTextToggle = new RadioGroupItem();
		richTextToggle.setTitle("Text type");
		richTextToggle.setName("richTextToggle");
		richTextToggle.setValueMap("Html", "Plain text");
		richTextToggle.setValue("Html");
		richTextToggle.setRedrawOnChange(true);
		array.add(richTextToggle);
		
		form.setNumCols(3);
		form.setColWidths(10, "*", 10);
		form.setWidth("100%");
		form.setResizeFrom("R");
		TextItem ansTextItem = new TextItem();
		ansTextItem.setTitle("Answer 1");
		ansTextItem.setWidth("*");

		ansTextItem.setStartRow(true);
//		ansTextItem.setColSpan(2);

		CheckboxItem ansItem = new CheckboxItem();
		ansItem.setStartRow(false);
		ansItem.setTitle("Correct");

		ansItem.setShowTitle(false);
		ansItem.setColSpan(1);
		array.add(ansTextItem);
		array.add(ansItem);
		
        
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
		richTextEditor.setOverflow(Overflow.HIDDEN);
		richTextEditor.setCanDragResize(true);
		richTextEditor.setShowEdges(true);
		richTextEditor.setControlGroups(new String[] { "fontControls",
				"formatControls", "styleControls" });
		return richTextEditor;
	}
	
}
