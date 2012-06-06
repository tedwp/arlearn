package org.celstec.arlearn2.beans.serializer.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.beans.AuthResponseSerializer;
import org.celstec.arlearn2.beans.dependencies.ActionDependency;
import org.celstec.arlearn2.beans.dependencies.AndDependency;
import org.celstec.arlearn2.beans.dependencies.BooleanDependency;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.dependencies.OrDependency;
import org.celstec.arlearn2.beans.dependencies.TimeDependency;
import org.celstec.arlearn2.beans.deserializer.CustomDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.OpenQuestionDeserializer;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunBean;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.TeamList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.UserList;
import org.celstec.arlearn2.beans.serializer.BeanSerializer;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class JsonBeanSerialiser extends BeanSerializer{

	public JsonBeanSerialiser(Object bean) {
		super(bean);
	}

	public JsonBean getCustomSerialiser() {
//		try {
//			return Class.forName(bean.getClass().getPackage().getName()+".serializer.json."+bean.getClass().getSimpleName());
//		} catch (ClassNotFoundException e) {
//			return null;
//		}
		return customSerializerMap.get(bean.getClass().getCanonicalName());
	}
	
	public static JsonBean getCustomSerialiser(Object bean) {
//		try {
//			return Class.forName(bean.getClass().getPackage().getName()+".serializer.json."+bean.getClass().getSimpleName());
//		} catch (ClassNotFoundException e) {
//			return null;
//		}
		return customSerializerMap.get(bean.getClass().getCanonicalName());
	}
	
	private static HashMap<String, JsonBean> customSerializerMap = new HashMap<String, JsonBean>();

	static {
		customSerializerMap.put(GeneralItem.class.getCanonicalName(), new GeneralItemSerializer());
		customSerializerMap.put(GeneralItemList.class.getCanonicalName(), new GeneralItemListSerializer());
		customSerializerMap.put(NarratorItem.class.getCanonicalName(), new NarratorItemSerializer());
		customSerializerMap.put(OpenQuestion.class.getCanonicalName(), new OpenQuestionSerializer());
		customSerializerMap.put(Dependency.class.getCanonicalName(), new DependencySerializer());
		customSerializerMap.put(ActionDependency.class.getCanonicalName(), new DependencySerializer());
		customSerializerMap.put(TimeDependency.class.getCanonicalName(), new DependencySerializer());
		customSerializerMap.put(BooleanDependency.class.getCanonicalName(), new DependencySerializer());
		customSerializerMap.put(AndDependency.class.getCanonicalName(), new DependencySerializer());
		customSerializerMap.put(OrDependency.class.getCanonicalName(), new DependencySerializer());
		customSerializerMap.put(Game.class.getCanonicalName(), new GameSerializer());
		customSerializerMap.put(GamesList.class.getCanonicalName(), new GamesListSerializer());
		customSerializerMap.put(Config.class.getCanonicalName(), new ConfigSerializer());
		customSerializerMap.put(Run.class.getCanonicalName(), new RunSerializer());
		customSerializerMap.put(RunList.class.getCanonicalName(), new RunListSerializer());
		customSerializerMap.put(RunBean.class.getCanonicalName(), new RunBeanSerialiser());
		customSerializerMap.put(AudioObject.class.getCanonicalName(), new AudioObjectSerializer());
		customSerializerMap.put(VideoObject.class.getCanonicalName(), new VideoObjectSerializer());
		customSerializerMap.put(MultipleChoiceTest.class.getCanonicalName(), new MultipleChoiceTestSerializer());
		customSerializerMap.put(MultipleChoiceAnswerItem.class.getCanonicalName(), new MultipleChoiceAnswerItemSerializer());
		customSerializerMap.put(User.class.getCanonicalName(), new UserSerializer());
		customSerializerMap.put(UserList.class.getCanonicalName(), new UserListSerializer());
		customSerializerMap.put(Team.class.getCanonicalName(), new TeamSerializer());
		customSerializerMap.put(TeamList.class.getCanonicalName(), new TeamListSerializer());
		customSerializerMap.put(RunModification.class.getCanonicalName(), new RunModificationSerializer());
		customSerializerMap.put(GeneralItemModification.class.getCanonicalName(), new GeneralItemModificationSerializer());
		customSerializerMap.put(AuthResponse.class.getCanonicalName(), new AuthResponseSerializer());
	}
	
	public static JSONObject serialiseToJson(Object bean) {
		JsonBean customSerialiser = getCustomSerialiser(bean);
		if (customSerialiser !=null) {
			return customSerialiser.toJSON(bean);
		}
		System.out.println("custom serialiser missing for "+bean.getClass());
		return null;
	}
	public JSONObject serialiseToJson() {
		JSONObject returnJson = new JSONObject();
		if (bean == null) return returnJson;
		return serialiseToJson(returnJson);
	}

	public JSONObject serialiseToJson(JSONObject returnJson) {
		JsonBean customSerialiser = getCustomSerialiser();
		if (customSerialiser !=null) {
			return customSerialiser.toJSON(bean);
//			try {
//				JsonBean jsonCustomSerialiser = (JsonBean) customSerialiser.newInstance();
//				return jsonCustomSerialiser.toJSON(bean);
//			} catch (InstantiationException  e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
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
