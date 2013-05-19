package org.celstec.arlearn2.portal.client.author.ui.gi.dependency;

import org.celstec.arlearn2.portal.client.author.ui.generic.maps.MapWidget;

import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.maps.gwt.client.Circle;
import com.google.maps.gwt.client.CircleOptions;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.DragEndHandler;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ProximityDependencyEditor extends VLayout {

	protected Slider radiusSlider;
	protected MapWidget map;

	protected CircleOptions populationOptions;
	protected Circle circle;

	protected LatLng location;
	
	private TreeNode proxTreeNode;
	private Tree proxTree;
	
	public ProximityDependencyEditor() {
		
		createRadiusSlider();
		radiusSlider.addValueChangedHandler(new ValueChangedHandler() {
			public void onValueChanged(ValueChangedEvent event) {
				int value = event.getValue();
				if (circle != null) circle.setRadius(value);
//				MapWidget.getInstance().forceParentLayout(MapWidget.getInstance().getParent());

			}
		});

		addMember(radiusSlider);
		
//		setWidth100();
		ProximityDependencyEditor.this.setVisibility(Visibility.HIDDEN);

	}
	
	public void createRadiusSlider(){
		radiusSlider = new Slider("radius");
		radiusSlider.setVertical(false);
		radiusSlider.setVertical(false);
		radiusSlider.setMinValue(10);
		radiusSlider.setMaxValue(100);
		radiusSlider.setWidth100();

	}

	public void setLocation(LatLng location) {
		this.location = location;
		MarkerOptions newMarkerOpts = MarkerOptions.create();
		newMarkerOpts.setPosition(location);
		newMarkerOpts.setMap(map.getMap());
		newMarkerOpts.setDraggable(true);
		
		
		Marker marker = Marker.create(newMarkerOpts);
		marker.addDragEndListener(new DragEndHandler() {
			
			@Override
			public void handle(MouseEvent event) {
				ProximityDependencyEditor.this.location = event.getLatLng();
				circle.setCenter(ProximityDependencyEditor.this.location);
				
			}
		});

		populationOptions = CircleOptions.create();
		populationOptions.setStrokeColor("#ff0000");
		populationOptions.setStrokeOpacity(0.8);
		populationOptions.setStrokeWeight(2);
		populationOptions.setFillColor("#ff0000");
		populationOptions.setFillOpacity(0.35);
		populationOptions.setMap(map.getMap());
		populationOptions.setCenter(location);
		populationOptions.setRadius(5);
		circle = Circle.create(populationOptions);
	}
	
	public void onSave() {
		if (proxTreeNode == null)
			return;
		
		for (TreeNode tn : proxTree.getChildren(proxTreeNode)) {
			if (tn.getAttribute("type").equals(ProximityDependencyNode.LAT)) {
				tn.setAttribute("Name", "Lat = " + location.lat());
				tn.setAttribute("Value", ""+location.lat());
			}
			if (tn.getAttribute("type").equals(ProximityDependencyNode.LNG)) {
				tn.setAttribute("Name", "Lng = " + location.lng());
				tn.setAttribute("Value", ""+location.lng());
			}
			if (tn.getAttribute("type").equals(ProximityDependencyNode.RADIUS)) {
				tn.setAttribute("Name", "Radius = " + radiusSlider.getValue());
				tn.setAttribute("Value", ""+radiusSlider.getValue());
			}
		}

		proxTreeNode = null;
	}
	
	public void showProximityDependencyNode() {
		map = MapWidget.getInstance();
		addMember(map);
		ProximityDependencyEditor.this.setVisibility(Visibility.INHERIT);

	}

	public void hideProximityDependencyNode() {
		ProximityDependencyEditor.this.setVisibility(Visibility.HIDDEN);

	}
	
	/**
	 * @param tn
	 * @param tree
	 */
	public void setTreeNode(TreeNode tn, Tree tree) {
		proxTreeNode = tn;
		proxTree = tree;

	}

}
