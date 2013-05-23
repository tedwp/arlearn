package org.celstec.arlearn2.portal.client.author.ui.gi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.author.ui.generic.maps.MapWidget;

import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.DragEndHandler;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.layout.VLayout;


public class GeneralItemMapView extends VLayout{
	private Game game;
	MapWidget mapWidget;
	
	public GeneralItemMapView() {
	}
	
	public MapWidget getMapWidget() {
		return mapWidget;
	}

	public void tabSelected() {
		if (mapWidget == null) {
			mapWidget = MapWidget.getInstance();
			addMember(mapWidget);
		}
		loadGeneralItems();
		
	}
	
	public void loadGeneralItems() {
		Criteria criteria = new Criteria();
		criteria.addCriteria(GameModel.GAMEID_FIELD, game.getGameId());
//		GeneralItemDataSource.getInstance().fetchData(criteria, new DSCallback() {
//			
//			@Override
//			public void execute(DSResponse response, Object rawData, DSRequest request) {
//				String title = response.getAttributeAsString(GeneralItemModel.NAME_FIELD);
////				String title = response.getAttributeAsString(rawData.getClass());
//				System.out.println("title "+title+ " "+rawData.getClass()+" "+rawData);
////				rawData
//				
//			}
//		});
		HashMap<Long, Record> records = GeneralItemDataSource.getInstance().getRecords(game.getGameId());
			for (Map.Entry<Long,Record> rec: records.entrySet()){
			addMarker(rec.getValue().getAttributeAsLong(GeneralItemModel.GENERALITEMID_FIELD),  
					rec.getValue().getAttributeAsString(GeneralItemModel.NAME_FIELD), 
					rec.getValue().getAttributeAsDouble(GeneralItemModel.LAT_FIELD),
					rec.getValue().getAttributeAsDouble(GeneralItemModel.LNG_FIELD),
					recordToGeneralItem(rec.getValue()));
		}
	}

	public void setGame(Game g) {
		this.game = g;
	}
	
	private HashMap<Long, Marker> markerMap = new HashMap<Long, Marker>();
	
	public void addMarker(final Long id, String name, Double lat, Double lng, final GeneralItem gi) {
		if (lat == null) lat = 0d;
		if (lng == null) lng = 0d;
	if (!markerMap.containsKey(id)) {
		  MapOptions myOptions = MapOptions.create();
		    myOptions.setZoom(4.0);
		    myOptions.setCenter(LatLng.create(-25.363882, 131.044922));
		    myOptions.setMapTypeId(MapTypeId.ROADMAP);

		    MarkerOptions newMarkerOpts = MarkerOptions.create();
		    newMarkerOpts.setPosition(LatLng.create(lat, lng));
		    newMarkerOpts.setMap(mapWidget.getMap());
		    newMarkerOpts.setIcon("https://chart.googleapis.com/chart?chst=d_bubble_text_small&chld=bb|"+name+"|C6EF8C|000000");
		    newMarkerOpts.setDraggable(true);
		    
		    Marker marker =  Marker.create(newMarkerOpts);
		    marker.addDragEndListener(new DragEndHandler() {
				
				@Override
				public void handle(MouseEvent arg0) {
					LatLng latlng = arg0.getLatLng();
					gi.setDouble(GeneralItemModel.LAT_FIELD	, latlng.lat());
					gi.setDouble(GeneralItemModel.LNG_FIELD	, latlng.lng());
					gi.writeToCloud(new JsonCallback());
				}
			});
		    markerMap.put(id, marker);
	}
	
	
//		    
//		m.addMarkerDragEndHandler(new MarkerDragEndHandler() {
//			
//			@Override
//			public void onDragEnd(MarkerDragEndEvent event) {
//				Marker draggedMarker = event.getSender();
//				relocate(idMap.get(draggedMarker), event.getSender().getLatLng().getLatitude(), event.getSender().getLatLng().getLongitude());
//			}
//		});
//
//		m.addMarkerDoubleClickHandler(new MarkerDoubleClickHandler() {
//			
//			@Override
//			public void onDoubleClick(MarkerDoubleClickEvent event) {
//				Marker clickedMarker = event.getSender();
//				edit(idMap.get(clickedMarker));
//				
//			}
//		});
//		
//		map.addOverlay(m);
//		markerMap.put(id, m);
//		idMap.put(m, id);
//	}
//	markerMap.get(id).setLatLng(LatLng.newInstance(lat, lng));

	
}
	
	private GeneralItem recordToGeneralItem(Record record) {
		return GeneralItem.createObject(((AbstractRecord) GeneralItemDataSource.getInstance().getRecord(record.getAttributeAsLong(GeneralItemModel.ID_FIELD))).getCorrespondingJsonObject());
	}

}
