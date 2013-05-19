package org.celstec.arlearn2.portal.client.author.ui.game;

import java.util.HashMap;
import java.util.Map;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.portal.client.author.ui.ListMasterSectionSectionStackDetailTab;
import org.celstec.arlearn2.portal.client.author.ui.gi.GeneralItemsTab;
import org.celstec.arlearn2.portal.client.author.ui.run.RunsTab;
import org.celstec.arlearn2.portal.client.author.ui.tabs.TabManager;

import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
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
	public static final String CREATERUNBUTTON = "button_createrun";
	public static final String CREATEGIBUTTON = "button_creategi";
	
	public GamesTab(TabManager tabManager) {
		super("Games");
		addNewGameButton();
		
		this.tabManager = tabManager;
		hideDetail();
	}

	protected void tabSelect() {
		hideDetail();
		GameDataSource.getInstance().loadDataFromWeb();

	}
	
	public void hideDetail() {
		super.hideDetail();
//		sharingConfigSection.hideDetail();

	}
	
	@Override
	protected void initGrid(){
		getMasterListGrid().setDataSource(GameDataSource.getInstance());

		ListGridField idField = new ListGridField(GameModel.GAMEID_FIELD, "id ");
		idField.setWidth(30);
		idField.setCanEdit(false);

		ListGridField titleGameField = new ListGridField(GameModel.GAME_TITLE_FIELD, "Title ");

		ListGridField accessGameField = new ListGridField(GameModel.GAME_ACCESS_STRING, "Game access ");
		accessGameField.setCanEdit(false);
		accessGameField.setWidth(100);
		
		ListGridField createRunField = new ListGridField(CREATERUNBUTTON, "create run");
		createRunField.setAlign(Alignment.CENTER);  
		createRunField.setWidth(100);

		ListGridField createGeneralItemsField = new ListGridField(CREATEGIBUTTON, "create content");
		createRunField.setAlign(Alignment.CENTER);  
		createGeneralItemsField.setWidth(100);
		
		ListGridField ccField = new ListGridField(GameModel.LICENSE_CODE, "License", 80);
		ccField.setAlign(Alignment.CENTER);
		ccField.setType(ListGridFieldType.IMAGE);
		ccField.setImageURLSuffix(".png");
//		ccField.setIconWidth(80);
//		ccField.setIconHeight(15);
		ccField.setImageHeight(15);
		ccField.setImageWidth(80);
		
		
		getMasterListGrid().setFields(new ListGridField[] { idField, titleGameField,  createRunField, createGeneralItemsField, ccField, accessGameField });
		
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
//		addSectionDetail(descriptionSection);
//		addSectionDetail(gameJsonEditSection);
	}
	
	public IButton initButton(String fieldName, final ListGridRecord record) {
		if (fieldName.equals(CREATERUNBUTTON)) {
			return initCreateRunButton(record);
		} else if  (fieldName.equals(CREATEGIBUTTON)) {
			return initGiButton(record);
		} 
		
		return null;
	}
	
	private IButton initCreateRunButton(final ListGridRecord record) {
		IButton button = new IButton();
		button.setHeight(18);
		button.setWidth(100);
		// button.setIcon("flags/16/" + record.getAttribute("countryCode") +
		// ".png");
		button.setTitle("Create Run");
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RunsTab.instance.getTabSet().selectTab(RunsTab.instance);
				final HashMap<String, String> defaultValues = new HashMap<String, String>();
				defaultValues.put(RunModel.GAMEID_FIELD, record.getAttribute(GameModel.GAMEID_FIELD));
				defaultValues.put(RunModel.GAME_TITLE_FIELD, record.getAttribute(GameModel.GAME_TITLE_FIELD));
				Timer t = new Timer() {
				      public void run() {
				    	  RunsTab.instance.getMasterListGrid().startEditingNew(defaultValues);
				      }
				    };

				    // Schedule the timer to run once in 5 seconds.
				    t.schedule(250);
				
			}
		});
		return button;
	}
	
	private IButton initGiButton(final ListGridRecord record) {
		IButton button = new IButton();
		button.setHeight(18);
		button.setWidth(100);
		button.setTitle("Create Content");
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Game game = recordToGame(record);
				GeneralItemsTab giTab = new GeneralItemsTab(game);
				tabManager.addTab(giTab);
			}
		});
		return button;
	}
	
	@Override
	protected void masterRecordClick(RecordClickEvent event) {
		loadDataFromRecord(event.getRecord());		
		showDetail();
	}
	
	@Override
	protected void masterRecordEditComplete(EditCompleteEvent event) {
		Map values = event.getNewValues();
		if (values.containsKey(GameModel.GAME_ACCESS)){
//		if (values.get(GameModel.GAMEID_FIELD) instanceof Integer) {
			updateGame((Integer)values.get(GameModel.GAMEID_FIELD), (String) values.get(GameModel.GAME_TITLE_FIELD));
		} else {
			createGame((String) values.get(GameModel.GAME_TITLE_FIELD), getMasterListGrid().getRecord(event.getRowNum()));
		}		
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
			getSectionConfiguration().showSection(2);
			getSectionConfiguration().hideSection(3);
			getSectionConfiguration().hideSection(4);
		case 3: //write
			getSectionConfiguration().hideSection(0);
			getSectionConfiguration().hideSection(1);
			getSectionConfiguration().hideSection(2);
			getSectionConfiguration().hideSection(3);
			getSectionConfiguration().showSection(4);
			getSectionConfiguration().expandSection(4);
		default:
			break;
		}
	}
		
	public void createGame(String title, final Record record) {
		Game newGame = new Game();
		newGame.setTitle(title);
		GameClient.getInstance().createGame(newGame, new JsonCallback(){
			
			public void onJsonReceived(JSONValue jsonValue) {
				GameDataSource.getInstance().removeData(record);
			}
		});
	}
	
	private void updateGame(long gameId, String title) {
		Game existingGame = new Game(((AbstractRecord)GameDataSource.getInstance().getRecord(gameId)).getCorrespondingJsonObject());
		existingGame.setTitle(title);
		GameClient.getInstance().createGame(existingGame, new JsonCallback(){});
		RunDataSource.getInstance().setServerTime(0);
		RunDataSource.getInstance().loadDataFromWeb();
	}


	private void addNewGameButton() {
		IButton button = new IButton("New Game");
		button.setTop(250);
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final HashMap<String, String> defaultValues = new HashMap<String, String>();
				defaultValues.put(GameModel.GAMEID_FIELD, ""+(-1*System.currentTimeMillis()));
				getMasterListGrid().startEditingNew(defaultValues);
			}
		});
		addMasterMember(button);
	}

	
	@Override
	protected void deleteItem(final ListGridRecord rollOverRecord) {
		SC.ask("delete this game", new BooleanCallback() {
			public void execute(Boolean value) {
				if (value != null && value) {
					GameClient.getInstance().deleteGame(rollOverRecord.getAttributeAsInt(GameModel.GAMEID_FIELD),new JsonCallback() {
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
