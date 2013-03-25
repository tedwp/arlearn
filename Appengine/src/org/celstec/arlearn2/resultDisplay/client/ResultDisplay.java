package org.celstec.arlearn2.resultDisplay.client;

import org.celstec.arlearn2.gwtcommonlib.client.auth.Authentication;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.TeamModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.OwnerResponseDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.TeamDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.UserDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationSubscriber;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tile.TileGrid;

public class ResultDisplay implements EntryPoint {

	private Grid tileGrid = null;
	private List listGrid = null;
	private Mixed columnTreeGrid = null;

	private static final String password = "arl3arn123";

	private SlideShow slide;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		TeamDataSource.getInstance().loadDataFromWeb(2l);
		UserDataSource.getInstance().loadDataFromWeb(2l);
		 showResultDisplay();
//		showGames();
		// showResponses();
//		 showRuns();
//		 showTeams();
	}

	private void showResultDisplay() {
		if (!Authentication.getInstance().isAuthenticated()) {
			Authentication.getInstance().userCredentialsReceived("arlearn1", password);
		} else {
			System.out.println(Authentication.getInstance().getAuthenticationToken());
		}
		OwnerResponseDataSource.getInstance().loadDataFromWeb(2l);

		tileGrid = Grid.getInstance();
		listGrid = List.getInstance();
		columnTreeGrid = Mixed.getInstance();

		tileGrid.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				showPreview(tileGrid);
			}
		});

		listGrid.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				showPreview(tileGrid);
			}
		});

		ResultDisplayLayout main = new ResultDisplayLayout(tileGrid, listGrid, columnTreeGrid);
		main.draw();
	}

	private void showPreview(final TileGrid tileGrid) {
		slide = SlideShow.getInstance(tileGrid);
		slide.show();
	}

	/**
	 * Legacy code -----------
	 * */

	private VLayout vLayout;

	public void showResponses() {
		RootPanel rootPanel = RootPanel.get("container");
		vLayout = new VLayout();
		vLayout.setMembersMargin(15);
		if (!Authentication.getInstance().isAuthenticated()) {
			Authentication.getInstance().userCredentialsReceived("arlearn1", password);
		} else {
			System.out.println(Authentication.getInstance().getAuthenticationToken());
		}
		OwnerResponseDataSource.getInstance().loadDataFromWeb(118l);

		// if (!Authentication.getInstance().isAuthenticated()) {
		// vLayout.addMember(new Label("not auth"));
		// } else {
		// NotificationSubscriber not = NotificationSubscriber.getInstance();
		//
		// }
		vLayout.setWidth100();
		vLayout.setHeight100();
		// vLayout.setSize("1000", "600");

		ListGrid listGrid = new ListGrid();

		listGrid.setDataSource(OwnerResponseDataSource.getInstance());
		OwnerResponseDataSource.getInstance().loadDataFromWeb(118l);
		ListGridField titleGameField = new ListGridField("responseValue", "responseValue");
		ListGridField creatorGameField = new ListGridField("userEmail", "email");
		ListGridField imageUrlField = new ListGridField("imageUrl", "imgae ");
		ListGridField roleField = new ListGridField("role", "read ");
		ListGridField readField = new ListGridField("read", "read ");

		listGrid.setFields(new ListGridField[] { titleGameField, creatorGameField, imageUrlField, readField, roleField });
		listGrid.setCanResizeFields(true);

		listGrid.fetchData();
		vLayout.addMember(listGrid);
		rootPanel.add(vLayout);
	}

	public void showRuns() {
		RootPanel rootPanel = RootPanel.get("container");
		// vLayout = new VLayout();
		// vLayout.setMembersMargin(15);
		if (!Authentication.getInstance().isAuthenticated()) {
			Authentication.getInstance().userCredentialsReceived("arlearn1", password);
		} else {
			System.out.println(Authentication.getInstance().getAuthenticationToken());
		}

		// vLayout.setWidth100();
		// vLayout.setHeight100();

		ListGrid listGrid = new ListGrid();

		listGrid.setDataSource(RunDataSource.getInstance());
		RunDataSource.getInstance().loadDataFromWeb();
		ListGridField titleGameField = new ListGridField("title", "title");
		ListGridField creatorGameField = new ListGridField("owner", "owner");

		listGrid.setFields(new ListGridField[] { titleGameField, creatorGameField });
		listGrid.setCanResizeFields(true);

		listGrid.fetchData();
		// vLayout.addMember(listGrid);
		rootPanel.add(listGrid);
	}

	public void showGames() {
		RootPanel rootPanel = RootPanel.get("container");
		if (!Authentication.getInstance().isAuthenticated()) {
			Authentication.getInstance().userCredentialsReceived("arlearn1", password);
		} else {
			System.out.println(Authentication.getInstance().getAuthenticationToken());
			NotificationSubscriber.getInstance();

			ListGrid listGrid = new ListGrid();

			listGrid.setDataSource(GameDataSource.getInstance());
			GameDataSource.getInstance().loadDataFromWeb();
			ListGridField titleGameField = new ListGridField("title", "title");

			listGrid.setFields(new ListGridField[] { titleGameField });
			listGrid.setCanResizeFields(true);
			listGrid.setWidth100();
			listGrid.setHeight100();
			listGrid.fetchData();
			rootPanel.add(listGrid);
		}

	}
	
	public void showTeams() {
		RootPanel rootPanel = RootPanel.get("container");
		if (!Authentication.getInstance().isAuthenticated()) {
			Authentication.getInstance().userCredentialsReceived("arlearn1", password);
		} else {
			System.out.println(Authentication.getInstance().getAuthenticationToken());
			NotificationSubscriber.getInstance();

			ListGrid listGrid = new ListGrid();

			listGrid.setDataSource(TeamDataSource.getInstance());
			TeamDataSource.getInstance().loadDataFromWeb(2l);
			ListGridField titleGameField = new ListGridField(TeamModel.NAME_FIELD, TeamModel.NAME_FIELD);
			ListGridField titlIdField = new ListGridField(TeamModel.TEAMID_FIELD, TeamModel.TEAMID_FIELD);

			listGrid.setFields(new ListGridField[] { titleGameField, titlIdField });
			listGrid.setCanResizeFields(true);
			listGrid.setWidth100();
			listGrid.setHeight100();
			listGrid.fetchData();
			rootPanel.add(listGrid);
		}

	}

}
