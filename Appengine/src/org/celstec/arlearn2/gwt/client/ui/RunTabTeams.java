/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.gwt.client.ui;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.team.TeamClient;
import org.celstec.arlearn2.gwt.client.network.team.TeamsDataSource;
import org.celstec.arlearn2.gwt.client.network.user.UsersDataSource;
import org.celstec.arlearn2.gwt.client.ui.modal.TeamWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
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

public class RunTabTeams extends Canvas {
	private ListGrid teamGrid;
	
	private RunTab runTab;
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public RunTabTeams(RunTab rt) {
		this.runTab = rt;
		
		initGrid();
		initGridData();
		initCellClickHandler();
		initButton();
		
		filterGrid();
		setOverflow(Overflow.AUTO);
	}
	
	private void initGrid() {
		teamGrid = new GenericListGrid(false, true) {
			protected void deleteItem(ListGridRecord rollOverRecord) {
				RunTabTeams.this.deleteTeam(rollOverRecord.getAttributeAsString("teamId"), rollOverRecord.getAttributeAsString("name"));
			}

		};
		teamGrid.setShowRollOverCanvas(true);
		teamGrid.setWidth("100%");
		teamGrid.setHeight(210);
		teamGrid.setShowAllRecords(true);
	}

	private void initGridData() {
		teamGrid.setDataSource(TeamsDataSource.getInstance());

		ListGridField nameField = new ListGridField("name", constants.teamName());
		teamGrid.setFields(new ListGridField[] { nameField });
		teamGrid.fetchData();

		addChild(teamGrid);
	}
	
	private void initCellClickHandler() {
		teamGrid.addCellClickHandler(new CellClickHandler() {
			@Override
			public void onCellClick(CellClickEvent event) {
				runTab.expandUsersSection();
				runTab.getUsersCanvas().filterGrid(event.getRecord().getAttribute("teamId"));
			}
		});
	}

		
	private void initButton() {
		IButton addTeamButton = new IButton(constants.newTeam());
		addTeamButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				(new TeamWindow(runTab.getRunId(), RunTabTeams.this)).show();
			}
		});
		
		HLayout buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.addMember(addTeamButton);
		buttonLayout.setTop(205);
		buttonLayout.setHeight(20);
		buttonLayout.setWidth100();

		addChild(buttonLayout);
	}
	
	public void refreshSources() {
		TeamsDataSource.getInstance().loadDataRun(runTab.getRunId());
		filterGrid();
	}

	public void filterGrid() {
		Criteria crit = new Criteria();
		crit.addCriteria("runId", (int) runTab.getRunId());
		teamGrid.filterData(crit);
	}

	
	protected void deleteTeam(final String teamId, String name) {
		SC.ask(constants.confirmDeleteUser().replace("***", name), new BooleanCallback() {
			public void execute(Boolean value) {
				if (value != null && value) {
					TeamClient.getInstance().deleteTeam(teamId, new JsonCallback() {
						public void onJsonReceived(JSONValue jsonValue) {
							runTab.refreshSources();
						}
					});
				}
			}
		});
	}
}
