package org.celstec.arlearn2.gwt.client.ui.items;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.taskdefs.Cvs;


import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.IsFloatValidator;

public abstract class GeneralItemCanvas extends Canvas{
	
	protected DynamicForm form = new DynamicForm();
	private HiddenItem idItem;
	private HiddenItem gameIdItem;
	private TextItem nameItem;
	private SelectItem selectScope;
	private TextItem radiusItem;
	private TextItem latItem;
	private TextItem lngItem;
	private TextAreaItem dependencyField;
	private String dependency = null;

	
	public GeneralItemCanvas () {
		
		form.setFields(getFields());
		addSpecificFields();
		this.addChild(form);
		cv.setErrorMessage("an empty value is not allowed");
	}
	
	public FormItem[] getFields() {
		ArrayList<FormItem> array = new ArrayList<FormItem>();

		idItem = new HiddenItem("id");
		array.add(idItem);
		
		gameIdItem = new HiddenItem("gameId");
		array.add(gameIdItem);
		
		nameItem = new TextItem("name");
		nameItem.setTitle("Item name");
		nameItem.setSelectOnFocus(true);
		nameItem.setWrapTitle(false);
		nameItem.setValidators(cv);
		array.add(nameItem);
		
		
		
		selectScope = new SelectItem();
		selectScope.setTitle("Select scope");
		selectScope.setValueMap("user", "team", "all");
		selectScope.setValidators(cv);
		array.add(selectScope);
		
		radiusItem = new TextItem("radius");
		radiusItem.setTitle("Radius");
		radiusItem.setSelectOnFocus(true);
		radiusItem.setWrapTitle(false);
//		radiusItem.setHint("Specifies the distance in meters. Only when a users is within that distance from the object, the item will become visible.");
		array.add(radiusItem);
		
		IsFloatValidator floatValidator = new IsFloatValidator();
		floatValidator.setErrorMessage("This is not a correct value");
		latItem = new TextItem("lat");
		latItem.setTitle("Latitude");
		latItem.setSelectOnFocus(true);
		latItem.setWrapTitle(false);
		latItem.setValidators(floatValidator);
		array.add(latItem);
		
		lngItem = new TextItem("lng");
		lngItem.setTitle("Longitude");
		lngItem.setSelectOnFocus(true);
		lngItem.setWrapTitle(false);
		array.add(lngItem);

		dependencyField = new TextAreaItem();  
		dependencyField.setTitle("Plain Text");
		dependencyField.setName("plainText");
		dependencyField.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {  
                return dependency != null;  
            }

        });  
		
		array.add(dependencyField);
		
		array.addAll(addSpecificFields());
		return array.toArray(new FormItem[]{});
	}
	
	public abstract List<FormItem>  addSpecificFields();
	public abstract Canvas  getNonFormPart();
	
	public void setGameId(int gameId) {
		gameIdItem.setValue((int) gameId);
	}
	
	public boolean validateForm() {
		return form.validate();
	}
	
	public static GeneralItemCanvas getCanvas(String itemId) {
		if ("org.celstec.arlearn2.beans.generalItem.NarratorItem".equals(itemId)) {
			System.out.println("returning narrator item");
			return new NarratorItemCanvas();	
		}
		return new MultiplechoiceCanvas();
	}

	public JSONObject generateObject() {
		JSONObject object = new JSONObject();
		System.out.println("idItem value "+idItem.getValue());
		if (idItem.getValue() != null) object.put("id", new JSONNumber((Integer) idItem.getValue()));
		if (gameIdItem.getValue() != null) object.put("gameId", new JSONNumber((Integer) gameIdItem.getValue()));
		object.put("name", new JSONString(nameItem.getValueAsString()));
		object.put("scope", new JSONString(selectScope.getValueAsString()));
		if (radiusItem.getValue() != null && !radiusItem.getValue().equals("")) {
			object.put("radius", new JSONNumber(Long.parseLong(""+radiusItem.getValue())));
		}
		if (latItem.getValue() != null && !latItem.getValue().equals("") && lngItem.getValue() != null && !lngItem.getValue().equals("")) {
			object.put("lat", new JSONNumber(Double.parseDouble(""+latItem.getValue())));
			object.put("lng", new JSONNumber(Double.parseDouble(""+lngItem.getValue())));
		}
		return object;
	}

	public void setItemValues(JSONValue jsonValue) {
		JSONObject o = jsonValue.isObject();
		if (o == null) return;
		idItem.setValue(o.get("id").isNumber().doubleValue());
		nameItem.setValue(o.get("name").isString().stringValue());
		selectScope.setValue(o.get("scope").isString().stringValue());
		if (o.get("radius") != null) radiusItem.setValue(o.get("radius").isNumber().doubleValue());
		if (o.get("lng") != null) lngItem.setValue(o.get("lng").isNumber().doubleValue());
		if (o.get("lat") != null) latItem.setValue(o.get("lat").isNumber().doubleValue());
		if (o.get("dependsOn") != null) {
			dependency = o.get("dependsOn").toString();
			dependencyField.setValue(dependency);
		} else {
			dependency = null;
			form.redraw();
		}
		
		
	}

	//Validators
	private CustomValidator cv = new CustomValidator() {
		
		@Override
		protected boolean condition(Object value) {
			if (value == null) return false;
			if ((""+value).trim().equals("")) return false;
			return true;
		}
	};
}
