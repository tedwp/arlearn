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

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.dependencies.ActionDependency;
import org.celstec.arlearn2.beans.dependencies.AndDependency;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.dependencies.OrDependency;
import org.celstec.arlearn2.beans.dependencies.TimeDependency;
import org.celstec.arlearn2.beans.deserializer.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.CustomDeserializer;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunBean;
import org.celstec.arlearn2.beans.run.User;
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

	public static Object deserialize(Class beanCls, JSONObject json) {
		CustomDeserializer cd = null;
		try {
			if (json.has("type")) cd = getCustomDeserializer(json.getString("type"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (cd == null) cd = getCustomDeserializer(beanCls);
		if (cd != null) return cd.toBean(json);
		return null;
	}
	
	public Object deserialize(Class beanCls) throws Exception {
		CustomDeserializer cd = null;
		try {
			if (json.has("type")) cd = getCustomDeserializer(json.getString("type"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (cd == null) cd = getCustomDeserializer(beanCls);
		if (cd != null) return cd.toBean(json);
		System.err.println("no custom deserializer for "+beanCls);
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
	
	private static CustomDeserializer getCustomDeserializer(Class beanCls) {
		return customDeserializerMap.get(beanCls);
	}
	
	private static CustomDeserializer getCustomDeserializer(String beanCls) {
		return customDeserializerMapString.get(beanCls);
	}
	
	private static HashMap<Class, CustomDeserializer> customDeserializerMap = new HashMap<Class, CustomDeserializer>();
	private static HashMap<String, CustomDeserializer> customDeserializerMapString = new HashMap<String, CustomDeserializer>();

	static {
		NarratorItemDeserializer nid = new NarratorItemDeserializer();
		AudioObjectDeserializer aod = new AudioObjectDeserializer();
		VideoObjectDeserializer vod = new VideoObjectDeserializer();
		MultipleChoiceTestDeserializer mct = new MultipleChoiceTestDeserializer();
		
		customDeserializerMap.put(GeneralItem.class, new GeneralItemDeserializer());
		customDeserializerMap.put(NarratorItem.class, nid);
		customDeserializerMap.put(AudioObject.class, aod);
		customDeserializerMap.put(VideoObject.class, vod);
		customDeserializerMap.put(OpenQuestion.class, new OpenQuestionDeserializer());
		customDeserializerMap.put(Dependency.class, new DependencyDeserializer());
		customDeserializerMap.put(ActionDependency.class, new DependencyDeserializer());
		customDeserializerMap.put(AndDependency.class, new DependencyDeserializer());
		customDeserializerMap.put(OrDependency.class, new DependencyDeserializer());
		customDeserializerMap.put(TimeDependency.class, new DependencyDeserializer());
		customDeserializerMap.put(Game.class, new GameDeserializer());
		customDeserializerMap.put(Config.class, new ConfigDeserializer());
		customDeserializerMap.put(RunBean.class, new RunBeanDeserializer());
		customDeserializerMap.put(Run.class, new RunDeserializer());
		customDeserializerMap.put(MultipleChoiceAnswerItem.class, new MultipleChoiceAnswerItemDeserializer());
		customDeserializerMap.put(MultipleChoiceTest.class, mct);
		customDeserializerMap.put(User.class, new UserDeserializer());

		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.NarratorItem", nid);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.AudioObject", aod);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.VideoObject", vod);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest", mct);
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
