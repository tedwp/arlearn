package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;

import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class GeneralItemDetail extends HLayout{
	
	private GeneralItemDetailView view;
	private GeneralItemDetailEdit edit;
	
	private boolean editing;
	
	public GeneralItemDetail(){
		editing = false;
		view = new GeneralItemDetailView();
		edit = new GeneralItemDetailEdit();
//		view.setVisibility(Visibility.HIDDEN);
//		edit.setVisibility(Visibility.HIDDEN);
//		addMember(view);
//		addMember(edit);
//		setDefaultLayoutAlign(Alignment.LEFT);
//		setAlign(Alignment.LEFT);
		

//		setBorder("1px solid #d6d6d6");

		final TabSet detailTabSet = new TabSet();
		detailTabSet.setWidth("*");
		detailTabSet.setTabBarPosition(Side.LEFT);

		Tab lTab1 = new Tab();
		lTab1.setIcon("/images/icon_edit.png", 16);

		lTab1.setPane(view);

		Tab lTab2 = new Tab();
		lTab2.setIcon("/images/icon_maps.png", 16);
		 lTab2.setPane(edit);

		detailTabSet.addTab(lTab1);
		detailTabSet.addTab(lTab2);
		addMember(detailTabSet);
	}
	
	public void setEditMode() {
		if (!editing) {
			view.setVisibility(Visibility.HIDDEN);
			edit.setVisibility(Visibility.VISIBLE);
		}
	}
	
	public void setViewMode() {
//		if (editing) {
			edit.setVisibility(Visibility.HIDDEN);
			view.setVisibility(Visibility.VISIBLE);
//		}
	}
	
	public void loadGeneralItem(GeneralItem gi) {
		setViewMode();
		view.loadGeneralItem(gi);
		edit.loadGeneralItem(gi);
	}

}
