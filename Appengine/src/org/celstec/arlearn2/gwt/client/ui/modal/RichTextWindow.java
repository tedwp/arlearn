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
import org.celstec.arlearn2.gwt.client.network.response.ResponseDataSource;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.HLayout;

public class RichTextWindow extends Window {

	HtmlSaver saver;
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);
	private boolean htmlEditingmode = false;
	
	public RichTextWindow(String html, String title, HtmlSaver s) {
		this.saver = s;
		setWidth(500);
		setHeight(400);
		setTitle(title);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		
		final DynamicForm form = new DynamicForm();
//		form.setColWidths("100%");
		final TextAreaItem textAreaItem = new TextAreaItem();  
        textAreaItem.setTitle("TextArea"); 
        textAreaItem.setShowTitle(false);
        textAreaItem.setColSpan(3);
        textAreaItem.setWidth("100%");
        textAreaItem.setHeight(300);

        textAreaItem.setValue(html);

        form.setFields( textAreaItem);  
        form.setWidth100();
        form.setVisibility(Visibility.HIDDEN);
        
		final RichTextEditor richTextEditor = new RichTextEditor();
		richTextEditor.setValue(html);
		richTextEditor.setHeight(300);
//		richTextEditor.setWidth(440);
		richTextEditor.setWidth100();
//		richTextEditor.setShowEdges(true);
		richTextEditor.setBorder("1px solid grey");

		richTextEditor.setControlGroups(new String[] { "fontControls",
				"formatControls", "styleControls" });
		
		
		final IButton submitButton = new IButton(constants.save());
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (htmlEditingmode) {
					saver.htmlReady(textAreaItem.getValueAsString());
				} else{
					saver.htmlReady(richTextEditor.getValue());	
				}
				
				RichTextWindow.this.destroy();
			}
		});
		
		final IButton toggleHtmlButton = new IButton(constants.htmlFormatting());
		
		HLayout buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.addMember(toggleHtmlButton);
		buttonLayout.addMember(submitButton);
		
		toggleHtmlButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (htmlEditingmode) {
					htmlEditingmode = false;
					richTextEditor.setVisibility(Visibility.VISIBLE);
					richTextEditor.setValue(textAreaItem.getValueAsString());
					form.setVisibility(Visibility.HIDDEN);
					toggleHtmlButton.setTitle(constants.htmlFormatting());
				} else {
					htmlEditingmode = true;
					richTextEditor.setVisibility(Visibility.HIDDEN);
					textAreaItem.setValue(richTextEditor.getValue());
					toggleHtmlButton.setTitle(constants.richFormatting());
					form.setVisibility(Visibility.VISIBLE);
				}
			}
		});
		addItem(form);
		addItem(richTextEditor);
		addItem(buttonLayout);
		
		addCloseClickHandler(new CloseClickHandler() {

			@Override
			public void onCloseClick(CloseClientEvent event) {
				richTextEditor.destroy();
				form.destroy();
				RichTextWindow.this.destroy();

			}
		});
	}

	public interface HtmlSaver {
		public void htmlReady(String html);
	}
}
