package org.celstec.arlearn2.gwt.client.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.generic.NEW;
import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.control.Authentication;
import org.celstec.arlearn2.gwt.client.control.GeneralItemsDataSource_Old;
import org.celstec.arlearn2.gwt.client.network.ChannelClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.action.ActionDatasource;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemRunDataSource;
import org.celstec.arlearn2.gwt.client.network.response.ResponseDataSource;
import org.celstec.arlearn2.gwt.client.network.user.UserClient;
import org.celstec.arlearn2.gwt.client.network.user.UsersDataSource;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;
import org.celstec.arlearn2.gwt.client.ui.modal.UserActionsWindow;
import org.celstec.arlearn2.gwt.client.ui.modal.UserWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class RunTabUsers extends Canvas {

	private ListGrid userGrid;
	private RunTab runTab;
	private PongHandler pongHandler = new PongHandler();
	public List<UserActionsWindow> uaws = new ArrayList<UserActionsWindow>();

	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public RunTabUsers(RunTab rt) {
		this.runTab = rt;
		
		initGrid();
		initGridData();
		initCellClickHandler();
		initButton();
		
		initNotifications();
		
		filterGrid(null);
		setOverflow(Overflow.AUTO);

	}

	private void initGrid() {
		userGrid = new GenericListGrid(false, true, false, false, true) {
			protected void deleteItem(ListGridRecord rollOverRecord) {
				RunTabUsers.this.deleteUser(rollOverRecord.getAttributeAsString("email"));
			}

			protected void browseItem(ListGridRecord rollOverRecord) {
				RunTabUsers.this.browseItem(rollOverRecord.getAttributeAsString("email"), rollOverRecord.getAttributeAsString("name"));
			}
		};
		userGrid.setShowRollOverCanvas(true);
		userGrid.setWidth("100%");
		userGrid.setHeight(200);
	}

	private void initGridData() {
		userGrid.setDataSource(UsersDataSource.getInstance());

		ListGridField nameField = new ListGridField("name", constants.userName());
		ListGridField emailField = new ListGridField("email", constants.email());
		ListGridField rolesField = new ListGridField("roles", constants.roles());
		ListGridField onlineStatusField = new ListGridField("status", " ", 30);
		onlineStatusField.setAlign(Alignment.CENTER);
		onlineStatusField.setType(ListGridFieldType.IMAGE);
		onlineStatusField.setImageURLSuffix(".png");
		
		userGrid.setFields(new ListGridField[] { onlineStatusField, nameField, emailField, rolesField });
		userGrid.fetchData();
		addChild(userGrid);
	}
	
	private void initCellClickHandler() {
		userGrid.addCellClickHandler(new CellClickHandler() {

			@Override
			public void onCellClick(final CellClickEvent event) {
				runTab.getGeneralItems().filterGrid(event.getRecord().getAttribute("email"), event.getRecord().getAttribute("teamId"));
				final ListGridRecord record = event.getRecord();
//				GeneralItemsDataSource.getInstance().loadItemsGame(runTab.getGameId(), runTab.getRunId(), record.getAttribute("email"), null);
//				if ("status".equals(userGrid.getFieldName(event.getColNum()))) {
//					String email = event.getRecord().getAttribute("email");
//					
//				}
			}
		});
	}
	
	private void initButton(){
		IButton addUserButton = new IButton(constants.newUser());
		addUserButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				(new UserWindow(runTab.getRunId(), runTab.getGameId(), RunTabUsers.this)).show();
			}
		});

		HLayout buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.addMember(addUserButton);
		buttonLayout.setTop(200);
		buttonLayout.setHeight(20);
		buttonLayout.setWidth100();
		// buttonLayout.setBorder("2px solid blue");

		// canvas.setBorder("1px solid blue");

		addChild(buttonLayout);
	}
	
	private void initNotifications() {
		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.notification.Pong",pongHandler);		
	}
	
	public void refreshSources() {
		UsersDataSource.getInstance().loadDataRun(runTab.getRunId());
		filterGrid(null);
		pingTimer.schedule(3000);
	}
	
	private void sendPingToUsers() {
		Criteria crit = new Criteria();
		crit.addCriteria("runId", ""+runTab.getRunId());
		UsersDataSource.getInstance().fetchData(crit, new DSCallback() {

			@Override
			public void execute(DSResponse response,
					Object rawData, DSRequest request) {
				Record[] records = response.getData();
				for (Record record : records) {
					sendPingTo(record.getAttribute("email"));

				}
			}
		});
	}
	
	private void sendPingTo(String account) {
		ChannelClient.getInstance().ping(new JsonCallback(){
			public void onReceived(){
			};
		}, Authentication.getInstance().getCurrentUser(), account, "");
	}

	public void filterGrid(String teamId) {
		Criteria crit = new Criteria();
		crit.addCriteria("deleted", false);
		crit.addCriteria("runId", (int) runTab.getRunId());

		if (teamId != null)
			crit.addCriteria("teamId", teamId);
		userGrid.filterData(crit);
	}
	
	protected void deleteUser(final String email) {
		SC.ask(constants.confirmDeleteUser().replace("***", email), new BooleanCallback() {
			public void execute(Boolean value) {
				if (value != null && value) {
					ActionDatasource.getInstance().deleteRunAccount(runTab.getRunId(), email);
					ResponseDataSource.getInstance().deleteRunAccount(runTab.getRunId(), email);
					runTab.generalItems.datasource.deleteRunAccount(runTab.getRunId(), email);
					UserClient.getInstance().deleteUser(runTab.getRunId(), email, new JsonCallback() {
						public void onJsonReceived(JSONValue jsonValue) {
							runTab.refreshSources();
						}
					});
				}
			}
		});
	}

	protected void browseItem(String email, String userName) {
		UserActionsWindow uaw = new UserActionsWindow(email, userName, runTab.getRunId());
		uaw.show();
		uaws.add(uaw);

	}
	
	public ListGridRecord[] getSelectedRecords() {
		return userGrid.getSelectedRecords();
		
	}
	
	public void deselectRecords() {
		userGrid.deselectAllRecords();

	}
	
	
	public class PongHandler implements NotificationHandler {
		@Override
		public void onNotification(final JSONObject bean) {
			Criteria crit = new Criteria();
			UsersDataSource.getInstance().fetchData(crit, new DSCallback() {
				@Override
				public void execute(DSResponse response,
						Object rawData, DSRequest request) {
					Record[] records = response.getData();
					for (Record record : records) {
						if (record.getAttribute("pk").contains(bean.get("from").isString().stringValue())) {
							record.setAttribute("status", "status_icon_green");
							UsersDataSource.getInstance().updateData(record);
						}
					}
				}
			});
			
		}
	}
	
	Timer pingTimer = new Timer() {

		@Override
		public void run() {
			Criteria crit = new Criteria();
			UsersDataSource.getInstance().fetchData(crit, new DSCallback() {
				@Override
				public void execute(DSResponse response,
						Object rawData, DSRequest request) {
					Record[] records = response.getData();
					for (Record record : records) {
							record.setAttribute("status", "status_icon_red");
							UsersDataSource.getInstance().updateData(record);
					}
					sendPingToUsers();
					pingTimer.schedule(30000);
				}
			});
			
			
		}
		
	};
}
