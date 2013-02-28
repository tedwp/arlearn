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

import java.util.ArrayList;

import org.celstec.arlearn2.gwt.client.control.Authentication;
import org.celstec.arlearn2.gwt.client.control.ReadyCallback;
import org.celstec.arlearn2.gwt.client.network.run.RunDataSource;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class GenericTab extends Tab {

	private HLayout navLayout;
	public GenericTab(String name) {
		super(name);
		setCanClose(false);
		if (!Authentication.getInstance().isAuthenticated())
			setDisabled(true);
		
		navLayout = new HLayout();
		navLayout.setMembersMargin(10);
		this.setPane(navLayout);
		
		addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				tabSelect();

			}
		});
	}
	
	protected void setLeft(Canvas left) {
		navLayout.addMember(left, 0);	
	}
	
	protected void setRight(Canvas right) {
		navLayout.addMember(right);	
	}
	
	protected DynamicForm getForm(String headerString, FormItem... fields) {
		final DynamicForm form = new DynamicForm();
		form.setBorder("1px solid");
		form.setWidth(280);

		HeaderItem header = new HeaderItem();
		header.setDefaultValue(headerString);
		ArrayList<FormItem> list = new ArrayList<FormItem>(fields.length+1);
		list.add(header);
		for (FormItem field: fields) {
			list.add(field);
		}
		
		form.setFields(list.toArray(new FormItem[]{}));
		return form;
	}
	
	protected void tabSelect() {}

}
