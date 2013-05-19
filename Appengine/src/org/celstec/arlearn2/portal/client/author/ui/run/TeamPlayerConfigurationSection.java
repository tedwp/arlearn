package org.celstec.arlearn2.portal.client.author.ui.run;

import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemGameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.ContactModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.TeamModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.UserModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.ContactsDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.TeamDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.UserDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.AccountClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.UserClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Account;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Run;
import org.celstec.arlearn2.gwtcommonlib.client.ui.grid.GenericListGrid;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.MultiComboBoxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverEvent;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.LayoutSpacer;

public class TeamPlayerConfigurationSection extends SectionConfig {
	private GenericListGrid playersGrid;
//	private GenericListGrid teamGrid;
	private Run run;

	public TeamPlayerConfigurationSection() {
		super("Teams and Users");
		HStack layout = new HStack();
		LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(10);

//		layout.addMember(getTeamsGrid());
//		layout.addMember(vSpacer);
		layout.addMember(getPlayersGrid());
		layout.addMember(vSpacer);
		layout.addMember(getAddPlayerForm());
		layout.setAlign(Alignment.CENTER);
		layout.setPadding(5);
		setItems(layout);
	}

//	private Canvas getTeamsGrid() {
//		teamGrid = new GenericListGrid(false, true, false, false, false) {
//			protected void deleteItem(ListGridRecord rollOverRecord) {
//			}
//		};
//		teamGrid.setWidth("30%");
//		teamGrid.setShowRollOverCanvas(true);
//
//		teamGrid.setAutoFetchData(true);
//		
//		teamGrid.setDataSource(TeamDataSource.getInstance());
//		ListGridField teamNameField = new ListGridField(TeamModel.NAME_FIELD, "Team name");
//		ListGridField teamIdField = new ListGridField(TeamModel.TEAMID_FIELD, "Team id");
//		teamIdField.setHidden(true);
//		teamGrid.setFields(new ListGridField[] { teamNameField, teamIdField });
//
//		return teamGrid;
//	}

	private Canvas getPlayersGrid() {
		playersGrid = new GenericListGrid(false, true, false, false, false) {
			protected void deleteItem(ListGridRecord rollOverRecord) {
			}
		};
		playersGrid.setWidth("30%");
		playersGrid.setShowRollOverCanvas(true);

		playersGrid.setAutoFetchData(true);

		playersGrid.setDataSource(UserDataSource.getInstance());
		 ListGridField pictureField = new ListGridField(UserModel.PICTURE_FIELD, "Pic", 40);  
	        pictureField.setAlign(Alignment.CENTER);  
	        pictureField.setType(ListGridFieldType.IMAGE);  
	         
		ListGridField nameField = new ListGridField(UserModel.NAME_FIELD, "name");
		ListGridField emailField = new ListGridField(UserModel.EMAIL_FIELD, "email");

		playersGrid.setFields(new ListGridField[] {pictureField, nameField, emailField });
		return playersGrid;
	}
	private DynamicForm playerForm;
	private Canvas getAddPlayerForm() {
		playerForm = new DynamicForm();
		playerForm.setWidth("30%");
		playerForm.setGroupTitle("Add Players");
		playerForm.setIsGroup(true);

		final MultiComboBoxItem playersComboBox = new MultiComboBoxItem(ContactModel.LOCAL_ID_FIELD, "Select accounts");
		playersComboBox.setDisplayField(ContactModel.NAME_FIELD);
		playersComboBox.setValueField(ContactModel.ACCOUNT_FIELD);
		playersComboBox.setAutoFetchData(true);
		playersComboBox.setOptionDataSource(ContactsDataSource.getInstance());
//		playersComboBox.setShowTitle(false);
		playersComboBox.setShowIfCondition(realAccountFunction);


		CheckboxItem 	qrToggle = new CheckboxItem("qrToggle","Player must authenticate");
		qrToggle.setValue(true);
		qrToggle.setRedrawOnChange(true);
		
		TextItem nameTextItem = new TextItem("Name");
		nameTextItem.setTitle("Define name");
		nameTextItem.setWrapTitle(false);
		nameTextItem.setShowIfCondition(playerMustNotAuthenticate);
		nameTextItem.setStartRow(true);
		
		ButtonItem submitButton = new ButtonItem("Submit");
		submitButton.setTitle("Submit Players");
		submitButton.setColSpan(2);
		submitButton.setAlign(Alignment.CENTER);

		submitButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if (run == null) return;
				if (playerForm.getValue("qrToggle").equals(Boolean.TRUE)) {
					for (String account : playersComboBox.getValues()) {
						UserClient.getInstance().createUser(run.getRunId(), null, account, null, null, new JsonCallback(){
							public void onJsonReceived(JSONValue jsonValue) {
								UserDataSource.getInstance().loadDataFromWeb(run.getRunId());
							}
	
						});
					}
				} else {
					Account account = new Account();
					account.setString(UserModel.NAME_FIELD, playerForm.getValueAsString("Name"));
					account.setString(UserModel.EMAIL_FIELD, playerForm.getValueAsString("Name"));
					account.setString(UserModel.PICTURE_FIELD, "");
					AccountClient.getInstance().createAnonymousContact(account, new JsonCallback(){
						public void onJsonReceived(JSONValue jsonValue) {
							JSONObject account = jsonValue.isObject();
							if (account.containsKey("localId")){
								UserClient.getInstance().createUser(run.getRunId(), null, "0:"+account.get("localId").isString().stringValue(), null, null, new JsonCallback(){
									public void onJsonReceived(JSONValue jsonValue) {
										UserDataSource.getInstance().loadDataFromWeb(run.getRunId());
									}
			
								});
							}
						}
					});
				}
			}
		});
		playerForm.setFields(playersComboBox, nameTextItem, qrToggle, submitButton);
		return playerForm;
	}
	
	FormItemIfFunction realAccountFunction = new FormItemIfFunction() {
		public boolean execute(FormItem item, Object value, DynamicForm form) {
			if (form.getValue("qrToggle") == null)
				return true;
			return form.getValue("qrToggle").equals(Boolean.TRUE) ;
		}

	};
	
	FormItemIfFunction playerMustNotAuthenticate = new FormItemIfFunction() {
		public boolean execute(FormItem item, Object value, DynamicForm form) {
			if (form.getValue("qrToggle") == null)
				return false;
			return !form.getValue("qrToggle").equals(Boolean.TRUE) ;
		}

	};

	public void loadRun(Run run) {
		this.run = run;
		long runId = run.getRunId();
		UserDataSource.getInstance().loadDataFromWeb(runId);
		Criteria criteria = new Criteria();
		criteria.addCriteria(RunModel.RUNID_FIELD,""+ runId);
		playersGrid.setCriteria(criteria);
//		teamGrid.setCriteria(criteria);
		
	}
}
