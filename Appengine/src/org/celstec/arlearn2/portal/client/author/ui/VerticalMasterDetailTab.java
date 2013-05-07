package org.celstec.arlearn2.portal.client.author.ui;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public abstract class VerticalMasterDetailTab extends Tab {
	private VLayout layout;
	private VLayout upperLayout;
	private VLayout lowerLayout;
	
	public VerticalMasterDetailTab(String name) {
		super(name);
		layout = new VLayout(5);
		upperLayout = new VLayout(1);
		Canvas masterCanvas = getMaster();
		masterCanvas.setCanDragResize(true);
		upperLayout.addMember(masterCanvas);
		Canvas detailCanvas = getDetail();
		detailCanvas.setCanDragResize(true);
		upperLayout.addMember(detailCanvas);
		upperLayout.setHeight100();
		upperLayout.setCanDragResize(true);
		layout.addMember(upperLayout);
		
		lowerLayout = new VLayout(1);
		lowerLayout.setHeight(40);
		lowerLayout.setCanDragResize(true);
		layout.addMember(lowerLayout);
		this.setPane(layout);

	}
	
	protected void addMasterMember(Canvas c) {
		lowerLayout.addMember(c);
	}
	
	public abstract Canvas getMaster() ;
	public abstract Canvas getDetail() ;
	
	
	

}
