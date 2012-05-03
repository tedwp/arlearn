package org.celstec.arlearn2.gwt.client.ui;

import java.util.LinkedHashMap;

import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemGameDataSource;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwt.client.ui.items.GeneralItemCanvas;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
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
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class GameTab extends Tab {

	private long gameId;
	private ListGrid listGrid;
	GeneralItemCanvas giCanvas;
	Canvas nonForm;
	HLayout horizontalButtons;
	
	public GameTab(String title, final long gameId) {
		super(title);
		this.gameId = gameId;
		setCanClose(true);
		HLayout navLayout = new HLayout();
		navLayout.setMembersMargin(10);
		Canvas rightCanvas = getRightCanvas();
		rightCanvas.setWidth(300);
		navLayout.addMember(rightCanvas);
		navLayout.addMember(getLeftCanvas());
		
//		navLayout.setMembers( getRightCanvas(),getLeftCanvas());
//		navLayout.setMembers(getRightCanvas());

		this.setPane(navLayout);
		addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				initGeneralItems();
			}
		});
	}
	
	private void initGeneralItems() {
		GeneralItemGameDataSource.getInstance().loadDataGame(gameId);
	}

	private  VLayout layout = new VLayout(10);
	private  SelectItem selectType;
	private DynamicForm selectForm;
	
	public Canvas getRightCanvas() {
		selectForm = new DynamicForm();
		layout.setBorder("1px solid");
		HeaderItem header = new HeaderItem();
		header.setDefaultValue("Create new general item");

		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("org.celstec.arlearn2.beans.generalItem.NarratorItem",
				"Narrator Item");
		valueMap.put(
				"org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest",
				"Multiple Choice");

		selectType = new SelectItem();
		selectType.setTitle("Select type");
		selectType.setValueMap(valueMap);

        
        selectForm.setFields(header, selectType);
		layout.addMember(selectForm);
		
		selectType.addChangeHandler(new ChangeHandler() {  
            public void onChange(ChangeEvent event) {  
                String selectedItem = (String) event.getValue();  
				createNewItem(selectedItem);
            }  
        });
		
		return layout;
	}
	
	private void createNewItem(String type) {
		initGenericControls(type);
		horizontalButtons = new HLayout();
		horizontalButtons.setWidth100();
		IButton createButtton = new IButton("create");
		createButtton.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {  
            	if (giCanvas.validateForm()){
            	
            	JSONObject object = giCanvas.generateObject();
            	System.out.println(object);
            	
            	GeneralItemsClient.getInstance().createGeneralItem(object, new JsonCallback() {
        			public void onJsonReceived(JSONValue jsonValue) {
		            	System.out.println("create complete");
		            	removeGenericControls();
            		}
				});
            	}
            }  
        });  
		horizontalButtons.addMember(createButtton);
		layout.addMember(horizontalButtons);
	}
	
	private void editItem(final String type, int id) {
		
		GeneralItemsClient.getInstance().getGeneralItem(gameId, id, new JsonCallback() {
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
		            	GeneralItemsClient.getInstance().createGeneralItem(object, new JsonCallback() {
		        			public void onJsonReceived(JSONValue jsonValue) {
				            	System.out.println("update complete");
				            	removeGenericControls();
		            		}
						});
		            }  
		        });  
				IButton discardButton  = new IButton("discard");
				discardButton.addClickHandler(new ClickHandler() {  
		            public void onClick(ClickEvent event) {
		            	selectType.setDisabled(false);
		            	layout.removeChild(giCanvas);
		            	layout.removeChild(horizontalButtons);
		            	if (nonForm != null) layout.removeChild(nonForm);
		            }
				});
				horizontalButtons.addMember(createButtton);
				horizontalButtons.addMember(discardButton);
				layout.addMember(horizontalButtons);
			}
		});
		
	}
	
	private void deleteItem(int id) {
		 GeneralItemsClient.getInstance().deleteGeneralItem(gameId, id, new JsonCallback() {
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
		if (nonForm != null) {
			layout.removeChild(nonForm);
			nonForm.markForDestroy();
		}
	}
	
	private void initGenericControls(String type) {
		removeGenericControls();

		giCanvas = GeneralItemCanvas.getCanvas(type);
		giCanvas.setGameId((int) this.gameId);
		nonForm = giCanvas.getNonFormPart();
		
		layout.addMember(giCanvas);
		if (nonForm != null) {
			layout.addMember(nonForm);

		}
	}

	public Canvas getLeftCanvas() {
		listGrid = new GenericListGrid(true, true) {
			protected void deleteItem(ListGridRecord rollOverRecord) {
				System.out.println("click delete");
				GameTab.this.deleteItem(rollOverRecord.getAttributeAsInt("id"));
			}
			protected void editItem(ListGridRecord rollOverRecord) {
				GameTab.this.editItem(rollOverRecord.getAttribute("type"), rollOverRecord.getAttributeAsInt("id"));
			}
		};
        listGrid.setShowRollOverCanvas(true);

		listGrid.setWidth(500);
		listGrid.setHeight(224);
		listGrid.setShowAllRecords(true);
		listGrid.setDataSource(GeneralItemGameDataSource.getInstance());

		ListGridField titleGameField = new ListGridField("name", "Title");
		ListGridField typeField = new ListGridField("simpleName", "Type");

		listGrid.setFields(new ListGridField[] { titleGameField, typeField});
		listGrid.setCanResizeFields(true);

		listGrid.setPadding(5);
		listGrid.setWidth("100%");
		listGrid.setHeight("100%");
		listGrid.fetchData();

		return listGrid;
	}

}
