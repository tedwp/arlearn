package org.celstec.arlearn2.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.ScoreDefinition;
import org.celstec.arlearn2.beans.game.ScoreDefinitionList;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

public class GamePackage extends Bean{

	private Game game;
	
	private List<GeneralItem> generalItems = new Vector();
	
	private List<ScoreDefinition> scoreDefinitions = new ArrayList<ScoreDefinition>();
	
	public GamePackage() {
		
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public List<GeneralItem> getGeneralItems() {
		return generalItems;
	}

	public void setGeneralItems(List<GeneralItem> generalItems) {
		this.generalItems = generalItems;
	}
	
	public void addGeneralItem(GeneralItem generalItem) {
		this.generalItems.add(generalItem);
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
