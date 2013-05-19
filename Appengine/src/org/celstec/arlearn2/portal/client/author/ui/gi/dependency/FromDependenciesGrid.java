package org.celstec.arlearn2.portal.client.author.ui.gi.dependency;

import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.NodeClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeClickHandler;

public class FromDependenciesGrid extends TreeGrid {

	public FromDependenciesGrid() {
		setWidth(200);
		// setHeight("*");
		setShowEdges(true);
		setBorder("0px");
		setBodyStyleName("normal");
		setShowHeader(false);
		setLeaveScrollbarGap(false);
		setManyItemsImage("cubes_all.png");
		setAppImgDir("pieces/16/");
		setCanReorderRecords(true);
		setCanAcceptDroppedRecords(false);
		setCanDragRecordsOut(true);
		setDragDataAction(DragDataAction.COPY);
		setData();

		addNodeClickHandler(new NodeClickHandler() {

			@Override
			public void onNodeClick(NodeClickEvent event) {
				System.out.println("click node" + event.getNode().getClass());
				System.out.println("click node" + event.getRecordNum());

			}
		});
	}

	public void setData() {
		Tree grid1Tree = new Tree();
		grid1Tree.setModelType(TreeModelType.CHILDREN);
		grid1Tree.setNameProperty("Name");
		grid1Tree.setRoot(new TreeNode("Root", new ActionDependencyNode(), new ProximityDependencyNode(),
				new OrDependencyTreeNode(), new AndDependencyTreeGrid()));

		setData(grid1Tree);
		getData().openAll();
	}

}
