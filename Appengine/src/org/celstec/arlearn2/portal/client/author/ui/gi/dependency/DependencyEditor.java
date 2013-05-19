package org.celstec.arlearn2.portal.client.author.ui.gi.dependency;


import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class DependencyEditor extends VLayout {

	protected HLayout buttonLayout;
	protected IButton advButton;

	protected AdvancedDependenciesEditor advEditor;
	protected ActionForm simpleEditor;

	public DependencyEditor() {
		createAdvDepButton();
		simpleEditor = new ActionForm(false);
		simpleEditor.showActionForm();
//		Canvas c = new Canvas();
//		c.addChild(simpleEditor);
		addMember(simpleEditor);
		createButtonLayout(advButton);
		addMember(buttonLayout);
	}

	private void createButtonLayout(IButton... buttons) {
		buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.setHeight(40);
		for (IButton but : buttons) {
			buttonLayout.addMember(but);
		}

	}

	private void createAdvDepButton() {
		advButton = new IButton("advanced");
		advButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				toggle(true);
			}
		});
	}

	public void toggle(boolean loadDep) {
		if (advEditor == null) {
			removeMember(simpleEditor);
			JSONObject dep = simpleEditor.getJsonObject();
			simpleEditor.destroy();
			simpleEditor = null;
			advEditor = new AdvancedDependenciesEditor();
			addMember(advEditor,0);
			advButton.setTitle("simple");
			advEditor.setHeight100();
			if (loadDep && dep !=null) advEditor.loadJson(dep);
		} else {
			removeMember(advEditor);
			advEditor.destroy();
			JSONObject dep = advEditor.getJson();
			advEditor = null;
			simpleEditor = new ActionForm(false);
			simpleEditor.showActionForm();
			simpleEditor.setHeight100();
			addMember(simpleEditor,0);
			advButton.setTitle("advanced");
			if (loadDep &&  dep.get("type").isString().stringValue().equals(ActionDependencyNode.DEP_TYPE)) {
				simpleEditor.loadJson(dep);
			}
			
		}
	}

	public JSONObject getJson() {
		if (advEditor != null) {
			return advEditor.getJson();
		}
		return null;
	}

	public void loadGeneralItem(JSONObject object) {
		if (!object.get("type").isString().stringValue().equals(ActionDependencyNode.DEP_TYPE)) {
			toggle(false);
		}
		if (advEditor != null) {
			advEditor.loadJson(object);
		} else {
			simpleEditor.loadJson(object);
		}
		
	}

}
