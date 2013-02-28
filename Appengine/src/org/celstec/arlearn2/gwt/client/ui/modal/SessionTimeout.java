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

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class SessionTimeout extends Window {
	
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public SessionTimeout() {
		setWidth(200);
		setHeight(150);
		setTitle(constants.timeOut());
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		
		Label l = new Label(constants.sessionTimeout());
		l.setHeight(50);
		
		final IButton submitButton = new IButton(constants.reload());
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				com.google.gwt.user.client.Window.Location.reload();
				SessionTimeout.this.destroy();
			}
		});
		
		HLayout buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.addMember(submitButton);
		buttonLayout.setHeight(50);
		
		addItem(l);
		addItem(buttonLayout);
	}

}
