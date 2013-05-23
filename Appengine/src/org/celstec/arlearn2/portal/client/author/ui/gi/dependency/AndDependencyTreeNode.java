package org.celstec.arlearn2.portal.client.author.ui.gi.dependency;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.widgets.tree.TreeNode;

public class AndDependencyTreeNode extends DependencyTreeGrid{
	public static final String DEP_TYPE = "org.celstec.arlearn2.beans.dependencies.AndDependency";

	public static final int TYPE = 1;
	
	public AndDependencyTreeNode() {
		 setAttribute("Name", "And Dependency");
		 setAttribute("type", "AndDependency");
		 setAttribute("typeInt", TYPE);
		 setAttribute("children", new TreeNode[]{}); 
         setCanAcceptDrop(true);
         setTitle("And Dependency");
	}
	
	public static JSONObject getJson(JSONObject[] childObjects) {
		JSONObject dep = new JSONObject();
		dep.put("type", new JSONString(DEP_TYPE));
		JSONArray array = new JSONArray();
		for (int i =0; i<childObjects.length; i++) {
			array.set(i, childObjects[i]);	
		}
		dep.put("dependencies", array);
		return dep;
	}
	
}
