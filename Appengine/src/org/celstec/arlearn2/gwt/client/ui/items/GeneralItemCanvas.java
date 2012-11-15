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
	protected SelectItem selectGeneralItem;
	protected SelectItem selectRole;
	protected SelectItem selectScope;
	
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
	private final String GENITEM_DEP = "generalItemId";
	private final String SCOPE_DEP = "scope";
	private final String ROLE_DEP = "role";
	
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
	
	private void createSimpleDependencyComponent() {
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
            	if (form.getValue(SIMPLE_DEP) == null) return false;

                return form.getValue(SIMPLE_DEP).equals(Boolean.TRUE);  
            }

        };
		selectAction = new SelectItem(ACTION_DEP);
		selectAction.setTitle(constants.actionDep());
		selectAction.setValueMap(createSimpleDependencyValues());
		selectAction.setWrapTitle(false);
		selectAction.setShowIfCondition(formIf);  
		selectAction.setStartRow(true);
		
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
				getActions(Long.parseLong(id));
//				Criteria crit = new Criteria();
//				crit.addCriteria("id", );
//				GeneralItemGameDataSource.getInstance().fetchData(crit, new DSCallback() {
//					@Override
//					public void execute(DSResponse response,
//							Object rawData, DSRequest request) {
//						Record[] records = response.getData();
//						for (Record record : records) {
//							System.out.println("query "+record);
//						}
//					}
//				});
				
			}
		});
		
		
		selectScope = new SelectItem(SCOPE_DEP);
		selectScope.setTitle(constants.scopeDep());
		selectScope.setValueMap(createScopeDependencyValues());
		if (selectScope.getValue() == null) selectScope.setValue(0);
		selectScope.setShowIfCondition(formIf);
		selectScope.setStartRow(true);
		
		selectRole = new SelectItem(ROLE_DEP);
		selectRole.setTitle(constants.roleDep());
		selectRole.setValueMap(roles);
		selectRole.setShowIfCondition(formIf);
		selectRole.setStartRow(true);
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
		if ("org.celstec.arlearn2.beans.generalItem.YoutubeObject".equals(itemId)) {
			return new YoutubeObjectCanvas(roles);	
		}
		if ("org.celstec.arlearn2.beans.generalItem.AudioObject".equals(itemId)) {
			return new AudioObjectCanvas(roles);	
		}
		if ("org.celstec.arlearn2.beans.generalItem.SingleChoiceTest".equals(itemId)) {
			return new SingleChoiceCanvas(roles);	
		}
		if (Authoring.hidden && "org.celstec.arlearn2.beans.generalItem.OpenUrl".equals(itemId)) {
			return new OpenUrlCanvas(roles);	
		}
		if (Authoring.hidden && "org.celstec.arlearn2.beans.generalItem.OpenBadge".equals(itemId)) {
			return new OpenBadgeCanvas(roles);	
		}
		if (Authoring.hidden && "org.celstec.arlearn2.beans.generalItem.ScanTag".equals(itemId)) {
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
			JSONObject actionDep = new JSONObject();
			//{"type":"org.celstec.arlearn2.beans.dependencies.ActionDependency", "action":"manual-30"}
			actionDep.put("type", new JSONString("org.celstec.arlearn2.beans.dependencies.ActionDependency"));
			boolean addDep = false;
			if (getValue(ACTION_DEP) != null) {
				addDep = true;
				actionDep.put("action", new JSONString((String) getValue(ACTION_DEP)));	
			}
			if (getValue(GENITEM_DEP) != null) {
				addDep = true;
				actionDep.put("generalItemId", new JSONNumber((Integer) getValue(GENITEM_DEP)));	
			}
			if (getValue(SCOPE_DEP) != null) {
				Object scope = getValue(SCOPE_DEP);
				if (scope instanceof Integer)		{
					actionDep.put("scope", new JSONNumber((Integer)getValue(SCOPE_DEP)));	
				} else {
					actionDep.put("scope", new JSONNumber(Integer.parseInt((String)  getValue(SCOPE_DEP))));	
				}	
			}
			if (getValue(ROLE_DEP) != null) {
				addDep = true;
				actionDep.put("role", new JSONString((String)  getValue(ROLE_DEP)));	
			}
			if (addDep) object.put("dependsOn", actionDep);
		}
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
					setValueString(ACTION_DEP, dep);
					setValueString(ROLE_DEP, dep);
					setValueDouble(GENITEM_DEP, dep);
					setValueDouble(SCOPE_DEP, dep);
			}
			
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
	
	public void getActions(long itemId) {
		 GeneralItemsClient.getInstance().getGeneralItem(getValueInteger(GAME_ID_NAME), itemId, new JsonCallback(){
			 public void onJsonReceived(JSONValue jsonValue) {
				 LinkedHashMap<String, String> map = createSimpleDependencyValues();
				 if (jsonValue.isObject() == null) return;
				 if (jsonValue.isObject().get("type").isString().stringValue().equals("org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest")) {
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
				 selectAction.setValueMap(map);
			 }
		 });
	}
	
}
