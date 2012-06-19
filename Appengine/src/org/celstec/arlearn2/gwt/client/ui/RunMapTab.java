package org.celstec.arlearn2.gwt.client.ui;

import java.util.HashMap;

import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MarkerDragEndHandler;
import com.google.gwt.maps.client.event.MarkerDragEndHandler.MarkerDragEndEvent;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.tab.Tab;

public class RunMapTab extends Tab implements NotificationHandler {

	private long runId;
	private long gameId;

	public RunMapTab(String title, final long runId, final long gameId) {
		super(title);
		this.runId = runId;
		this.gameId = gameId;
		setCanClose(true);
		buildLayout();
		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.run.LocationUpdate", RunMapTab.this);


	}

	public void buildLayout() {
		HLayout navLayout = new HLayout();
		navLayout.setMembersMargin(10);

		navLayout.setMembers(getControlPart(), getMapPart());
		this.setPane(navLayout);

	}

	private Canvas getControlPart() {
		final SectionStack sectionStack = new SectionStack();
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setWidth(300);
		return sectionStack;
	}

	private VerticalPanel vPanel2;

	private Canvas getMapPart() {
		Maps.loadMapsApi("AIzaSyAyNa-4zOAleXRkWRSTJLLkzyLLULS9Vxk", "2", false,
				new Runnable() {
					public void run() {
						buildMapUi();
					}
				});

		vPanel2 = new VerticalPanel();
		vPanel2.setHeight("100%");
		vPanel2.setWidth("100%");

		Canvas c = new Canvas();
		c.setWidth100();
		c.setHeight100();
		c.setBorder("1px");
		c.setBackgroundColor("#22AAFF");

		c.addChild(vPanel2);

		return c;

	}
	private MapWidget map;
	private void buildMapUi() {
		// Open a map centered on Cawker City, KS USA
		LatLng cawkerCity = LatLng.newInstance(39.509, -98.434);

		map = new MapWidget(cawkerCity, 2);
		map.setSize("100%", "100%");
		// Add some controls for the zoom level
		map.addControl(new LargeMapControl());

		// Add a marker
		MarkerOptions options = MarkerOptions.newInstance();
		options.setDraggable(true);

//		Marker m = new Marker(map.getCenter(), options);
//		m.setImage("yellow.png");
//		map.addOverlay(m);
		
//		LatLng ll = LatLng.newInstance(50.0, 5.0);
//		Icon icon = Icon.newInstance(Icon.DEFAULT_ICON);
//		icon.setImageURL("yellow.png");
//		ops.setIcon(icon);
//		Marker m = new Marker(ll, ops);
//		map.addOverlay(m);
		
		
		LatLng ll = LatLng.newInstance(51.0, 5.0);
		Icon icon = Icon.newInstance(Icon.DEFAULT_ICON);
		icon.setImageURL("images/black.png");
		icon.setIconSize(Size.newInstance(40, 40));
		MarkerOptions ops = MarkerOptions.newInstance(icon);

		ops.setIcon(icon);
		Marker m = new Marker(ll, ops);
		map.addOverlay(m);
		
	    
		
//		if (!GoogleMapsUtility.isLoaded(DefaultPackage.CONTEXT_MENU_CONTROL)) {
//			  GoogleMapsUtility.loadUtilityApi(new Runnable() {
//			    public void run() {
//			        LabeledMarkerOptions opts = LabeledMarkerOptions.newInstance();
////					opts.setIcon(icon);
//				    opts.setClickable(true);
//				    opts.setLabelOffset(Size.newInstance(-10, -6));
//				    opts.setLabelText("test");
//			        LabeledMarker m1 = new LabeledMarker(LatLng.newInstance(10, 10), opts);
//					map.addOverlay(m1);
//			    }
//			  }, DefaultPackage.CONTEXT_MENU_CONTROL);
//			}
		// Add an info window to highlight a point of interest
		map.getInfoWindow().open(m.getLatLng(), new InfoWindowContent("start..."));
		
		vPanel2.add(map);

	}

	@Override
	public void onNotification(JSONObject bean) {
		System.out.println(bean);
		updateUserLocation(bean.get("account").isString().stringValue(), bean.get("lat").isNumber().doubleValue(), bean.get("lng").isNumber().doubleValue());
//		LatLng ll = LatLng.newInstance(bean.get("lat").isNumber().doubleValue(), bean.get("lng").isNumber().doubleValue());
//		Marker m = new Marker(ll);
//		m.setImage("yellow.png");
//
//		map.addOverlay(m);
		
	}
	
	private void updateUserLocation(String user, double lat, double lng) {
		Marker userMarker = getMarker(user, lat, lng);
		userMarker.setLatLng(LatLng.newInstance(lat, lng));
		
	}
	
	private Marker getMarker(String account, double lat, double lng) {
		if (!markerMap.containsKey(account)) {
			Icon icon = Icon.newInstance(Icon.DEFAULT_ICON);
			icon.setImageURL("images/black.png");
			icon.setIconSize(Size.newInstance(30, 30));
			MarkerOptions ops = MarkerOptions.newInstance(icon);
			ops.setIcon(icon);
			Marker m = new Marker(LatLng.newInstance(lat, lng), ops);
			map.addOverlay(m);
			
			markerMap.put(account, m);
		}
		return markerMap.get(account);
	}
	
	private HashMap<String, Marker> markerMap = new HashMap<String, Marker>();
}
