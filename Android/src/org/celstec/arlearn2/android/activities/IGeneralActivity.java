package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.menu.MenuHandler;

public interface IGeneralActivity {
	void notifyTaskFinished();
	
	MenuHandler getMenuHandler();
	
	boolean isGenItemActivity();
}
