package org.celstec.arlearn2.beans;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Deprecated
public class BeanDeserialiser {
	private JSONObject json;

	public BeanDeserialiser(String jsonString) throws JSONException {
		json = new JSONObject(jsonString);
	}

	public BeanDeserialiser(JSONObject json) throws JSONException {
		this.json = json;
	}
	protected  BeanDeserialiser() {
		
	}

	protected List<Field> getRelevantBeanProperties(Class beanCls) {
		Vector<Field> returnFields = new Vector<Field>();
		Field[] fields = beanCls.getDeclaredFields();
		if (fields == null || fields.length == 0) {
			return returnFields;
		}
		for (int i = 0; i < fields.length; i++) {
			checkField(fields[i], beanCls, returnFields);
		}
		Class superClass = beanCls.getSuperclass();
		if (!superClass.equals(Object.class)){
			List<Field> subfields = getRelevantBeanProperties(superClass);
			returnFields.addAll(subfields);
		}
		
		return returnFields;
	}
	
	protected void checkField(Field f, Class beanCls, List<Field> returnFields) {
		Class type = f.getType();
		String typeName = type.getName();
		if (json.has(f.getName())) {
			try {
				Method m = beanCls.getDeclaredMethod(getBeanMethodName(f.getName()), f.getType());
				// log("method will be added "+m.getName());
				if (!returnFields.contains(f)) returnFields.add(f);
			} catch (NoSuchMethodException e) {
				// log("no such method");
			} catch (SecurityException e) {
			}

		}
	}

	protected String getBeanMethodName(String nameOfField) {
		if (nameOfField == null || nameOfField == "")
			return "";
		String method_name = "set";
		method_name += nameOfField.substring(0, 1).toUpperCase();

		if (nameOfField.length() == 1) return method_name;

		method_name += nameOfField.substring(1);
		return method_name;
	}

	private boolean processField(Class beanCls, Field field, String type, Object returnObject, String jsonMethod) {
		if (field.getType().getName().equals(type)) {
			Method m;
			try {
				m = beanCls.getDeclaredMethod(getBeanMethodName(field.getName()), field.getType());
				m.setAccessible(true);
				Method mJson = JSONObject.class.getDeclaredMethod(jsonMethod, String.class);
				m.invoke(returnObject, mJson.invoke(json, field.getName()));
				return true;
			} catch (NoSuchMethodException e) {
				if (!beanCls.getSuperclass().equals(Object.class)){
					return processField(beanCls.getSuperclass(), field, type, returnObject, jsonMethod);
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private void processFieldList(Class beanClass, Object returnObject, Field field) throws ClassNotFoundException, Exception {
		String generic = field.getGenericType().toString();
		if (generic.indexOf("<") != -1) {
			// extract generic from <>
			String genericType = generic.substring(generic.lastIndexOf("<") + 1, generic.lastIndexOf(">"));
			if (genericType != null) {
				JSONArray array = null;
				try {
					array = json.getJSONArray(field.getName());
				} catch (Exception ex) {
					array = new JSONArray();
					array.put(json.getJSONObject(field.getName()));

				}
				@SuppressWarnings("rawtypes")
				ArrayList arrayList = new ArrayList();
				for (int j = 0; j < array.length(); j++) {
					BeanDeserialiser bd = new BeanDeserialiser(array.getJSONObject(j));
					arrayList.add(bd.deserialize(Class.forName(genericType)));

				}
//				System.out.println(arrayList);
				Method m = beanClass.getDeclaredMethod(getBeanMethodName(field.getName()), field.getType());
				m.setAccessible(true);
				m.invoke(returnObject, arrayList);
			}

		}

	}

	public Object deserialize(Class beanCls) throws Exception {
		if (beanCls == null) {
			return null;
		}

		Object returnObject = beanCls.getConstructor().newInstance();
		if (json.has("type")){
			try {				
			 returnObject = Class.forName(beanCls.getPackage().getName()+"."+json.getString("type")).getConstructor().newInstance();
			 beanCls = Class.forName(beanCls.getPackage().getName()+"."+json.getString("type"));
			} catch (Exception e) {
				//je.printStackTrace();
			}
		}
		
		Iterator<Field> fieldIt = getRelevantBeanProperties(beanCls).iterator();
		while (fieldIt.hasNext()) {
			
			Field field = (Field) fieldIt.next();
			if (!processField(beanCls, field, "int", returnObject, "getInt"))
				if (!processField(beanCls, field, "java.lang.Integer", returnObject, "getInt"))
					if (!processField(beanCls, field, "long", returnObject, "getLong"))
						if (!processField(beanCls, field, "java.lang.Long", returnObject, "getLong"))
					if (!processField(beanCls, field, "double", returnObject, "getDouble"))
						if (!processField(beanCls, field, "java.lang.Double", returnObject, "getDouble"))
							if (!processField(beanCls, field, "java.lang.Boolean", returnObject, "getBoolean"))
								if (!processField(beanCls, field, "boolean", returnObject, "getBoolean"))
									if (!processField(beanCls, field, "java.lang.String", returnObject, "getString")) {
										if (field.getType().getName().equals(List.class.getName())
												|| field.getType().getName().equals(ArrayList.class.getName())
												|| field.getType().getName().equals(Vector.class.getName())) {
											processFieldList(beanCls, returnObject, field);

										}
									}
		}

		return returnObject;
	}

	public static void main2(String[] args) throws Exception {
		String jsons = " {'runs':{'runId':'123','startTime':'1310926319084','title':'een excursie in Leuven'}}";
		BeanDeserialiser bd = new BeanDeserialiser(jsons);
		RunList rl = (RunList) bd.deserialize(RunList.class);
		System.out.println(rl);
		System.out.println(rl.getRuns());
		System.out.println(rl.getRuns().size());
		System.out.println(rl.getRuns().get(0).getStartTime());
	}
	public static void main(String[] args) throws Exception {
		String jsons = "{'generalItems':[{'@type':'narratorItem','id':'123','name':'OUNL Entry gate','description':'Hard work is done here','type':'NarratorItem','radius':'20','showAtTimeStamp':'1200','lng':'5.958125','lat':'50.879215','videoUrl':'test','text':'this is a NarratorItem without audio, images or video'},{'@type':'multipleChoiceTest','id':'124','name':'Multiple Choice','description':'Wat is de naam van dit gebouw','dependsOn':'{event: \"narator123read\", scope: \"user\" }','type':'MultipleChoiceTest','radius':'20','lng':'5.958025','lat':'50.879215','text':'Apart from the fields, we find some important institution here. Just look around!','question':'This is the entry gate of which institution?','answers':[{'isCorrect':'false','answer':'Belastingdienst Heerlen'},{'isCorrect':'false','answer':'RWTH Aachen'},{'isCorrect':'true','answer':'Open Universiteit Nederland'}]},{'@type':'pickupItem','id':'125','name':'Key','description':'a key','type':'PickupItem','radius':'20','showAtTimeStamp':'300','lng':'5.95812','lat':'50.87921','imageUrl':'<a href=\"http://www.flickr.com/photos/bohman/210977249/\" title=\"Keys. by Bohman, on Flickr\"></a>','dropZoneId':'126'},{'@type':'dropZone','id':'126','name':'Door','description':'a door','type':'DropZone','radius':'20','lng':'5.958197','lat':'50.879008','pickupItemId':'125'}]}";
		
		// String jsons =
		// " {'team':{'name':'Team Strong','teamId':'34034f47-1a67-4f8d-9c9a-2c1c39579ce0'} }";
		BeanDeserialiser bd = new BeanDeserialiser(jsons);
		GeneralItemList list = (GeneralItemList) bd.deserialize(GeneralItemList.class);
		System.out.println("teams size " + list.getGeneralitems().size());
		System.out.println("teams size " + ((NarratorItem)list.getGeneralitems().get(0)).getVideoUrl());
		System.out.println("teams size " + ((NarratorItem)list.getGeneralitems().get(0)).getText());
		System.out.println("teams size " + ((NarratorItem)list.getGeneralitems().get(0)).getLat());
		System.out.println("teams size " + ((NarratorItem)list.getGeneralitems().get(0)).getId());

		System.out.println("teams size " + ((MultipleChoiceTest) list.getGeneralitems().get(1)).getAnswers().size());
		
	}

	public static void main1(String[] args) throws Exception {
		// String jsons =
		// " {'team':[ {'name':'Team Strong','teamId':'34034f47-1a67-4f8d-9c9a-2c1c39579ce0'},{'name':'Team Strong','teamId':'34034f47-1a67-4f8d-9c9a-2c1c39579ce0'}]}";

		String jsons = " {'name':'Team Strong','teamId':'34034f47-1a67-4f8d-9c9a-2c1c39579ce0', test:1, test2:2, test3:true}";
		// String jsons = " {test3:true}";
		BeanDeserialiser bd = new BeanDeserialiser(jsons);
		
		Team ret = (Team) bd.deserialize(Team.class);

		System.out.println(ret.getTeamId());
		System.out.println(ret.getName());
		
		// // System.out.println(json.keys().next());

	}

}

