package org.celstec.arlearn2.beans.deserializer.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.celstec.arlearn2.beans.deserializer.BeanDeserializer;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class JsonBeanDeserializer  extends BeanDeserializer{

	private JSONObject json;

	public JsonBeanDeserializer(String jsonString) throws JSONException {
		json = new JSONObject(jsonString);
	}

	public JsonBeanDeserializer(JSONObject json) throws JSONException {
		this.json = json;
	}
	
	protected  JsonBeanDeserializer() {
		super();
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
	
	private Object getJsonAttribute(Field field, String jsonMethod) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		Method mJson = JSONObject.class.getDeclaredMethod(jsonMethod, String.class);
		jsonMethod = getWrapperTypes().get(field.getType());
		if (jsonMethod == null) jsonMethod = "getString";
		Method mJson = JSONObject.class.getDeclaredMethod(jsonMethod, String.class);

		return mJson.invoke(json, field.getName());
	}

	@SuppressWarnings("unchecked")
	private void processFieldList(Class beanClass, Object returnObject, Field field) throws ClassNotFoundException, Exception {
		String generic;
		try {
			generic = field.getGenericType().toString();
		} catch (Exception e) {
			generic = "list<"+(String) beanClass.getField(field.getName()+"Type").get(null)+">";
		}
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
					JsonBeanDeserializer bd = new JsonBeanDeserializer(array.getJSONObject(j));
					Class typeClass = Class.forName(genericType);
					if (array.getJSONObject(j).has("type")) {
						typeClass = Class.forName((String) array.getJSONObject(j).get("type"));
					}
					arrayList.add(bd.deserialize(typeClass));

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

		Object returnObject = null;
		if (json.has("type")){
			try {				
			 returnObject = Class.forName(json.getString("type")).getConstructor().newInstance();
			 beanCls = Class.forName(json.getString("type"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			returnObject = beanCls.getConstructor().newInstance();
		}
		
		Iterator<Field> fieldIt = getRelevantBeanProperties(beanCls).iterator();
		while (fieldIt.hasNext()) {
			
			Field field = (Field) fieldIt.next();
			String wrapperMethodGetter = getWrapperTypes().get(field.getType().getName());
			if (wrapperMethodGetter != null) {
				processField(beanCls, field, field.getType().getName(), returnObject, wrapperMethodGetter);
			} else if (field.getType().getName().equals(List.class.getName())
					|| field.getType().getName().equals(ArrayList.class.getName())
					|| field.getType().getName().equals(Vector.class.getName())) {
				processFieldList(beanCls, returnObject, field);

			}else {
//				Method m = beanCls.getDeclaredMethod(getBeanMethodName(field.getName()), field.getType());
				Method m = getDeclaredMethod(beanCls, getBeanMethodName(field.getName()), field.getType());
				JsonBeanDeserializer jbd = new JsonBeanDeserializer((String) getJsonAttribute(field, "getString"));
				m.invoke(returnObject, jbd.deserialize(field.getType())); 
			}
		}

		return returnObject;
	}
	
	private Method getDeclaredMethod(Class beanCls, String methodName, Class type) throws NoSuchMethodException {
		try {
			return beanCls.getDeclaredMethod(methodName, type);
		} catch (NoSuchMethodException e) {
			if (beanCls.getSuperclass().equals(Object.class)) throw e;
			return getDeclaredMethod(beanCls.getSuperclass(), methodName, type);
		}
	}
	
	private static final HashMap<String, String> WRAPPER_TYPES = getWrapperTypes();

	 private static HashMap<String, String> getWrapperTypes()
	    {
		 HashMap<String, String> ret = new HashMap<String, String>();
	        ret.put("java.lang.Integer", "getInt");
	        ret.put("int", "getInt");
	        ret.put("double", "getDouble");
	        ret.put("long", "getLong");
	        ret.put("java.lang.Long", "getLong");
	        ret.put("java.lang.Double", "getDouble");
	        ret.put("java.lang.String", "getString");
	        ret.put("boolean", "getBoolean");
	        ret.put("java.lang.Boolean", "getBoolean");
//	        ret.put(Boolean.class, "getBoolean");
//	        ret.add(Character.class);
//	        ret.add(Byte.class);
//	        ret.add(Short.class);
//	        ret.add(Integer.class);
//	        ret.add(Long.class);
//	        ret.add(Float.class);
//	        ret.add(Double.class);
//	        ret.add(Void.class);
//	        ret.add(String.class);
	        return ret;
	    }
	@Override
	protected boolean hasProperty(String name) {
		return json.has(name);
	}
}
