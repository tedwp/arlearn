package org.celstec.arlearn2.gwt.client.ui.modal;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class RichTextWindow extends Window {

	HtmlSaver saver;
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public RichTextWindow(String html, String title, HtmlSaver s) {
		this.saver = s;
		setWidth(500);
		setHeight(400);
		setTitle(title);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		
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
				saver.htmlReady(richTextEditor.getValue());
				RichTextWindow.this.destroy();
			}
		});
		
		HLayout buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.addMember(submitButton);
		
		addItem(richTextEditor);
		addItem(buttonLayout);
	}

	public interface HtmlSaver {
		public void htmlReady(String html);
	}
}
