package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class ScoreDefinitionJDO extends GameClass {

	@Persistent
    private String generalItemId;
	
	@Persistent
    private String generalItemType;
	
	@Persistent
    private String action;
	
	@Persistent
    private String scope;
	
	@Persistent
    private Long value;

	public String getScoreId() {
		if (id.getName()!=null) return id.getName();
		return ""+id.getId();
	}
	
	public void setScoreId() {
		String key = getGameId()+":"+getGeneralItemId()+":"+getGeneralItemType()+":"+getAction()+":"+getScope();
		setScoreId(KeyFactory.createKey(ScoreDefinitionJDO.class.getSimpleName(), key));
	}
	
	public void setScoreId(Key gameId) {
		this.id = gameId;
	}
	
	public String getGeneralItemId() {
		return generalItemId;
	}

	public void setGeneralItemId(String generalItemId) {
		this.generalItemId = generalItemId;
	}

	public String getGeneralItemType() {
		return generalItemType;
	}

	public void setGeneralItemType(String generalItemType) {
		this.generalItemType = generalItemType;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}
	
	
	
}
