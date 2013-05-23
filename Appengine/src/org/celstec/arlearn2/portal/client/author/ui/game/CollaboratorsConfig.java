package org.celstec.arlearn2.portal.client.author.ui.game;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.CollaboratorModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.ContactModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.UserModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.ContactsDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameCollaboratorDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.CollaborationClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.ui.grid.GenericListGrid;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;

import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.MultiComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

public class CollaboratorsConfig extends SectionConfig {
	private GenericListGrid collabGrid;
	// private GenericListGrid teamGrid;
//	private Run run;
	private DynamicForm addCollaboratorForm;
	private Game currentGame;


	public CollaboratorsConfig() {
		super("Add Collaborators");
		VLayout collabLayout = new VLayout();
//		collabLayout.setWidth(200);
		
		HStack layout = new HStack();
		LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(10);

		layout.addMember(getCollaboratorsGrid());
		layout.addMember(vSpacer);
		layout.addMember(collabLayout);
		
		collabLayout.addMember(getAddPlayerForm());
		collabLayout.addMember(getInviteForm());
		
		layout.setAlign(Alignment.CENTER);
		layout.setPadding(5);
		setItems(layout);
		ContactsDataSource.getInstance().loadDataFromWeb();
	}

	private Canvas getInviteForm() {

		final DynamicForm form = new DynamicForm();
		form.setGroupTitle("Invite new contact");
		form.setIsGroup(true);
		form.setWidth(300);

		TextItem subjectItem = new TextItem("Contact");
		subjectItem.setTitle("Contact Email");

		ButtonItem saveButton = new ButtonItem("Save");
		saveButton.setTitle("Invite");
		saveButton.setColSpan(2);
		saveButton.setAlign(Alignment.CENTER);

		form.setFields(subjectItem, saveButton);

		saveButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				CollaborationClient.getInstance().addContactViaEmail(form.getValueAsString("Contact"), new JsonCallback() {
				});
				form.setValue("Contact", "");
			}
		});
		form.setWidth(300);
		return form;
	}
	private Canvas getCollaboratorsGrid() {
		collabGrid = new GenericListGrid(false, true, false, false, false) {
			protected void deleteItem(ListGridRecord rollOverRecord) {
				
				GameClient.getInstance().removeAccess(currentGame.getGameId(), rollOverRecord.getAttribute(CollaboratorModel.ACCOUNT_FIELD), new JsonCallback() {
					public void onJsonReceived(JSONValue jsonValue) {
						
					};
				});
				GameCollaboratorDataSource.getInstance().removeData(rollOverRecord);
			}
		};
		collabGrid.setWidth("30%");
		collabGrid.setShowRollOverCanvas(true);

		collabGrid.setAutoFetchData(true);

		collabGrid.setDataSource(GameCollaboratorDataSource.getInstance());
		ListGridField pictureField = new ListGridField(CollaboratorModel.PICTURE_FIELD, "Pic", 40);
		pictureField.setAlign(Alignment.CENTER);
		pictureField.setType(ListGridFieldType.IMAGE);

		ListGridField nameField = new ListGridField(ContactModel.NAME_FIELD,"name");
		ListGridField accessField = new ListGridField(CollaboratorModel.ACCESS_PICTURE, "Game Access");
		accessField.setType(ListGridFieldType.IMAGE);
		
		collabGrid.setFields(new ListGridField[] { pictureField, nameField, accessField });
		
		return collabGrid;
	}

	private Canvas getAddPlayerForm() {
		final CheckboxItem canEdit = new CheckboxItem("canEdit", "Can edit");

		addCollaboratorForm = new DynamicForm();
		addCollaboratorForm.setWidth("30%");
		addCollaboratorForm.setGroupTitle("Add Collaborator");
		addCollaboratorForm.setIsGroup(true);

		final MultiComboBoxItem playersComboBox = new MultiComboBoxItem(
				ContactModel.LOCAL_ID_FIELD, "Select accounts");
		playersComboBox.setDisplayField(ContactModel.NAME_FIELD);
		playersComboBox.setValueField(ContactModel.ACCOUNT_FIELD);
		playersComboBox.setAutoFetchData(true);
		playersComboBox.setOptionDataSource(ContactsDataSource.getInstance());
		// playersComboBox.setShowTitle(false);

		ButtonItem submitButton = new ButtonItem("Submit");
		submitButton.setTitle("Submit Players");
		submitButton.setColSpan(2);
		submitButton.setAlign(Alignment.CENTER);

		submitButton
				.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

					@Override
					public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
						int editRights = 3;
						if(canEdit.getValue()!= null && ((Boolean) canEdit.getValue())) {
							editRights = 2;
						}
						for (String account : playersComboBox.getValues()) {
							GameClient.getInstance().addAccess(currentGame.getGameId(), account, editRights, new JsonCallback() {
								public void onJsonReceived(JSONValue jsonValue) {
									GameCollaboratorDataSource.getInstance().loadDataFromWeb(currentGame.getGameId());
								};
							});
						}
						playersComboBox.setValues();
					}
				});
		addCollaboratorForm.setFields(canEdit, playersComboBox, submitButton);
		addCollaboratorForm.setWidth(300);
		return addCollaboratorForm;
	}
	public void loadDataFromRecord(Game game) {
		
		GameCollaboratorDataSource.getInstance().loadDataFromWeb(game.getGameId());
		Criteria criteria = new Criteria();
		criteria.addCriteria(GameModel.GAMEID_FIELD, game.getGameId());
		collabGrid.filterData(criteria);
		currentGame = game;
//		loadGameAccess();
	}
}
