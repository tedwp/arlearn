package org.celstec.arlearn2.gwt.client.ui;

import java.util.LinkedHashMap;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.game.GameClient;
import org.celstec.arlearn2.gwt.client.ui.modal.RoleWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverEvent;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverHandler;

public class ConfigForm {

	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);
	GameTab gameTab;

	private SelectItem mapType;
	private CheckboxItem mapAvailable;
	private SelectItem offlineZones;
	private DynamicForm simpleForm = new DynamicForm();

	public ConfigForm(GameTab gt) {
		setMapAvailableItem();
		setMapTypeItem();
		setOfflineZonesItem();
		this.gameTab = gt;

	}

	public DynamicForm getSimpleConfiguration() {
		simpleForm.setFields(mapAvailable, mapType);
		return simpleForm;
	}

	private void setMapAvailableItem() {
		mapAvailable = new CheckboxItem();
		mapAvailable.setName("mapAvailable");
		mapAvailable.setStartRow(true);
		mapAvailable.setTitle(constants.withMap());
		mapAvailable.addItemHoverHandler(new ItemHoverHandler() {
			@Override
			public void onItemHover(ItemHoverEvent event) {
				mapAvailable.setPrompt(constants.instructMap());
			}
		});
		mapAvailable.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				GameClient.getInstance().setWithMap(gameTab.getGameId(), (Boolean) event.getValue(), new JsonCallback() {
					public void onJsonReceived(JSONValue jsonValue) {
						gameTab.updateConfig(jsonValue.isObject().get("config").isObject());
					};
				});
			}
		});
		mapAvailable.setRedrawOnChange(true);
	}

	private void setMapTypeItem() {
		mapType = new SelectItem();
		mapType.setName("mapType");
		mapType.setTitle(constants.mapsType());

		mapType.setMultiple(true);
		// mapType.setHeight(50);
		mapType.setMultipleAppearance(MultipleAppearance.PICKLIST);
		// mapType.setValueMap(new String[] {"Google Maps", "Open Street Map"});
		mapType.setValueMap(getMapTypes());
		mapType.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {  
                return form.getValue("mapAvailable")!= null &&((Boolean) form.getValue("mapAvailable"));  
            }
        });  
		mapType.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				GameClient.getInstance().setMapType(gameTab.getGameId(), Integer.parseInt("" + event.getValue()), new JsonCallback() {
					public void onJsonReceived(JSONValue jsonValue) {
						gameTab.updateConfig(jsonValue.isObject().get("config").isObject());
					};
				});
			}
		});
	}

	public LinkedHashMap<String, String> getMapTypes() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("0", "Google Maps");
		valueMap.put("1", "Open Street Maps");
		return valueMap;
	}

	private void setOfflineZonesItem() {
		offlineZones = new SelectItem();
		offlineZones.setName("offlineZones");
		offlineZones.setTitle("i18 -Offline zones");

		offlineZones.setMultiple(true);
		offlineZones.setHeight(50);
		offlineZones.setMultipleAppearance(MultipleAppearance.GRID);
		// offlineZones.setValueMap(new String[] {"Google Maps",
		// "Open Street Map"});
	}

	public void updateConfig(JSONObject jsonValue) {
		boolean mapAvailableBoolean = true;
		if (jsonValue.containsKey("mapAvailable")) {
			mapAvailableBoolean  = jsonValue.get("mapAvailable").isBoolean().booleanValue();
		}
		mapAvailable.setValue(mapAvailableBoolean);
		String mapType = "0";
		if (jsonValue.containsKey("mapType")) {
			mapType  = ""+((int) jsonValue.get("mapType").isNumber().doubleValue());
		}
		simpleForm.setValue("mapType", mapType);
		simpleForm.redraw();
		
	}
}
