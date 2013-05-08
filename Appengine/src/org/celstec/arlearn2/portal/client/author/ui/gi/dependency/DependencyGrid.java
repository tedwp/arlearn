package org.celstec.arlearn2.portal.client.author.ui.gi.dependency;

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

	public DependencyGrid(ActionForm actionForm) {
		this.actionForm = actionForm;
		setWidth(200);
		setHeight(200);
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
			case AndDependencyTreeGrid.TYPE:
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
			case AndDependencyTreeGrid.TYPE:
				return AndDependencyTreeGrid.getJson(childJsons);
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

}
