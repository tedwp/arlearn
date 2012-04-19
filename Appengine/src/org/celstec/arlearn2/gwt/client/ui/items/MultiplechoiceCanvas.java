package org.celstec.arlearn2.gwt.client.ui.items;

import java.util.ArrayList;


import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class MultiplechoiceCanvas extends GeneralItemCanvas{

	@Override
	public ArrayList<FormItem>  addSpecificFields() {
		ArrayList<FormItem> array = new ArrayList<FormItem>();

		final TextItem nameItem = new TextItem("question");
		nameItem.setTitle("Question");
		nameItem.setSelectOnFocus(true);
		nameItem.setWrapTitle(false);
		array.add(nameItem);
		return array;
	}

	@Override
	public Canvas getNonFormPart() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
