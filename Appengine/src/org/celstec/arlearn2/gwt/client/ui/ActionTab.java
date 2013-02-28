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
package org.celstec.arlearn2.gwt.client.ui;

import org.celstec.arlearn2.gwt.client.network.ActionClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.ResponseClient;

import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class ActionTab extends Tab {

//	public ActionTab() {
//		super("actions");
//		HLayout navLayout = new HLayout();
//		navLayout.setMembersMargin(10);
//
//		navLayout.setMembers(getActionForm(), getResponseForm());
//		this.setPane(navLayout);
//	}
//	
//	public DynamicForm getActionForm() {
//		final DynamicForm form = new DynamicForm();
//		form.setBorder("1px solid");
//		form.setWidth(345);
//		// form.setUseAllDataSourceFields(true);
//		HeaderItem header = new HeaderItem();
//		header.setDefaultValue("Create an action");
//
//		final TextItem itemId = new TextItem("itemId");
//		itemId.setTitle("General item Id");
//		itemId.setSelectOnFocus(true);
//		itemId.setWrapTitle(false);
//
//		
//		final TextItem runId = new TextItem("runId");
//		runId.setTitle("runId");
//		runId.setWrapTitle(false);
//		
//		final TextItem itemType = new TextItem("itemType");
//		itemType.setTitle("Item Type");
//		itemType.setWrapTitle(false);
//		
//		final TextItem userEmail = new TextItem("userEmail");
//		userEmail.setTitle("userEmail");
//		userEmail.setWrapTitle(false);
//		
//		final TextItem action = new TextItem("action");
//		action.setTitle("action");
//		action.setWrapTitle(false);
//
//		ButtonItem button = new ButtonItem("submit", "Submit");
//		// button.setStartRow(true);
//		button.setWidth(80);
//		button.setAlign(Alignment.RIGHT);
//
//		button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
//			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
//				ActionClient.getInstance().createAction(
//						Long.parseLong(form.getValueAsString("itemId")), 
//								Long.parseLong(form.getValueAsString("runId")), 
//								form.getValueAsString("action"), 
//								form.getValueAsString("userEmail"), 
//								form.getValueAsString("itemType"), 
//								new JsonCallback() {
//									
//									@Override
//									public void onJsonReceived(JSONValue jsonValue) {
//										// TODO Auto-generated method stub
//										
//									}
//									
//									@Override
//									public void onError() {
//										// TODO Auto-generated method stub
//										
//									}
//								});
//			}
//		});
//		form.setFields(header, itemId,runId, itemType, userEmail, action, button);
//		return form;
//	}
//	
//	public DynamicForm getResponseForm() {
//		final DynamicForm form = new DynamicForm();
//		form.setBorder("1px solid");
//		form.setWidth(345);
//		// form.setUseAllDataSourceFields(true);
//		HeaderItem header = new HeaderItem();
//		header.setDefaultValue("Create a response");
//
//		final TextItem itemId = new TextItem("itemId");
//		itemId.setTitle("General item Id");
//		itemId.setSelectOnFocus(true);
//		itemId.setWrapTitle(false);
//
//		
//		final TextItem runId = new TextItem("runId");
//		runId.setTitle("runId");
//		runId.setWrapTitle(false);
//		
//		final TextItem itemType = new TextItem("itemType");
//		itemType.setTitle("Item Type");
//		itemType.setWrapTitle(false);
//		
//		final TextItem userEmail = new TextItem("userEmail");
//		userEmail.setTitle("userEmail");
//		userEmail.setWrapTitle(false);
//		
//		final TextItem action = new TextItem("responseValue");
//		action.setTitle("responseValue");
//		action.setWrapTitle(false);
//
//		ButtonItem button = new ButtonItem("submit", "Submit");
//		// button.setStartRow(true);
//		button.setWidth(80);
//		button.setAlign(Alignment.RIGHT);
//
//		button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
//			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
//				ResponseClient.getInstance().createResponse(Long.parseLong(form.getValueAsString("itemId")), 
//								Long.parseLong(form.getValueAsString("runId")),
//								form.getValueAsString("responseValue"), 
//								form.getValueAsString("userEmail"), 
//								new JsonCallback() {
//									
//									@Override
//									public void onJsonReceived(JSONValue jsonValue) {
//										// TODO Auto-generated method stub
//										
//									}
//									
//									@Override
//									public void onError() {
//										// TODO Auto-generated method stub
//										
//									}
//								});
//			}
//		});
//		form.setFields(header, itemId,runId, itemType, userEmail, action, button);
//		return form;
//	}
}
