package org.celstec.arlearn2.portal.client.author.ui.game;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.ContactModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.ContactsDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.CollaborationClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.ui.grid.GenericListGrid;
import org.celstec.arlearn2.portal.client.AuthoringConstants;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.MultiComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.LayoutSpacer;

public class CollaboratorsConfig extends SectionConfig {
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);
	// SelectItem roleGrid;
	private GenericListGrid listGrid;
	private Game currentGame;

	public CollaboratorsConfig() {
		super("Add Collaborators");
		HStack layout = new HStack();
		//
		// listGrid = new GenericListGrid(false, true, false, false, false){
		// // protected void deleteItem(ListGridRecord rollOverRecord) {
		// //
		// //
		// RoleConfigSection.this.deleteRole(rollOverRecord.getAttributeAsString(GameRoleModel.ROLE_FIELD));
		// // }
		// };
		// listGrid.setWidth(300);
		// listGrid.setCanDragRecordsOut(true);
		// listGrid.setCanAcceptDroppedRecords(true);
		// listGrid.setDragDataAction(DragDataAction.COPY);
		// listGrid.setAutoFetchData(true);
		//
		// listGrid.setDataSource(GameRolesDataSource.getInstance());
		// ListGridField roleField = new ListGridField(GameRoleModel.ROLE_FIELD,
		// "Role");
		// listGrid.setFields(new ListGridField[] { roleField });

		layout.addMember(getInviteForm());
		LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(10);
		layout.addMember(vSpacer);

		// GenericListGrid listGrid2 = new GenericListGrid(false, true, false,
		// false, false) {
		// // protected void deleteItem(ListGridRecord rollOverRecord) {
		// //
		// //
		// RoleConfigSection.this.deleteRole(rollOverRecord.getAttributeAsString(GameRoleModel.ROLE_FIELD));
		// // }
		// };
		// listGrid2.setDataSource(ContactsDataSource.getInstance());
		// listGrid2.setWidth(300);
		// listGrid2.setCanDragRecordsOut(true);
		// listGrid2.setCanAcceptDroppedRecords(true);
		// listGrid2.setDragDataAction(DragDataAction.COPY);
		// listGrid2.setAutoFetchData(true);
		//
		// ListGridField roleField2 = new ListGridField(ContactModel.NAME_FIELD,
		// "Role");
		// listGrid2.setFields(new ListGridField[] { roleField2 });
		// layout.addMember(listGrid2);

		layout.addMember(canWriteForm());
		layout.addMember(vSpacer);
		layout.addMember(canReadForm());

		ContactsDataSource.getInstance().loadDataFromWeb();

		setItems(layout);
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

		return form;
	}

	private DynamicForm writeForm;
	private MultiComboBoxItem canWrite;
	private MultiComboBoxItem canRead;
	private DynamicForm readForm;

	private Canvas canWriteForm() {
		writeForm = new DynamicForm();
		canWrite = new MultiComboBoxItem("canwrite");
		canWrite.setDisplayField(ContactModel.NAME_FIELD);
		canWrite.setValueField(ContactModel.ACCOUNT_FIELD);
		canWrite.setAutoFetchData(true);
		canWrite.setOptionDataSource(ContactsDataSource.getInstance());
		canWrite.setShowTitle(false);
		canWrite.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String oldValue = "" + event.getOldValue();
				String newValue = "" + event.getValue();

				if (oldValue.equals("")) {
					canRead.setValues(removeValue(canWrite.getValues(), newValue));
					GameClient.getInstance().addAccess(currentGame.getGameId(), newValue, 2, new JsonCallback() {
						public void onJsonReceived(JSONValue jsonValue) {
						};
					});
				}
				if (newValue.equals("")) {
					GameClient.getInstance().removeAccess(currentGame.getGameId(), oldValue, new JsonCallback() {
						public void onJsonReceived(JSONValue jsonValue) {
						};
					});
				}
			}
		});

		writeForm.setFields(canWrite);
		writeForm.setWidth(240);
		writeForm.setGroupTitle("Can write");
		writeForm.setIsGroup(true);
		return writeForm;
	}

	private Canvas canReadForm() {
		readForm = new DynamicForm();
		canRead = new MultiComboBoxItem(ContactModel.LOCAL_ID_FIELD);
		canRead.setDisplayField(ContactModel.NAME_FIELD);
		canRead.setValueField(ContactModel.ACCOUNT_FIELD);
		canRead.setAutoFetchData(true);
		canRead.setOptionDataSource(ContactsDataSource.getInstance());
		canRead.setShowTitle(false);

		canRead.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String oldValue = "" + event.getOldValue();
				String newValue = "" + event.getValue();

				if (oldValue.equals("")) {
					canWrite.setValues(removeValue(canWrite.getValues(), newValue));
					GameClient.getInstance().addAccess(currentGame.getGameId(), newValue, 3, new JsonCallback() {
						public void onJsonReceived(JSONValue jsonValue) {
						};
					});
				}
				if (newValue.equals("")) {
					GameClient.getInstance().removeAccess(currentGame.getGameId(), oldValue, new JsonCallback() {
						public void onJsonReceived(JSONValue jsonValue) {
						};
					});
				}
			}
		});

		readForm.setFields(canRead);
		readForm.setWidth(240);
		readForm.setGroupTitle("Can read");
		readForm.setIsGroup(true);
		return readForm;
	}

	private String[] removeValue(String[] string, String value) {
		if (string.length == 0)
			return string;
		String[] returnString = new String[string.length - 1];
		int i = 0;
		for (String itValue : returnString) {
			if (!itValue.equals(value)) {
				returnString[i++] = itValue;
			}
		}
		return returnString;
	}

	public void loadDataFromRecord(Game game) {
		canWrite.setValues();
		canRead.setValues();
		GameClient.getInstance().getGamesAccessAccount(game.getGameId(), new JsonObjectListCallback("gamesAccess", null) {
			public void onJsonObjectReceived(JSONObject jsonObject) {
				System.out.println("gameAccess " + jsonObject);
				// System.out.println("gameAccess "+jsonObject);
				// canWrite.setValues("101754523769925754305");
				int accessRight = (int) jsonObject.get("accessRights").isNumber().doubleValue();
				String account = jsonObject.get("account").isString().stringValue();
				switch (accessRight) {
				case 2:
					canWrite.setValues(account, canWrite.getValues());
					break;
				case 3:
					canRead.setValues(account, canRead.getValues());
					break;
				default:
					break;
				}
			}
		});

		currentGame = game;
	}

	public void hideDetail() {
		// TODO Auto-generated method stub
		
	}
}
