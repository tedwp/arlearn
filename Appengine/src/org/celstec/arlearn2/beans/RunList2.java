package org.celstec.arlearn2.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="runs")
public class RunList2 extends Bean implements Serializable{
	
	private List<Run> games = new Vector();

	public RunList2() {
		
	}
	
	@XmlElement(name="run")
	public List<Run> getRuns() {
		return games;
	}

	public void setRuns(List<Run> games) {
		this.games = games;
	}

	public void addRun(Run game) {
		games.add(game);
	}
	
	
	

}