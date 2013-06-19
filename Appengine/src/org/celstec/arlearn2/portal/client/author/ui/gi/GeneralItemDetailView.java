package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class GeneralItemDetailView extends VLayout {

	protected VLayout basicMetadataLayout;
	protected VLayout extendedMetadataLayout;
//	protected Label titleLabel = new Label("nog");
	protected HTMLPane htmlPane;
	protected HLayout buttonLayout;
	protected IButton editButton;

//	public void setVisibility(Visibility visibility) {
//		super.setVisibility(visibility);
//		basicMetadataLayout.setVisibility(visibility)
//	}
	
	public GeneralItemDetailView(boolean canEdit) {
		if (canEdit) createEditButton();
		if (canEdit) createButtonLayout(editButton);
		
//		createTitleLabel();
		createHtmlLabel();
		createBasicMetadataView();
		createExtendedMetadataView();
		setHeight100();
		
		HLayout layout = new HLayout();
		layout.addMember(basicMetadataLayout);
		layout.addMember(extendedMetadataLayout);
		setAlign(Alignment.LEFT);

		setBorder("1px solid #d6d6d6");
		setPadding(5);

		addMember(layout);
		if (canEdit) addMember(buttonLayout);
	}
	
	private void createBasicMetadataView() {
		basicMetadataLayout = new VLayout();
		basicMetadataLayout.setWidth(350);
		basicMetadataLayout.setLayoutAlign(Alignment.LEFT);
		basicMetadataLayout.setBorder("1px solid #d6d6d6");

//		basicMetadataLayout.addMember(titleLabel);
		basicMetadataLayout.addMember(htmlPane);
		basicMetadataLayout.setPadding(10);
//		basicMetadataLayout.addMember(editButton);
		
	}
	
	private void createExtendedMetadataView() {
		extendedMetadataLayout = new VLayout();
		extendedMetadataLayout.setVisibility(Visibility.INHERIT);
		extendedMetadataLayout.setBorder("1px solid #d6d6d6");
		extendedMetadataLayout.setPadding(10);
	}

	
	private void createEditButton() {
		editButton = new IButton("edit");
		editButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				editClick();
			}
		});
	}

//	private void createTitleLabel() {
//		titleLabel.setHeight(50);
//		titleLabel.setBorder("1px solid #d6d6d6");
//	}

	private void createHtmlLabel() {
		htmlPane = new HTMLPane();
		htmlPane.setWidth(350);
		htmlPane.setHeight("*");
	}

	private void createButtonLayout(IButton... buttons) {
		buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.setHeight(40);
		for (IButton but : buttons) {
			buttonLayout.addMember(but);
		}

	}

	protected void editClick() {
		
	}
	
	
	
	public void loadGeneralItem(GeneralItem gi) {
//		titleLabel.setContents(gi.getTitle());
		String contents = "<b>Title:</b> "+gi.getTitle()+"<br><br>";
		if (gi.getRichText()!= null)
		contents += "<b>Description:</b><br>"+gi.getRichText();
		htmlPane.setContents(contents);
		extendedMetadataLayout.removeMembers(extendedMetadataLayout.getMembers());
		extendedMetadataLayout.addMember(gi.getViewerComponent());
	}
}
