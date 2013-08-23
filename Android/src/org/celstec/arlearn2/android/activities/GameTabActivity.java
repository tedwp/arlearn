/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Bernardo Tabuenca
 ******************************************************************************/
package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.list.GeneralItemListAdapter;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.ListitemClickInterface;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class GameTabActivity extends TabActivity implements
		ListitemClickInterface {

	private String CLASSNAME = this.getClass().getName();


	private Game selectedGame = null;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle extras = getIntent().getExtras();

		if (extras != null) {
			selectedGame = (Game) extras.get("selectedGame");
		}

		// setContentView(R.layout.listgeneralitemscreen);
		setContentView(R.layout.gametabs);

		Resources res = getResources();

		TabHost th = getTabHost();

		Intent intentI = new Intent().setClass(this,
				GameItemsActivity.class);
		TabSpec tsItems = th.newTabSpec("Items")
				.setIndicator("", res.getDrawable(R.drawable.gi_add_48x))
				.setContent(intentI);

		Intent intentR = new Intent().setClass(this, GameRunsActivity.class);
		TabSpec tsRuns = th.newTabSpec("Runs")
				.setIndicator("", res.getDrawable(R.drawable.add_user_48x))
				.setContent(intentR);
		
		Intent intentG = new Intent().setClass(this, GameDescActivity.class);
		TabSpec tsGameDesc = th.newTabSpec("Game")
				.setIndicator("", res.getDrawable(R.drawable.list_icon))
				.setContent(intentG);
		

		th.addTab(tsGameDesc);
		th.addTab(tsItems);
		th.addTab(tsRuns);

		th.setCurrentTab(0);

	}

	@Override
	public void onListItemClick(View v, int position,
			GenericListRecord messageListRecord) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean setOnLongClickListener(View v, int position,
			GenericListRecord messageListRecord) {
		// TODO Auto-generated method stub
		return false;
	}

}
