package org.celstec.arlearn2.gwt.client.ui.items;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class OpenBadgeCanvas  extends GeneralItemCanvas {
	
	protected DynamicForm form1 = new DynamicForm();
	protected TextItem badgeUrlItem;
	private final String BADGE_URL = "badgeUrl";
	
	protected TextItem descriptionItem;
	private final String DESCRIPTION = "description";
	
	protected TextItem evidenceItem;
	private final String EVIDENCE = "evidence";

	protected TextItem badgePngItem;
	private final String IMAGE = "image";
	
	public OpenBadgeCanvas(String roles[]) {
		super(roles);
		initComponents();
		doLayout();
	}
	protected void initComponents() {
		super.initComponents();
		createDescription();
		createBadgeUrl();
		createImage();
		createEvidence();
	}
	
	protected void doLayout() {
		doLayoutForm1();
	}
	
	protected void doLayoutForm1() {
		this.addMember(form1);
		form1.setFields(idItem, gameIdItem, nameItem, sortItem, descriptionItem, badgePngItem, badgeUrlItem, evidenceItem, isSimpleDependency, selectAction, selectScope, selectRole, selectGeneralItem, isAutolaunch, roleGrid);
		addField(form1,idItem, gameIdItem, nameItem, sortItem, descriptionItem, badgePngItem, badgeUrlItem, evidenceItem, isSimpleDependency, selectAction, selectScope, selectRole, selectGeneralItem, isAutolaunch, roleGrid);
	}
	
	protected void createBadgeUrl() {
		badgeUrlItem = new TextItem(BADGE_URL);
		badgeUrlItem.setTitle("Badge Url - i18");
		badgeUrlItem.setSelectOnFocus(true);
		badgeUrlItem.setWrapTitle(false);
	}
	
	protected void createDescription() {
		descriptionItem = new TextItem(DESCRIPTION);
		descriptionItem.setTitle("Badge Description - i18");
		descriptionItem.setSelectOnFocus(true);
		descriptionItem.setWrapTitle(false);
	}
	
	private void createEvidence() {
		evidenceItem = new TextItem(EVIDENCE);
		evidenceItem.setTitle("Evidence");
		evidenceItem.setSelectOnFocus(true);
		evidenceItem.setWrapTitle(false);
		
	}
	private void createImage() {
		badgePngItem = new TextItem(IMAGE);
		badgePngItem.setTitle("Image");
		badgePngItem.setSelectOnFocus(true);
		badgePngItem.setWrapTitle(false);
		
	}
	
	public JSONObject generateObject() {
		JSONObject object = super.generateObject();
		object.put("type", new JSONString("org.celstec.arlearn2.beans.generalItem.OpenBadge"));

		putStringValue(object, BADGE_URL);
		putStringValue(object, DESCRIPTION);
		putStringValue(object, IMAGE);
		putStringValue(object, EVIDENCE);
		return object;
	}
	
	public void setItemValues(JSONValue jsonValue) {
		super.setItemValues(jsonValue);
		
		JSONObject o = jsonValue.isObject();
		setValueString(BADGE_URL, o);
		setValueString(DESCRIPTION, o);
		setValueString(IMAGE, o);
		setValueString(EVIDENCE, o);
		
		form1.redraw();

	}
	@Override
	public boolean validateForm() {
		return form1.validate();
	}
	
}

