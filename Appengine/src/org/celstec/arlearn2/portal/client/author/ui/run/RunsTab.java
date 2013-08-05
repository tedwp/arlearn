package org.celstec.arlearn2.portal.client.author.ui.run;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Run;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.author.ui.ListMasterSectionSectionStackDetailTab;
import org.celstec.arlearn2.portal.client.author.ui.run.i18.RunConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;

public class RunsTab extends ListMasterSectionSectionStackDetailTab {

	private static RunConstants constants = GWT.create(RunConstants.class);

	public static RunsTab instance;
	TeamPlayerConfigurationSection teamPlayerConfigurationSection;
	TeamConfigurationSection teamConfigurationSection;
	
	public RunsTab() {
		super(constants.runs());
		instance = this;
	}
	
	protected void tabSelect() {
//		hideDetail();
		RunDataSource.getInstance().loadDataFromWeb();
	}
	
	@Override
	protected void initGrid(){
		getMasterListGrid().setDataSource(RunDataSource.getInstance());
		getMasterListGrid().setCanEdit(false);
		
		ListGridField idField = new ListGridField(RunModel.RUNID_FIELD, "id");
		idField.setWidth(30);
		idField.setHidden(true);
		
		ListGridField gameIdField = new ListGridField(RunModel.GAMEID_FIELD, "gameId ");
		gameIdField.setHidden(true);
		
		ListGridField titleRunField = new ListGridField(RunModel.RUNTITLE_FIELD, constants.runTitle());
		ListGridField titleGameField = new ListGridField(RunModel.GAME_TITLE_FIELD, "Game Title ");
		titleGameField.setCanEdit(false);
		
		ListGridField accessRunField = new ListGridField(RunModel.RUN_ACCESS_STRING, "Run access ");
		accessRunField.setCanEdit(false);
		accessRunField.setWidth(100);
		
		ListGridField deleteField = new ListGridField("deleteField", " ");  
		 deleteField.setWidth(20);
        if (AccountManager.getInstance().isAdministrator()) {
            getMasterListGrid().setCanEdit(true);
            getMasterListGrid().setShowFilterEditor(true);
        }
		getMasterListGrid().setFields(new ListGridField[] { idField, gameIdField, titleRunField,  titleGameField, accessRunField, deleteField });
		Criteria criteria = new Criteria();
		criteria.addCriteria(GameModel.DELETED_FIELD, false);
		getMasterListGrid().setCriteria(criteria);
	}
	
	@Override
	protected void initConfigSections() {
		teamPlayerConfigurationSection = new TeamPlayerConfigurationSection();
		teamConfigurationSection = new TeamConfigurationSection();
		addSectionDetail(teamPlayerConfigurationSection);
		addSectionDetail(teamConfigurationSection);
		
	}

	@Override
	protected void masterRecordClick(RecordClickEvent event) {
		if (event.getRecord() == null || event.getRecord().getAttributeAsLong(RunModel.RUNID_FIELD) == null) return;
		Run run = new Run(((AbstractRecord) RunDataSource.getInstance().getRecord(event.getRecord().getAttributeAsLong(RunModel.RUNID_FIELD))).getCorrespondingJsonObject());
		teamPlayerConfigurationSection.loadRun(run);
		teamConfigurationSection.loadRun(run);
		showDetail();
		
	}

//	@Override
//	protected void masterRecordEditComplete(EditCompleteEvent event) {
//		Map values = event.getNewValues();
//		if (values.containsKey(RunModel.RUNID_FIELD)) {
//			updateRun((Integer)values.get(RunModel.RUNID_FIELD), (String) values.get(RunModel.RUNTITLE_FIELD));
//		} else {
//			String gameIdString = ""+values.get(RunModel.GAMEID_FIELD);
//			createRun(
//					(String) values.get(RunModel.RUNTITLE_FIELD),
//					Long.parseLong(gameIdString),
//					getMasterListGrid().getRecord(event.getRowNum()));
//		}	
//		
//	}
	
//	private void updateRun(long runId, String title) {
//		Run existingRun = new Run(((AbstractRecord)RunDataSource.getInstance().getRecord(runId)).getCorrespondingJsonObject());
//		existingRun.setTitle(title);
//		RunClient.getInstance().createRun(existingRun,  new JsonCallback(){});
//
//	}
	
//	public void createRun(String title, Long gameId, final Record record) {
//		Run newRun = new Run();
//		newRun.setTitle(title);
//		newRun.setGameId(gameId);
//		RunClient.getInstance().createRun(newRun, new JsonCallback(){
//			
//			public void onJsonReceived(JSONValue jsonValue) {
//				RunDataSource.getInstance().removeData(record);
//				RunDataSource.getInstance().loadDataFromWeb();
//			}
//		});
//		
//	}

	@Override
	protected void deleteItem(final ListGridRecord rollOverRecord) {
		SC.ask(constants.deleteThisRun().replace("***", rollOverRecord.getAttributeAsString(RunModel.RUNTITLE_FIELD)), new BooleanCallback() {
			public void execute(Boolean value) {
				if (value != null && value) {
					RunClient.getInstance().deleteItemsForRun(rollOverRecord.getAttributeAsLong("runId"), new JsonCallback() {
						
						@Override
						public void onJsonReceived(JSONValue jsonValue) {
							
							RunDataSource.getInstance().loadDataFromWeb();
		
						}
					});	
					
				}
			}
		});	
	}

	
}
