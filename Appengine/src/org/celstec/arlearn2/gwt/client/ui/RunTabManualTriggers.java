package org.celstec.arlearn2.gwt.client.ui;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.control.TriggerDataSource;
import org.celstec.arlearn2.gwt.client.network.ActionClient;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.user.UsersDataSource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

public class RunTabManualTriggers extends VLayout {
	
	private ListGrid actionGrid;

	private RunTab runTab;
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public RunTabManualTriggers(RunTab rt) {
		this.runTab = rt;
		
		Label l = new Label("Send manual notifications to players");
		l.setHeight(20);
		addMember(l);
		
		initGrid();
		initGridData();
		initButton();
	}


	private void initGrid() {
		actionGrid = new ListGrid();
		actionGrid.setWidth("100%");
		actionGrid.setHeight(150);
		
	}
	
	private void initGridData() {
		actionGrid.setDataSource(TriggerDataSource.getInstance());

		ListGridField itemIdField = new ListGridField("itemId", "Item Id");
		ListGridField nameField = new ListGridField("name", constants.itemName());
		actionGrid.setFields(new ListGridField[] { itemIdField, nameField });

		actionGrid.fetchData();
		
		addMember(actionGrid);

	}
	
	private void initButton(){
		IButton sendTriggers = new IButton(constants.send());
		sendTriggers.setLeft(0);
		sendTriggers.setTop(160);
		sendTriggers.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				for (ListGridRecord userRec : runTab.getUsersCanvas().getSelectedRecords()) {

					for (ListGridRecord actionRec : actionGrid.getSelectedRecords()) {

						ActionClient.getInstance().notify(runTab.getRunId(), userRec.getAttribute("email"), actionRec.getAttribute("itemId"),actionRec.getAttributeAsBoolean("autoLaunch"), new JsonCallback() {

							@Override
							public void onJsonReceived(JSONValue jsonValue) {
								runTab.getUsersCanvas().deselectRecords();
								actionGrid.deselectAllRecords();
							}

							@Override
							public void onError() {
								runTab.getUsersCanvas().deselectRecords();
								actionGrid.deselectAllRecords();
							}
						});
						runTab.getUsersCanvas().deselectRecords();
						actionGrid.deselectAllRecords();
					}
				}

			}
		});
		
		addMember(sendTriggers);
	}
	
	public void refreshSources() {
		if (actionGrid != null) {
			Criteria crit = new Criteria();

			crit.addCriteria("gameId", (int) runTab.getGameId());
			actionGrid.filterData(crit);
			TriggerDataSource.getInstance().fetchData(crit, new DSCallback() {
				
				@Override
				public void execute(DSResponse response, Object rawData, DSRequest request) {
					if (response.getData().length != 0) {
						 setVisibility(Visibility.INHERIT);

					} 
					
				}
			});
		}
	}


}
