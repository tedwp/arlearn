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
package org.celstec.arlearn2.gwt.client.ui.items;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;

public class OpenUrlCanvas extends GeneralItemCanvas {
	
	protected DynamicForm form1 = new DynamicForm();
	protected TextItem urlItem;
	private final String OPENURL = "url";


	public OpenUrlCanvas(String roles[]) {
		super(roles);
		initComponents();
		doLayout();
	}
	protected void initComponents() {
		super.initComponents();
		createOpenUrl();
	}
	protected void doLayout() {
		doLayoutForm1();
	}
	
	protected void doLayoutForm1() {
		this.addMember(form1);
		form1.setFields(idItem, gameIdItem, nameItem, urlItem,  roleGrid);
		addField(form1,idItem, gameIdItem, nameItem, urlItem,  roleGrid);
	}
	
	protected void createOpenUrl() {
		urlItem = new TextItem(OPENURL);
		urlItem.setTitle("browser url");
		urlItem.setSelectOnFocus(true);
		urlItem.setWrapTitle(false);
	}
	
	public JSONObject generateObject() {
		JSONObject object = super.generateObject();
		object.put("type", new JSONString("org.celstec.arlearn2.beans.generalItem.OpenUrl"));

		putStringValue(object, OPENURL);
		return object;
	}
	
	public void setItemValues(JSONValue jsonValue) {
		super.setItemValues(jsonValue);
		
		JSONObject o = jsonValue.isObject();
		setValueString(OPENURL, o);

	}
	@Override
	public boolean validateForm() {
		return form1.validate();
	}
	
}
