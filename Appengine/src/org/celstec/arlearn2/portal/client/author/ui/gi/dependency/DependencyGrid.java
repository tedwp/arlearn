package org.celstec.arlearn2.portal.client.author.ui.gi.dependency;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.events.RemoveRecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RemoveRecordClickHandler;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;
import com.smartgwt.client.widgets.tree.events.NodeClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeClickHandler;

public class DependencyGrid extends TreeGrid {

	private Tree tree;
	ActionForm actionForm;
	ProximityDependencyEditor proxEditor;

	public DependencyGrid(ActionForm actionForm, ProximityDependencyEditor proxEditor) {
		this.actionForm = actionForm;
		this.proxEditor = proxEditor;
		setWidth(200);
//		setHeight("*");
		setHeight100();
		setShowEdges(true);
		setBorder("0px");
		setBodyStyleName("normal");
		setShowHeader(false);
		setLeaveScrollbarGap(false);
		setEmptyMessage("<br>Drag & drop parts here");
		setManyItemsImage("cubes_all.png");
		setAppImgDir("pieces/16/");
		setCanReorderRecords(true);
		setCanAcceptDroppedRecords(true);
		setCanDragRecordsOut(true);
		setCanRemoveRecords(true);
		addFolderDropHandler(new FolderDropHandler() {

			@Override
			public void onFolderDrop(FolderDropEvent event) {
				System.out.println("folder dropped " + event.getSourceWidget().getClassName());
				// event.getNodes()[event.getIndex()].setID(""+System.currentTimeMillis());
				event.getNodes()[event.getIndex()].setAttribute("Name", "" + System.currentTimeMillis());
				System.out.println("rec number : " + event.getNodes()[event.getIndex()]);
				// event.getNodes()[event.getIndex()].setTitle("And dep");

			}
		});
		addNodeClickHandler(new NodeClickHandler() {

			@Override
			public void onNodeClick(NodeClickEvent event) {

				DependencyGrid.this.actionForm.hideActionForm();

				if (event.getNode().getAttributeAsInt("typeInt") == ActionDependencyNode.TYPE) {
					DependencyGrid.this.actionForm.showActionForm();
					DependencyGrid.this.actionForm.setTreeNode(event.getNode(), tree);
				} else 	if (event.getNode().getAttributeAsInt("typeInt") == ProximityDependencyNode.TYPE) {
					DependencyGrid.this.proxEditor.showProximityDependencyNode();
					DependencyGrid.this.proxEditor.setTreeNode(event.getNode(), tree);
				}

			}
		});

		addRemoveRecordClickHandler(new RemoveRecordClickHandler() {

			@Override
			public void onRemoveRecordClick(RemoveRecordClickEvent event) {
				cleanTree(tree.getChildren(tree.getRoot()));
			}
		});
		tree = new Tree();
		tree.setModelType(TreeModelType.CHILDREN);
		tree.setNameProperty("Name");
		setData(tree);
		// setAutoFetchData(true);

	}

	private void cleanTree(TreeNode[] nodes) {
		for (TreeNode node : nodes) {
			cleanTree(node);
		}

	}

	private void cleanTree(TreeNode node) {
		TreeNode[] childNodes = tree.getChildren(node);
		if (node.getAttributeAsInt("typeInt") != null) {
			switch (node.getAttributeAsInt("typeInt")) {
			case AndDependencyTreeNode.TYPE:
				cleanTree(childNodes);
				break;
			case OrDependencyTreeNode.TYPE:
				cleanTree(childNodes);
				break;
			case ActionDependencyNode.TYPE:
				if (childNodes.length != 4) {
					tree.remove(node);
					redraw();
				}
				break;
			case ProximityDependencyNode.TYPE:
				if (childNodes.length != 3) {
					tree.remove(node);
					redraw();
				}
				break;
			default:
				break;
			}
		}
	}

	public void update() {
		setData(tree);
		redraw();
	}

	public JSONObject getJson() {
		if (tree.getRoot() != null) {
			TreeNode[] nodes = tree.getChildren(tree.getRoot());
			if (nodes.length > 0) {
				return getJson(nodes[0]);
			}
		}
		return new JSONObject();
	}

	private JSONObject getJson(TreeNode node) {
		TreeNode[] childNodes = tree.getChildren(node);
		JSONObject[] childJsons = new JSONObject[childNodes.length];
		for (int i = 0; i < childNodes.length; i++) {
			childJsons[i] = getJson(childNodes[i]);
		}
		if (node.getAttributeAsInt("typeInt") != null) {
			switch (node.getAttributeAsInt("typeInt")) {
			case AndDependencyTreeNode.TYPE:
				return AndDependencyTreeNode.getJson(childJsons);
			case OrDependencyTreeNode.TYPE:
				return OrDependencyTreeNode.getJson(childJsons);
			case ActionDependencyNode.TYPE:
				return ActionDependencyNode.getJson(childNodes);
			default:
				break;
			}
		}

		return new JSONObject();
	}

	public void loadJson(JSONObject object) {
		loadJson(tree.getRoot(), object);
	}
	
	public void loadJson(TreeNode context, JSONObject object) {
		if (object.get("type").isString().stringValue().equals(AndDependencyTreeNode.DEP_TYPE)) {
			AndDependencyTreeNode node = new AndDependencyTreeNode();
			tree.add(node, context);
			JSONArray array = object.get("dependencies").isArray();
			for (int i = 0;i<array.size();i++) {
				loadJson(node, array.get(i).isObject());
			}
		} else if (object.get("type").isString().stringValue().equals(OrDependencyTreeNode.DEP_TYPE)) {
			System.out.println("jsonobject to analyse "+object);
			
			OrDependencyTreeNode node = new OrDependencyTreeNode();
			tree.add(node, context);
			JSONArray array = object.get("dependencies").isArray();
			for (int i = 0;i<array.size();i++) {
				loadJson(node, array.get(i).isObject());
			}
		} else if (object.get("type").isString().stringValue().equals(ActionDependencyNode.DEP_TYPE)) {
			ActionDependencyNode node = new ActionDependencyNode();
			tree.add(node, context);
			for (TreeNode tn : tree.getChildren(node)) {
				if (tn.getAttribute("type").equals(ActionDependencyNode.ACTION)) {
					if (object.containsKey("action")){
						tn.setAttribute("Name", "Action = " + object.get("action").isString().stringValue());
						tn.setAttribute("Value", object.get("action").isString().stringValue());
					}
				}
				if (tn.getAttribute("type").equals(ActionDependencyNode.GENERALITEM)) {
					if (object.containsKey("generalItemId")){
						tn.setAttribute("Name", "ItemId = " + (long) object.get("generalItemId").isNumber().doubleValue());
						tn.setAttribute("Value", (long) object.get("generalItemId").isNumber().doubleValue());
					}
				}
				if (tn.getAttribute("type").equals(ActionDependencyNode.SCOPE)) {
					if (object.containsKey("scope")){
					tn.setAttribute("Name", "Scope = " + (int) object.get("scope").isNumber().doubleValue());
					tn.setAttribute("Value", (int) object.get("scope").isNumber().doubleValue());
					}
				}
				if (tn.getAttribute("type").equals(ActionDependencyNode.ROLE)) {
					if (object.containsKey("role")){
						tn.setAttribute("Name", "Role = " + object.get("role").isString().stringValue());
						tn.setAttribute("Value", object.get("role").isString().stringValue());
					}
				}
			}
		}
		
	}

}
