package org.celstec.arlearn2.gwt.client.ui;

import java.util.HashMap;
import java.util.Map;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MenuMapTypeControl;
import com.google.gwt.maps.client.event.MapDoubleClickHandler;
import com.google.gwt.maps.client.event.MarkerDoubleClickHandler;
import com.google.gwt.maps.client.event.MarkerDragEndHandler;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public abstract class MapTab extends Tab {

	private VerticalPanel mapContainerPanel;
	private MapWidget map;
	
	private static AuthoringConstants constants = GWT.create(AuthoringConstants.class);


	public MapTab(String title) {
		super(title);
		setCanClose(true);
		buildLayout();
	}

	public void buildLayout() {
		HLayout navLayout = new HLayout();
		navLayout.setMembersMargin(10);

		navLayout.setMembers(getControlPart(), getMapPart());
		this.setPane(navLayout);

	}

	protected abstract Canvas getControlPart();
	
	protected MapWidget getMap() {
		return map;
	}

	protected Canvas getMapPart() {
		Maps.loadMapsApi("AIzaSyAyNa-4zOAleXRkWRSTJLLkzyLLULS9Vxk", "2", false,
				new Runnable() {
					public void run() {
						buildMapUi();
					}
				});

		mapContainerPanel = new VerticalPanel();
		mapContainerPanel.setHeight("100%");
		mapContainerPanel.setWidth("100%");

		HLayout searchLayout = new HLayout();
		searchLayout.setWidth100();
		final DynamicForm form = new DynamicForm();  
		form.setWidth100();
		  
        TextItem textItem = new TextItem("location");
        textItem.setShowTitle(false);
        textItem.setColSpan(2);  
        textItem.setWidth("*");  
        form.setFields(textItem);
        Button button = new Button(constants.search());
        button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Geocoder geo = new Geocoder();
				geo.getLocations(form.getValueAsString("location"), new LocationCallback() {
					
					@Override
					public void onSuccess(JsArray<Placemark> locations) {
						if (locations.length() >0) {
							map.setCenter(locations.get(0).getPoint());
							Marker m = new Marker(locations.get(0).getPoint());
							map.addOverlay(m);
							form.setValue("location", "");
						}
						
					}
					
					@Override
					public void onFailure(int statusCode) {
						form.setValue("location", "");
					}
				});
			}
		});
        searchLayout.setMembers(form, button);
        
		VLayout vlayout = new VLayout();
		Canvas c = new Canvas();
		c.setPadding(2);
		c.setWidth100();
		c.setHeight100();
		c.setBorder("1px");
		c.setBackgroundColor("#22AAFF");

		c.addChild(mapContainerPanel);

		vlayout.setMembers(searchLayout, c);
		return vlayout;

	}


	private void buildMapUi() {
		map = new MapWidget();
		map.setSize("100%", "100%");
		map.addControl(new LargeMapControl());
		map.addControl(new MenuMapTypeControl());
		map.setDraggable(true);
		
		map.addMapDoubleClickHandler(new MapDoubleClickHandler() {
			
			@Override
			public void onDoubleClick(MapDoubleClickEvent event) {
				doubleClick(event.getLatLng());
			}

			
		});
		map.setZoomLevel(10);
		mapContainerPanel.add(map);
		mapLoaded();
	}
	private boolean positioned = false;
	public void addMarker(int id, String type, String name, double lat, double lng) {
		if (!markerMap.containsKey(id)) {
			Icon icon = Icon.newInstance("https://chart.googleapis.com/chart?chst=d_bubble_text_small&chld=bb|"+name+"|C6EF8C|000000");
			icon.setIconAnchor(Point.newInstance(-5, 40));

			MarkerOptions ops = MarkerOptions.newInstance();
			ops.setDraggable(true);
			ops.setIcon(icon);

			Marker m = new Marker(LatLng.newInstance(lat, lng), ops);

		
			
			if (!positioned) {
				map.setCenter(m.getLatLng());
				positioned = true;
			}

			m.addMarkerDragEndHandler(new MarkerDragEndHandler() {
				
				@Override
				public void onDragEnd(MarkerDragEndEvent event) {
					Marker draggedMarker = event.getSender();
					relocate(idMap.get(draggedMarker), event.getSender().getLatLng().getLatitude(), event.getSender().getLatLng().getLongitude());
				}
			});

			m.addMarkerDoubleClickHandler(new MarkerDoubleClickHandler() {
				
				@Override
				public void onDoubleClick(MarkerDoubleClickEvent event) {
					Marker clickedMarker = event.getSender();
					edit(idMap.get(clickedMarker));
					
				}
			});
			
			map.addOverlay(m);
			markerMap.put(id, m);
			idMap.put(m, id);
		}
		markerMap.get(id).setLatLng(LatLng.newInstance(lat, lng));

		
	}
	private HashMap<Integer, Marker> markerMap = new HashMap<Integer, Marker>();
	private HashMap<Marker, Integer> idMap = new HashMap<Marker, Integer>();

	public void removeAllMarkers() {
		// TODO Auto-generated method stub
		for (Map.Entry<Integer, Marker> entry : markerMap.entrySet() ){
			map.removeOverlay(entry.getValue());	
		}
		markerMap = new HashMap<Integer, Marker>();
		
		
	};
	
	public abstract void relocate(int id, double lat, double lng);
	public abstract void edit(int id);
	public abstract void mapLoaded();
	protected abstract void doubleClick(LatLng latLng) ;
}
