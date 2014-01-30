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
package org.celstec.arlearn2.beans.serializer.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.beans.AuthResponseSerializer;
import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.GamePackage;
import org.celstec.arlearn2.beans.GamePackageSerializer;
import org.celstec.arlearn2.beans.Info;
import org.celstec.arlearn2.beans.RunPackage;
import org.celstec.arlearn2.beans.Version;
import org.celstec.arlearn2.beans.VersionSerializer;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.account.AccountList;
import org.celstec.arlearn2.beans.dependencies.ActionDependency;
import org.celstec.arlearn2.beans.dependencies.AndDependency;
import org.celstec.arlearn2.beans.dependencies.BooleanDependency;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.dependencies.OrDependency;
import org.celstec.arlearn2.beans.dependencies.ProximityDependency;
import org.celstec.arlearn2.beans.dependencies.TimeDependency;
import org.celstec.arlearn2.beans.deserializer.CustomDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.OpenQuestionDeserializer;
import org.celstec.arlearn2.beans.game.*;
import org.celstec.arlearn2.beans.generalItem.*;
import org.celstec.arlearn2.beans.notification.APNDeviceDescription;
import org.celstec.arlearn2.beans.notification.DeviceDescriptionList;
import org.celstec.arlearn2.beans.notification.GCMDeviceDescription;
import org.celstec.arlearn2.beans.notification.GameModification;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.celstec.arlearn2.beans.notification.Ping;
import org.celstec.arlearn2.beans.notification.Pong;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.notification.TeamModification;
import org.celstec.arlearn2.beans.notification.authoring.GameCreationStatus;
import org.celstec.arlearn2.beans.notification.authoring.RunCreationStatus;
import org.celstec.arlearn2.beans.oauth.OauthInfo;
import org.celstec.arlearn2.beans.oauth.OauthInfoList;
import org.celstec.arlearn2.beans.run.*;
import org.celstec.arlearn2.beans.serializer.BeanSerializer;
import org.celstec.arlearn2.beans.store.Category;
import org.celstec.arlearn2.beans.store.CategoryList;
import org.celstec.arlearn2.beans.store.GameCategory;
import org.celstec.arlearn2.beans.store.GameCategoryList;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class JsonBeanSerialiser extends BeanSerializer{

	private static final Logger logger = Logger.getLogger(JsonBeanSerialiser.class.getName());

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
		customSerializerMap.put(Bean.class.getCanonicalName(), new org.celstec.arlearn2.beans.serializer.json.BeanSerializer());
		customSerializerMap.put(Info.class.getCanonicalName(), Info.serializer);
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
		customSerializerMap.put(ProximityDependency.class.getCanonicalName(), new ProximityDependency.Serializer());
		customSerializerMap.put(GamePackage.class.getCanonicalName(), new GamePackageSerializer());
		customSerializerMap.put(Game.class.getCanonicalName(), new GameSerializer());
		customSerializerMap.put(GameAccess.class.getCanonicalName(), GameAccess.serializer);
		customSerializerMap.put(GameAccessList.class.getCanonicalName(), GameAccessList.serializer);
		customSerializerMap.put(VariableDefinition.class.getCanonicalName(), VariableDefinition.serializer);
        customSerializerMap.put(VariableEffectDefinition.class.getCanonicalName(), VariableEffectDefinition.serializer);
		customSerializerMap.put(GamesList.class.getCanonicalName(), new GamesListSerializer());
		customSerializerMap.put(GameCreationStatus.class.getCanonicalName(), new GameCreationStatusSerializer());
		customSerializerMap.put(RunCreationStatus.class.getCanonicalName(), new RunCreationStatus.GameCreationStatusSerializer());
		customSerializerMap.put(Config.class.getCanonicalName(), new ConfigSerializer());
		customSerializerMap.put(MapRegion.class.getCanonicalName(), new MapRegion.MapRegionSerializer());
		customSerializerMap.put(Location.class.getCanonicalName(), new Location.Serializer());
		customSerializerMap.put(LocationUpdateConfig.class.getCanonicalName(), new LocationUpdateConfigSerializer());
		customSerializerMap.put(Run.class.getCanonicalName(), new RunSerializer());
		customSerializerMap.put(RunList.class.getCanonicalName(), new RunListSerializer());
		customSerializerMap.put(RunAccess.class.getCanonicalName(), RunAccess.serializer);
		customSerializerMap.put(RunAccessList.class.getCanonicalName(), RunAccessList.serializer);
		customSerializerMap.put(RunBean.class.getCanonicalName(), new RunBeanSerialiser());
		customSerializerMap.put(GeneralItemVisibility.class.getCanonicalName(), GeneralItemVisibility.serializer);
		customSerializerMap.put(GeneralItemVisibilityList.class.getCanonicalName(), GeneralItemVisibilityList.serializer);
		customSerializerMap.put(Message.class.getCanonicalName(), Message.serializer);
        customSerializerMap.put(VariableInstance.class.getCanonicalName(), VariableInstance.serializer);

		customSerializerMap.put(Action.class.getCanonicalName(), new ActionSerializer());
		customSerializerMap.put(ActionList.class.getCanonicalName(), new ActionListSerializer());
		customSerializerMap.put(Response.class.getCanonicalName(), new ResponseSerializer());
		customSerializerMap.put(ResponseList.class.getCanonicalName(), new ResponseListSerializer());
		customSerializerMap.put(UserScore.class.getCanonicalName(), new UserScoreSerializer());
		customSerializerMap.put(AudioObject.class.getCanonicalName(), new AudioObjectSerializer());
		customSerializerMap.put(VideoObject.class.getCanonicalName(), new VideoObjectSerializer());
		customSerializerMap.put(MultipleChoiceTest.class.getCanonicalName(), MultipleChoiceTest.serializer);
		customSerializerMap.put(SingleChoiceTest.class.getCanonicalName(), SingleChoiceTest.serializer);
		customSerializerMap.put(SingleChoiceImageTest.class.getCanonicalName(), SingleChoiceImageTest.serializer);
		customSerializerMap.put(MultipleChoiceImageTest.class.getCanonicalName(), MultipleChoiceImageTest.serializer);
		customSerializerMap.put(MultipleChoiceAnswerItem.class.getCanonicalName(), new MultipleChoiceAnswerItemSerializer());
		customSerializerMap.put(MultipleChoiceImageAnswerItem.class.getCanonicalName(), MultipleChoiceImageAnswerItem.serializer);
		customSerializerMap.put(OpenUrl.class.getCanonicalName(), OpenUrl.serializer);
		customSerializerMap.put(ScanTag.class.getCanonicalName(), ScanTag.serializer);
        customSerializerMap.put(FileReference.class.getCanonicalName(), FileReference.serializer);

        customSerializerMap.put(ObjectCollectionDisplay.class.getCanonicalName(), ObjectCollectionDisplay.objectCollectionDisplaySerializer);
        customSerializerMap.put(ObjectCollectionDisplay.DisplayObject.class.getCanonicalName(), ObjectCollectionDisplay.displayObjectSerializer);
        customSerializerMap.put(ObjectCollectionDisplay.DisplayZone.class.getCanonicalName(), ObjectCollectionDisplay.displayZoneSerializer);

        customSerializerMap.put(Category.class.getCanonicalName(), Category.serializer);
        customSerializerMap.put(CategoryList.class.getCanonicalName(), CategoryList.serializer);
        customSerializerMap.put(GameCategory.class.getCanonicalName(), GameCategory.serializer);
        customSerializerMap.put(GameCategoryList.class.getCanonicalName(), GameCategoryList.serializer);

        customSerializerMap.put(YoutubeObject.class.getCanonicalName(), YoutubeObject.serializer);
		customSerializerMap.put(OpenBadge.class.getCanonicalName(), OpenBadge.serializer);
		customSerializerMap.put(OpenBadgeAssertion.class.getCanonicalName(), OpenBadgeAssertion.serializer);
		customSerializerMap.put(User.class.getCanonicalName(), User.serializer);
		customSerializerMap.put(UserList.class.getCanonicalName(), new UserListSerializer());
		customSerializerMap.put(Team.class.getCanonicalName(), new TeamSerializer());
		customSerializerMap.put(TeamList.class.getCanonicalName(), new TeamListSerializer());
		customSerializerMap.put(RunModification.class.getCanonicalName(), new RunModificationSerializer());
		customSerializerMap.put(GameModification.class.getCanonicalName(), GameModification.serializer);
		customSerializerMap.put(TeamModification.class.getCanonicalName(), TeamModification.serializer);
		customSerializerMap.put(GeneralItemModification.class.getCanonicalName(), new GeneralItemModificationSerializer());
		customSerializerMap.put(AuthResponse.class.getCanonicalName(), new AuthResponseSerializer());
		customSerializerMap.put(Version.class.getCanonicalName(), new VersionSerializer());
		customSerializerMap.put(LocationUpdate.class.getCanonicalName(), new LocationUpdateSerializer());
		customSerializerMap.put(RunPackage.class.getCanonicalName(), new RunPackage.RunPackageSerializer());
		customSerializerMap.put(Ping.class.getCanonicalName(), new Ping.Serializer());
		customSerializerMap.put(Pong.class.getCanonicalName(), new Pong.Serializer());
		customSerializerMap.put(APNDeviceDescription.class.getCanonicalName(), new APNDeviceDescription.Serializer());
		customSerializerMap.put(Account.class.getCanonicalName(), Account.serializer);
		customSerializerMap.put(AccountList.class.getCanonicalName(), AccountList.serializer);
		customSerializerMap.put(OauthInfo.class.getCanonicalName(), OauthInfo.serializer);
		customSerializerMap.put(OauthInfoList.class.getCanonicalName(), OauthInfoList.serializer);
		customSerializerMap.put(GCMDeviceDescription.class.getCanonicalName(), new GCMDeviceDescription.Serializer());
		customSerializerMap.put(DeviceDescriptionList.class.getCanonicalName(), new DeviceDescriptionList.Serializer());
	}
	
	public static JSONObject serialiseToJson(Object bean) {
		if (bean == null) {
			logger.log(Level.SEVERE, "bean is null");
			return null;
		}
		return serialiseToJson(bean, getCustomSerialiser(bean));
//		JsonBean customSerialiser = getCustomSerialiser(bean);
//		if (customSerialiser !=null) {
//			return customSerialiser.toJSON(bean);
//		}
//		
//		System.out.println("custom serialiser missing for "+bean.getClass());
//		throw new NullPointerException("serializer missing");
//		return null;
	}
	
	public static JSONObject serialiseToJson(Object bean, JsonBean customSerialiser) {
		
		if (customSerialiser !=null) {
			return customSerialiser.toJSON(bean);
		}
		
		System.out.println("custom serialiser missing for "+bean.getClass());
		throw new NullPointerException("serializer missing");
//		return null;
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
