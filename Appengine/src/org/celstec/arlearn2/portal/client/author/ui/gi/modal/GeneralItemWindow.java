package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class GeneralItemWindow extends Window {
	
	public GeneralItemWindow() {
		initComponent();
		setWidth(360);
		setHeight(200);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
	}

	public void  initComponent() {
		final IButton submitButton = new IButton("create");
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				createClick();
			}
		});

		HLayout buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.addMember(submitButton);
		addItem(buttonLayout);
	}
	
	protected void createClick() {
	}

	public static void initWindow(String valueAsString, Game game) {
		if ("Narrator Item".equals(valueAsString)) {
			new NarratorItemWindow(game).show();
		}
		
	}
	
}
