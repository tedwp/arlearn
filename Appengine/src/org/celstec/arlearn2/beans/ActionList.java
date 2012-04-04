package org.celstec.arlearn2.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "actions")
@XmlType(name="Actions", namespace="http://celstec.org/arlearn2")
public class ActionList {
	

	//TODO: migrate this code
	
//	public List<Action> getMatchingActions(DependsOn dOn){
//		ArrayList<Action> returnList = new ArrayList<Action>();
//		for (Action a: actions) {
//			if (dependsOnConditionMatches(a, dOn)) returnList.add(a);
//		}
//		return returnList;
//	}
//	
//	private boolean dependsOnConditionMatches(Action action, DependsOn dOn) {
//		if (dOn.getAction() != null && !dOn.getAction().equals(action.getAction())) return false; 
//		if (dOn.getGeneralItemId() != null && !dOn.getGeneralItemId().equals(action.getGeneralItemId())) return false; 
//		if (dOn.getGeneralItemType() != null && !dOn.getGeneralItemType().equals(action.getGeneralItemType())) return false; 
//		return true;
//	}

}
