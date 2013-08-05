/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.gwt.client.ui;

import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.game.GameClient;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemGameDataSource;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwt.client.network.team.GameTeamDataSource;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.ui.modal.SelectTypeWindow;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
//import com.google.gwt.maps.client.geom.LatLng;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class GameMapTab extends MapTab implements NotificationHandler {

	@Override
	public void onNotification(JSONObject bean) {
		// TODO Auto-generated method stub
		
	}

//	private long gameId;
//	private GeneralItemControlCanvas controlItem;
//	private boolean mapLoaded = false;
//	
//	public GameMapTab(String title, final long gameId) {
//		super(title);
//		this.gameId = gameId;
//		
////		SelectTypeWindow stw = new SelectTypeWindow(gameId, controlItem, 50.0d, 10.0d);
////		stw.show();
//		
//		controlItem.setGameId(gameId);
//		addTabSelectedHandler(new TabSelectedHandler() {
//
//			@Override
//			public void onTabSelected(TabSelectedEvent event) {
//				 loadExistingItems();
//			}
//		});
//	}
//	
//	@Override
//	public void onNotification(JSONObject bean) {
//		
//	}
//
//	@Override
//	protected Canvas getControlPart() {
//		controlItem = new GeneralItemControlCanvas(gameId);
//		controlItem.setModificationHandler(new GeneralItemControlCanvas.ItemModification() {
//			
//			@Override
//			public void modified() {
//				loadExistingItems();
//				
//			}
//		});
//		return controlItem;
//	}
//	
//	private void loadExistingItems() {
//		if (mapLoaded) GeneralItemsClient.getInstance().getGeneralItemsGame(gameId, new JsonCallback() {
//			
//			public void onJsonReceived(JSONValue jsonValue) {
//				if (jsonValue.isObject().containsKey("generalItems")) {
//					super.jsonValue = jsonValue.isObject().get("generalItems").isArray();
//				}
//				onReceived();
//			}
//			public void onReceived(){
//				removeAllMarkers();
//				for (int i = 0; i < size(); i++) {
//					Double lat = getAttributeDouble(i, "lat");
//					Double lng = getAttributeDouble(i, "lng");
//					int id = getAttributeInteger(i, "id");
// 
//					String type = getAttributeString(i, "type");
//					String name = getAttributeString(i, "name");
//					Boolean deleted = getAttributeBoolean(i, "deleted");
//					if (deleted == null) deleted = false; 
//					if (!deleted & (lat != null || lng != null)) addMarker(id, type, name, lat, lng);
//				}
//			}
//			
//		});
//		
//		GameTeamDataSource.getInstance().loadDataGame(gameId);
//		
//		GameClient.getInstance().getGameConfig(gameId, new JsonCallback() {
//
//			public void onJsonReceived(JSONValue jsonValue) {
//
////				updateConfig(jsonValue.isObject());
//				GeneralItemGameDataSource.getInstance().loadDataGame(gameId);
//
//			}
//
//		});
//	}
//
//	@Override
//	public void relocate(int id, final double lat, final double lng) {
//		GeneralItemsClient.getInstance().getGeneralItem(gameId, id,
//				new JsonCallback() {
//					public void onJsonReceived(JSONValue jsonValue) {
//						controlItem.updateLocation(jsonValue.isObject(), lat, lng);
//					}
//		});
//	}
//
//	@Override
//	public void edit(int id) {
//		GeneralItemsClient.getInstance().getGeneralItem(gameId, id,
//				new JsonCallback() {
//					public void onJsonReceived(JSONValue jsonValue) {
//						controlItem.edit(jsonValue);
//					}
//		});
//		
//	}
//
//	@Override
//	public void mapLoaded() {
//		mapLoaded = true;
//		loadExistingItems();
//	}

//	@Override
//	protected void doubleClick(LatLng latLng) {
//		SelectTypeWindow stw = new SelectTypeWindow(gameId, controlItem, latLng.getLatitude(), latLng.getLongitude());
//		stw.show();
//	}

}
