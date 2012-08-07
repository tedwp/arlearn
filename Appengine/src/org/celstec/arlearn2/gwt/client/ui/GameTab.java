package org.celstec.arlearn2.gwt.client.ui;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.game.GameClient;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemGameDataSource;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwt.client.network.team.GameTeamDataSource;
import org.celstec.arlearn2.gwt.client.ui.modal.RoleWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;

public class GameTab extends MasterDetailTab {

	private long gameId;
	private GeneralItemControlCanvas controlItem;

	
	private ListGrid listGrid;
	private SelectItem roleGrid;
	 private AuthoringConstants constants = GWT.create(AuthoringConstants.class);


	public GameTab(String title, final long gameId) {
		super(title);
		this.gameId = gameId;
		controlItem = new GeneralItemControlCanvas(gameId);
		controlItem.setModificationHandler(new GeneralItemControlCanvas.ItemModification() {
			
			@Override
			public void modified() {
				tabSelected(null);
				
			}
		});
		setMasterCanvas(controlItem);
		setDetailCanvas(initDetailCanvas());
		
		setCanClose(true);
	}
	
	private VLayout initDetailCanvas() {
		VLayout rightCanvas = new VLayout();
		rightCanvas.setHeight100();
		rightCanvas.addMember(getListGeneralItemsCanvas());
		HLayout smallConfigCanvas = new HLayout();
		smallConfigCanvas.setHeight(100);
		smallConfigCanvas.addMember(getRolesCanvas());
		rightCanvas.addMember(smallConfigCanvas);
		return rightCanvas;
	}

	public void tabSelected(TabSelectedEvent event) {
		GeneralItemGameDataSource.getInstance().loadDataGame(gameId);
		GameTeamDataSource.getInstance().loadDataGame(gameId);
		Criteria crit = new Criteria();
		crit.addCriteria("deleted", false);
		listGrid.filterData(crit);
		GameClient.getInstance().getGameConfig(gameId, new JsonCallback() {

			public void onJsonReceived(JSONValue jsonValue) {

				updateConfig(jsonValue.isObject());
			}

		});
	}

	public void updateConfig(JSONObject jsonValue) {
		if (jsonValue.containsKey("roles")) {
			JSONArray array = jsonValue.get("roles").isArray();
			if (array != null) {
				String[] roleValues = new String[array.size()];
				for (int i = 0; i < array.size(); i++) {
					roleValues[i] = array.get(i).isString().stringValue();
				}
				controlItem.setRoleValues(roleValues);
				roleGrid.setValueMap(roleValues);
			}
		}
	}

	

	private void editItem(final String itemType, int id) {

		GeneralItemsClient.getInstance().getGeneralItem(gameId, id,
				new JsonCallback() {
					public void onJsonReceived(JSONValue jsonValue) {
						controlItem.edit(jsonValue);
					}
				});

	}

	private void deleteItem(int id) {
		GeneralItemsClient.getInstance().deleteGeneralItem(gameId, id,
				new JsonCallback() {
					public void onJsonReceived(JSONValue jsonValue) {
						tabSelected(null);
					}
				});
	}

	public Canvas getListGeneralItemsCanvas() {
		Canvas canvas = new Canvas();
		listGrid = new GenericListGrid(true, true) {
			protected void deleteItem(ListGridRecord rollOverRecord) {
				GameTab.this.deleteItem(rollOverRecord.getAttributeAsInt("id"));
			}

			protected void editItem(ListGridRecord rollOverRecord) {
				GameTab.this.editItem(rollOverRecord.getAttribute("type"),
						rollOverRecord.getAttributeAsInt("id"));
			}
		};
		listGrid.setShowRollOverCanvas(true);


		listGrid.setShowAllRecords(true);
		listGrid.setDataSource(GeneralItemGameDataSource.getInstance());

		ListGridField titleGameField = new ListGridField("name", constants.title());
		ListGridField typeField = new ListGridField("simpleName", constants.type());
		ListGridField rolesField = new ListGridField("roles", constants.roles());
		ListGridField deletedField = new ListGridField("deleted", "deleted");
		ListGridField sortKeyField = new ListGridField("sortKey", "Order");
		sortKeyField.setWidth(35);
		listGrid.setFields(new ListGridField[] { sortKeyField, titleGameField, typeField, rolesField });
		listGrid.setSortField("sortKey");
		listGrid.setCanResizeFields(true);

		listGrid.setPadding(5);
		listGrid.setWidth("100%");
		listGrid.setHeight("100%");
		listGrid.fetchData();
		canvas.addChild(listGrid);
		canvas.setHeight("*");
//		canvas.setBorder("2px solid red");
		return canvas;
	}

	private Canvas getRolesCanvas() {
		// Canvas canvas = new Canvas();
		VStack layout = new VStack();
//		layout.setBorder("2px solid blue");

		layout.setHeight(100);
		Label title = new Label(constants.roles());
		title.setHeight(15);
		layout.addMember(title);

		DynamicForm form = new DynamicForm();
		form.setHeight("*");
		roleGrid = new SelectItem();
		roleGrid.setTitle("Select Multiple (Grid)");

		roleGrid.setMultiple(true);
		roleGrid.setHeight(100);
		roleGrid.setShowTitle(false);
		roleGrid.setMultipleAppearance(MultipleAppearance.GRID);
		form.setFields(roleGrid);
		layout.addMember(form);

		IButton button = new IButton(constants.newRole());
		button.setHeight(20);
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				(new RoleWindow(gameId, GameTab.this)).show();
				// ListGridRecord lgr = new ListGridRecord();
				// lgr.setAttribute("id", gameId);
				// lgr.setAttribute("pk", ""+gameId+System.currentTimeMillis());
				// listGrid.startEditingNew(lgr);
			}
		});
		layout.addMember(button);
//		layout.setBorder("2px solid blue");
		// canvas.setHeight("*");

		return layout;
	}

	public class RoleDataSource extends DataSource {

		public RoleDataSource() {

			setClientOnly(true);
		}
	}

}
