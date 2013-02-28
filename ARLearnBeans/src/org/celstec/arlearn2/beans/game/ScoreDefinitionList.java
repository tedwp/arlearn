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
