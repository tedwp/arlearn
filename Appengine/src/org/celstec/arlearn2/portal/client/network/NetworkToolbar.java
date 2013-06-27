package org.celstec.arlearn2.portal.client.network;

import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.toolbar.ToolBar;

import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;

public class NetworkToolbar extends ToolBar {

	private NetworkPage networkPage;

	public NetworkToolbar(NetworkPage nwPage) {
		this.networkPage = nwPage;
	}

	protected void addButtons() {
		addMenuButton(addGameMenuButtons());
		addMenuButton(addRunMenuButtons());
		addMenuButton(addUsersMenuButtons());
		addMenuButton(addAccountsMenuButtons());
		addMenuButton(addReponsesMenuButtons());
		addMenuButton(addActionsMenuButtons());
		addMenuButton(addMessageMenuButtons());

	}

	private ToolStripMenuButton addMessageMenuButtons() {
		Menu menu = new Menu();
		menu.setShowShadow(true);
		menu.setShadowDepth(3);

		MenuItemSeparator separator = new MenuItemSeparator();

		menu.setItems(

//				createMenuItemGET("Get responses", "response/runId/***"),
//				createMenuItemGET("Get responses (resumption)", "response/runId/***?resumptionToken=**"),
//				createMenuItemGET("Get responses (from/until)", "response/runId/***?from=0&until="+System.currentTimeMillis()),
//				separator,
				createMenuItemPOST(
						"Create Message",
						"messages/userId/***",
						"{  \"type\": \"org.celstec.arlearn2.beans.run.Message\",  \"runId\": 0, \"title\": \"Heading\", \"messageBody\": \"Here comes some text\" }")
				
		);
		
		
		

		ToolStripMenuButton menuButton = new ToolStripMenuButton("User Messages",
				menu);
		menuButton.setWidth(100);
		// addMenuButton(menuButton);
		return menuButton;
	}
	
	private ToolStripMenuButton addActionsMenuButtons() {
		Menu menu = new Menu();
		menu.setShowShadow(true);
		menu.setShadowDepth(3);
		MenuItemSeparator separator = new MenuItemSeparator();
		menu.setItems(
				createMenuItemGET("Get actions", "actions/runId/***")				
		);
		ToolStripMenuButton menuButton = new ToolStripMenuButton("User Actions",
				menu);
		menuButton.setWidth(100);
		// addMenuButton(menuButton);
		return menuButton;
	}
	
	private ToolStripMenuButton addReponsesMenuButtons() {
		Menu menu = new Menu();
		menu.setShowShadow(true);
		menu.setShadowDepth(3);

		MenuItemSeparator separator = new MenuItemSeparator();

		menu.setItems(

				createMenuItemGET("Get responses", "response/runId/***"),
				createMenuItemGET("Get responses (resumption)", "response/runId/***?resumptionToken=**"),
				createMenuItemGET("Get responses (from/until)", "response/runId/***?from=0&until="+System.currentTimeMillis()),
				separator,
				createMenuItemPOST(
						"Create Response",
						"response",
						"{  \"type\": \"org.celstec.arlearn2.beans.run.Response\",  \"timestamp\": "+System.currentTimeMillis()+", \"responseValue\": \"value\", \"runId\": 0, \"userEmail\": \""+AccountManager.getInstance().getAccount().getAccountType()+":"+AccountManager.getInstance().getAccount().getLocalId()+"\" }")
				
		);
		
		
		

		ToolStripMenuButton menuButton = new ToolStripMenuButton("User Responses",
				menu);
		menuButton.setWidth(100);
		// addMenuButton(menuButton);
		return menuButton;
	}
	
	private ToolStripMenuButton addAccountsMenuButtons() {
		Menu menu = new Menu();
		menu.setShowShadow(true);
		menu.setShadowDepth(3);

		MenuItemSeparator separator = new MenuItemSeparator();

		menu.setItems(

		createMenuItemGET("Get details", "account/accountDetails"));

		ToolStripMenuButton menuButton = new ToolStripMenuButton("Accounts",
				menu);
		menuButton.setWidth(100);
		// addMenuButton(menuButton);
		return menuButton;
	}

	private ToolStripMenuButton addUsersMenuButtons() {
		Menu menu = new Menu();
		menu.setShowShadow(true);
		menu.setShadowDepth(3);

		MenuItemSeparator separator = new MenuItemSeparator();

		menu.setItems(

				createMenuItemGET("Get user", "users/runId/***"),
				createMenuItemGET("Get user by id", "users/runId/***/account/***"),
				createMenuItemGET("Get user by team",
						"users/runId/***/teamId/***"),

				separator,
				createMenuItemPOST(
						"Create User",
						"users",
						"{  \"type\": \"org.celstec.arlearn2.beans.run.User\",  \"gameId\": 1, \"runId\": 1, \"email\": \"*\" }"));

		ToolStripMenuButton menuButton = new ToolStripMenuButton("Users", menu);
		menuButton.setWidth(100);
		// addMenuButton(menuButton);
		return menuButton;
	}

	private ToolStripMenuButton addRunMenuButtons() {
		Menu menu = new Menu();
		menu.setShowShadow(true);
		menu.setShadowDepth(3);

		MenuItemSeparator separator = new MenuItemSeparator();

		menu.setItems(
				createMenuItemGET("Run access", "myRuns/runAccess"),
				createMenuItemGET("Get run", "myRuns/runId/***"),
				createMenuItemGET("Runs Participate", "myRuns/participate"),
				createMenuItemGET("Runs For Tag", "myRuns/tagId/***"),

				separator,
				createMenuItemGET("Give Access",
						"myRuns/access/runId/*/account/*/accessRight/1"),
				createMenuItemPOST(
						"Create run",
						"myRuns",
						"{  \"type\": \"org.celstec.arlearn2.beans.run.Run\",  \"gameId\": 1, \"title\": \"my run\" }"));

		ToolStripMenuButton menuButton = new ToolStripMenuButton("Run", menu);
		menuButton.setWidth(100);
		// addMenuButton(menuButton);
		return menuButton;
	}

	private MenuItem createMenuItemGET(String label, final String path) {
		MenuItem item = new MenuItem(label);
		item.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				networkPage.pastePath(path);
			}
		});
		return item;
	}

	private MenuItem createMenuItemPOST(String label, final String path,
			final String post) {
		MenuItem item = new MenuItem(label);
		item.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				networkPage.pastePath(path);
				networkPage.pastePost(post);
			}
		});
		return item;
	}

	private ToolStripMenuButton addGameMenuButtons() {
		Menu menu = new Menu();
		menu.setShowShadow(true);
		menu.setShadowDepth(3);

		MenuItem gameAccess = new MenuItem("Game access");
		gameAccess.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				networkPage.pastePath("myGames/gameAccess");
			}
		});

		MenuItem getGame = new MenuItem("Get game");
		getGame.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				networkPage.pastePath("myGames/gameId/*");
			}
		});

		MenuItem participate = new MenuItem("Games Participate");
		participate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				networkPage.pastePath("myGames/participate");

			}
		});

		MenuItem postGame = new MenuItem("Post games");
		postGame.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				networkPage.pastePath("myGames");
				networkPage
						.pastePost("{  \"type\": \"org.celstec.arlearn2.beans.game.Game\",  \"title\": \"test\",   \"config\": {  \"type\": \"org.celstec.arlearn2.beans.game.Config\",  \"mapAvailable\": false,  \"manualItems\": [],  \"locationUpdates\": []}}");

			}
		});

		MenuItem giveAccess = new MenuItem("Give access");
		giveAccess.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(MenuItemClickEvent event) {
				networkPage
						.pastePath("myGames/access/gameId/*/account/*/accessRight/1");

			}
		});
		MenuItemSeparator separator = new MenuItemSeparator();

		menu.setItems(gameAccess, getGame, participate, separator, postGame,
				giveAccess);

		ToolStripMenuButton menuButton = new ToolStripMenuButton("Game", menu);
		menuButton.setWidth(100);
		// addMenuButton(menuButton);
		return menuButton;
	}

}
