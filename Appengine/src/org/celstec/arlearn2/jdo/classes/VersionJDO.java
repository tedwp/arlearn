package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class VersionJDO {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key primKey;
	
	@Persistent
	private Integer versionCode;

	@Persistent
	private Text payLoad;

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.primKey = KeyFactory.createKey(VersionJDO.class.getSimpleName(), versionCode);
		this.versionCode = versionCode;
	}

	public String getPayLoad() {
		return payLoad.getValue();
	}

	public void setPayLoad(String payLoad) {
		this.payLoad = new Text(payLoad);
	}

	
	
	
}
