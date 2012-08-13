package org.celstec.arlearn2.gwt.client.ui;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.control.Authentication;
import org.celstec.arlearn2.gwt.client.control.GeneralItemsDataSource_Old;
import org.celstec.arlearn2.gwt.client.control.ReadyCallback;
import org.celstec.arlearn2.gwt.client.control.TriggerDataSource;
//import org.celstec.arlearn2.gwt.client.control.UsersDataSource;
import org.celstec.arlearn2.gwt.client.network.ActionClient;
import org.celstec.arlearn2.gwt.client.network.ChannelClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.action.ActionDatasource;
import org.celstec.arlearn2.gwt.client.network.response.ResponseDataSource;
import org.celstec.arlearn2.gwt.client.network.team.TeamClient;
import org.celstec.arlearn2.gwt.client.network.team.TeamsDataSource;
import org.celstec.arlearn2.gwt.client.network.user.UserClient;
import org.celstec.arlearn2.gwt.client.network.user.UsersDataSource;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;
import org.celstec.arlearn2.gwt.client.ui.modal.OpenQuestionAnswerWindow;
import org.celstec.arlearn2.gwt.client.ui.modal.TeamWindow;
import org.celstec.arlearn2.gwt.client.ui.modal.UserActionsWindow;
import org.celstec.arlearn2.gwt.client.ui.modal.UserWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DialogButtons;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;

public class RunTab extends MasterDetailTab {

	private SectionStack sectionStack;

	private long runId;
	private long gameId;
	
	protected RunTabUsers users;
	private RunTabGeneralItems generalItems;
	private RunTabTeams teams;
	private RunTabConfig config;
	private RunTabManualTriggers manualTriggers;
	
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public RunTab(String title, final long runId, final long gameId) {
		super(title);
		this.runId = runId;
		this.gameId = gameId;

		users = new RunTabUsers(this);
		generalItems = new RunTabGeneralItems(this);
		teams = new RunTabTeams(this);
		config = new RunTabConfig(this);
		manualTriggers = new RunTabManualTriggers(this);
		
		setCanClose(true);


		setMasterCanvas(getMaster());
		setDetailCanvas(getDetail());
		ActionDatasource.getInstance().loadDataRun(runId);
		ResponseDataSource.getInstance().loadDataRun(runId);

	}
	
	public void tabSelected(TabSelectedEvent event) {
		refreshSources();
//		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.run.Action", generalItems.actionNotificationHandler);
		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.run.Response", generalItems.responseNotificationHandler);
	}


	public void refreshSources() {
		generalItems.filterGrid(null, null);
		users.refreshSources();
		teams.refreshSources();
		config.refreshSources();
		manualTriggers.refreshSources();
	}

	private Canvas getMaster() {

		sectionStack = new SectionStack();
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setWidth(300);

//		IButton expandButton = new IButton("Expand Players");
		SectionStackSection configSection = new SectionStackSection(constants.config());
		configSection.setExpanded(false);
		configSection.addItem(config);
		sectionStack.addSection(configSection);
		
		SectionStackSection section1 = new SectionStackSection(constants.teams());
		section1.setExpanded(true);
		section1.addItem(teams);
		sectionStack.addSection(section1);
		
		final SectionStackSection section2 = new SectionStackSection(constants.players());
		section2.setExpanded(true);
		section2.setCanCollapse(true);
		section2.addItem(users);
		sectionStack.addSection(section2);

//		expandButton.setWidth(150);
//		expandButton.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				sectionStack.expandSection(1);
//
//			}
//		});

		return sectionStack;
	}


	

	public VLayout getDetail() {
		VLayout layout = new VLayout();
		layout.addMember(generalItems);
//		triggerCanvas = getActionTriggerCanvas();
		 layout.addMember(manualTriggers);
		 manualTriggers.setVisibility(Visibility.HIDDEN);
		return layout;
	}


//	public Canvas getActionTriggerCanvas() {
//		
//		
//		// ListGridField gameField = new ListGridField("gameId", "Game Id");
//		
//
//		
//		return canvas;
//
//	}
	
	public void expandUsersSection() {
		sectionStack.expandSection(1);

	}
	

	public long getRunId() {
		return runId;
	}
	
	public long getGameId() {
		return gameId;
	}
	
	public RunTabGeneralItems getGeneralItems() {
		return generalItems;
	}
	
	public RunTabUsers getUsersCanvas() {
		return users;
	}
}
