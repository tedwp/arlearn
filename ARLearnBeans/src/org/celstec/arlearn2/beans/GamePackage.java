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
