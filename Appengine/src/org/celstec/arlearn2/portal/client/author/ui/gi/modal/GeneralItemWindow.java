package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceTest;
import org.celstec.arlearn2.gwtcommonlib.client.objects.ScanTagObject;
import org.celstec.arlearn2.gwtcommonlib.client.objects.SingleChoiceTest;
import org.celstec.arlearn2.gwtcommonlib.client.objects.VideoObject;
import org.celstec.arlearn2.gwtcommonlib.client.objects.YoutubeObject;
import org.celstec.arlearn2.portal.client.author.ui.gi.BasicMetadataEditor;

import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public abstract class GeneralItemWindow extends Window {
	protected BasicMetadataEditor editor;
	protected IButton submitButton;
	protected IButton toggleHtml;

	protected Game game;

	protected HLayout buttonLayout;

	public GeneralItemWindow(Game game) {
		this.game = game;

		initComponent();
		setWidth(360);
		setHeight(200);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
	}

	public void initComponent() {
		createMetadataEditor();
		createToggleButton();
		createSubmitButton();
		
		createButtonLayout(toggleHtml, submitButton);

		addItem(editor);
		
		Canvas extensions = getMetadataExtensions();
		if (extensions != null) {
			addItem(extensions);
		}
		addItem(buttonLayout);

	}
	
	protected Canvas getMetadataExtensions() {
		return null;
	}

	private void createMetadataEditor() {
		editor = new BasicMetadataEditor(true, true);
		editor.setHeight("350");
	}

	private void createToggleButton() {
		toggleHtml = new IButton("Toggle HTML");
		toggleHtml.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				editor.toggleHtml();
			}
		});
	}

	private void createSubmitButton() {
		submitButton = new IButton("create");
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				createClick();
			}
		});
	}

	private void createButtonLayout(IButton... buttons) {
		buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		for (IButton but : buttons) {
			buttonLayout.addMember(but);
		}

	}

	protected void createClick() {
		if (!validate()) return;
		submitButton.setDisabled(true);
		
		GeneralItem ni = createItem();
		ni.setLong(GeneralItemModel.SORTKEY_FIELD, 0);
		editor.saveToBean(ni);
		ni.linkToGame(game);
		GeneralItemsClient.getInstance().createGeneralItem(ni, new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				GeneralItemWindow.this.destroy();
				GeneralItemDataSource.getInstance().loadDataFromWeb(game.getGameId());
			}
		});
		
	}
	
	protected abstract GeneralItem createItem() ;

	public static void initWindow(String valueAsString, Game game) {
		if ("Narrator Item".equals(valueAsString)) {
			new NarratorItemWindow(game).show();
		} else if (VideoObject.HUMAN_READABLE_NAME.equals(valueAsString)) {
			new VideoObjectWindow(game).show();
		} else if (YoutubeObject.HUMAN_READABLE_NAME.equals(valueAsString)) {
			new YoutubeObjectWindow(game).show();
		} else if (ScanTagObject.HUMAN_READABLE_NAME.equals(valueAsString)) {
			new ScanTagObjectWindow(game).show();
		} else if (SingleChoiceTest.HUMAN_READABLE_NAME.equals(valueAsString)) {
			new SingleChoiceTestWindow(game).show();
		} else if (MultipleChoiceTest.HUMAN_READABLE_NAME.equals(valueAsString)) {
			new MultipleChoiceTestWindow(game).show();
		}
	}
	
	public boolean validate() {
		return true;
	}

}
