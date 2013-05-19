package org.celstec.arlearn2.portal.client;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameRolesDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.portal.client.author.ui.generic.maps.MapWidget;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.AdvancedDependenciesEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.DependencyEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.ProximityDependencyEditor;

import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.maps.gwt.client.LatLng;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class TestPage {

	public void loadPage2() {

		final AdvancedDependenciesEditor depEdit = new AdvancedDependenciesEditor();
//		final DependencyEditor depEdit = new DependencyEditor();
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
				System.out.println("click" + depEdit.getJson());

			}
		});
		RootPanel.get("test").add(button);

	}
	
	public void loadPage() {

		final AdvancedDependenciesEditor depEdit = new AdvancedDependenciesEditor();
		MapWidget widget = MapWidget.getInstance();
//		HLayout layout = new HLayout();
//		layout.setWidth100();
//		layout.addMember(widget);
		ProximityDependencyEditor edit = new ProximityDependencyEditor();
		edit.setLocation(LatLng.create(51, 5.7266));
//		RootPanel.get("test").add(edit);
		RootPanel.get("test").add(widget);

	}
}
