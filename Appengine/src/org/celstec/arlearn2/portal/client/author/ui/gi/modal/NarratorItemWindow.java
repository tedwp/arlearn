package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.NarratorItem;
import org.celstec.arlearn2.portal.client.author.ui.gi.BasicMetadataEditor;

import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class NarratorItemWindow extends GeneralItemWindow {
	protected  BasicMetadataEditor editor;
	protected IButton submitButton;
	protected Game game;
	
	public NarratorItemWindow(Game game) {
		System.out.println("NarratorItemWindow"+game );
		this.game = game;
		setTitle("Create Narrator Item");
		setWidth(500);
		setHeight(450);
	}

	public void initComponent() {
		editor = new BasicMetadataEditor(true, true);
		editor.setHeight("350");
		addItem(editor);
		
		final IButton toggleHtml = new IButton("Toggle HTML");
		toggleHtml.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				editor.toggleHtml();
			}
		});

		HLayout buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.addMember(toggleHtml);
		
		 submitButton = new IButton("create");
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				createClick();
			}
		});

		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.addMember(submitButton);
		
		addItem(buttonLayout);
	}

	protected void createClick() {
		submitButton.setDisabled(true);
		NarratorItem ni = new NarratorItem();
		editor.saveToBean(ni);
		ni.linkToGame(game);
		GeneralItemsClient.getInstance().createGeneralItem(ni, new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				NarratorItemWindow.this.destroy();
				GeneralItemDataSource.getInstance().loadDataFromWeb(game.getGameId());
			}
		});
	}

}
