/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
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
