package org.celstec.arlearn2.beans.serializer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Vector;



public class BeanSerializer {
	protected Object bean;

	public BeanSerializer () {}
	
	public BeanSerializer(Object bean) {
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

	
}
