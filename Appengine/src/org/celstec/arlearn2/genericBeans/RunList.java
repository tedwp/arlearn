package org.celstec.arlearn2.genericBeans;

import java.util.List;
import java.util.Vector;

public class RunList {

	private List<Run> runs = new Vector();

	public RunList() {

	}

	public List<Run> getRuns() {
		return runs;
	}

	public void setRuns(List<Run> runs) {
		this.runs = runs;
	}

	public void addRun(Run run) {
		runs.add(run);
	}

}
