package org.celstec.arlearn2.portal.client.author.ui.gi.dependency;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.widgets.tree.TreeNode;

public class OrDependencyTreeNode extends DependencyTreeGrid{
	public static final String DEP_TYPE = "org.celstec.arlearn2.beans.dependencies.OrDependency";

	public static final int TYPE = 3;

	
	public OrDependencyTreeNode() {
		 setAttribute("Name", "Or Dependency");
		 setAttribute("typeInt", TYPE);
		 setAttribute("children", new TreeNode[]{}); 
         setCanAcceptDrop(true);
         setTitle("Or Dependency");
	}
	
	public static JSONObject getJson(JSONObject[] childObjects) {
		JSONObject dep = new JSONObject();
		dep.put("type", new JSONString("org.celstec.arlearn2.beans.dependencies.OrDependency"));
		JSONArray array = new JSONArray();
		for (int i =0; i<childObjects.length; i++) {
			array.set(i, childObjects[i]);	
		}
		dep.put("dependencies", array);
		return dep;
	}
}
