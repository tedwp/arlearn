package org.celstec.arlearn2.portal.client.author.ui.run;

import java.util.Map;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Run;
import org.celstec.arlearn2.portal.client.author.ui.ListMasterSectionSectionStackDetailTab;

import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;

public class RunsTab extends ListMasterSectionSectionStackDetailTab {

	public static RunsTab instance;
	TeamPlayerConfigurationSection teamPlayerConfigurationSection;
	
	public RunsTab() {
		super("Runs");
		instance = this;
	}
	
	@Override
	protected void initGrid(){
		getMasterListGrid().setDataSource(RunDataSource.getInstance());
		RunDataSource.getInstance().loadDataFromWeb();

		ListGridField idField = new ListGridField(RunModel.RUNID_FIELD, "id ");
		idField.setWidth(30);
		idField.setCanEdit(false);
		idField.setHidden(true);
		
		ListGridField gameIdField = new ListGridField(RunModel.GAMEID_FIELD, "GameId ");
		gameIdField.setCanEdit(false);
		gameIdField.setHidden(true);
		
		ListGridField titleRunField = new ListGridField(RunModel.RUNTITLE_FIELD, "Run Title ");
		ListGridField titleGameField = new ListGridField(RunModel.GAME_TITLE_FIELD, "Game Title ");
		titleGameField.setCanEdit(false);
		
		
		getMasterListGrid().setFields(new ListGridField[] { idField, gameIdField, titleRunField,  titleGameField });
	}
	
	@Override
	protected void initConfigSections() {
		teamPlayerConfigurationSection = new TeamPlayerConfigurationSection();
		addSectionDetail(teamPlayerConfigurationSection);
	}

	@Override
	protected void masterRecordClick(RecordClickEvent event) {
		Run run = new Run(((AbstractRecord) RunDataSource.getInstance().getRecord(event.getRecord().getAttributeAsLong(RunModel.RUNID_FIELD))).getCorrespondingJsonObject());
		teamPlayerConfigurationSection.loadRun(run);
		
	}

	@Override
	protected void masterRecordEditComplete(EditCompleteEvent event) {
		Map values = event.getNewValues();
		if (values.containsKey(RunModel.RUNID_FIELD)) {
			updateRun((Integer)values.get(RunModel.RUNID_FIELD), (String) values.get(RunModel.RUNTITLE_FIELD));
		} else {
			createRun(
					(String) values.get(RunModel.RUNTITLE_FIELD),
					(long) (Integer) values.get(RunModel.GAMEID_FIELD),
					getMasterListGrid().getRecord(event.getRowNum()));
		}	
		
	}
	
	private void updateRun(long runId, String title) {
		Run existingRun = new Run(((AbstractRecord)RunDataSource.getInstance().getRecord(runId)).getCorrespondingJsonObject());
		existingRun.setTitle(title);
		RunClient.getInstance().createRun(existingRun,  new JsonCallback(){});

	}
	
	public void createRun(String title, Long gameId, final Record record) {
		Run newRun = new Run();
		newRun.setTitle(title);
		newRun.setGameId(gameId);
		RunClient.getInstance().createRun(newRun, new JsonCallback(){
			
			public void onJsonReceived(JSONValue jsonValue) {
				RunDataSource.getInstance().removeData(record);
				RunDataSource.getInstance().loadDataFromWeb();
			}
		});
		
	}

	
}
