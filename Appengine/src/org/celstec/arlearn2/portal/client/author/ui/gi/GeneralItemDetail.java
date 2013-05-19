package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;

public class GeneralItemDetail extends HLayout {

	private GeneralItemDetailView view;
	private GeneralItemDetailEdit edit;

	private boolean editing;

	public GeneralItemDetail() {
		LayoutSpacer spacer = new LayoutSpacer();
		spacer.setWidth(24);
		addMember(spacer);
//		initView();
	}

	public void viewGeneralItem(final GeneralItem gi, final boolean canEdit) {
		eraseView();
		view = new GeneralItemDetailView(canEdit) {
			protected void editClick() {
//				setEditMode();
				editGeneralItem(gi);
			}
		};
		view.loadGeneralItem(gi);
		addMember(view);
	}
	
	public void editGeneralItem(final GeneralItem gi) {
		eraseView();

		edit = new GeneralItemDetailEdit(){
			protected void saveClick() {
				super.saveClick();
				viewGeneralItem(gi, true);
			}
		};
		edit.loadGeneralItem(gi);
		addMember(edit);

	}
	
	
//	public void setEditMode() {
//		if (!editing) {
//			view.setVisibility(Visibility.HIDDEN);
//			edit.setVisibility(Visibility.VISIBLE);
//		}
//	}
//
//	public void setViewMode() {
//		edit.setVisibility(Visibility.HIDDEN);
//		view.setVisibility(Visibility.VISIBLE);
//	}

//	public void loadGeneralItem(GeneralItem gi) {
//		setViewMode();
//		view.loadGeneralItem(gi);
//		edit.loadGeneralItem(gi);
//	}
	
	public void eraseView() {
		if (view != null) removeMember(view);
		if (edit != null) removeMember(edit);
	}



//	public void initView() {
//		editing = false;
//		view = new GeneralItemDetailView() {
//			protected void editClick() {
//				removeMember(view);
//				view.destroy();
//				setEditMode();
//			}
//		};
//		edit = new GeneralItemDetailEdit(){
//			protected void saveClick() {
//				super.saveClick();
//				setViewMode();
//			}
//		};
//
//		LayoutSpacer spacer = new LayoutSpacer();
//		spacer.setWidth(24);
//		addMember(spacer);
//		addMember(view);
//		addMember(edit);
//		setViewMode();
//		
//	}

}
