package org.celstec.arlearn2.portal.client.author.ui.gi.dependency;

import java.util.LinkedHashMap;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameRoleModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameRolesDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ActionForm extends DynamicForm {

	protected SelectItem selectAction;
	protected SelectItem selectGeneralItem;
	protected SelectItem selectRole;
	protected SelectItem selectScope;

	private final String ACTION_DEP = "action";
	private final String ACTION_DEP_STRING = "actionString";
	private final String GENITEM_DEP = "generalItemId";
	private final String SCOPE_DEP = "scope";
	private final String ROLE_DEP = "role";

	private TreeNode actionTreeNode;
	private Tree actionTree;
	
	public ActionForm() {
		setGroupTitle("Action based Dependency");
		setIsGroup(true);
		initGeneralItem();
		initAction();
		initScope();
		initRole();

		ButtonItem saveButton = new ButtonItem("Save");
		saveButton.setTitle("Save Dependency");
		saveButton.setColSpan(2);
		saveButton.setAlign(Alignment.CENTER);

		setFields(selectGeneralItem, selectAction, selectScope, selectRole, saveButton);

		saveButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				ActionForm.this.setVisibility(Visibility.HIDDEN);
				onSave();
			}
		});
		ActionForm.this.setVisibility(Visibility.HIDDEN);
	}
	
	public void onSave() {
		if (actionTreeNode == null) return;
		for (TreeNode tn: actionTree.getChildren(actionTreeNode)) {
			if (tn.getAttribute("type").equals(ActionDependencyNode.ACTION)) {
				tn.setAttribute("Name", "Action = "+getValueAsString(ACTION_DEP));
				tn.setAttribute("Value", getValueAsString(ACTION_DEP));
			}
			if (tn.getAttribute("type").equals(ActionDependencyNode.GENERALITEM)) {
				tn.setAttribute("Name", "ItemId = "+getValueAsString(GENITEM_DEP));
				tn.setAttribute("Value", getValueAsString(GENITEM_DEP));
			}
			if (tn.getAttribute("type").equals(ActionDependencyNode.SCOPE)) {
				tn.setAttribute("Name", "Scope = "+getValueAsString(SCOPE_DEP));
				tn.setAttribute("Value", getValueAsString(SCOPE_DEP));
			}
			if (tn.getAttribute("type").equals(ActionDependencyNode.ROLE)) {
				tn.setAttribute("Name", "Role = "+getValueAsString(ROLE_DEP));
				tn.setAttribute("Value", getValueAsString(ROLE_DEP));
			}
		}
	
		actionTreeNode = null;
	}

	public void setTreeNode(TreeNode tn, Tree tree) {
		actionTreeNode = tn;
		actionTree = tree;
		
	}
	
	private void initAction() {
		selectAction = new SelectItem(ACTION_DEP);
		selectAction.setTitle("action");
		selectAction.setValueMap(createSimpleDependencyValues());
		selectAction.setWrapTitle(false);
		// selectAction.setShowIfCondition(actionFixedString);
		selectAction.setStartRow(true);
	}

	private void initScope() {
		selectScope = new SelectItem(SCOPE_DEP);
		selectScope.setTitle("scope");
		selectScope.setValueMap(createScopeDependencyValues());
		if (selectScope.getValue() == null)
			selectScope.setValue(0);
		// selectScope.setShowIfCondition(formIf);
		selectScope.setStartRow(true);
	}

	private void initRole() {

		selectRole = new SelectItem(ROLE_DEP);
		selectRole.setTitle("role");
		selectRole.setValueField(GameRoleModel.ROLE_FIELD);
		selectRole.setDisplayField(GameRoleModel.ROLE_FIELD);
		 selectRole.setOptionDataSource(GameRolesDataSource.getInstance());
		 selectRole.setAllowEmptyValue(true);
		// selectRole.setShowIfCondition(formIf);
		selectRole.setStartRow(true);
	}

	private void initGeneralItem() {
		selectGeneralItem = new SelectItem(GENITEM_DEP);
		selectGeneralItem.setTitle("item");
		selectGeneralItem.setWrapTitle(false);
		selectGeneralItem.setDisplayField(GeneralItemModel.NAME_FIELD);
		Criteria crit = new Criteria();
		crit.addCriteria("deleted", false);
		selectGeneralItem.setPickListCriteria(crit);
		selectGeneralItem.setValueField("id");
		 selectGeneralItem.setOptionDataSource(GeneralItemDataSource.getInstance());
		// selectGeneralItem.setShowIfCondition(formIf);
		selectGeneralItem.setStartRow(true);
		// selectGeneralItem.addChangedHandler(new ChangedHandler() {
		//
		// @Override
		// public void onChanged(ChangedEvent event) {
		// String id = selectGeneralItem.getValueAsString();
		// getActions(Long.parseLong(id), SIMPLE_DEP);
		// }
		// });
	}

	public void showActionForm() {
		ActionForm.this.setVisibility(Visibility.VISIBLE);

	}
	public void hideActionForm() {
		ActionForm.this.setVisibility(Visibility.HIDDEN);

	}

	public LinkedHashMap<String, String> createSimpleDependencyValues() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("read", "Read"); // TODO i18n
		valueMap.put("answer_given", "Answer given"); // TODO
		return valueMap;
	}

	public LinkedHashMap<String, String> createScopeDependencyValues() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("0", "User");
		valueMap.put("1", "Team");
		valueMap.put("2", "All");
		return valueMap;
	}
}
