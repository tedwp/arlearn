package org.celstec.arlearn2.tasks.beans;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public abstract class GenericBean  implements Runnable{
    private static final Logger log = Logger.getLogger(GenericBean.class.getName());

	private String token;
	
	public GenericBean() {
		
	}
	
	public GenericBean(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void scheduleTask() {
		Queue queue = QueueFactory.getDefaultQueue();
		TaskOptions to  = TaskOptions.Builder.withUrl("/asyncTask")
	    		.param("type", this.getClass().getName());
	    queue.add(setParameters(to));
	}
	
	protected TaskOptions setParameters(TaskOptions to) {
		Iterator<Field> fields = getRelevantBeanProperties(this.getClass()).iterator();
		while (fields.hasNext()) {
			Field field = (Field) fields.next();
			try {
				 if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
				Method m = getClass().getMethod(getBeanMethodName(field.getName()));
				Object value = m.invoke(this);
				if (value !=null) to = to.param(field.getName(),value.toString());
				 }
			} catch(NoSuchMethodException e){
				if (!"log".equals(field.getName()))
				log.log(Level.WARNING, e.getMessage(), e);
			}catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			} 
			
		}
		return to;
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
	
	
	public void run() {		
	}
}
