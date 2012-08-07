package org.celstec.arlearn2.gwt.client.ui;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class MasterDetailTab extends Tab {
	
	private HLayout mainLayout;
	private VLayout masterLayout;
	private VLayout detailLayout;
	
	public MasterDetailTab(String title) {
		super(title);
		
		initMainLayout();
		initMasterLayout();
		initDetailLayout();
		
		this.setPane(mainLayout);
		addTabSelectedHandler(new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				tabSelected(event);
			}
		});

	}
	
	public void setMasterCanvas(Canvas master) {
		masterLayout.addMember(master);
	}
	
	public void setDetailCanvas(Canvas detail) {
		detailLayout.addMember(detail);
	}
	
	public void tabSelected(TabSelectedEvent event){
		
	}

	
	private void initMainLayout() {
		mainLayout = new HLayout();
		mainLayout.setMembersMargin(10);
	}
	
	private void initMasterLayout() {
		masterLayout = new VLayout();
		masterLayout.setWidth(300);
		mainLayout.addMember(masterLayout);
	}
	
	private void initDetailLayout() {
		detailLayout = new VLayout();
		detailLayout.setHeight100();
		mainLayout.addMember(detailLayout);
	}

}