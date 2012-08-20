package org.celstec.arlearn2.gwt.client.ui;

import java.util.LinkedHashMap;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwt.client.ui.items.GeneralItemCanvas;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VStack;

public class GeneralItemControlCanvas extends VStack {

	private DynamicForm selectForm;
	private SelectItem selectType;
	private GeneralItemCanvas giCanvas;
	HLayout formButtonsLayout;
	private long gameId;
	private String[] roleValues;
	
	private ItemModification modificationHandler;

	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	private ChangeHandler selectTypeChangeHandler = new ChangeHandler() {
		public void onChange(ChangeEvent event) {
			String selectedItem = (String) event.getValue();
			createNewItem(selectedItem);
			event.getForm().resetValues();
		}
	};

	public GeneralItemControlCanvas(long gameId) {
		this.gameId = gameId;
		setBorder("1px solid gray");
		setWidth(300);
		createSelectForm();
	}
	
	public void setModificationHandler(ItemModification im) {
		this.modificationHandler = im;
	}
	
	public void setGameId(long gameId){
		this.gameId = gameId;
	}

	public String[] getRoleValues() {
		return roleValues;
	}

	public void setRoleValues(String[] roleValues) {
		this.roleValues = roleValues;
	}

	private void createSelectForm() {
		selectForm = new DynamicForm();

		HeaderItem header = new HeaderItem();
		header.setDefaultValue(constants.createNewGeneralItem());

		selectType = new SelectItem();
		selectType.setName("selectGi");
		selectType.setTitle(constants.selectType());
		selectType.setValueMap(getItemValues());
		selectType.setWrapTitle(false);

		selectType.addChangeHandler(selectTypeChangeHandler);

		selectForm.setFields(header, selectType);
		addMember(selectForm);
	}

	public void createNewItem(String type) {
		initGeneralItemPane(type);
		initFormButtonsCreate();
	}

	private void initGeneralItemPane(String type) {
		removeGenericControls();

		giCanvas = GeneralItemCanvas.getCanvas(type, roleValues);

		giCanvas.setGameId((int) this.gameId);
		giCanvas.setWidth100();

		addMember(giCanvas);
	}

	private void initFormButtonsCreate() {
		

		IButton createButton = new IButton(constants.create());
		createButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (giCanvas.validateForm()) {

					JSONObject object = giCanvas.generateObject();

					GeneralItemsClient.getInstance().createGeneralItem(object, new JsonCallback() {
						public void onJsonReceived(JSONValue jsonValue) {
							// loadRoles();
							removeGenericControls();
							if (modificationHandler != null) modificationHandler.modified();
						}
					});
				}
			}
		});

		IButton cancelButton = new IButton(constants.cancel());
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeGenericControls();
			}
		});
		initFormButtons(createButton, cancelButton);
	}

	private void initFormButtonsEdit() {
		IButton saveButton = new IButton(constants.save());
		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				JSONObject object = giCanvas.generateObject();

				GeneralItemsClient.getInstance().createGeneralItem(object, new JsonCallback() {
					public void onJsonReceived(JSONValue jsonValue) {
						if (modificationHandler != null) modificationHandler.modified();
						selectType.setDisabled(false);
						removeGenericControls();
					}
				});
			}
		});
		IButton discardButton = new IButton(constants.discard());
		discardButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				selectType.setDisabled(false);
				removeGenericControls();
			}
		});
		initFormButtons(saveButton, discardButton);
	}

	private void initFormButtons(IButton saveCreateButton, IButton cancelButton) {
		formButtonsLayout = new HLayout();
		formButtonsLayout.setAlign(Alignment.CENTER);
		formButtonsLayout.setLayoutMargin(6);
		formButtonsLayout.setMembersMargin(6);
		formButtonsLayout.setHeight(20);
		formButtonsLayout.setWidth100();
		formButtonsLayout.addMember(saveCreateButton);
		formButtonsLayout.addMember(cancelButton);
		addMember(formButtonsLayout);
	}

	private void removeGenericControls() {
		if (giCanvas != null) {
			removeChild(giCanvas);
			giCanvas.markForDestroy();
		}
		if (formButtonsLayout != null) {
			removeChild(formButtonsLayout);
			formButtonsLayout.markForDestroy();
		}
	}

	public LinkedHashMap<String, String> getItemValues() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("org.celstec.arlearn2.beans.generalItem.NarratorItem", "Narrator Item");
		valueMap.put("org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest", "Multiple Choice");
		valueMap.put("org.celstec.arlearn2.beans.generalItem.VideoObject", "Video Object");
		valueMap.put("org.celstec.arlearn2.beans.generalItem.YoutubeObject", "Youtube movie");
		valueMap.put("org.celstec.arlearn2.beans.generalItem.AudioObject", "Audio Object");
		return valueMap;
	}

	public void edit(JSONValue jsonValue) {
		initGeneralItemPane(jsonValue.isObject().get("type").isString().stringValue());
		selectType.setDisabled(true);
		giCanvas.setItemValues(jsonValue);
		initFormButtonsEdit();

	}

	public void updateLocation(JSONObject jsonValue, double lat, double lng) {
		jsonValue.isObject().put("lat", new JSONNumber(lat));
		jsonValue.isObject().put("lng", new JSONNumber(lng));
		GeneralItemsClient.getInstance().createGeneralItem(jsonValue, new JsonCallback() {
			public void onJsonReceived(JSONValue jsonValue) {
				removeGenericControls();
			}
		});
		
	}

	public void setLocation(double lat, double lng) {
		giCanvas.setLocation(lat, lng);
		
	}
	
	public interface ItemModification {
		public void modified();
	}

}
