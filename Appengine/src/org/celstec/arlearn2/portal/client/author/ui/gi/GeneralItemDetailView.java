package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class GeneralItemDetailView extends HLayout {

	VLayout infoLayout;
	Label titleLabel = new Label("nog");
	HTMLPane htmlPane;

	public GeneralItemDetailView() {
		infoLayout = new VLayout();
		infoLayout.setWidth(350);

		titleLabel.setHeight(50);
		infoLayout.addMember(titleLabel);
		// infoLayout.setAlign(Alignment.LEFT);
		infoLayout.setLayoutAlign(Alignment.CENTER);
		infoLayout.setBorder("1px solid #d6d6d6");
		titleLabel.setBorder("1px solid #d6d6d6");

		htmlPane = new HTMLPane();
		htmlPane.setWidth(350);
		htmlPane.setHeight("*");
		infoLayout.addMember(htmlPane);

		setWidth("*");
		addMember(infoLayout);
		setAlign(Alignment.LEFT);
		// setBorder("1px dashed blue");

	}

	public void loadGeneralItem(GeneralItem gi) {
		titleLabel.setContents(gi.getTitle());
		htmlPane.setContents(gi.getRichText());
	}
}
