package org.celstec.arlearn2.portal.client.author.ui.gi.dependency;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.layout.HLayout;

public class AdvancedDependenciesEditor extends HLayout {

	private DependencyGrid depTreeGrid;
	private FromDependenciesGrid fromDep = new FromDependenciesGrid();
	private ActionForm actionForm = new ActionForm(true){
		public void onSave() {
			super.onSave();
			depTreeGrid.update();
		}
	};
	
	private ProximityDependencyEditor proxDep = new ProximityDependencyEditor() {
		public void onSave() {
			super.onSave();
			depTreeGrid.update();
		}
	};
	
	public AdvancedDependenciesEditor() {
		depTreeGrid = new DependencyGrid(actionForm, proxDep);
		addMember(fromDep);
		// addMember(moveControls);
		addMember(depTreeGrid);
		addMember(actionForm);
		addMember(proxDep);
		setHeight("*");
		setBorder("2px solid blue");
		
	}


	public JSONObject getJson() {
		return depTreeGrid.getJson();
	}


	public void loadJson(JSONObject object) {
		depTreeGrid.loadJson(object);
	}

	
}
