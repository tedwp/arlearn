package org.celstec.arlearn2.android.db.beans;

import java.util.List;
import java.util.Vector;

public class RunListDeprecated {
	public static String runsType = "org.celstec.arlearn2.android.db.beans.Run";
	
	private List<RunDeprecated> runs = new Vector();

	public RunListDeprecated() {

	}

	public List<RunDeprecated> getRuns() {
		return runs;
	}

	public void setRuns(List<RunDeprecated> runs) {
		this.runs = runs;
	}

	public void addRun(RunDeprecated run) {
		runs.add(run);
	}

}
