package org.celstec.arlearn2.gwt.client.ui;

import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.ui.modal.SelectTypeWindow;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class GameMapTab extends MapTab implements NotificationHandler {

	private long gameId;
	private GeneralItemControlCanvas controlItem;
	private boolean mapLoaded = false;
	
	public GameMapTab(String title, final long gameId) {
		super(title);
		this.gameId = gameId;
		
		controlItem.setGameId(gameId);
		addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				 loadExistingItems();
			}
		});
	}
	
	@Override
	public void onNotification(JSONObject bean) {
		
	}

	@Override
	protected Canvas getControlPart() {
		controlItem = new GeneralItemControlCanvas(gameId);
		controlItem.setModificationHandler(new GeneralItemControlCanvas.ItemModification() {
			
			@Override
			public void modified() {
				loadExistingItems();
				
			}
		});
		return controlItem;
	}
	
//	protected Canvas getMapPart() {
//		return new VStack();
//	}
	
	private void loadExistingItems() {
		if (mapLoaded) GeneralItemsClient.getInstance().getGeneralItemsGame(gameId, new JsonCallback() {
			
			public void onJsonReceived(JSONValue jsonValue) {
				if (jsonValue.isObject().containsKey("generalItems")) {
					super.jsonValue = jsonValue.isObject().get("generalItems").isArray();
				}
				onReceived();
			}
			public void onReceived(){
				removeAllMarkers();
				for (int i = 0; i < size(); i++) {
					Double lat = getAttributeDouble(i, "lat");
					Double lng = getAttributeDouble(i, "lng");
					int id = getAttributeInteger(i, "id");
 
					String type = getAttributeString(i, "type");
					String name = getAttributeString(i, "name");
					Boolean deleted = getAttributeBoolean(i, "deleted");
					if (deleted == null) deleted = false; 
					if (!deleted & (lat != null || lng != null)) addMarker(id, type, name, lat, lng);
				}
			}
			
		});
	}

	@Override
	public void relocate(int id, final double lat, final double lng) {
		GeneralItemsClient.getInstance().getGeneralItem(gameId, id,
				new JsonCallback() {
					public void onJsonReceived(JSONValue jsonValue) {
						controlItem.updateLocation(jsonValue.isObject(), lat, lng);
					}
		});
	}

	@Override
	public void edit(int id) {
		GeneralItemsClient.getInstance().getGeneralItem(gameId, id,
				new JsonCallback() {
					public void onJsonReceived(JSONValue jsonValue) {
						controlItem.edit(jsonValue);
					}
		});
		
	}

	@Override
	public void mapLoaded() {
		mapLoaded = true;
		loadExistingItems();
	}

	@Override
	protected void doubleClick(LatLng latLng) {
//		Window.alert("clicked at1 "+latLng.getLatitude()+ " " + latLng.getLongitude());
		SelectTypeWindow stw = new SelectTypeWindow(gameId, controlItem, latLng.getLatitude(), latLng.getLongitude());
		stw.show();
	}

}
