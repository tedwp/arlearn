package org.celstec.arlearn2.gwt.client.ui;

import java.util.HashSet;

import org.celstec.arlearn2.gwt.client.Authoring;
import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.control.Authentication;
//import org.celstec.arlearn2.gwt.client.control.GameDataSource;
import org.celstec.arlearn2.gwt.client.control.ReadyCallback;
//import org.celstec.arlearn2.gwt.client.network.GameClient;
//import org.celstec.arlearn2.gwt.client.network.GamesCallback;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.game.GameClient;
import org.celstec.arlearn2.gwt.client.network.game.GameDataSource;
import org.celstec.arlearn2.gwt.client.network.run.RunDataSource;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Progressbar;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.RowSpacerItem;
import com.smartgwt.client.widgets.form.fields.SectionItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverEvent;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.google.gwt.core.client.GWT;

public class GamesTab extends GenericTab implements NotificationHandler {

	 private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	  
	public static ListGrid listGrid;
	private Progressbar hBar2;
	
	public GamesTab() {
		super("Games");		
		setLeft(getLeft());
		setRight(getGameCanvas());
		listGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {  
            public void onCellDoubleClick(CellDoubleClickEvent event) {  
            	long gameId = Long.parseLong(event.getRecord().getAttribute("gameId"));
            	GameTab tab = new GameTab("Game: "+event.getRecord().getAttribute("title"), gameId);  
                Authoring.addTab(tab, "game:"+gameId);
               
            }  
        });  
	}
	
	@Override
	protected void tabSelect() {
//		listGrid.fetchData();
//	    RootPanel.get("loading").setVisible(true);
		final WaitPopup wp = new WaitPopup();
//		wp.show("loading");
		listGrid.setLoadingDataMessage("is loading");
		GameDataSource.getInstance().loadData(new ReadyCallback() {
			
			@Override
			public void ready() {
//			    RootPanel.get("loading").setVisible(false);
//				wp.hide();
				
				listGrid.fetchData();
				
			}
		});
	}
	
	public Canvas getLeft() {
		VLayout vLayout = new VLayout(10);
		vLayout.addMember(getNewGameForm());
		vLayout.addMember(getExistingGameForm());

		hBar2 = new Progressbar();
		hBar2.setVertical(false);
		hBar2.setHeight(24);
		hBar2.setWidth("*");
		vLayout.addMember(hBar2);
		hBar2.setVisibility(Visibility.HIDDEN);

		return vLayout;
	}

	
	private Canvas getNewGameForm() {		

//		HeaderItem header = new HeaderItem();
//		header.setDefaultValue(constants.createNewGame());

		final TextItem titleGame = new TextItem("titleGame");
		titleGame.setTitle(constants.gameName());
		titleGame.setSelectOnFocus(true);
		titleGame.setWrapTitle(false);

		final TextItem creatorGame = new TextItem("creator");
		creatorGame.setTitle(constants.author());
		creatorGame.setWrapTitle(false);

		final CheckboxItem withMap = new CheckboxItem("withMap");
		withMap.setTitle(constants.withMap());
		withMap.setValue(true);
		withMap.addItemHoverHandler(new ItemHoverHandler() {
			public void onItemHover(ItemHoverEvent event) {
				withMap.setPrompt(constants.instructMap());
			}
		});
		
		ButtonItem button = new ButtonItem("submit", constants.submit());
		// button.setStartRow(true);
		button.setWidth(80);
		button.setStartRow(false);
		button.setEndRow(false);
		button.setColSpan(4);
		button.setAlign(Alignment.CENTER);
		final DynamicForm form = getForm(constants.createNewGame(), titleGame, creatorGame, withMap, new RowSpacerItem(),
				button);
		form.setWidth(285);
		button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				GameClient.getInstance().createGame(
						form.getValueAsString("titleGame"),
						form.getValueAsString("creator"),
						(Boolean) form.getValue("withMap"),
						new JsonCallback() {
							public void onJsonReceived(JSONValue jsonValue) {
//								tabSelect();
							}
						});
				titleGame.setValue("");
				creatorGame.setValue("");
			}
		});
		return form;
	}
	
	private Canvas getExistingGameForm() {		
		

		final UploadItem fileItem = new UploadItem("uploadRun");
		fileItem.setTitle(constants.chooseAGame());
		fileItem.setWidth(150);
		fileItem.setWrapTitle(false);


		HiddenItem authItem = new HiddenItem("auth");
		authItem.setValue("auth="
				+ Authentication.getInstance().getAuthenticationToken());

		ButtonItem button = new ButtonItem("submit", constants.upload());
		// button.setStartRow(true);
		button.setWidth(80);
		button.setStartRow(false);
		button.setEndRow(false);
		button.setColSpan(4);
		button.setAlign(Alignment.CENTER);
		
		
		
		final DynamicForm form = getForm(constants.uploadGame(), fileItem, new RowSpacerItem(),
				button, authItem);
		form.setEncoding(Encoding.MULTIPART);


		button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
//				gameIdItem.setValue(form.getValue("itemID"));
				form.setCanSubmit(true);
				form.submit();
				NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.notification.authoring.GameCreationStatus", GamesTab.this);
				hBar2.setPercentDone(0);
				hBar2.setVisibility(Visibility.VISIBLE);
				
//				pollForUpdate();
			}
		});

		form.setTarget("hidden_frame");
		form.setAction("/uploadGame/fileUpload.html");
		return form;
	}

	public Canvas getGameCanvas() {

		listGrid = new GenericListGrid(false, true, true, true, false) {
			protected void deleteItem(ListGridRecord rollOverRecord) {
				GamesTab.this.deleteGame(rollOverRecord.getAttributeAsInt("gameId"), rollOverRecord.getAttributeAsString("title"));
			}
			protected void download(ListGridRecord rollOverRecord) {
				GamesTab.this.download(rollOverRecord);
			}
			
			protected void mapItem(ListGridRecord rollOverRecord) {
				GamesTab.this.map(rollOverRecord);
			} 
		};
		listGrid.setShowRollOverCanvas(true);

		listGrid.setWidth(500);
		listGrid.setHeight(224);
		listGrid.setShowAllRecords(true);
		listGrid.setDataSource(GameDataSource.getInstance());
		
		ListGridField mapStatusField = new ListGridField("status_map", " ", 30);
		mapStatusField.setAlign(Alignment.CENTER);
		mapStatusField.setType(ListGridFieldType.IMAGE);
		mapStatusField.setImageURLSuffix(".png");
		ListGridField titleGameField = new ListGridField("title", constants.game());  
	    ListGridField creatorGameField = new ListGridField("creator", constants.creator());  
		
		listGrid.setFields(new ListGridField[] {mapStatusField, titleGameField, creatorGameField });
		listGrid.setCanResizeFields(true);

		listGrid.setPadding(5);
		listGrid.setWidth("100%");
		listGrid.setHeight("100%");
//		listGrid.fetchData();
		listGrid.setAutoFetchData(true);

		return listGrid;
	}

	protected void deleteGame(final int gameId, String name) {
		
		SC.ask(constants.confirmDeleteUser().replace("***", name), new BooleanCallback() {
			public void execute(Boolean value) {
				if (value != null && value) {
					GameClient.getInstance().deleteGame(gameId, null);
				}
			}
		});
	}
	
	protected void map(ListGridRecord record) {
		String statusMap = record.getAttribute("status_map");
		if (statusMap == null || statusMap.equals("icon_maps")) {
		long gameId = Long.parseLong(record.getAttribute("gameId"));
		GameMapTab tab = new GameMapTab("Game map: " + record.getAttribute("title"), 
				Long.parseLong(record.getAttribute("gameId")));
		Authoring.addTab(tab, "gamemap:" + gameId);
		}
	}
	
	protected void download(ListGridRecord record) {
		long gameId = Long.parseLong(record.getAttribute("gameId"));
		String auth = Authentication.getInstance().getAuthenticationToken();
		Window.open( "../download/game?gameId="+gameId+"&auth="+auth+"&type=game", "_self", "");
		
		
	}
	
//	private int amountOfRecords = 0;
//	private Timer t = null;

	@Override
	public void onNotification(JSONObject bean) {
		int status = (int) bean.get("status").isNumber().doubleValue();

		hBar2.setPercentDone((int) (100/3*(status+1)));  
		if (status == 100) {
			hBar2.setVisibility(Visibility.HIDDEN);
			tabSelect();
		}

	}

	
}
