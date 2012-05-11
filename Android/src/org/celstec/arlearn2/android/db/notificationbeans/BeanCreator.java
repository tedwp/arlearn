package org.celstec.arlearn2.android.db.notificationbeans;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.celstec.arlearn2.android.db.beans.BeanDeserialiser;
import org.codehaus.jettison.json.JSONObject;
import org.jivesoftware.smack.packet.DefaultPacketExtension;

public class BeanCreator extends BeanDeserialiser {

	private DefaultPacketExtension packetExtension;

	public BeanCreator(DefaultPacketExtension packetExtension) {
		super();
		this.packetExtension = packetExtension;
	}

	public NotificationBean createBean() {
		try {
			Object returnObject = Class.forName("org.celstec.arlearn2.android.db.notificationbeans." + packetExtension.getValue("type")).getConstructor()
					.newInstance();

			Iterator<Field> fieldIt = getRelevantBeanProperties(returnObject.getClass()).iterator();
			while (fieldIt.hasNext()) {
				Field field = (Field) fieldIt.next();
				String value = packetExtension.getValue(field.getName());
				if (value != null) {
					Method m = getMethod(returnObject.getClass(), field);
					if (field.getType().equals(String.class)) {
						m.invoke(returnObject, value);
					}
					if (field.getType().equals(Integer.class)) {
						m.invoke(returnObject, Integer.parseInt(value));
					}
					if (field.getType().equals(Double.class)) {
						m.invoke(returnObject, Double.parseDouble(value));
					}
					if (field.getType().equals(Boolean.class)) {
						m.invoke(returnObject, Boolean.parseBoolean(value));
					}
					if (field.getType().equals(Long.class)) {
						m.invoke(returnObject, Long.parseLong(value));
					}
				}
			}
			return (NotificationBean) returnObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Method getMethod(Class returnObject, Field field) {
		try {
			return returnObject.getDeclaredMethod(getBeanMethodName(field.getName()), field.getType());
		} catch (NoSuchMethodException e) {
			if (!returnObject.getSuperclass().equals(Object.class)) {
				return getMethod(returnObject.getSuperclass(), field);
			}
		}
		return null;
	}

	protected void checkField(Field f, Class beanCls, List<Field> returnFields) {
		Class type = f.getType();
		String typeName = type.getName();
		if (packetExtension.getValue(f.getName()) != null) {
			try {
				Method m = beanCls.getDeclaredMethod(getBeanMethodName(f.getName()), f.getType());
				if (!returnFields.contains(f))
					returnFields.add(f);
			} catch (NoSuchMethodException e) {
				// log("no such method");
			} catch (SecurityException e) {
			}

		}
	}

}
