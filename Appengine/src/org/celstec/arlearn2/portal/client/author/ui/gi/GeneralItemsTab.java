package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfiguration;
import org.celstec.arlearn2.portal.client.author.ui.VerticalMasterDetailTab;

import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDropEvent;
import com.smartgwt.client.widgets.grid.events.RecordDropHandler;
import com.smartgwt.client.widgets.grid.events.RemoveRecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RemoveRecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class GeneralItemsTab extends VerticalMasterDetailTab {

	private Game game;
	private ListGrid masterList;
	private boolean recordSelection = true;

	private GeneralItemDetail giDetail;

	public GeneralItemsTab(Game g) {
		super(g.getString(GameModel.GAME_TITLE_FIELD));
		this.game = g;
		GeneralItemDataSource.getInstance().loadDataFromWeb(game.getGameId());
		setCanClose(true);

	}

	@Override
	public Canvas getMaster() {
		HLayout layout = new HLayout();
		layout.addMember(getMastGiTabSet());
		layout.addMember(new CreateReuseGeneralItem((GeneralItemDetail) getDetail(), this));
		layout.setMembersMargin(5);
		return layout;
	}

	public Canvas getMastGiTabSet() {
		final TabSet giListTabSet = new TabSet();
		giListTabSet.setWidth("*");
		giListTabSet.setTabBarPosition(Side.LEFT);

		Tab lTab1 = new Tab();
		lTab1.setIcon("/images/list_icon.png", 16);

		lTab1.setPane(getMasterList());

		Tab lTab2 = new Tab();
		lTab2.setIcon("/images/icon_maps.png", 16);
		// lTab2.setPane(getMasterList());

		giListTabSet.addTab(lTab1);
		giListTabSet.addTab(lTab2);

		return giListTabSet;
	}

	public Canvas getMasterList() {
		masterList = new ListGrid();
		masterList.setShowRecordComponentsByCell(true);
		// masterList.setCanRemoveRecords(true);

		masterList.setShowAllRecords(true);
		masterList.setShowRecordComponents(true);
		masterList.setCanAcceptDroppedRecords(true);

		masterList.setHeight(350);
		masterList.setWidth100();
		masterList.setHeight("40%");
		masterList.setAutoFetchData(true);
		masterList.setCanEdit(true);
		initGrid();

		masterList.addRecordDropHandler(new RecordDropHandler() {

			@Override
			public void onRecordDrop(RecordDropEvent event) {
				System.out.println("record dropped " + event.getDropRecords()[0].toMap());
				for (final Record r : event.getDropRecords()) {
					GeneralItemsClient.getInstance().getGeneralItem(r.getAttributeAsInt(GameModel.GAMEID_FIELD), r.getAttributeAsInt(GeneralItemModel.ID_FIELD), new JsonCallback() {
						public void onJsonReceived(JSONValue jsonValue) {
							GeneralItemDataSource.getInstance().removeData(r);
							GeneralItem item = GeneralItem.createObject(jsonValue.isObject());
							item.removeAttribute(GeneralItemModel.ID_FIELD);
							item.setLong(GameModel.GAMEID_FIELD, game.getGameId());
							item.removeAttribute("dependsOn");
							item.removeAttribute("disappearOn");
							GeneralItemsClient.getInstance().createGeneralItem(item, new JsonCallback() {
								public void onJsonReceived(JSONValue jsonValue) {
									GeneralItemDataSource.getInstance().loadDataFromWeb(game.getGameId());
								}
							});
						}
					});

				}

			}
		});

		masterList.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				if (recordSelection)
					masterRecordClick(event);
			}
		});
		masterList.addEditCompleteHandler(new EditCompleteHandler() {
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				masterRecordEditComplete(event);

			}
		});
		masterList.addRemoveRecordClickHandler(new RemoveRecordClickHandler() {

			@Override
			public void onRemoveRecordClick(RemoveRecordClickEvent event) {
				GeneralItemsClient.getInstance().deleteGeneralItem(masterList.getRecord(event.getRowNum()).getAttributeAsLong(GameModel.GAMEID_FIELD), masterList.getRecord(event.getRowNum()).getAttributeAsLong(GeneralItemModel.ID_FIELD), new JsonCallback() {

				});
			}
		});
		return masterList;
	}

	@Override
	public Canvas getDetail() {
		if (giDetail == null)
			giDetail = new GeneralItemDetail() {
//				public void setEditMode() {
//					super.setEditMode();
//					masterList.setCanEdit(false);
//					recordSelection = false;
//
//				}
//
//				public void setViewMode() {
//					super.setViewMode();
//					masterList.setCanEdit(true);
//					recordSelection = true;
//				}
			};

		return giDetail;
	}

	protected void initGrid() {
		masterList.setDataSource(GeneralItemDataSource.getInstance());
		ListGridField idField = new ListGridField(GeneralItemModel.ID_FIELD, "id ");
		idField.setWidth(30);
		idField.setCanEdit(false);

		ListGridField giTitleField = new ListGridField(GeneralItemModel.NAME_FIELD, "Title ");
		masterList.setFields(new ListGridField[] { idField, giTitleField });

	}

	protected void masterRecordClick(RecordClickEvent event) {
		giDetail.viewGeneralItem(recordToGeneralItem(event.getRecord()), true);
//		giDetail.loadGeneralItem(recordToGeneralItem(event.getRecord()));
		
	}

	private GeneralItem recordToGeneralItem(Record record) {
		return GeneralItem.createObject(((AbstractRecord) GeneralItemDataSource.getInstance().getRecord(record.getAttributeAsLong(GeneralItemModel.ID_FIELD))).getCorrespondingJsonObject());
	}

	protected void masterRecordEditComplete(EditCompleteEvent event) {

		// TODO Auto-generated method stub

	}

	public Game getGame() {
		return game;
	}

	public void hideDetail() {
////		giDetail.setVisibility(Visibility.HIDDEN);
		giDetail.eraseView();
	}
	
//	public void showDetail() {
//		giDetail.eraseView();
//		giDetail.initView();
//		giDetail.setVisibility(Visibility.VISIBLE);
//	}
}
