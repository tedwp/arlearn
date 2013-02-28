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

public class OpenBadgeCanvas  extends GeneralItemCanvas {
	
	protected DynamicForm form1 = new DynamicForm();
	protected TextItem badgeUrlItem;
	private final String BADGE_URL = "badgeUrl";
	
	protected TextItem descriptionItem;
	private final String DESCRIPTION = "description";
	
	protected TextItem evidenceItem;
	private final String EVIDENCE = "evidence";

	protected TextItem badgePngItem;
	private final String IMAGE = "image";
	
	public OpenBadgeCanvas(String roles[]) {
		super(roles);
		initComponents();
		doLayout();
	}
	protected void initComponents() {
		super.initComponents();
		createDescription();
		createBadgeUrl();
		createImage();
		createEvidence();
	}
	
	protected void doLayout() {
		doLayoutForm1();
	}
	
	protected void doLayoutForm1() {
		this.addMember(form1);
		form1.setFields(idItem, gameIdItem, nameItem, sortItem, descriptionItem, badgePngItem, badgeUrlItem, evidenceItem, isSimpleDependency, selectGeneralItem, selectAction, selectActionString, selectRole, selectScope, isDisapperOnDependency,  disAppearOnGeneralItem, disAppearOnAction, disAppearSelectActionString, disAppearOnRole, disAppearOnScope, showCountDownCb, disTime,  isAutolaunch, roleGrid);
		addField(form1,idItem, gameIdItem, nameItem, sortItem, descriptionItem, badgePngItem, badgeUrlItem, evidenceItem, isSimpleDependency, selectGeneralItem, selectAction, selectActionString, selectRole, selectScope, isDisapperOnDependency,  disAppearOnGeneralItem, disAppearOnAction, disAppearSelectActionString, disAppearOnRole, disAppearOnScope, showCountDownCb, disTime,  isAutolaunch, roleGrid);
	}
	
	protected void createBadgeUrl() {
		badgeUrlItem = new TextItem(BADGE_URL);
		badgeUrlItem.setTitle(constants.badgeUrl());
		badgeUrlItem.setSelectOnFocus(true);
		badgeUrlItem.setWrapTitle(false);
	}
	
	protected void createDescription() {
		descriptionItem = new TextItem(DESCRIPTION);
		descriptionItem.setTitle(constants.badgeDescription());
		descriptionItem.setSelectOnFocus(true);
		descriptionItem.setWrapTitle(false);
	}
	
	private void createEvidence() {
		evidenceItem = new TextItem(EVIDENCE);
		evidenceItem.setTitle(constants.badgeEvidence());
		evidenceItem.setSelectOnFocus(true);
		evidenceItem.setWrapTitle(false);
		
	}
	private void createImage() {
		badgePngItem = new TextItem(IMAGE);
		badgePngItem.setTitle(constants.badgeImage());
		badgePngItem.setSelectOnFocus(true);
		badgePngItem.setWrapTitle(false);
		
	}
	
	public JSONObject generateObject() {
		JSONObject object = super.generateObject();
		object.put("type", new JSONString("org.celstec.arlearn2.beans.generalItem.OpenBadge"));

		putStringValue(object, BADGE_URL);
		putStringValue(object, DESCRIPTION);
		putStringValue(object, IMAGE);
		putStringValue(object, EVIDENCE);
		return object;
	}
	
	public void setItemValues(JSONValue jsonValue) {
		super.setItemValues(jsonValue);
		
		JSONObject o = jsonValue.isObject();
		setValueString(BADGE_URL, o);
		setValueString(DESCRIPTION, o);
		setValueString(IMAGE, o);
		setValueString(EVIDENCE, o);
		
		form1.redraw();

	}
	@Override
	public boolean validateForm() {
		return form1.validate();
	}
	
}

