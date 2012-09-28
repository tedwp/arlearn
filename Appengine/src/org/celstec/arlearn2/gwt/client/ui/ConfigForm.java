package org.celstec.arlearn2.gwt.client.ui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Vector;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.game.GameClient;
import org.celstec.arlearn2.gwt.client.ui.modal.RegionWindow;
import org.celstec.arlearn2.gwt.client.ui.modal.RoleWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.DoubleClickEvent;
import com.smartgwt.client.widgets.form.fields.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverEvent;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;

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
		simpleForm.setFields(mapAvailable, mapType, offlineZones);
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
		mapType.setMultipleAppearance(MultipleAppearance.GRID);
		// mapType.setValueMap(new String[] {"Google Maps", "Open Street Map"});
		mapType.setValueMap(getMapTypes());
		mapType.setRedrawOnChange(true);

		mapType.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {  
                return mapTypeVisible(form);  
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
	
	private boolean mapTypeVisible(DynamicForm form) {
		return form.getValue("mapAvailable")!= null &&((Boolean) form.getValue("mapAvailable"));
	}

	private static final String OPENSTREETMAPS ="Open Street Maps";
	private static final String GOOGLEMAPS = "Google Maps" ;
	public LinkedHashMap<String, String> getMapTypes() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("0", GOOGLEMAPS);
		valueMap.put("1", OPENSTREETMAPS);
		System.out.println(valueMap);
		return valueMap;
	}

	private void setOfflineZonesItem() {
		offlineZones = new SelectItem();
		offlineZones.setName("offlineZones");
		offlineZones.setTitle(constants.offlineMaps());

		offlineZones.setMultiple(true);
		offlineZones.setHeight(50);
		offlineZones.setMultipleAppearance(MultipleAppearance.GRID);
		offlineZones.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {  
                return mapTypeVisible(form) && form.getValue("mapType")!= null &&(form.getValueAsString("mapType").equals("1"));  
            }
        });  
		offlineZones.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				RegionWindow rw = new RegionWindow(ConfigForm.this);
				rw.show();
				
			}
		});
//		offlineZones.addKeyPressHandler(new KeyPressHandler() {
//			
//			@Override
//			public void onKeyPress(KeyPressEvent event) {
//				System.out.println(""+event);
//				System.out.println(""+event.getKeyName() );
//				System.out.println(""+offlineZones.getSelectedRecords());
//			}
//		});
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
		
		if (jsonValue.containsKey("mapRegions")) {
			JSONArray regionsArray = jsonValue.get("mapRegions").isArray();
			
			for (int i = 0; i< regionsArray.size(); i++) {
				JSONObject region = regionsArray.get(i).isObject();
				Region r = new Region();
				r.setLatUp(region.get("latUp").isNumber().doubleValue());
				r.setLatDown(region.get("latDown").isNumber().doubleValue());
				r.setLngLeft(region.get("lngLeft").isNumber().doubleValue());
				r.setLngRight(region.get("lngRight").isNumber().doubleValue());
				r.setMinZoom((int )region.get("minZoom").isNumber().doubleValue());
				r.setMaxZoom((int )region.get("maxZoom").isNumber().doubleValue());
				regions.put(r.toString(),r);
				Set<String> keySet = regions.keySet();
				offlineZones.setValueMap(keySet.toArray(new String[keySet.size()]));
			}
		}
		simpleForm.redraw();
		
	}
	public void addRegion(Region r) {
		regions.put(r.toString(),r);
		Set<String> keySet = regions.keySet();
		offlineZones.setValueMap(keySet.toArray(new String[keySet.size()]));
		JSONArray array = new JSONArray();
		int index = 0;
		for (String key: keySet) {
			array.set(index++, regions.get(key).toJson());
		}
		GameClient.getInstance().addMapRegion(gameTab.getGameId(), array, new JsonCallback() {
			public void onJsonReceived(JSONValue jsonValue) {
				gameTab.updateConfig(jsonValue.isObject().get("config").isObject());
			};
		});

	}

	private HashMap<String, Region> regions = new HashMap<String, Region>();
	
	public class Region {
		
		private double latUp;
		private double latDown;
		private double lngLeft;
		private double lngRight;
		
		private int minZoom;
		private int maxZoom;
		
		public Region() {
		}

		public double getLatUp() {
			return latUp;
		}

		public void setLatUp(double latUp) {
			this.latUp = latUp;
		}

		public double getLatDown() {
			return latDown;
		}

		public void setLatDown(double latDown) {
			this.latDown = latDown;
		}

		public double getLngLeft() {
			return lngLeft;
		}

		public void setLngLeft(double lngLeft) {
			this.lngLeft = lngLeft;
		}

		public double getLngRight() {
			return lngRight;
		}

		public void setLngRight(double lngRight) {
			this.lngRight = lngRight;
		}

		public int getMinZoom() {
			return minZoom;
		}

		public void setMinZoom(int minZoom) {
			this.minZoom = minZoom;
		}

		public int getMaxZoom() {
			return maxZoom;
		}

		public void setMaxZoom(int maxZoom) {
			this.maxZoom = maxZoom;
		}
		
		public String toString() {
			return "("+latUp+","+lngLeft+")..("+latDown+","+lngRight+") ("+minZoom+"-"+maxZoom+")";
		}
		
		public JSONObject toJson() {
			JSONObject json = new JSONObject();
			json.put("type", new JSONString("org.celstec.arlearn2.beans.game.MapRegion"));
			json.put("latUp", new JSONNumber(getLatUp()));
			json.put("latDown", new JSONNumber(getLatDown()));
			json.put("lngLeft", new JSONNumber(getLngLeft()));
			json.put("lngRight", new JSONNumber(getLngRight()));
			
			json.put("minZoom", new JSONNumber(getMinZoom()));
			json.put("maxZoom", new JSONNumber(getMaxZoom()));
			return json;
		}
		
		
	}
}
