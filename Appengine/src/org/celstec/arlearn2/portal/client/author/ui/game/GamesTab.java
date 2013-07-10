package org.celstec.arlearn2.portal.client.author.ui.game;

import java.util.HashMap;
import java.util.Map;

import org.celstec.arlearn2.gwt.client.control.Authentication;
import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthClient;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Run;
import org.celstec.arlearn2.portal.client.AuthoringConstants;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.author.ui.ListMasterSectionSectionStackDetailTab;
import org.celstec.arlearn2.portal.client.author.ui.game.i18.GameConstants;
import org.celstec.arlearn2.portal.client.author.ui.gi.GeneralItemsTab;
import org.celstec.arlearn2.portal.client.author.ui.run.RunsTab;
import org.celstec.arlearn2.portal.client.author.ui.tabs.TabManager;
import org.celstec.arlearn2.portal.client.toolbar.ToolBar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;

public class GamesTab extends ListMasterSectionSectionStackDetailTab {

	private GameConfigSection gameConfigSection ;
	private RoleConfigSection roleConfigSection;
//	private MapConfigSection mapConfigSection ;
	private SharingConfigSection sharingConfigSection;
	private CollaboratorsConfig collabConfigSection ;
	private ShowDescriptionSection descriptionSection ;
	private GameJsonEditSection gameJsonEditSection;

	private TabManager tabManager;
	private static GameConstants constants = GWT.create(GameConstants.class);
	
	public GamesTab(TabManager tabManager) {
		super(constants.games());
		this.tabManager = tabManager;
		hideDetail();
	}

	protected Canvas createRecordComponent2(final ListGridRecord record, Integer colNum, String fieldName) {
		if (fieldName.equals("runField")) {
			ImgButton runImg = createRunImg() ;
			runImg.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					createRun(record.getAttributeAsLong(GameModel.GAMEID_FIELD), record.getAttribute(GameModel.GAME_TITLE_FIELD));	
				}
			});

			return runImg;
		} else if (fieldName.equals("giField")) {
				ImgButton itemsImg = createItemsImg();
				itemsImg.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						Game game = recordToGame(record);
						GeneralItemsTab giTab = new GeneralItemsTab(game);
						tabManager.addTab(giTab);
					}
				});

				return itemsImg;
		} else if (fieldName.equals("downloadField")) {
			ImgButton itemsImg = createDownloadImg();
			itemsImg.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					download(record);
				}
			});

			return itemsImg;
	}
		
		return super.createRecordComponent2(record, colNum, fieldName);
	}


	private void createRun(final long gameId, String gameTitle){
		SC.askforValue(constants.createRunMessageHeader(), constants.createRunMessage().replace("***", gameTitle), new ValueCallback() {
			
			@Override
			public void execute(String value) {
				if (value != null) {
				RunsTab.instance.getTabSet().selectTab(RunsTab.instance);
				Run newRun = new Run();
				newRun.setTitle(value);
				newRun.setGameId(gameId);
				RunClient.getInstance().createRun(newRun, null);
				}
			}
		});
	}
	
	private ImgButton createRunImg() {
		ImgButton runImg = new ImgButton();
		runImg.setShowDown(false);
		runImg.setShowRollOver(false);
		runImg.setAlign(Alignment.CENTER);
		runImg.setSrc("/images/add_user.png");
		runImg.setPrompt(constants.createRunMessageHoover());
		runImg.setHeight(16);
		runImg.setWidth(16);
		return runImg;
	}
	
	private ImgButton createItemsImg() {
		ImgButton itemsImg = new ImgButton();
		itemsImg.setShowDown(false);
		itemsImg.setShowRollOver(false);
		itemsImg.setAlign(Alignment.CENTER);
		itemsImg.setSrc("/images/gi_add.png");
		itemsImg.setPrompt(constants.openGeneralItemsHoover());
		itemsImg.setHeight(16);
		itemsImg.setWidth(16);
		return itemsImg;
	}
	
	private ImgButton createDownloadImg() {
		ImgButton itemsImg = new ImgButton();
		itemsImg.setShowDown(false);
		itemsImg.setShowRollOver(false);
		itemsImg.setAlign(Alignment.CENTER);
		itemsImg.setSrc("/images/icon_down.png");
		itemsImg.setPrompt("download game");
		itemsImg.setHeight(16);
		itemsImg.setWidth(16);
		return itemsImg;
	}

	protected void tabSelect() {
		hideDetail();
		GameDataSource.getInstance().loadDataFromWeb();
	}
	
	public void hideDetail() {
		super.hideDetail();
	}
	
	@Override
	protected void initGrid(){
		getMasterListGrid().setDataSource(GameDataSource.getInstance());
		getMasterListGrid().setCanEdit(false);
		
		ListGridField mapStatusField = new ListGridField(GameModel.MAP_ICON, " ", 30);
		mapStatusField.setAlign(Alignment.CENTER);
		mapStatusField.setType(ListGridFieldType.IMAGE);
		mapStatusField.setImageURLSuffix(".png");
		 
		ListGridField idField = new ListGridField(GameModel.GAMEID_FIELD, "id");
		idField.setWidth(30);
		idField.setHidden(true);
		idField.setCanEdit(false);

		ListGridField titleGameField = new ListGridField(GameModel.GAME_TITLE_FIELD, constants.title());

		ListGridField accessGameField = new ListGridField(GameModel.GAME_ACCESS_STRING, constants.gameAccess());
		accessGameField.setCanEdit(false);
		accessGameField.setWidth(100);
		
		ListGridField runField = new ListGridField("runField", " ");  
		 runField.setWidth(20);
	
		ListGridField giField = new ListGridField("giField", " ");  
		giField.setWidth(20);
		
		ListGridField downloadField = new ListGridField("downloadField", " ");  
		downloadField.setWidth(20);
		
		ListGridField ccField = new ListGridField(GameModel.LICENSE_CODE, constants.license(), 80);
		ccField.setAlign(Alignment.CENTER);
		ccField.setType(ListGridFieldType.IMAGE);
		ccField.setImageURLSuffix(".png");
		ccField.setImageHeight(15);
		ccField.setImageWidth(80);
		
		ListGridField deleteField = new ListGridField("deleteField", "");  
		deleteField.setWidth(20);
		deleteField.setPrompt(constants.deleteGame());
		
		if (AccountManager.getInstance().isAdvancedUser()) { //
			getMasterListGrid().setFields(new ListGridField[] {mapStatusField, giField,  idField, runField, downloadField, titleGameField,    ccField, accessGameField, deleteField });
		} else {
			getMasterListGrid().setFields(new ListGridField[] {mapStatusField, giField, idField, runField, titleGameField,    ccField, accessGameField, deleteField });
		}
			
		
		getMasterListGrid().addCellClickHandler(new CellClickHandler() {
			
			@Override
			public void onCellClick(CellClickEvent event) {
				 if (GameModel.MAP_ICON.equals(getMasterListGrid().getFieldName(event.getColNum()))){
					 Game existingGame = new Game(((AbstractRecord)GameDataSource.getInstance().getRecord(event.getRecord().getAttributeAsLong(GameModel.GAMEID_FIELD))).getCorrespondingJsonObject());
					 existingGame.setMapAvailable(!event.getRecord().getAttribute(GameModel.MAP_ICON).equals("icon_maps"));
					 GameClient.getInstance().createGame(existingGame, new JsonCallback(){});
				 }
			}
		});
		getMasterListGrid().setSort(new SortSpecifier[]{
                new SortSpecifier(GameModel.GAME_TITLE_FIELD, SortDirection.ASCENDING),
		});
		Criteria criteria = new Criteria();
		criteria.addCriteria(GameModel.DELETED_FIELD, false);
		getMasterListGrid().setCriteria(criteria);
	}
	
	
	
	@Override
	protected void initConfigSections() {
		gameConfigSection = new GameConfigSection();
		roleConfigSection = new RoleConfigSection();
//		private MapConfigSection mapConfigSection = new MapConfigSection();
		sharingConfigSection = new SharingConfigSection();
		collabConfigSection = new CollaboratorsConfig();
		descriptionSection = new ShowDescriptionSection();
		gameJsonEditSection = new GameJsonEditSection();
		
		addSectionDetail(gameConfigSection);
		addSectionDetail(roleConfigSection);
		addSectionDetail(sharingConfigSection);
		addSectionDetail(collabConfigSection);
		addSectionDetail(descriptionSection);
		addSectionDetail(gameJsonEditSection);
	}

	@Override
	protected void masterRecordClick(RecordClickEvent event) {
		showDetail();
		loadDataFromRecord(event.getRecord());		
	}
	
	private Game recordToGame(Record record) {
		return new Game(((AbstractRecord)GameDataSource.getInstance().getRecord(record.getAttributeAsLong(GameModel.GAMEID_FIELD))).getCorrespondingJsonObject());
	}
	
	public void loadDataFromRecord(Record record) {
		Game game = recordToGame(record);

		gameConfigSection.loadDataFromRecord(game);
		roleConfigSection.loadDataFromRecord(game);
		sharingConfigSection.loadDataFromRecord(game);
//		mapConfigSection.loadDataFromRecord(game);
		collabConfigSection.loadDataFromRecord(game);
		descriptionSection.loadDataFromRecord(game);
		if (gameJsonEditSection != null) gameJsonEditSection.loadDataFromRecord(game);
		if (AccountManager.getInstance().isAdministrator()) {
			getSectionConfiguration().showSection(5);
		} else {
			getSectionConfiguration().hideSection(5);
		}
		switch (record.getAttributeAsInt(GameModel.GAME_ACCESS)) {
		case 1: //owner
			getSectionConfiguration().showSection(0);
			getSectionConfiguration().showSection(1);
			getSectionConfiguration().showSection(2);
			getSectionConfiguration().showSection(3);
			getSectionConfiguration().hideSection(4);
			break;
		case 2: //write
			getSectionConfiguration().showSection(0);
			getSectionConfiguration().showSection(1);
			getSectionConfiguration().hideSection(2);
			getSectionConfiguration().hideSection(3);
			getSectionConfiguration().hideSection(4);
			break;
		case 3: //read
			getSectionConfiguration().hideSection(0);
			getSectionConfiguration().hideSection(1);
			getSectionConfiguration().hideSection(2);
			getSectionConfiguration().hideSection(3);
			getSectionConfiguration().showSection(4);
			getSectionConfiguration().expandSection(4);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void deleteItem(final ListGridRecord record) {
		int gameAccess = record.getAttributeAsInt(GameModel.GAME_ACCESS);
		if (gameAccess == 2 || gameAccess == 3) {
			SC.ask(constants.removeAccess().replace("***", record.getAttribute(GameModel.GAME_TITLE_FIELD)),new BooleanCallback() {
				public void execute(Boolean value) {
					if (value != null && value) {
						GameClient.getInstance().removeAccess(record.getAttributeAsLong(GameModel.GAMEID_FIELD),new JsonCallback() {
							@Override
							public void onJsonReceived(JSONValue jsonValue) {
								GameDataSource.getInstance().removeData(record);
								GameDataSource.getInstance().loadDataFromWeb();
							}
						});	
					}
				}
			});
		} else if (gameAccess == 1) {
			SC.ask(constants.deleteThisGame().replace("***", record.getAttribute(GameModel.GAME_TITLE_FIELD)), new BooleanCallback() {
				public void execute(Boolean value) {
					if (value != null && value) {
						GameClient.getInstance().deleteGame(record.getAttributeAsLong(GameModel.GAMEID_FIELD),new JsonCallback() {
							@Override
							public void onJsonReceived(JSONValue jsonValue) {
								GameDataSource.getInstance().loadDataFromWeb();
							}
						});	
					}
				}
			});
		}
	}
	
	protected void download(ListGridRecord record) {
		Window.open( "../download/game?gameId="+record.getAttributeAsLong(GameModel.GAMEID_FIELD)+"&auth="+OauthClient.checkAuthentication().getAccessToken()+"&type=game", "_self", "");
	}

}
