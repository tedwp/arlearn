package org.celstec.arlearn2.portal.client.author.ui.game;

import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.portal.client.AuthoringConstants;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.LayoutSpacer;

public class SharingConfigSection extends SectionConfig {
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);
	RadioGroupItem sharingOptions;
	RadioGroupItem licenseOptions;
	DynamicForm sharingForm;
	DynamicForm licenseForm;

	public SharingConfigSection() {
		super("Sharing");
		HStack layout = new HStack();

		layout.addMember(getAccessForm());
		LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(10);
		layout.addMember(vSpacer);
		layout.addMember(getLicenseForm());

		layout.setAlign(Alignment.CENTER);
		layout.setPadding(5);
		setItems(layout);
	}

	private Canvas getLicenseForm() {
		
		licenseOptions = new RadioGroupItem();
		licenseOptions.setName("license");
		licenseOptions.setTitle("choose creative commons license");
		licenseOptions.setValueMap(new String[] { "Attribution CC BY" , 
				"Attribution-NoDerivs CC BY-ND",
				"Attribution-NonCommercial-ShareAlike CC BY-NC-SA",
				"Attribution-ShareAlike  CC BY-SA",
				"Attribution-NonCommercial CC BY-NC",
				"Attribution-NonCommercial-NoDerivs CC BY-NC-ND"
				});
		
		licenseForm = new DynamicForm();
		licenseForm.setGroupTitle("License");
		licenseForm.setIsGroup(true);
		licenseForm.setFields();
		licenseForm.setWidth(500);
		licenseForm.setVisibility(Visibility.HIDDEN);
		licenseForm.setFields(licenseOptions);
		return licenseForm;
	}
	
	private Canvas getAccessForm() {
		sharingOptions = new RadioGroupItem();
		sharingOptions.setName("sharing");
		sharingOptions.setTitle(constants.shareVisibilityOptions());
		sharingOptions.setValueMap(new String[] { constants.privateSharing(), constants.linkSharing(), constants.publicSharing() });
		sharingOptions.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				int sharingType = 1;
				if (event.getValue() != null) {
					if (event.getValue().equals(constants.linkSharing())) {
						sharingType = 2;
					}
					if (event.getValue().equals(constants.publicSharing())) {
						sharingType = 3;
						licenseForm.setVisibility(Visibility.VISIBLE);
					} else {
						licenseForm.setVisibility(Visibility.HIDDEN);
					}
				}

			}
		});
		sharingForm = new DynamicForm();
		sharingForm.setGroupTitle("Access");
		sharingForm.setIsGroup(true);
		sharingForm.setWidth(300);

		sharingForm.setFields(sharingOptions);
		return sharingForm;
	}

	public void loadDataFromRecord(Game game) {

	}

	public void setSharing(int sharing) {
		if (sharingOptions != null) {
			switch (sharing) {
			case 1:
				sharingOptions.setValue(constants.privateSharing());
				licenseForm.setVisibility(Visibility.HIDDEN);
				break;
			case 2:
				sharingOptions.setValue(constants.linkSharing());
				licenseForm.setVisibility(Visibility.HIDDEN);
				break;
			case 3:
				sharingOptions.setValue(constants.publicSharing());
				licenseForm.setVisibility(Visibility.VISIBLE);

				break;

			default:
				break;
			}
			sharingForm.redraw();
		}
	}

}
