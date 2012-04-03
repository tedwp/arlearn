package org.celstec.arlearn2.beans.run;

import java.util.List;
import java.util.Vector;

import org.celstec.arlearn2.beans.Bean;

public class RunList extends Bean{
	public static String runsType = "org.celstec.arlearn2.beans.run.Run";

	private List<Run> runs = new Vector<Run>();

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
