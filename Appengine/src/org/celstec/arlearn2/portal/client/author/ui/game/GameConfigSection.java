package org.celstec.arlearn2.portal.client.author.ui.game;

import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.portal.client.author.ui.generic.canvas.RichTextCanvas;
import org.celstec.arlearn2.portal.client.author.ui.generic.canvas.RichTextCanvas.HtmlSaver;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;

import com.google.gwt.json.client.JSONValue;

public class GameConfigSection extends SectionConfig {

//	private DynamicForm basicMetadata;

	RichTextCanvas canvas;
	private Game game;
	
	public GameConfigSection() {
		super("About this game");
		canvas = new RichTextCanvas("", "test", new HtmlSaver() {
			
			@Override
			public void htmlReady(String html) {
				if (game != null) {
					game.setDescription(html);
					game.writeToCloud(new JsonCallback(){
						public void onJsonReceived(JSONValue jsonValue) {
							GameConfigSection.this.setExpanded(false);
						}

					});
				}
			}
		});
		setItems(canvas);

	}


	public void loadDataFromRecord(Game game) {
		this.game = game;
		canvas.updateHtml(game.getDescription());
//		basicMetadata.setValue(GameModel.GAME_TITLE_FIELD, game.getGameId());
	}

}
