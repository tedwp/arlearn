package org.celstec.arlearn2.portal.client.author.ui.game;

import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;

public class ShowDescriptionSection extends SectionConfig {
	private Game game;
	HTMLPane paneLink;

	public ShowDescriptionSection() {
		super("About this game");
		paneLink = new HTMLPane();
		HStack hstack = new HStack();
		String displayString = "game description?";
		paneLink.setContents(displayString);
		paneLink.setWidth100();
		paneLink.setHeight100();
		
		hstack.addMember(paneLink);
		hstack.setAlign(Alignment.LEFT);
		setItems(hstack);

	}

	public void loadDataFromRecord(Game game) {
		this.game = game;
		paneLink.setContents(game.getDescription());
	}

}
