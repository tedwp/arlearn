package org.celstec.arlearn2.gwt.client.ui.modal;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.ui.GeneralItemControlCanvas;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;

public class SelectTypeWindow extends Window {
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);
	private GeneralItemControlCanvas controlItem;
	private double lat;
	private double lng;
	
	public SelectTypeWindow(final long gameId, GeneralItemControlCanvas controlItem, double lat, double lng) {
		this.controlItem = controlItem;
		this.lat = lat;
		this.lng = lng;
		setWidth(360);
		setHeight(100);
		setTitle(constants.addRole());
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		final DynamicForm form = new DynamicForm();
		form.setHeight100();
		form.setWidth100();
		form.setPadding(5);
		HeaderItem header = new HeaderItem();
		header.setDefaultValue(constants.createNewGeneralItem());

		SelectItem selectType = new SelectItem();
		selectType.setName("selectGi");
		selectType.setTitle(constants.selectType());
		selectType.setValueMap(controlItem.getItemValues());
		selectType.setWrapTitle(false);

		selectType.addChangeHandler(selectTypeChangeHandler);

		form.setFields(header, selectType);
		addItem(form);
	}
	
	private ChangeHandler selectTypeChangeHandler = new ChangeHandler() {
		public void onChange(ChangeEvent event) {
			String selectedItem = (String) event.getValue();
			controlItem.createNewItem(selectedItem);
			event.getForm().resetValues();
			controlItem.setLocation(lat, lng);
			SelectTypeWindow.this.destroy();
		}
	};
}
