package org.celstec.arlearn2.android.db.beans;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class BeanSerialiser {
	private Object bean;

	public BeanSerialiser(Object bean) {
		this.bean = bean;
	}

	protected List<Field> getRelevantBeanProperties(Class beanCls) {
		Vector<Field> returnFields = new Vector<Field>();
		Field[] fields = beanCls.getDeclaredFields();
		if (fields == null || fields.length == 0) {
			return returnFields;
		}
		for (int i = 0; i < fields.length; i++) {
			returnFields.add(fields[i]);
		}
		Class superClass = beanCls.getSuperclass();
		if (!superClass.equals(Object.class)) {
			List<Field> subfields = getRelevantBeanProperties(superClass);
			returnFields.addAll(subfields);
		}

		return returnFields;
	}

	protected String getBeanMethodName(String nameOfField) {
		if (nameOfField == null || nameOfField == "")
			return "";
		String method_name = "get";
		method_name += nameOfField.substring(0, 1).toUpperCase();

		if (nameOfField.length() == 1)
			return method_name;

		method_name += nameOfField.substring(1);
		return method_name;
	}

	public String serialise() {
		return serialiseToJson().toString();
	}

	public JSONObject serialiseToJson() {
		JSONObject returnJson = new JSONObject();
		Iterator<Field> it = getRelevantBeanProperties(bean.getClass()).iterator();
		while (it.hasNext()) {
			Field field = (Field) it.next();
			try {
				Method m = bean.getClass().getDeclaredMethod(getBeanMethodName(field.getName()));
				if (field.getType().equals(List.class)) {
					JSONArray ar = new JSONArray();
					Iterator it2 = ((List) m.invoke(bean)).iterator();
					while (it2.hasNext()) {
						ar.put((new BeanSerialiser(it2.next())).serialiseToJson());
					}

					returnJson.put(field.getName(), ar);
				} else {
					returnJson.put(field.getName(), m.invoke(bean));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnJson;
	}

}
