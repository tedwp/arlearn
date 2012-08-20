package org.celstec.arlearn2.gwt.client.ui.modal;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.game.GameClient;
import org.celstec.arlearn2.gwt.client.network.team.TeamClient;
import org.celstec.arlearn2.gwt.client.network.team.TeamsDataSource;
import org.celstec.arlearn2.gwt.client.network.user.UserClient;
import org.celstec.arlearn2.gwt.client.ui.RunTab;
import org.celstec.arlearn2.gwt.client.ui.RunTabUsers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.layout.HLayout;

public class UserWindow  extends Window {

	RunTabUsers returnTab;
	private String[] roles;
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public UserWindow(final long runId, final long gameId, final RunTabUsers rt) {
		this.returnTab = rt;
		this.setTitle(constants.addUser());
		setWidth(360);
		setHeight(200);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		final DynamicForm form = new DynamicForm();
		form.setHeight100();
		form.setWidth100();
		form.setPadding(5);

		final SelectItem selectTeamItem = new SelectItem();  
		selectTeamItem.setName("teamId");
        selectTeamItem.setTitle(constants.selectTeam());
        selectTeamItem.setDisplayField("name");
        selectTeamItem.setValueField("teamId");
        selectTeamItem.setOptionDataSource(TeamsDataSource.getInstance());
        selectTeamItem.setWrapTitle(false);
        selectTeamItem.setValidators(cv);
        cv.setErrorMessage(constants.emptyValue());
  
        final SelectItem roleGrid = new SelectItem();
		roleGrid.setName("roles");
		roleGrid.setTitle(constants.selectRole());

		roleGrid.setMultiple(true);
		roleGrid.setHeight(40);
		roleGrid.setMultipleAppearance(MultipleAppearance.GRID);
//		roleGrid.setValueMap(roles);
		roleGrid.setWrapTitle(false);
		roleGrid.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {  
                return roles != null && roles.length != 0;  
            }

        });  
		GameClient.getInstance().getGameConfig(gameId, new JsonCallback() {

			public void onJsonReceived(JSONValue jsonValueOld) {
				JSONObject jsonValue = jsonValueOld.isObject();
				
				if (jsonValue.containsKey("roles")) {
					JSONArray array = jsonValue.get("roles").isArray();
					if (array != null) {
						roles = new String[array.size()];
						for (int i = 0; i < array.size(); i++) {
							
							roles[i] = array.get(i).isString().stringValue();
						}
						roleGrid.setValueMap(roles);
						form.redraw();

					}
				}
			}

		});
        
        
		TextItem userName = new TextItem();
		userName.setName("userName");
		userName.setTitle(constants.userName());
		userName.setWrapTitle(false);

		
		TextItem userEmail = new TextItem();
		userEmail.setName("userEmail");
		userEmail.setTitle(constants.userEmail());
		userEmail.setWrapTitle(false);
		
		final IButton submitButton = new IButton(constants.add());
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form.validate())
				UserClient.getInstance().createUser(
						runId, 
						form.getValueAsString("teamId"), 
						form.getValueAsString("userEmail"), 
						form.getValueAsString("userName"), 
						roleGrid.getValues(), 
						new JsonCallback(){
					
					public void onReceived(){
						rt.refreshSources();
//						rt.runTab.generalItems.refr
						//						returnTab.filterUsers(null);
						UserWindow.this.destroy();
					};
				});
			}
		});

		HLayout buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.addMember(submitButton);
		
		form.setFields( userName, userEmail, roleGrid, selectTeamItem);
		addItem(form);
		addItem(buttonLayout);

	}
	
	private CustomValidator cv = new CustomValidator() {
		
		@Override
		protected boolean condition(Object value) {
			if (value == null) return false;
			if ((""+value).trim().equals("")) return false;
			return true;
		}
	};

}

