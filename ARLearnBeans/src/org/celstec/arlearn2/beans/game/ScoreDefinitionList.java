package org.celstec.arlearn2.beans.game;

import java.util.ArrayList;
import java.util.List;


public class ScoreDefinitionList {

	public static String scoreDefinitionsType = "org.celstec.arlearn2.beans.game.ScoreDefinition";
	
	private List<ScoreDefinition> scoreDefinitions = new ArrayList<ScoreDefinition>();

	public ScoreDefinitionList() {
	}

	public List<ScoreDefinition> getScoreDefinitions() {
		return scoreDefinitions;
	}

	public void setScoreDefinitions(List<ScoreDefinition> scoreDefinitions) {
		this.scoreDefinitions = scoreDefinitions;
	}
	
	public void addScoreDefinition(ScoreDefinition sd) {
		scoreDefinitions.add(sd);
	}
	
	public void addScoreDefinition(List<ScoreDefinition> sds) {
		this.scoreDefinitions.addAll(sds);
	}
}
