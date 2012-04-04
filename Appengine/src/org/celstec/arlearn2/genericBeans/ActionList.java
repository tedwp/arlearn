package org.celstec.arlearn2.genericBeans;

import java.util.ArrayList;
import java.util.List;

public class ActionList {
	private List<Action> actions = new ArrayList<Action>();

	public ActionList() {

	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public void addAction(Action action) {
		actions.add(action);
	}
	
	public void addActions(List<Action> actions) {
		actions.addAll(actions);
	}
}
