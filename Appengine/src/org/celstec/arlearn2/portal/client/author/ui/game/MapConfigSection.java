package org.celstec.arlearn2.portal.client.author.ui.game;

import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.portal.client.AuthoringConstants;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.layout.VStack;

public class MapConfigSection extends SectionConfig {

	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);
	
	public MapConfigSection() {
		super("map");
		VStack layout = new VStack();

		setItems(layout);
	}

	public void loadDataFromRecord(Game game) {
		
	}

}
