package org.celstec.arlearn2.beans.serializer.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.serializer.BeanSerializer;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class JsonBeanSerialiser extends BeanSerializer{

	public JsonBeanSerialiser(Object bean) {
		super(bean);
	}

	public Class getCustomSerialiser() {
//		System.out.println(bean.getClass().getPackage().getName()+".serializer.json");
		try {
			return Class.forName(bean.getClass().getPackage().getName()+".serializer.json."+bean.getClass().getSimpleName());
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	public JSONObject serialiseToJson() {
		JSONObject returnJson = new JSONObject();
		if (bean == null) return returnJson;
		return serialiseToJson(returnJson);
	}

	public JSONObject serialiseToJson(JSONObject returnJson) {
		Class customSerialiser = getCustomSerialiser();
		if (customSerialiser !=null) {
			try {
				JsonBean jsonCustomSerialiser = (JsonBean) customSerialiser.newInstance();
				return jsonCustomSerialiser.toJSON(bean);
			} catch (InstantiationException  e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		Iterator<Field> it = getRelevantBeanProperties(bean.getClass()).iterator();
		while (it.hasNext()) {
			Field field = (Field) it.next();
			try {
//				Method m = bean.getClass().getDeclaredMethod(getBeanMethodName(field.getName()));
				Method m = getMethod(bean.getClass(), field.getName());
				if (field.getType().equals(List.class)) {
					JSONArray ar = new JSONArray();
					Iterator it2 = ((List) m.invoke(bean)).iterator();
					while (it2.hasNext()) {
						ar.put((new JsonBeanSerialiser(it2.next())).serialiseToJson());
					}

					returnJson.put(field.getName(), ar);
				} else {
					try {
					Object o =  m.invoke(bean);
					if (isWrapperType(o.getClass())) {
						returnJson.put(field.getName(), m.invoke(bean));
					} else {
						JsonBeanSerialiser jsonB = new JsonBeanSerialiser(o);
						returnJson.put(field.getName(), jsonB.serialiseToJson());

					}
					
					}catch (NullPointerException ne) {
						//in case of static fields
//						ne.printStackTrace();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnJson;
	}
	
	 private static final HashSet<Class<?>> WRAPPER_TYPES = getWrapperTypes();

	    public static boolean isWrapperType(Class<?> clazz)
	    {
	        return WRAPPER_TYPES.contains(clazz);
	    }

	    private static HashSet<Class<?>> getWrapperTypes()
	    {
	        HashSet<Class<?>> ret = new HashSet<Class<?>>();
	        ret.add(Boolean.class);
	        ret.add(Character.class);
	        ret.add(Byte.class);
	        ret.add(Short.class);
	        ret.add(Integer.class);
	        ret.add(Long.class);
	        ret.add(Float.class);
	        ret.add(Double.class);
	        ret.add(Void.class);
	        ret.add(String.class);
	        return ret;
	    }

	protected Method getMethod(Class c, String fieldName) {
		try {
			return c.getDeclaredMethod(getBeanMethodName(fieldName));
		} catch (Exception e) {
			if (c.equals(Object.class)) return null;
			return getMethod(c.getSuperclass(), fieldName);
		}
	}
}
