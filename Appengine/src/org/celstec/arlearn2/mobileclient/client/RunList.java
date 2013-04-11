package org.celstec.arlearn2.mobileclient.client;

import org.celstec.arlearn2.mobileclient.client.common.datasource.mobile.RunDataSource;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.types.TableMode;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.tableview.TableView;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickEvent;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickHandler;

public class RunList extends ScrollablePanel {

    public RunList(String title) {
        super(title);
        this.setWidth("100%");
        TableView tableView = new TableView();
        tableView.setTitleField("title");
        tableView.setShowNavigation(false);
        tableView.setShowIcons(true);
        tableView.setTableMode(TableMode.GROUPED);
        tableView.addRecordNavigationClickHandler(clickHandler);
        RunDataSource rds = RunDataSource.getInstance();
        tableView.setData(rds);
//        Criteria criteria = new Criteria();
//        criteria.addCriteria("title", "role");
//        tableView.fetchData(criteria);
        addMember(tableView);
    }
    

	@Override
	public void onLoad() {
		RunDataSource rds = RunDataSource.getInstance();
		rds.loadDataFromWeb();
	}
    
    RecordNavigationClickHandler clickHandler = new RecordNavigationClickHandler() {
        @Override
        public void onRecordNavigationClick(RecordNavigationClickEvent event) {
            final Record selectedRecord = event.getRecord();
            if (selectedRecord.getAttributeAsBoolean("mapAvailable")) {
//            	GeneralItemsMapView gimv = new GeneralItemsMapView("GeneralItems", selectedRecord.getAttributeAsLong("runId"));
//                MobileClient.navStack.push(gimv);
            } else {
//                MobileClient.navStack.push(new GeneralItemsList("GeneralItems", selectedRecord.getAttributeAsLong("runId")));
            }
        }
    };
}

