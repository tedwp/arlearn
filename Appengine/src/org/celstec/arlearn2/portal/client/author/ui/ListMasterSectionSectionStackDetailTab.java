package org.celstec.arlearn2.portal.client.author.ui;

import org.celstec.arlearn2.portal.client.author.ui.game.GamesTab;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public abstract class ListMasterSectionSectionStackDetailTab extends VerticalMasterDetailTab {

	private ListGrid masterList;
	private SectionConfiguration configuration;

	public ListMasterSectionSectionStackDetailTab(String name) {
		super(name);
	}

	protected abstract void initGrid();

	protected abstract void initConfigSections();

	protected abstract void masterRecordClick(RecordClickEvent event);

	protected abstract void masterRecordEditComplete(EditCompleteEvent event);

	

	@Override
	public Canvas getMaster() {
		masterList = new ListGrid() {
			@Override
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {

				String fieldName = this.getFieldName(colNum);
				if (fieldName != null && fieldName.startsWith("button")) {
					return ((GamesTab) (ListMasterSectionSectionStackDetailTab.this)).initButton(fieldName, record);
				} else {
					return null;
				}

			}
		};
		masterList.setShowRecordComponentsByCell(true);
		masterList.setCanRemoveRecords(true);

		masterList.setShowAllRecords(true);
		masterList.setShowRecordComponents(true);

		masterList.setHeight(350);
		masterList.setWidth100();
		masterList.setHeight("40%");
		masterList.setAutoFetchData(true);
		masterList.setCanEdit(true);
		initGrid();

		masterList.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				masterRecordClick(event);
			}
		});
		masterList.addEditCompleteHandler(new EditCompleteHandler() {
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				masterRecordEditComplete(event);

			}
		});
		return masterList;
	}

	@Override
	public Canvas getDetail() {
		configuration = new SectionConfiguration();
		initConfigSections();
		return configuration;
	}

	public void addSectionDetail(SectionConfig detail) {
		configuration.add(detail);
	}

	public ListGrid getMasterListGrid() {
		return masterList;
	}

	protected SectionConfiguration getSectionConfiguration() {
		return configuration;
	}

}