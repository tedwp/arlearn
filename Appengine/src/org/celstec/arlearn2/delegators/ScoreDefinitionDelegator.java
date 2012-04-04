package org.celstec.arlearn2.delegators;

import java.util.List;

import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.game.ScoreDefinition;
import org.celstec.arlearn2.cache.ScoreDefinitionCache;
import org.celstec.arlearn2.jdo.manager.ScoreDefinitionManager;

import com.google.gdata.util.AuthenticationException;

public class ScoreDefinitionDelegator extends GoogleDelegator {

	public ScoreDefinitionDelegator(String authToken) throws AuthenticationException {
		super(authToken);
	}

	public ScoreDefinitionDelegator(GoogleDelegator gd) {
		super(gd);
	}
	
	public ScoreDefinition createScoreDefinition(ScoreDefinition sd) {
		if (sd.getGameId() == null) {
			sd.setError("No game identifier specified");
			return sd;
		}
		ScoreDefinitionManager.addScoreDefinition(sd.getAction(), sd.getGeneralItemId(), sd.getGeneralItemType(), sd.getGameId(), sd.getScope(), sd.getValue());
		ScoreDefinitionCache.getInstance().removeScoreDefinitonList(sd.getGameId());
		return sd;
	}
	
	public List<ScoreDefinition> getScoreDefinitionsList(Long gameId, String action, Long generalItemId, String generalItemType) {
		List<ScoreDefinition> sdList = ScoreDefinitionCache.getInstance().getScoreDefinitionList(gameId, action, generalItemId, generalItemType);
		if (sdList == null) {
			sdList = ScoreDefinitionManager.getScoreDefinitions(gameId, action, generalItemId, generalItemType);
			ScoreDefinitionCache.getInstance().putScoreDefinitionList(sdList, gameId, action, generalItemId, generalItemType);
		}
		return sdList;
	}

	public ScoreDefinition getScoreDefinitionForAction(Long gameId, Action action) {
		List<ScoreDefinition> list = getScoreDefinitionsList(gameId, action.getAction(), action.getGeneralItemId(), action.getGeneralItemType());
		if (list.isEmpty())
			return null;
		return list.get(0);
	}
	
	public void deleteScoreDefinition(Long gameId) {
		List<ScoreDefinition> list = getScoreDefinitionsList(gameId, null, null, null);
		for (ScoreDefinition sd : list) {
			deleteScoreDefinition(sd);
		}
		
	}

	private void deleteScoreDefinition(ScoreDefinition sd) {
		ScoreDefinitionManager.deleteScoreDefinition(sd.getId());
		ScoreDefinitionCache.getInstance().removeScoreDefinitonList(sd.getGameId());
	}
	
}
