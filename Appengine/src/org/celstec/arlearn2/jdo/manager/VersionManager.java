package org.celstec.arlearn2.jdo.manager;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.Version;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.VersionJDO;
import org.codehaus.jettison.json.JSONException;

import com.google.appengine.api.datastore.KeyFactory;

public class VersionManager {

	
	public static Version addVersion(Version v) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		VersionJDO version = toJDO(v);
		try {
			return toBean(pm.makePersistent(version));
		} finally{
			pm.close();
		}
	}
	
	public static Version getVersion(Integer versionCode){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(VersionJDO.class);
		query.setFilter("versionCode  == versionCodeParam");
		query.declareParameters("Integer versionCodeParam");
		try {
			List<VersionJDO> results = (List<VersionJDO>) query.execute(versionCode);
			if (results.isEmpty()) return null;
			return toBean(results.get(0));
		
		} finally {
			query.closeAll();
			pm.close();
		}
		
	}

	private static Version toBean(VersionJDO versionJDO) {
		Version v;
		try {
			v = (Version) JsonBeanDeserializer.deserialize(versionJDO.getPayLoad());
		} catch (JSONException e) {
			v = new Version();
			v.setError(e.getMessage());
			e.printStackTrace();
		}
		v.setVersionCode(versionJDO.getVersionCode());
		return v;
	}

	private static VersionJDO toJDO(Version v) {
		VersionJDO jdo = new VersionJDO();
		jdo.setVersionCode(v.getVersionCode());
		jdo.setPayLoad(v.toString());
		return jdo;
	}
}
