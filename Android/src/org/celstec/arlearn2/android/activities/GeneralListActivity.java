package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.menu.MenuHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

public abstract class GeneralListActivity extends ListActivity implements IGeneralActivity{

		protected static final int LOGIN = 0;
		protected static final int LOGOUT = 1;
		protected static final int EXIT = 2;
		protected static final int ANNOTATE = 3;
		protected static final int EXPLORE = 4;
		protected static final int RESET = 5;

		protected MenuHandler menuHandler;

		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			menuHandler = new MenuHandler(this);
		}

		public boolean onOptionsItemSelected(MenuItem item) {
			return menuHandler.onOptionsItemSelected(item);

		}

		public void notifyTaskFinished() {

		}

		public MenuHandler getMenuHandler() {
			return menuHandler;
		}
		
		protected void onResume() {
			super.onResume();
			if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
				this.finish();
			}
		}
//TODO find solution for this
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (!isGenItemActivity()) return super.onKeyDown(keyCode, event);
//		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//			this.finish();
//			if (isMessage()) {
//				startActivity(new Intent(this, ListMessagesActivity.class));
//			} else {
//				startActivity(new Intent(this, MapViewActivity.class));
//
//			}
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
		
		public abstract boolean isMessage();
}
