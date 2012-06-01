package org.celstec.arlearn2.gwt.client.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.game.GameClient;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemGameDataSource;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwt.client.network.team.GameTeamDataSource;
import org.celstec.arlearn2.gwt.client.ui.items.GeneralItemCanvas;
import org.celstec.arlearn2.gwt.client.ui.modal.RoleWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceEnumField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class GameTab extends Tab {

	private long gameId;
	private ListGrid listGrid;
	GeneralItemCanvas giCanvas;
	HLayout horizontalButtons;
	private SelectItem roleGrid;
	private String[] roleValues;
	
	private VStack layout = new VStack();
	private SelectItem selectType;
	private DynamicForm selectForm;
	 private AuthoringConstants constants = GWT.create(AuthoringConstants.class);


	public GameTab(String title, final long gameId) {
		super(title);
		this.gameId = gameId;
		setCanClose(true);
		HLayout navLayout = new HLayout();
		navLayout.setMembersMargin(10);
		Canvas newGeneralItemCanvas = getNewGeneralItemCanvas();
		newGeneralItemCanvas.setWidth(300);
		navLayout.addMember(newGeneralItemCanvas);
		VLayout rightCanvas = new VLayout();
		navLayout.addMember(rightCanvas);

		rightCanvas.addMember(getListGeneralItemsCanvas());

		HLayout smallConfigCanvas = new HLayout();
		rightCanvas.addMember(smallConfigCanvas);
		smallConfigCanvas.addMember(getRolesCanvas());

		this.setPane(navLayout);
		addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				initGeneralItems();
			}
		});
	}

	public void initGeneralItems() {
		GeneralItemGameDataSource.getInstance().loadDataGame(gameId);
		GameTeamDataSource.getInstance().loadDataGame(gameId);
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
				roleValues = new String[array.size()];
				for (int i = 0; i < array.size(); i++) {
					
					roleValues[i] = array.get(i).isString().stringValue();
				}
				roleGrid.setValueMap(roleValues);

			}
		}
	}

	
	public Canvas getNewGeneralItemCanvas() {
		selectForm = new DynamicForm();
		layout.setBorder("1px solid gray");
		
		HeaderItem header = new HeaderItem();
		header.setDefaultValue(constants.createNewGeneralItem());

		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("org.celstec.arlearn2.beans.generalItem.NarratorItem",
				"Narrator Item");
		valueMap.put(
				"org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest",
				"Multiple Choice");
		valueMap.put(
				"org.celstec.arlearn2.beans.generalItem.VideoObject",
				"Video Object");
		valueMap.put(
				"org.celstec.arlearn2.beans.generalItem.AudioObject",
				"Audio Object");

		selectType = new SelectItem();
		selectType.setName("selectGi");
		selectType.setTitle(constants.selectType());
		selectType.setValueMap(valueMap);
		selectType.setWrapTitle(false);

		selectForm.setFields(header, selectType);
		layout.addMember(selectForm);

		selectType.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				String selectedItem = (String) event.getValue();
				createNewItem(selectedItem);
				event.getForm().resetValues();
			}
		});

		// selectType.addClickHandler(new
		// com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
		//
		// @Override
		// public void onClick(
		// com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
		// System.out.println("a click");
		// String selectedItem = event.getForm().getValueAsString("selectGi");
		// if (selectedItem != null) {
		// createNewItem(selectedItem);
		//
		// }
		// // System.out.println("on "+(String)
		// event.getForm().getValueAsString("selectGi"));
		//
		// }
		// });

		return layout;
	}

	private void createNewItem(String type) {
		initGenericControls(type);
		horizontalButtons = new HLayout();
		
		horizontalButtons.setAlign(Alignment.CENTER);
		horizontalButtons.setLayoutMargin(6);
		horizontalButtons.setMembersMargin(6);
//		horizontalButtons.setTop(245);
		horizontalButtons.setHeight(20);
		horizontalButtons.setWidth100();
//		horizontalButtons.setBorder("2px solid blue");

		
		IButton createButtton = new IButton(constants.create());
		createButtton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (giCanvas.validateForm()) {

					JSONObject object = giCanvas.generateObject();

					GeneralItemsClient.getInstance().createGeneralItem(object,
							new JsonCallback() {
								public void onJsonReceived(JSONValue jsonValue) {
									initGeneralItems();
									removeGenericControls();
								}
							});
				}
			}
		});
		
		IButton cancelButtton = new IButton(constants.cancel());
		cancelButtton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeGenericControls();
			}
		});
		horizontalButtons.addMember(createButtton);
		horizontalButtons.addMember(cancelButtton);
		layout.addMember(horizontalButtons);
	}

	private void editItem(final String type, int id) {

		GeneralItemsClient.getInstance().getGeneralItem(gameId, id,
				new JsonCallback() {
					public void onJsonReceived(JSONValue jsonValue) {
						initGenericControls(type);
						selectType.setDisabled(true);
						giCanvas.setItemValues(jsonValue);
						horizontalButtons = new HLayout();
						horizontalButtons.setWidth100();

						IButton createButtton = new IButton("save");
						createButtton.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								JSONObject object = giCanvas.generateObject();
								System.out.println(object);

								GeneralItemsClient.getInstance()
										.createGeneralItem(object,
												new JsonCallback() {
													public void onJsonReceived(
															JSONValue jsonValue) {
														System.out
																.println("update complete");
														removeGenericControls();
													}
												});
							}
						});
						IButton discardButton = new IButton("discard");
						discardButton.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								selectType.setDisabled(false);
								removeGenericControls();
							}
						});
						horizontalButtons.addMember(createButtton);
						horizontalButtons.addMember(discardButton);
						layout.addMember(horizontalButtons);
					}
				});

	}

	private void deleteItem(int id) {
		GeneralItemsClient.getInstance().deleteGeneralItem(gameId, id,
				new JsonCallback() {
					public void onJsonReceived(JSONValue jsonValue) {
						initGeneralItems();
					}
				});
	}

	private void removeGenericControls() {
		if (giCanvas != null) {
			layout.removeChild(giCanvas);
			giCanvas.markForDestroy();
		}
		if (horizontalButtons != null) {
			layout.removeChild(horizontalButtons);
			horizontalButtons.markForDestroy();
		}
	}

	private void initGenericControls(String type) {
		removeGenericControls();

		giCanvas = GeneralItemCanvas.getCanvas(type, roleValues);
		giCanvas.setGameId((int) this.gameId);
		giCanvas.setWidth100();
//		nonForm = giCanvas.getNonFormPart();

		layout.addMember(giCanvas);
//		if (nonForm != null) {
//			layout.addMember(nonForm);
//
//		}
	}

	public Canvas getListGeneralItemsCanvas() {
		Canvas canvas = new Canvas();
		listGrid = new GenericListGrid(true, true) {
			protected void deleteItem(ListGridRecord rollOverRecord) {
				System.out.println("click delete");
				GameTab.this.deleteItem(rollOverRecord.getAttributeAsInt("id"));
			}

			protected void editItem(ListGridRecord rollOverRecord) {
				GameTab.this.editItem(rollOverRecord.getAttribute("type"),
						rollOverRecord.getAttributeAsInt("id"));
			}
		};
		listGrid.setShowRollOverCanvas(true);

		listGrid.setWidth(500);
		listGrid.setHeight(224);
		listGrid.setShowAllRecords(true);
		listGrid.setDataSource(GeneralItemGameDataSource.getInstance());

		ListGridField titleGameField = new ListGridField("name", constants.title());
		ListGridField typeField = new ListGridField("simpleName", constants.type());
		ListGridField rolesField = new ListGridField("roles", constants.roles());
		ListGridField sortKeyField = new ListGridField("sortKey", "Order");
		sortKeyField.setWidth(35);
//sortKeyField,
		listGrid.setFields(new ListGridField[] { sortKeyField, titleGameField, typeField, rolesField });
		listGrid.setSortField("sortKey");
		listGrid.setCanResizeFields(true);

		listGrid.setPadding(5);
		listGrid.setWidth("100%");
		listGrid.setHeight("100%");
		listGrid.fetchData();
		canvas.addChild(listGrid);
		canvas.setHeight("75%");
//		canvas.setBorder("2px solid blue");
		return canvas;
	}

	private Canvas getRolesCanvas() {
		// Canvas canvas = new Canvas();
		VLayout layout = new VLayout();
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
