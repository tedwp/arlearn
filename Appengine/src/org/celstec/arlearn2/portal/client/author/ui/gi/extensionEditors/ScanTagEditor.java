package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.ScanTagObject;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class ScanTagEditor extends VLayout implements ExtensionEditor{

	protected DynamicForm form;

	
	public ScanTagEditor() {
		form = new DynamicForm();
		final CheckboxItem qrCheckBox = new CheckboxItem(ScanTagObject.AUTOLAUNCHQRREADER, "Automatically start QR reader");
		form.setFields(qrCheckBox);
		form.setWidth100();
		addMember(form);
	}
	
	public ScanTagEditor(GeneralItem gi) {
		this();
		form.setValue(ScanTagObject.AUTOLAUNCHQRREADER, gi.getBoolean(ScanTagObject.AUTOLAUNCHQRREADER));
	}
	
	@Override
	public void saveToBean(GeneralItem gi) {
		if (form.getValue(ScanTagObject.AUTOLAUNCHQRREADER)!= null) gi.setBoolean(ScanTagObject.AUTOLAUNCHQRREADER, (Boolean) form.getValue(ScanTagObject.AUTOLAUNCHQRREADER));

	}
	
	@Override
	public boolean validate() {
		return true;
	}
}
