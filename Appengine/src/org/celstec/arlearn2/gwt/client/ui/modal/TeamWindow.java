package org.celstec.arlearn2.gwt.client.ui.modal;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.team.TeamClient;
import org.celstec.arlearn2.gwt.client.ui.RunTab;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.layout.HLayout;

public class TeamWindow extends Window {

	RunTab returnTab;
	long runId;
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public TeamWindow(final long runId, RunTab rt) {
		this.returnTab = rt;
		this.runId = runId;
		setWidth(360);
		setHeight(100);
		setTitle(constants.addteam());
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		final DynamicForm form = new DynamicForm();
		form.setHeight100();
		form.setWidth100();
		form.setPadding(5);

		TextItem textItem = new TextItem();
		textItem.setName("teamName");
		textItem.setTitle(constants.teamName());
		final IButton submitButton = new IButton(constants.add());
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				TeamClient.getInstance().createTeam(runId, form.getValueAsString("teamName"), new JsonCallback(){
					
					public void onReceived(){
						returnTab.refreshSources();
						TeamWindow.this.destroy();
					};
				});
			}
		});

		HLayout buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.addMember(submitButton);
		
		form.setFields(textItem);
		addItem(form);
		addItem(buttonLayout);

	}
	
	

}
