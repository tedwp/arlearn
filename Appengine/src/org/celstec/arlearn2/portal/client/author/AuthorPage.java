package org.celstec.arlearn2.portal.client.author;

import org.celstec.arlearn2.portal.client.author.ui.game.GamesTab;
import org.celstec.arlearn2.portal.client.author.ui.run.RunsTab;
import org.celstec.arlearn2.portal.client.author.ui.tabs.TabManager;

import com.google.gwt.user.client.ui.RootPanel;

public class AuthorPage {
	
	


	public void loadPage() {

		TabManager tabManager = new TabManager();
		tabManager.setAccountInformation("stefaan", "https://lh3.googleusercontent.com/-rRb8mSKLrNY/AAAAAAAAAAI/AAAAAAAAEQY/Y8BKx96IyHQ/photo.jpg");
		tabManager.addTab(new GamesTab(tabManager));
		tabManager.addTab(new RunsTab());
        RootPanel.get("author").add(tabManager.getDrawableWidget());		
	}

}
