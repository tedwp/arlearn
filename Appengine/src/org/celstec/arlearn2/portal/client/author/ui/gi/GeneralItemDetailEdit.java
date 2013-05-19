package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.DependencyEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.ExtensionEditor;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class GeneralItemDetailEdit extends VLayout {

	private GeneralItem gi;

	protected VLayout infoLayout;

	protected GeneralItemEditStack stack = new GeneralItemEditStack();
	protected HLayout buttonLayout;
	protected IButton saveButton;
	protected ExtensionEditor extensionEditor;

	protected BasicMetadataEditor editor;
	protected DependencyEditor appearEdit;
	protected DependencyEditor disappearEdit;

	public GeneralItemDetailEdit() {
		createEditButton();

		createButtonLayout(saveButton);

		createBasicMetadataEditor();
//		createSectionStack();

		HLayout layout = new HLayout();
		layout.addMember(editor);
		layout.addMember(stack);
		setAlign(Alignment.LEFT);
		// setBorder("1px dashed blue");
		addMember(layout);
		addMember(buttonLayout);
	}

	private void createEditButton() {
		saveButton = new IButton("save");
		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				saveClick();
			}
		});
	}

	protected void saveClick() {
		editor.saveToBean(gi);
		if (extensionEditor != null) extensionEditor.saveToBean(gi);
		if (appearEdit != null) {
			JSONObject appearDep = appearEdit.getJson();
			if (appearDep != null) gi.getJsonRep().put("dependsOn", appearDep);
		}
		if (disappearEdit != null) {
			JSONObject disappearDep = disappearEdit.getJson();
			if (disappearDep != null) gi.getJsonRep().put("disappearOn", disappearDep);
		}
		GeneralItemsClient.getInstance().createGeneralItem(gi, new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				GeneralItemDataSource.getInstance().loadDataFromWeb(gi.getValueAsLong(GameModel.GAMEID_FIELD));
			}

		});
	}

	private void createButtonLayout(IButton... buttons) {
		buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.setHeight(40);
		for (IButton but : buttons) {
			buttonLayout.addMember(but);
		}

	}

//	private void createSectionStack() {
//		createGeneralItemExensionMetadataSectionStack();
//
//		stack = new SectionStack();
//		stack.addSection(extensionstack);
//		SectionStackSection appearStack = new SectionStackSection("Appear");
//		appearStack.setItems(new AdvancedDependenciesEditor());
//		stack.addSection(appearStack);
//	}

	public void createBasicMetadataEditor() {
		editor = new BasicMetadataEditor(true, true);
		editor.setHeight100();
	}

//	public void createGeneralItemExensionMetadataSectionStack() {
//		extensionstack = new SectionStackSection("Video Object");
//	}

	public void loadGeneralItem(GeneralItem gi) {
		this.gi = gi;
		editor.loadGeneralItem(gi);
		extensionEditor = (ExtensionEditor) gi.getMetadataExtensionEditor();
		disappearEdit = new DependencyEditor();
		if (gi.getJsonRep().containsKey("disappearOn")){
			disappearEdit.loadGeneralItem(gi.getJsonRep().get("disappearOn").isObject());
		}
		appearEdit =  new DependencyEditor();
		if (gi.getJsonRep().containsKey("dependsOn")){
			appearEdit.loadGeneralItem(gi.getJsonRep().get("dependsOn").isObject());
		}
		if (extensionEditor != null) {
			stack.setExtensionStack(gi, extensionEditor);
		}
		stack.setAppearStack(appearEdit);
		stack.setDisappearStack(disappearEdit);
		
	}
}
