package org.celstec.arlearn2.gwt.client.ui.items;

import java.util.HashMap;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;


import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.IsFloatValidator;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

public abstract class GeneralItemCanvas extends VStack{
	
//	protected DynamicForm form = new DynamicForm();
	protected HiddenItem idItem;
	protected HiddenItem gameIdItem;
	protected TextItem nameItem;
	protected SelectItem selectScope;
//	protected TextItem radiusItem;
	protected TextItem latItem;
	protected TextItem lngItem;
	protected TextItem sortItem;
	protected TextAreaItem dependencyField;
	protected SelectItem roleGrid;
	private String dependency = null;
	private String roles[];
	
	private final String ID_NAME = "id";
	private final String ORDER = "sortKey";
	private final String GAME_ID_NAME = "gameId";
	private final String NAME = "name";
	private final String SCOPE = "scope";
//	private final String RADIUS = "radius";
	private final String LAT = "lat";
	private final String LNG = "lng";
	private final String ROLE = "roles";
	private final String DEPENDENCY = "dependency";
	
	protected AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public GeneralItemCanvas (String roles[]) {
		this.roles = roles;
		 setWidth100();  
		initValidators();
	}
	
	protected void initComponents() {
		createIdItem();
		createGameIdItem();
		createNameItem();
//		createRadiusItem();
		createLatItem();
		createLngItem();
		createSortItem();
		createDepencyItem();
		createRoleItem();
	}
	

	protected void createIdItem() {
		idItem = new HiddenItem(ID_NAME);
	}
	
	protected void createGameIdItem() {
		gameIdItem = new HiddenItem(GAME_ID_NAME);
	}

	protected void createNameItem() {
		nameItem = new TextItem(NAME);
		nameItem.setTitle(constants.itemName());
		nameItem.setSelectOnFocus(true);
		nameItem.setWrapTitle(false);
		nameItem.setValidators(cv);
	}
	
	protected void createSelectScopeItem() {
		selectScope = new SelectItem(SCOPE);
		selectScope.setTitle("Select scope");
		selectScope.setValueMap("user", "team", "all");
		selectScope.setDisabled(true);
	}
	
//	protected void createRadiusItem() {
//		radiusItem = new TextItem(RADIUS);
//		radiusItem.setTitle(constants.radius());
//		radiusItem.setSelectOnFocus(true);
//		radiusItem.setWrapTitle(false);
//	}

	protected void createLatItem() {
		latItem = new TextItem(LAT);
		latItem.setTitle(constants.latitude());
		latItem.setSelectOnFocus(true);
		latItem.setWrapTitle(false);
		latItem.setValidators(floatValidator);
	}

	
	protected void createLngItem() {
		lngItem = new TextItem(LNG);
		lngItem.setTitle(constants.longitude());
		lngItem.setSelectOnFocus(true);
		lngItem.setWrapTitle(false);
		lngItem.setValidators(floatValidator);

	}	
	
	protected void createSortItem() {
		sortItem = new TextItem(ORDER);
		sortItem.setTitle("order");
		sortItem.setSelectOnFocus(true);
		sortItem.setWrapTitle(false);
		sortItem.setValidators(intValidator);
	}
	
	protected void createDepencyItem(){
		dependencyField = new TextAreaItem();  
		dependencyField.setTitle("Plain Text");
		dependencyField.setName(DEPENDENCY);
		dependencyField.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {  
                return dependency != null;  
            }
        });  
	}
	
	protected void createRoleItem() {
		roleGrid = new SelectItem();
		roleGrid.setName(ROLE);
		roleGrid.setTitle(constants.selectRole());

		roleGrid.setMultiple(true);
		roleGrid.setHeight(50);
		roleGrid.setMultipleAppearance(MultipleAppearance.GRID);
		roleGrid.setValueMap(roles);
		roleGrid.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {  
                return roles != null && roles.length != 0;  
            }
        });  
	}
	
	public void setGameId(int gameId) {
		gameIdItem.setValue((int) gameId);
	}
	
	public abstract boolean validateForm();
	
	protected void addField(DynamicForm form, FormItem... fields) {
		for (FormItem fi: fields) {
			formMapping.put(fi.getName(), form);
		}
	}
	protected HashMap<String, DynamicForm> formMapping = new HashMap<String, DynamicForm>();
	
	protected Object getValue(String name) {
		if (formMapping.get(name) == null) {
			System.err.println(name +" does not exists");
			return null;
		}
		return formMapping.get(name).getValue(name);
	}
	
	
	
	protected int getValueInteger(String name) {
		if (formMapping.get(name) == null) {
			System.err.println(name +" does not exists");
			return 0;
		}
		Object ret = formMapping.get(name).getValue(name);
		if (ret instanceof Integer) return (Integer) ret;
		if (ret instanceof String) return Integer.parseInt((String)ret);
		return 0;
	}
	
	protected double getValueDouble(String name) {
		if (formMapping.get(name) == null) {
			System.err.println(name +" does not exists");
			return 0;
		}
		Object ret = formMapping.get(name).getValue(name);
		if (ret instanceof Double) return (Double) ret;
		if (ret instanceof Integer) return (double) ((Integer) ret).intValue();
		if (ret instanceof Float) return (double)((Float) ret).floatValue();
		if (ret instanceof String) return Double.parseDouble(""+ret);
		System.out.println(ret.getClass());
		return 0;
	}
	
	protected String getValueAsString(String name) {
		if (formMapping.get(name) == null) {
			System.err.println(name +" does not exists");
			return null;
		}
		return formMapping.get(name).getValueAsString(name);
	}
	
	protected void setValueDouble(String name, JSONObject o){
		if (formMapping.get(name) != null) {
			if (o.get(name)!= null && o.get(name).isNumber() != null) formMapping.get(name).setValue(name, o.get(name).isNumber().doubleValue());
		}
	}
	
	protected void setValueString(String name, JSONObject o){
		if (formMapping.get(name) != null) {
			if (o.get(name)!= null && o.get(name).isString() != null) formMapping.get(name).setValue(name, o.get(name).isString().stringValue());
		}
	}
	
	public static GeneralItemCanvas getCanvas(String itemId, String[] roles) {
		if ("org.celstec.arlearn2.beans.generalItem.NarratorItem".equals(itemId)) {
			return new NarratorItemCanvas(roles);	
		}
		if ("org.celstec.arlearn2.beans.generalItem.VideoObject".equals(itemId)) {
			return new VideoObjectCanvas(roles);	
		}
		if ("org.celstec.arlearn2.beans.generalItem.AudioObject".equals(itemId)) {
			return new AudioObjectCanvas(roles);	
		}
		return new MultiplechoiceCanvas(roles);
	}

	public JSONObject generateObject() {
		JSONObject object = new JSONObject();
		putIntegerValue(object, ID_NAME, GAME_ID_NAME, ORDER);
//		putIntegerValue(object, ID_NAME, GAME_ID_NAME, RADIUS);
		putStringValue(object, NAME, SCOPE);
		putDoubleValue(object, LAT, LNG);
		putArrayValue(object, roleGrid);
//		if (roleGrid.getValues().length != 0) {
//			JSONArray ar = new JSONArray();
//			
//			object.put("roles", ar);
//			for (int i = 0; i <roleGrid.getValues().length; i++) {
////				ar.put(i, roleGrid.getValues()[i]);
//				ar.set(i, new JSONString(roleGrid.getValues()[i]));
//			}
//		}
		
		return object;
	}
	
	protected void putArrayValue(JSONObject object, SelectItem... items) {
		for (SelectItem item: items) {
			if (item.getValues().length != 0) {
				JSONArray ar = new JSONArray();
				object.put(ROLE, ar);
				for (int i = 0; i <item.getValues().length; i++) {
					ar.set(i, new JSONString(item.getValues()[i]));
				}
			}
		}
	}
	
	protected void putStringValue(JSONObject object, String... names) {
		for (String name: names) {
			if (getValueAsString(name) !=null) object.put(name, new JSONString(getValueAsString(name)));
		}
	}
	
	protected void putIntegerValue(JSONObject object, String... names) {
		for (String name: names) {
			if (getValue(name) != null && !getValue(name).equals("")) object.put(name, new JSONNumber(getValueInteger(name)));	
		}
	}
	
	protected void putDoubleValue(JSONObject object, String... names) {
		for (String name: names) {
			if (getValue(name) != null && !getValue(name).equals("")) object.put(name, new JSONNumber(getValueDouble(name)));	
		}
	}

	public void setItemValues(JSONValue jsonValue) {
		JSONObject o = jsonValue.isObject();
		if (o == null) return;
		setValueDouble(ID_NAME, o);
		setValueDouble(ORDER, o);
		setValueString(NAME, o);
		setValueString(SCOPE, o);
//		setValueDouble(RADIUS, o);
		setValueDouble(LAT, o);
		setValueDouble(LNG, o);

		if (o.get("dependsOn") != null) {
			dependency = o.get("dependsOn").toString();
//			dependencyField.setValue(dependency);
			formMapping.get(DEPENDENCY).setValue(DEPENDENCY, dependency);
		} else {
			dependency = null;
			formMapping.get(DEPENDENCY).redraw();
		}
//		
		if (o.get(ROLE) != null) {
			JSONArray ar = o.get(ROLE).isArray();
			String[] values = new String[ar.size()];
			for (int i = 0; i<ar.size(); i++) {
				values[i] = ar.get(i).isString().stringValue();
			}
			roleGrid.setValues(values);
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
	
	protected CustomValidator urlValidator = new CustomValidator() {
		
		@Override
		protected boolean condition(Object value) {
			if (value == null) return false;
			String stringValue = (""+value);
			if ((""+value).trim().equals("")) return false;
			if (!stringValue.startsWith("http://")) return false;
			return true;
		}
	};
	
	IsFloatValidator floatValidator = new IsFloatValidator();
	IsIntegerValidator intValidator = new IsIntegerValidator();
	
	protected void initValidators() {
		cv.setErrorMessage(constants.emptyValue());
		floatValidator.setErrorMessage(constants.incorrectValue());
		urlValidator.setErrorMessage(constants.invalidUrl());
	}

	public void setLocation(double lat, double lng) {
		formMapping.get(LAT).setValue(LAT, lat);
		formMapping.get(LNG).setValue(LNG, lng);
	}
	
}
