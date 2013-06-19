package org.celstec.arlearn2.portal.client.author.ui.game;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;
import org.celstec.arlearn2.portal.client.author.ui.game.i18.GameConstants;
import org.celstec.arlearn2.portal.client.author.ui.generic.canvas.RichTextCanvas;
import org.celstec.arlearn2.portal.client.author.ui.generic.canvas.RichTextCanvas.HtmlSaver;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class GameConfigSection extends SectionConfig {

	private static GameConstants constants = GWT.create(GameConstants.class);
	RichTextCanvas canvas;
	private Game game;
	private DynamicForm form;
	
	public GameConfigSection() {
		super(constants.aboutThisGame());
		form = new DynamicForm();
		final TextItem titleText = new TextItem(GameModel.GAME_TITLE_FIELD, constants.title());
		titleText.setWidth("100%");
		form.setFields(titleText);
		form.setWidth100();
	
		canvas = new RichTextCanvas("", "", new HtmlSaver() {
			
			@Override
			public void htmlReady(String html) {
				if (game != null) {
					game.setDescription(html);
					game.setString(GameModel.GAME_TITLE_FIELD, form.getValueAsString(GameModel.GAME_TITLE_FIELD));
					game.writeToCloud(new JsonCallback(){
						public void onJsonReceived(JSONValue jsonValue) {
							GameConfigSection.this.setExpanded(false);
						}
					});
				}
			}
		});
		setItems(form, canvas);

	}

	public void loadDataFromRecord(Game game) {
		this.game = game;
		form.setValue(GameModel.GAME_TITLE_FIELD, game.getString(GameModel.GAME_TITLE_FIELD));
		canvas.updateHtml(game.getDescription());
	}

}
