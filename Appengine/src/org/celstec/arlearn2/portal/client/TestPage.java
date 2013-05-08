package org.celstec.arlearn2.portal.client;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameRolesDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.AdvancedDependenciesEditor;

import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

public class TestPage {

	public void loadPage() {

		final AdvancedDependenciesEditor depEdit = new AdvancedDependenciesEditor();
        RootPanel.get("test").add(depEdit);		
        IButton button = new IButton();
		GeneralItemDataSource.getInstance().loadDataFromWeb(1l);
		GameDataSource.getInstance().loadDataFromWeb();
		GameClient.getInstance().getGame(1, new JsonCallback() {
			public void onJsonReceived(JSONValue jsonValue) {
				Game game = new Game(jsonValue.isObject());
				GameRolesDataSource.getInstance().addRole(game.getGameId(), game.getRoles());
			}

		});
		

        button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
System.out.println("click"+ depEdit.getJson());	
				
			}
		});
        RootPanel.get("test").add(button);		

	}
}
