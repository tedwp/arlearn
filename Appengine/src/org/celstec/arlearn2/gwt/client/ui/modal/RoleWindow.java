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
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.game.GameClient;
import org.celstec.arlearn2.gwt.client.ui.GameTab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;

public class RoleWindow extends Window {
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public RoleWindow(final long gameId, final GameTab gameTab) {
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

		TextItem textItem = new TextItem();
		textItem.setName("roleName");
		textItem.setTitle(constants.roleName());
		final IButton submitButton = new IButton(constants.add());
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				GameClient.getInstance().createRole(gameId, form.getValueAsString("roleName"), new JsonCallback(){
					public void onJsonReceived(JSONValue jsonValue) {
						gameTab.updateConfig(jsonValue.isObject().get("config").isObject());
						RoleWindow.this.destroy();
					};
				});
			}
		});

		HLayout buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.addMember(submitButton);
		
		form.setFields(textItem);
		addItem(form);
		addItem(buttonLayout);

	}

}
