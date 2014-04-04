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
package org.celstec.arlearn2.beans.deserializer.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.celstec.arlearn2.beans.AuthResponseDeserializer;
import org.celstec.arlearn2.beans.GamePackageDeserializer;
import org.celstec.arlearn2.beans.Info;
import org.celstec.arlearn2.beans.RunPackage;
import org.celstec.arlearn2.beans.VersionDeserializer;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.account.AccountList;
import org.celstec.arlearn2.beans.dependencies.ActionDependency;
import org.celstec.arlearn2.beans.dependencies.AndDependency;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.dependencies.OrDependency;
import org.celstec.arlearn2.beans.dependencies.ProximityDependency;
import org.celstec.arlearn2.beans.dependencies.TimeDependency;
import org.celstec.arlearn2.beans.deserializer.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.CustomDeserializer;
import org.celstec.arlearn2.beans.game.*;
import org.celstec.arlearn2.beans.generalItem.*;
import org.celstec.arlearn2.beans.notification.*;
import org.celstec.arlearn2.beans.notification.authoring.RunCreationStatus;
import org.celstec.arlearn2.beans.oauth.OauthInfo;
import org.celstec.arlearn2.beans.oauth.OauthInfoList;
import org.celstec.arlearn2.beans.run.*;
import org.celstec.arlearn2.beans.run.Thread;
import org.celstec.arlearn2.beans.store.Category;
import org.celstec.arlearn2.beans.store.CategoryList;
import org.celstec.arlearn2.beans.store.GameCategory;
import org.celstec.arlearn2.beans.store.GameCategoryList;
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
		return deserialize(beanCls, json, cd);
		
	}
	
	public static Object deserialize(Class beanCls, JSONObject json, CustomDeserializer cd ) {
		if (cd == null) cd = getCustomDeserializer(beanCls);
		if (cd != null) return cd.toBean(json);
		return null;
	}
	
	public static Object deserialize(String json) throws JSONException {
		return deserialize(new JSONObject(json));
	}
	public static Object deserialize(JSONObject json) {
		CustomDeserializer cd = null;
		try {
			if (json.has("type")) cd = getCustomDeserializer(json.getString("type"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
		GeneralItemDeserializer gid = new GeneralItemDeserializer();
		OpenQuestionDeserializer oqd =new OpenQuestionDeserializer();
		
		DependencyDeserializer dd = new DependencyDeserializer();
		GameDeserializer gd = new GameDeserializer();
		ConfigDeserializer cd = new ConfigDeserializer();
		RunBeanDeserializer rbd = new RunBeanDeserializer();
		RunDeserializer rd = new RunDeserializer();
		MultipleChoiceAnswerItemDeserializer mcaid = new MultipleChoiceAnswerItemDeserializer();
		
		customDeserializerMap.put(GeneralItem.class, gid);
		customDeserializerMap.put(NarratorItem.class, nid);
		customDeserializerMap.put(AudioObject.class, aod);
		customDeserializerMap.put(VideoObject.class, vod);
		customDeserializerMap.put(OpenQuestion.class, oqd);
		customDeserializerMap.put(Dependency.class, dd);
		customDeserializerMap.put(ActionDependency.class, dd);
		customDeserializerMap.put(AndDependency.class, dd);
		customDeserializerMap.put(OrDependency.class, dd);
		customDeserializerMap.put(TimeDependency.class, dd);
		customDeserializerMap.put(Game.class, gd);
		customDeserializerMap.put(Config.class,cd);
		customDeserializerMap.put(RunBean.class, rbd);
		customDeserializerMap.put(Run.class, rd);
		
		customDeserializerMap.put(MultipleChoiceAnswerItem.class, mcaid);
		customDeserializerMap.put(MultipleChoiceImageAnswerItem.class, MultipleChoiceImageAnswerItem.deserializer);
		customDeserializerMap.put(MultipleChoiceTest.class, MultipleChoiceTest.deserializer);
		customDeserializerMap.put(SingleChoiceTest.class, SingleChoiceTest.deserializer);
		customDeserializerMap.put(SingleChoiceImageTest.class, SingleChoiceImageTest.deserializer);
		customDeserializerMap.put(MultipleChoiceImageTest.class, MultipleChoiceImageTest.deserializer);
		customDeserializerMap.put(User.class, User.deserializer);

		customDeserializerMapString.put("org.celstec.arlearn2.beans.Bean", new org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.Info", Info.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.notification.authoring.GameCreationStatus", new GameCreationStatusDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.notification.authoring.RunCreationStatus", new RunCreationStatus.RunCreationStatusDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.NarratorItem", nid);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.AudioObject", aod);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.VideoObject", vod);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.GeneralItem", gid);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.GeneralItemList", new GeneralItemListDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.Bean.OpenQuestion", oqd);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.dependencies.Dependency", dd);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.dependencies.ActionDependency", dd);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.dependencies.AndDependency", dd);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.dependencies.OrDependency", dd);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.dependencies.TimeDependency", dd);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.dependencies.ProximityDependency", new ProximityDependency.Deserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.GamePackage", new GamePackageDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.game.Game", gd);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.game.GameAccess", GameAccess.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.game.GameAccessList", GameAccessList.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.game.VariableDefinition", VariableDefinition.deserializer);
        customDeserializerMapString.put("org.celstec.arlearn2.beans.game.VariableEffectDefinition", VariableEffectDefinition.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.game.Config", cd);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.game.MapRegion", new MapRegion.MapRegionDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.Location", new Location.Deserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.game.LocationUpdateConfig", new LocationUpdateConfigDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.RunPackage", new RunPackage.RunPackageDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.RunBean", rbd);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.Run", rd);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.RunList", new RunListDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.RunAccess", RunAccess.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.RunAccessList", RunAccessList.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.Action", new ActionDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.ActionList", new ActionListDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.Response", new ResponseDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.ResponseList", new ResponseListDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.UserScore", new UserScoreDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.GeneralItemVisibility", GeneralItemVisibility.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.GeneralItemVisibilityList", GeneralItemVisibilityList.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.Message", Message.deserializer);
        customDeserializerMapString.put("org.celstec.arlearn2.beans.run.MessageList", MessageList.deserializer);
        customDeserializerMapString.put("org.celstec.arlearn2.beans.run.VariableInstance", VariableInstance.deserializer);
        customDeserializerMapString.put("org.celstec.arlearn2.beans.run.Thread", Thread.deserializer);
        customDeserializerMapString.put("org.celstec.arlearn2.beans.run.ThreadList", ThreadList.deserializer);

		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem", mcaid);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.MultipleChoiceImageAnswerItem", MultipleChoiceImageAnswerItem.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest", MultipleChoiceTest.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.SingleChoiceTest", SingleChoiceTest.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest", SingleChoiceImageTest.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.MultipleChoiceImageTest", MultipleChoiceImageTest.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.OpenUrl", OpenUrl.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.ScanTag", ScanTag.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.YoutubeObject", YoutubeObject.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.OpenBadge", OpenBadge.deserializer);
        customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.FileReference", FileReference.deserializer);

        customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.ObjectCollectionDisplay", ObjectCollectionDisplay.objectCollectionDisplayDeserializer);
        customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.ObjectCollectionDisplay$DisplayObject", ObjectCollectionDisplay.displayObjectDeserializer);
        customDeserializerMapString.put("org.celstec.arlearn2.beans.generalItem.ObjectCollectionDisplay$DisplayZone", ObjectCollectionDisplay.displayZoneDeserializer);

        customDeserializerMapString.put("org.celstec.arlearn2.beans.store.Category", Category.deserializer);
        customDeserializerMapString.put("org.celstec.arlearn2.beans.oauth.CategoryList", CategoryList.deserializer);
        customDeserializerMapString.put("org.celstec.arlearn2.beans.oauth.GameCategory", GameCategory.deserializer);
        customDeserializerMapString.put("org.celstec.arlearn2.beans.oauth.GameCategoryList", GameCategoryList.deserializer);

        customDeserializerMapString.put("org.celstec.arlearn2.beans.run.User", User.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.AuthResponse", new AuthResponseDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.Version", new VersionDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.UserList", new UserListDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.Team", new TeamDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.TeamList", new TeamListDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.run.LocationUpdate", new LocationUpdateDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.notification.RunModification", new RunModificationDeserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.notification.GameModification", GameModification.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.notification.GeneralItemModification", new GeneralItemModificationDeserializer());
        customDeserializerMapString.put("org.celstec.arlearn2.beans.notification.MessageNotification", MessageNotification.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.notification.Ping", new Ping.Deserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.notification.Pong", new Pong.Deserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.notification.TeamModification", TeamModification.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.notification.APNDeviceDescription", new APNDeviceDescription.Deserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.account.Account", Account.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.account.AccountList", AccountList.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.oauth.OauthInfo", OauthInfo.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.oauth.OauthInfoList", OauthInfoList.deserializer);
		customDeserializerMapString.put("org.celstec.arlearn2.beans.notification.GCMDeviceDescription", new GCMDeviceDescription.Deserializer());
		customDeserializerMapString.put("org.celstec.arlearn2.beans.notification.DeviceDescriptionList", new DeviceDescriptionList.Deserializer());
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
