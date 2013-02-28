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
package org.celstec.arlearn2.beans.deserializer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

public abstract class BeanDeserializer {

	protected  BeanDeserializer() {
		
	}
	
	protected abstract boolean hasProperty(String name);
	
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
		if (hasProperty(f.getName())) {
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
	
}
