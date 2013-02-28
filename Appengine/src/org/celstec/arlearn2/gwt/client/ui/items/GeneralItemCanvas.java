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
package org.celstec.arlearn2.gwt.client.ui.items;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.celstec.arlearn2.gwt.client.Authoring;
import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemGameDataSource;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemsClient;


import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverEvent;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverHandler;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.IsFloatValidator;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.layout.VStack;

public abstract class GeneralItemCanvas extends VStack{
	
//	protected DynamicForm form = new DynamicForm();
	protected HiddenItem idItem;
	protected HiddenItem gameIdItem;
	protected TextItem nameItem;
//	protected TextItem radiusItem;
	protected TextItem latItem;
	protected TextItem lngItem;
	protected TextItem sortItem;
//	protected TextAreaItem dependencyField;
	protected CheckboxItem isAutolaunch;
	protected CheckboxItem isSimpleDependency;
	protected SelectItem selectAction;
	protected TextItem selectActionString;
	protected SelectItem selectGeneralItem;
	protected SelectItem selectRole;
	protected SelectItem selectScope;
	protected CheckboxItem isDisapperOnDependency;
	protected SelectItem disAppearOnAction;
	protected TextItem disAppearSelectActionString;
	protected SelectItem disAppearOnGeneralItem;
	protected SelectItem disAppearOnRole;
	protected SelectItem disAppearOnScope;
	protected CheckboxItem showCountDownCb;
	protected SelectItem disTime;

	
	protected SelectItem roleGrid;
	private String dependency = null;
	private String roles[];
	
	private final String ID_NAME = "id";
	private final String ORDER = "sortKey";
	private final String GAME_ID_NAME = "gameId";
	private final String NAME = "name";
//	private final String RADIUS = "radius";
	private final String LAT = "lat";
	private final String LNG = "lng";
	private final String ROLE = "roles";
	private final String DEPENDENCY = "dependency";
	private final String AUTOLAUNCH = "autoLaunch";
	private final String SIMPLE_DEP = "simpleDep";
	private final String ACTION_DEP = "action";
	private final String ACTION_DEP_STRING = "actionString";
	private final String GENITEM_DEP = "generalItemId";
	private final String SCOPE_DEP = "scope";
	private final String ROLE_DEP = "role";
	
	private final String DIS_SIMPLE_DEP = "disAppearDep";
	private final String DIS_ACTION_DEP = "disAppearAction";
	private final String DIS_ACTION_DEP_STRING = "disActionString";
	private final String DIS_GENITEM_DEP = "disAppeargeneralItemId";
	private final String DIS_SCOPE_DEP = "disAppearscope";
	private final String DIS_ROLE_DEP = "disAppearrole";
	private final String SHOW_COUNTDOWN = "showCountDown";
	private final String DIS_TIME = "disTime";
	
	private boolean scantagStringAppear = false;
	private boolean scantagStringDisappear = false;
	
	IsFloatValidator floatValidator = new IsFloatValidator();
	IsIntegerValidator intValidator = new IsIntegerValidator();
	
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
//		createDepencyItem();
		createRoleItem();
		createAutoLaunchComponent();
		createSimpleDependencyComponent();
		
		createDisapperOnDependencyComponent();
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
	
//	protected void createDepencyItem(){
//		dependencyField = new TextAreaItem();  
//		dependencyField.setTitle("Plain Text");
//		dependencyField.setName(DEPENDENCY);
//		dependencyField.setShowIfCondition(new FormItemIfFunction() {  
//            public boolean execute(FormItem item, Object value, DynamicForm form) {  
//                return dependency != null;  
//            }
//        });  
//	}
	
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
	
	private void createAutoLaunchComponent() {
		isAutolaunch = new CheckboxItem();
		isAutolaunch.setName(AUTOLAUNCH);
		isAutolaunch.setStartRow(true);
		isAutolaunch.setTitle(constants.autoLaunch());
		isAutolaunch.addItemHoverHandler(new ItemHoverHandler() {
			
			@Override
			public void onItemHover(ItemHoverEvent event) {
			 
			 isAutolaunch.setPrompt(constants.autoLaunchHover());
			}
		});
	}
	
	private void createSimpleDependencyComponent() { // createDisapperOnDependencyComponent
		isSimpleDependency = new CheckboxItem();
		isSimpleDependency.setName(SIMPLE_DEP);
		isSimpleDependency.setValue(false);
		isSimpleDependency.setTitle(constants.simpleDep());
		isSimpleDependency.addItemHoverHandler(new ItemHoverHandler() {
			@Override
			public void onItemHover(ItemHoverEvent event) {
				isSimpleDependency.setPrompt(constants.simpleDepExplain());
			}
		});
		isSimpleDependency.setRedrawOnChange(true);

		FormItemIfFunction formIf = new FormItemIfFunction() {
			public boolean execute(FormItem item, Object value, DynamicForm form) {
				if (form.getValue(SIMPLE_DEP) == null)
					return false;

				return form.getValue(SIMPLE_DEP).equals(Boolean.TRUE);
			}

		};
		
		FormItemIfFunction actionFreeString = new FormItemIfFunction() {
			public boolean execute(FormItem item, Object value, DynamicForm form) {
				if (form.getValue(SIMPLE_DEP) == null)
					return false;

				return form.getValue(SIMPLE_DEP).equals(Boolean.TRUE) && scantagStringAppear;
			}

		};
		FormItemIfFunction actionFixedString = new FormItemIfFunction() {
			public boolean execute(FormItem item, Object value, DynamicForm form) {
				if (form.getValue(SIMPLE_DEP) == null)
					return false;

				return form.getValue(SIMPLE_DEP).equals(Boolean.TRUE) && !scantagStringAppear;
			}

		};
		selectAction = new SelectItem(ACTION_DEP);
		selectAction.setTitle(constants.actionDep());
		selectAction.setValueMap(createSimpleDependencyValues());
		selectAction.setWrapTitle(false);
		selectAction.setShowIfCondition(actionFixedString);
		selectAction.setStartRow(true);
		
		selectActionString = new TextItem(ACTION_DEP_STRING);
		selectActionString.setTitle(constants.actionDep());
		selectActionString.setWrapTitle(false);
		selectActionString.setShowIfCondition(actionFreeString);
		selectActionString.setStartRow(true);
		

		selectGeneralItem = new SelectItem(GENITEM_DEP);
		selectGeneralItem.setTitle(constants.generalItemDep());
		selectGeneralItem.setValueMap(createSimpleDependencyValues());
		selectGeneralItem.setWrapTitle(false);
		selectGeneralItem.setDisplayField("name");
		Criteria crit = new Criteria();
		crit.addCriteria("deleted", false);
		selectGeneralItem.setPickListCriteria(crit);
		selectGeneralItem.setValueField("id");
		selectGeneralItem.setOptionDataSource(GeneralItemGameDataSource.getInstance());
		selectGeneralItem.setShowIfCondition(formIf);
		selectGeneralItem.setStartRow(true);
		selectGeneralItem.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				String id = selectGeneralItem.getValueAsString();
				getActions(Long.parseLong(id), SIMPLE_DEP);
			}
		});

		selectScope = new SelectItem(SCOPE_DEP);
		selectScope.setTitle(constants.scopeDep());
		selectScope.setValueMap(createScopeDependencyValues());
		if (selectScope.getValue() == null)
			selectScope.setValue(0);
		selectScope.setShowIfCondition(formIf);
		selectScope.setStartRow(true);

		selectRole = new SelectItem(ROLE_DEP);
		selectRole.setTitle(constants.roleDep());
		selectRole.setValueMap(roles);
		selectRole.setShowIfCondition(formIf);
		selectRole.setStartRow(true);
	}
	
	
	private void createDisapperOnDependencyComponent() {
		isDisapperOnDependency = new CheckboxItem();
		isDisapperOnDependency.setName(DIS_SIMPLE_DEP);
		isDisapperOnDependency.setValue(false);
		isDisapperOnDependency.setTitle(constants.disappearDep());
		isDisapperOnDependency.addItemHoverHandler(new ItemHoverHandler() {
			@Override
			public void onItemHover(ItemHoverEvent event) {
				isDisapperOnDependency.setPrompt(constants.disappearDepExplain());
			}
		});
		isDisapperOnDependency.setRedrawOnChange(true);

		FormItemIfFunction formIf = new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {  
            	if (form.getValue(DIS_SIMPLE_DEP) == null) return false;

                return form.getValue(DIS_SIMPLE_DEP).equals(Boolean.TRUE);  
            }

        };
        
        FormItemIfFunction actionFreeString = new FormItemIfFunction() {
			public boolean execute(FormItem item, Object value, DynamicForm form) {
				if (form.getValue(DIS_SIMPLE_DEP) == null)
					return false;

				return form.getValue(DIS_SIMPLE_DEP).equals(Boolean.TRUE) && scantagStringDisappear;
			}

		};
		FormItemIfFunction actionFixedString = new FormItemIfFunction() {
			public boolean execute(FormItem item, Object value, DynamicForm form) {
				if (form.getValue(DIS_SIMPLE_DEP) == null)
					return false;

				return form.getValue(DIS_SIMPLE_DEP).equals(Boolean.TRUE) && !scantagStringDisappear;
			}

		};
        
		disAppearOnAction = new SelectItem(DIS_ACTION_DEP);
		disAppearOnAction.setTitle(constants.actionDep());
		disAppearOnAction.setValueMap(createSimpleDependencyValues());
		disAppearOnAction.setWrapTitle(false);
		disAppearOnAction.setShowIfCondition(actionFixedString);  
		disAppearOnAction.setStartRow(true);
		
		
		disAppearSelectActionString = new TextItem(DIS_ACTION_DEP_STRING);
		disAppearSelectActionString.setTitle(constants.actionDep());
		disAppearSelectActionString.setWrapTitle(false);
		disAppearSelectActionString.setShowIfCondition(actionFreeString);
		disAppearSelectActionString.setStartRow(true);
		
		disAppearOnGeneralItem = new SelectItem(DIS_GENITEM_DEP);
		disAppearOnGeneralItem.setTitle(constants.generalItemDep());
		disAppearOnGeneralItem.setValueMap(createSimpleDependencyValues());
		disAppearOnGeneralItem.setWrapTitle(false);
		disAppearOnGeneralItem.setDisplayField("name");
		Criteria crit = new Criteria();
		crit.addCriteria("deleted", false);
		disAppearOnGeneralItem.setPickListCriteria(crit);
		disAppearOnGeneralItem.setValueField("id");
		disAppearOnGeneralItem.setOptionDataSource(GeneralItemGameDataSource.getInstance());
		disAppearOnGeneralItem.setShowIfCondition(formIf);
		disAppearOnGeneralItem.setStartRow(true);
		disAppearOnGeneralItem.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				String id = disAppearOnGeneralItem.getValueAsString();
				getActions(Long.parseLong(id), DIS_SIMPLE_DEP);
			}
		});
		
		
		disAppearOnScope = new SelectItem(SCOPE_DEP);
		disAppearOnScope.setTitle(constants.scopeDep());
		disAppearOnScope.setValueMap(createScopeDependencyValues());
		if (disAppearOnScope.getValue() == null) disAppearOnScope.setValue(0);
		disAppearOnScope.setShowIfCondition(formIf);
		disAppearOnScope.setStartRow(true);
		
		disAppearOnRole = new SelectItem(DIS_ROLE_DEP);
		disAppearOnRole.setTitle(constants.roleDep());
		disAppearOnRole.setValueMap(roles);
		disAppearOnRole.setShowIfCondition(formIf);
		disAppearOnRole.setStartRow(true);
		
		showCountDownCb = new CheckboxItem();
		showCountDownCb.setName(SHOW_COUNTDOWN);
		showCountDownCb.setValue(false);
		showCountDownCb.setTitle(constants.showCountDown());
		showCountDownCb.setShowIfCondition(formIf);
		showCountDownCb.setStartRow(true);
		
		disTime = new SelectItem();
		disTime.setName(DIS_TIME);
		disTime.setValue("-1");
		disTime.setValueMap(createDisappearTimes());
		disTime.setTitle(constants.disTime());
		disTime.setShowIfCondition(formIf);
		disTime.setStartRow(true);
		
	}
	
	
	 
	public LinkedHashMap<String, String> createSimpleDependencyValues() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("read", "Read"); //TODO i18n
		valueMap.put("answer_given", "Answer given"); //TODO
		return valueMap;
	}
	
	public LinkedHashMap<String, String> createScopeDependencyValues() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("0", "User");
		valueMap.put("1", "Team");
		valueMap.put("2", "All");
		return valueMap;
	}
	
	public LinkedHashMap<String, String> createDisappearTimes() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put(  "-1", "no time");
		valueMap.put(  "10000", "10s");
		valueMap.put(  "30000", "30s");
		valueMap.put(  "60000", "1m");
		valueMap.put( "120000", "2m");
		valueMap.put( "300000", "5m");
		valueMap.put( "600000", "10m");
		valueMap.put("1800000", "30m");
		return valueMap;
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
	
	protected double setValueDouble(String name, JSONObject o){
		if (formMapping.get(name) != null) {
			if (o.get(name)!= null && o.get(name).isNumber() != null) {
				double value = o.get(name).isNumber().doubleValue();
				formMapping.get(name).setValue(name, value);
				return value;
			}
		}
		return 0;
	}
	
	protected void setValueDouble(String constantName, String name, JSONObject o){
		if (formMapping.get(constantName) != null) {
			if (o.get(name)!= null && o.get(name).isNumber() != null) formMapping.get(constantName).setValue(constantName, o.get(name).isNumber().doubleValue());
		}
	}
	
	protected void setValueString(String name, JSONObject o){
		if (formMapping.get(name) != null) {
			if (o.get(name)!= null && o.get(name).isString() != null) formMapping.get(name).setValue(name, o.get(name).isString().stringValue());
		}
	}
	
	protected void setValueString(String constantName, String name, JSONObject o){
		if (formMapping.get(constantName) != null) {
			if (o.get(name)!= null && o.get(name).isString() != null) formMapping.get(constantName).setValue(constantName, o.get(name).isString().stringValue());
		}
	}
	
	public static GeneralItemCanvas getCanvas(String itemId, String[] roles) {
		if ("org.celstec.arlearn2.beans.generalItem.NarratorItem".equals(itemId)) {
			return new NarratorItemCanvas(roles);	
		}
		if ("org.celstec.arlearn2.beans.generalItem.VideoObject".equals(itemId)) {
			return new VideoObjectCanvas(roles);	
		}
		if ("org.celstec.arlearn2.beans.generalItem.YoutubeObject".equals(itemId)) {
			return new YoutubeObjectCanvas(roles);	
		}
		if ("org.celstec.arlearn2.beans.generalItem.AudioObject".equals(itemId)) {
			return new AudioObjectCanvas(roles);	
		}
		if (SingleChoiceCanvas.type.equals(itemId)) {
			return new SingleChoiceCanvas(roles);	
		}
		if (Authoring.hidden &&  SingleChoiceImageCanvas.type.equals(itemId)) {
			return new SingleChoiceImageCanvas(roles);	
		}
		if (Authoring.hidden &&  MultipleChoiceImageCanvas.type.equals(itemId)) {
			return new MultipleChoiceImageCanvas(roles);	
		}
		if (Authoring.hidden && "org.celstec.arlearn2.beans.generalItem.OpenUrl".equals(itemId)) {
			return new OpenUrlCanvas(roles);	
		}
		if (Authoring.hidden && "org.celstec.arlearn2.beans.generalItem.OpenBadge".equals(itemId)) {
			return new OpenBadgeCanvas(roles);	
		}
		if ("org.celstec.arlearn2.beans.generalItem.ScanTag".equals(itemId)) {
			return new ScanTagCanvas(roles);	
		}
		return new MultiplechoiceCanvas(roles);
	}

	public JSONObject generateObject() {
		JSONObject object = new JSONObject();
		putIntegerValue(object, ID_NAME, GAME_ID_NAME, ORDER);
//		putIntegerValue(object, ID_NAME, GAME_ID_NAME, RADIUS);
		putStringValue(object, NAME);
		putDoubleValue(object, LAT, LNG);
		putArrayValue(object, roleGrid);
		object.put("autoLaunch", JSONBoolean.getInstance( getValue(AUTOLAUNCH)==null?false:(Boolean) getValue(AUTOLAUNCH)) );

		
		if (getValue(SIMPLE_DEP) != null && (Boolean) getValue(SIMPLE_DEP)) {
			JSONObject actionDep = generateDependency(ACTION_DEP, ACTION_DEP_STRING, GENITEM_DEP, SCOPE_DEP, ROLE_DEP, null);
			if (actionDep != null) object.put("dependsOn", actionDep);
		}
		if (getValue(DIS_SIMPLE_DEP) != null && (Boolean) getValue(DIS_SIMPLE_DEP)) {
			JSONObject actionDep = generateDependency(DIS_ACTION_DEP, DIS_ACTION_DEP_STRING, DIS_GENITEM_DEP, DIS_SCOPE_DEP, DIS_ROLE_DEP, DIS_TIME);
			
			if (getValue(SHOW_COUNTDOWN) != null) object.put(SHOW_COUNTDOWN, JSONBoolean.getInstance((Boolean) getValue(SHOW_COUNTDOWN)));
			if (actionDep != null) object.put("disappearOn", actionDep);
		}
		return object;
	}
	
	private JSONObject generateDependency(String actionDepString, String actionString, String genItemString, String scopeDepString, String roleDepString, String disTime) {
		if (disTime != null && getValue(disTime) != null && !getValue(disTime).equals("-1")) {
			System.out.println("time "+getValue(disTime));
			JSONObject timeDep = new JSONObject();
			timeDep.put("type", new JSONString("org.celstec.arlearn2.beans.dependencies.TimeDependency"));
			timeDep.put("timeDelta", new JSONNumber(Long.parseLong((String)getValue(disTime))));
			
		}
		JSONObject actionDep = new JSONObject();
		actionDep.put("type", new JSONString("org.celstec.arlearn2.beans.dependencies.ActionDependency"));
		boolean addDep = false;
		if (actionString.equals(ACTION_DEP_STRING)) {
			if (scantagStringAppear) { //TODO make generic
				if ( getValue(actionString) != null) {
					addDep = true;
					actionDep.put("action", new JSONString((String) getValue(actionString)));	
				}
			} else {
				if (getValue(actionDepString) != null ) {
					addDep = true;
					actionDep.put("action", new JSONString((String) getValue(actionDepString)));	
				}	
			}
		} else {
			if (scantagStringDisappear) { //TODO make generic
				if ( getValue(actionString) != null) {
					addDep = true;
					actionDep.put("action", new JSONString((String) getValue(actionString)));	
				}
			} else {
				if (getValue(actionDepString) != null ) {
					addDep = true;
					actionDep.put("action", new JSONString((String) getValue(actionDepString)));	
				}	
			}
		}
		
		
		
		
		if (getValue(genItemString) != null) {
			addDep = true;
			actionDep.put("generalItemId", new JSONNumber((Integer) getValue(genItemString)));	
		}
		if (getValue(scopeDepString) != null) {
			Object scope = getValue(scopeDepString);
			if (scope instanceof Integer)		{
				actionDep.put("scope", new JSONNumber((Integer)getValue(scopeDepString)));	
			} else {
				actionDep.put("scope", new JSONNumber(Integer.parseInt((String)  getValue(scopeDepString))));	
			}	
		}
		if (getValue(roleDepString) != null) {
			addDep = true;
			actionDep.put("role", new JSONString((String)  getValue(roleDepString)));	
		}
		if (!addDep) return null;
		if (disTime != null && getValue(disTime) != null && !getValue(disTime).equals("-1")) {
			System.out.println("time "+getValue(disTime));
			JSONObject timeDep = new JSONObject();
			timeDep.put("type", new JSONString("org.celstec.arlearn2.beans.dependencies.TimeDependency"));
			timeDep.put("timeDelta", new JSONNumber(Long.parseLong((String)getValue(disTime))));
			timeDep.put("offset", actionDep);
			return timeDep;
			
		}
		return actionDep;
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
//		setValueDouble(RADIUS, o);
		setValueDouble(LAT, o);
		setValueDouble(LNG, o);
		
		if (o.get(AUTOLAUNCH) != null) {
			formMapping.get(AUTOLAUNCH).setValue(AUTOLAUNCH, o.get(AUTOLAUNCH).isBoolean().booleanValue());
		}

		if (o.containsKey("dependsOn") && o.get("dependsOn").isObject() != null) {
			JSONObject dep = o.get("dependsOn").isObject();
			if (dep.containsKey("type")) {
				formMapping.get(SIMPLE_DEP).setValue(SIMPLE_DEP, "org.celstec.arlearn2.beans.dependencies.ActionDependency".equals(dep.get("type").isString().stringValue()));
				double itemId = setValueDouble(GENITEM_DEP, dep);
				Record rec = GeneralItemGameDataSource.getInstance().getGeneralItem((long) itemId);
				if ("Scan Tag".equals(rec.getAttribute("simpleName"))) {
					scantagStringAppear = true; //TODO make generic
					setValueString(ACTION_DEP_STRING, ACTION_DEP, dep);
				} else {
					setValueString(ACTION_DEP, dep);
				}
				setValueString(ROLE_DEP, dep);

				setValueDouble(SCOPE_DEP, dep);
			}
			
		}
		if (o.containsKey("disappearOn") && o.get("disappearOn").isObject() != null) {
			JSONObject dep = o.get("disappearOn").isObject();
			if (dep.containsKey("type")) {
				formMapping.get(DIS_SIMPLE_DEP).setValue(DIS_SIMPLE_DEP, false);
				if ("org.celstec.arlearn2.beans.dependencies.ActionDependency".equals(dep.get("type").isString().stringValue())) {
					formMapping.get(DIS_SIMPLE_DEP).setValue(DIS_SIMPLE_DEP, true);
				}
				if ("org.celstec.arlearn2.beans.dependencies.TimeDependency".equals(dep.get("type").isString().stringValue())) {
					if (dep.get("offset") != null && dep.get("offset").isObject() != null && "org.celstec.arlearn2.beans.dependencies.ActionDependency".equals(dep.get("offset").isObject().get("type").isString().stringValue())) {
						formMapping.get(DIS_SIMPLE_DEP).setValue(DIS_SIMPLE_DEP, true);
						formMapping.get(DIS_TIME).setValue(DIS_TIME, ""+((long)dep.get("timeDelta").isNumber().doubleValue()));
						dep = dep.get("offset").isObject();
					}
				}
				Record rec = GeneralItemGameDataSource.getInstance().getGeneralItem((long) dep.get("generalItemId").isNumber().doubleValue());
				if ("Scan Tag".equals(rec.getAttribute("simpleName"))) {
					scantagStringDisappear = true; //TODO make generic
					setValueString(DIS_ACTION_DEP_STRING, ACTION_DEP, dep);
				} else {
					setValueString(DIS_ACTION_DEP, ACTION_DEP, dep);
				}
				setValueString(DIS_ROLE_DEP, ROLE_DEP, dep);
				setValueDouble(DIS_GENITEM_DEP, GENITEM_DEP, dep);
				setValueDouble(DIS_SCOPE_DEP, SCOPE_DEP, dep);
			}
			
		}
		if (o.containsKey(SHOW_COUNTDOWN) && o.get(SHOW_COUNTDOWN).isBoolean() != null) {
			formMapping.get(SHOW_COUNTDOWN).setValue(SHOW_COUNTDOWN, o.get(SHOW_COUNTDOWN).isBoolean().booleanValue());
		}
//			dependency = o.get("dependsOn").toString();
////			dependencyField.setValue(dependency);
//			formMapping.get(DEPENDENCY).setValue(DEPENDENCY, dependency);
//		} else {
//			dependency = null;
//			formMapping.get(DEPENDENCY).redraw();
//		}
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
	

	
	protected void initValidators() {
		cv.setErrorMessage(constants.emptyValue());
		floatValidator.setErrorMessage(constants.incorrectValue());
		urlValidator.setErrorMessage(constants.invalidUrl());
	}

	public void setLocation(double lat, double lng) {
		formMapping.get(LAT).setValue(LAT, lat);
		formMapping.get(LNG).setValue(LNG, lng);
	}
	
	public void getActions(long itemId, final String depKind) {
		 GeneralItemsClient.getInstance().getGeneralItem(getValueInteger(GAME_ID_NAME), itemId, new JsonCallback(){
			 public void onJsonReceived(JSONValue jsonValue) {
				 LinkedHashMap<String, String> map = createSimpleDependencyValues();
				 if (jsonValue.isObject() == null) return;
				 if (jsonValue.isObject().get("type").isString().stringValue().equals("org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest")
						 ||jsonValue.isObject().get("type").isString().stringValue().equals("org.celstec.arlearn2.beans.generalItem.SingleChoiceTest")) {
					 JSONValue answers = jsonValue.isObject().get("answers");
					 JSONArray answersArray = answers.isArray(); 
					 if (answers != null) {
						 for (int i = 0; i < answersArray.size(); i++) {
							 JSONObject answerobject = answersArray.get(i).isObject();
							 if (answerobject.get("id") != null) {
								 map.put("answer_"+answerobject.get("id").isString().stringValue(), answerobject.get("answer").isString().stringValue());
							 }
						 }
					 }
				 }
				 if (jsonValue.isObject().get("type").isString().stringValue().equals("org.celstec.arlearn2.beans.generalItem.ScanTag")) {
					 if (depKind.equals(SIMPLE_DEP)) {
						 scantagStringAppear = true;	
						 formMapping.get(ACTION_DEP).redraw();

					 } else {
						 scantagStringDisappear = true;	 
						 formMapping.get(DIS_ACTION_DEP).redraw();
					 }
				 } else {
					 if (depKind.equals(SIMPLE_DEP)) {
						 scantagStringAppear = false;
						 formMapping.get(ACTION_DEP).redraw();

					 } else {
						 scantagStringDisappear = false;	 
						 formMapping.get(DIS_ACTION_DEP).redraw();
					 }
					 
				 }

				 selectAction.setValueMap(map);
			 }
		 });
	}
	
}
