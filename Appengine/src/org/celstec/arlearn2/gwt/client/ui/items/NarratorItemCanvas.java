package org.celstec.arlearn2.gwt.client.ui.items;

import java.util.ArrayList;
import java.util.List;


import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.form.fields.FormItem;

public class NarratorItemCanvas  extends GeneralItemCanvas{
	private RichTextEditor richTextEditor;
	
	public NarratorItemCanvas() {
		super();
		setHeight(100);
	}
	
	@Override
	public List<FormItem> addSpecificFields() {
		ArrayList<FormItem> array = new ArrayList<FormItem>();
		return array;
	}

	@Override
	public Canvas getNonFormPart() {
		richTextEditor = new RichTextEditor();  
        richTextEditor.setHeight(155);  
//        richTextEditor.setWidth("100%");  
        richTextEditor.setOverflow(Overflow.HIDDEN);  
        richTextEditor.setCanDragResize(true);  
        richTextEditor.setShowEdges(true);
        richTextEditor.setControlGroups(new String[]{"fontControls", "formatControls", "styleControls" });
        return richTextEditor;
	}

}
