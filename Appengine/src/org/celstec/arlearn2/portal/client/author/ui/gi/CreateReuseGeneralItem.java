package org.celstec.arlearn2.portal.client.author.ui.gi;


import org.celstec.arlearn2.gwt.client.network.game.GameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.QueryGeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.author.ui.gi.modal.GeneralItemWindow;
import org.celstec.arlearn2.portal.client.author.ui.gi.modal.NarratorItemWindow;

import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class CreateReuseGeneralItem extends VLayout {
	
	QueryGeneralItemDataSource searchDs = new QueryGeneralItemDataSource();
	GeneralItemsTab generalItemsTab;
	
	public CreateReuseGeneralItem(final GeneralItemDetail giDetail, GeneralItemsTab generalItemsTab) {
		System.out.println("CreateReuseGeneralItem"+generalItemsTab );
		this.generalItemsTab = generalItemsTab;
		setWidth(300);
		setPadding(5);
		setMembersMargin(10);

		setBorder("1px solid #d6d6d6");

		CreateItemForm cif = new CreateItemForm();
		cif.setWidth100();
		addMember(cif);
		SearchForm sf = new SearchForm();
		sf.setWidth100();
		addMember(sf);
		ListGrid lg = new ListGrid();
		lg.setDataSource(searchDs);
		lg.setAutoFetchData(true);

		lg.setHeight("*");

		ListGridField titlefield = new ListGridField(GeneralItemModel.NAME_FIELD, "title");
		
		lg.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				long itemId = event.getRecord().getAttributeAsLong(GeneralItemModel.ID_FIELD);
				long gameId = event.getRecord().getAttributeAsLong(GameModel.GAMEID_FIELD);
				GeneralItemsClient.getInstance().getGeneralItem(gameId, itemId, new JsonCallback(){
					public void onJsonReceived(JSONValue jsonValue) {
						System.out.println("gi json "+jsonValue);
						GeneralItem gi = new GeneralItem(jsonValue.isObject());
						giDetail.loadGeneralItem(gi);
					}

				});

			}
		});
		lg.setFields(new ListGridField[] { titlefield });
		
		
		addMember(lg);
	}
	
	class CreateItemForm extends DynamicForm {
		public CreateItemForm() {
			setNumCols(6);

			final SelectItem itemTypeSelect = new SelectItem("itemType");
			itemTypeSelect.setShowTitle(false);
			itemTypeSelect.setColSpan(5);
			itemTypeSelect.setEndRow(false);
			itemTypeSelect.setStartRow(true);
			itemTypeSelect.setWidth(220);
			String [] itemTypes = GeneralItemsManagement.getItemTypes(false);
			itemTypeSelect.setValueMap(itemTypes);
			
			itemTypeSelect.setPickListWidth(220);
			itemTypeSelect.setValueField("itemType");
			itemTypeSelect.setValue(itemTypes[0]);
			

			ButtonItem findItem = new ButtonItem("Create");
			findItem.setStartRow(false);
			findItem.setEndRow(true);

			findItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					GeneralItemWindow.initWindow(itemTypeSelect.getValueAsString(), CreateReuseGeneralItem.this.generalItemsTab.getGame());
				}
			});

			setItems(itemTypeSelect, findItem);
		}
	}

	class SearchForm extends DynamicForm {

		private ButtonItem findItem;

		public SearchForm() {
			setNumCols(6);

			final TextItem searchText = new TextItem("searchValue");
			searchText.setShowTitle(false);
			searchText.setColSpan(5);
			searchText.setEndRow(false);
			searchText.setStartRow(true);
			searchText.setWidth(220);


			findItem = new ButtonItem("Find");
			findItem.setIcon("/images/find.png");
			findItem.setStartRow(false);
			findItem.setEndRow(true);

			findItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					searchDs.search(searchText.getValueAsString());
					
				}
			});

			setItems(searchText, findItem);
		}

	}
}