package org.celstec.arlearn2.jdo.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.UserList;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.UserJDO;
import org.datanucleus.store.appengine.query.JDOCursorHelper;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

public class UserManager {

	private static final String params[] = new String[]{"name", "email", "teamId", "runId"};
	private static final String paramsNames[] = new String[]{"nameParam", "emailParam", "teamIdParam", "runIdParam"};
	private static final String types[] = new String[]{"String", "String", "String", "Long"};

	public static void addUser(User bean) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserJDO user = new UserJDO();
		user.setTeamId(bean.getTeamId());
		user.setName(bean.getName());
		user.setRunId(bean.getRunId());
		user.setEmail(bean.getEmail());
		user.setLastModificationDate(System.currentTimeMillis());

		user.setId();
		JsonBeanSerialiser jbs = new JsonBeanSerialiser(bean);
		user.setPayload(new Text(jbs.serialiseToJson().toString()));
		try {
			pm.makePersistent(user);
		} finally {
			pm.close();
		}

	}
	
	public static List<User> getUserList(String name, String email, String teamId, Long runId) {
		ArrayList<User> returnProgressDefinitions = new ArrayList<User>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Iterator<UserJDO> it = getUsers(pm, name, email, teamId, runId).iterator();
		while (it.hasNext()) {
			returnProgressDefinitions.add(toBean((UserJDO) it.next()));
		}
		return returnProgressDefinitions;
	}
	
	public static List<UserJDO> getUsers(PersistenceManager pm, String name, String email, String teamId, Long runId) {
		Query query = pm.newQuery(UserJDO.class);
		Object args [] ={name, email, teamId, runId};
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return  (List<UserJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));
	}
	
	@Deprecated
	public static UserList getUsers(Long runId, String teamId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			
			Query query = pm.newQuery(UserJDO.class);
			Iterator<UserJDO> it = null;
			if (teamId != null) {
				query.setFilter("runId == runIdParam && teamId == teamIdParam");
				query.declareParameters("Long runIdParam, String teamIdParam");
				it = ((List<UserJDO>) query.executeWithArray(runId, teamId)).iterator();

			} else {
				query.setFilter("runId == runIdParam ");
				query.declareParameters("Long runIdParam");
				it = ((List<UserJDO>) query.executeWithArray(runId)).iterator();
			}
			UserList returnList = new UserList();
			returnList.setRunId(runId);
			while (it.hasNext()) {
				returnList.addUser(toBean((UserJDO) it.next()));
			}
			return returnList;
		} finally {
			pm.close();
		}
	}

	public static User getUser(Long runId, String email) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			return toBean(getUserJDO(pm, runId, email));
		} finally {
			pm.close();
		}
	}

	private static UserJDO getUserJDO(PersistenceManager pm, Long runId, String email) {
		try {
			Key k = KeyFactory.createKey(UserJDO.class.getSimpleName(), UserJDO.generateId(email, runId));
			UserJDO userJDO = pm.getObjectById(UserJDO.class, k);
			return userJDO;
		} catch (Exception e) {
			return null;
		}
	}

	private static User toBean(UserJDO jdo) {
		if (jdo == null)
			return null;
		User userBean = null;
		try {
			JsonBeanDeserializer jbd = new JsonBeanDeserializer(jdo.getPayload().getValue());
			userBean = (User) jbd.deserialize(User.class);
		} catch (Exception e) {
			e.printStackTrace();
			userBean = new User();
		}
		userBean.setName(jdo.getName());
		userBean.setRunId(jdo.getRunId());
		userBean.setTeamId(jdo.getTeamId());
		userBean.setEmail(jdo.getEmail());
		userBean.setDeleted(jdo.getDeleted());
		return userBean;
	}

	public static void deleteUser(Long runId, String email) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			UserJDO userJDO = getUserJDO(pm, runId, email);
			if (userJDO != null) {
				pm.deletePersistent(userJDO);
			}
		} finally {
			pm.close();
		}
	}
	
	public static void deleteUser(PersistenceManager pm, UserJDO userJDO) {
		pm.deletePersistent(userJDO);
	}
	
	public static void setStatusDeleted(long runId, String email) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<UserJDO> deleteList = getUsers(pm, null, email, null, runId);
			for (UserJDO jdo: deleteList) {
				jdo.setDeleted(true);
				jdo.setLastModificationDate(System.currentTimeMillis());
			}
		} finally {
			pm.close();
		}
	}
	private final static int LIMIT = 20;

	
	public static List<UserJDO> listAllUsers(PersistenceManager pm, String cursorString) {
		javax.jdo.Query query = pm.newQuery(UserJDO.class);
		if (cursorString != null) {
			Cursor cursor = Cursor.fromWebSafeString(cursorString);
			Map<String, Object> extensionMap = new HashMap<String, Object>();
			extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
			query.setExtensions(extensionMap);
		}
		query.setRange(0, LIMIT);
		return (List<UserJDO>) query.execute();
	}

	

	
//	private static User toBean(UserJDO jdo) {
//		if (jdo == null) return null;
//		User pd = new User();
//		pd.setName(jdo.getName());
//		pd.setEmail(jdo.getEmail());
//		pd.setRunId(jdo.getRunId());
//		pd.setTeamId(jdo.getTeamId());
//		
//		return pd;
//	}
}
