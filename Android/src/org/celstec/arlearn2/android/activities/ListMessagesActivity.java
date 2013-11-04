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
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.android.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import android.util.Log;
import android.view.Menu;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.delegators.ResponseDelegator;
import org.celstec.arlearn2.android.delegators.RunDelegator;
import org.celstec.arlearn2.android.list.GenericListRecord;
import org.celstec.arlearn2.android.list.GenericMessageListAdapter;
import org.celstec.arlearn2.android.list.ListitemClickInterface;
import org.celstec.arlearn2.android.list.MessageListRecord;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ListMessagesActivity extends GeneralActivity implements ListitemClickInterface {

    private GeneralItem[] gis = null;
    private GenericMessageListAdapter adapter;
    private long runId = -1;
    private Long gameId = null;
    TreeSet<GeneralItem> gil;
//    private static long lastReloadDate = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listexcursionscreen);

        ActionsDelegator.getInstance().synchronizeActionsWithServer(this);
        runId = PropertiesAdapter.getInstance(this).getCurrentRunId();

        RunDelegator.getInstance().loadRun(this, runId);
        ResponseDelegator.getInstance().synchronizeResponsesWithServer(this, runId);
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("runId", runId);
        outState.putLong("gameId", gameId);
    }

    protected void unpackBundle(Bundle inState) {
        super.unpackBundle(inState);
        runId = inState.getLong("runId");
        gameId = inState.getLong("gameId");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        menu.add(0, MenuHandler.SYNC, 0, getString(R.string.sync));
        return ret;
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameId = RunCache.getInstance().getGameId(runId);
        if (gameId != null) {
            GeneralItemsDelegator.getInstance().synchronizeGeneralItemsWithServer(this, runId, gameId);
            loadMessagesFromCache();

            super.onResume();
        } else {
            finish();
        }
    }

    private TreeSet<String> sections;
    private HashMap<String, Boolean> showSection = new HashMap<String, Boolean>();

    private void loadSections(TreeSet<GeneralItem> gil) {
        sections = new TreeSet<String>();
        for (GeneralItem gi: gil) {
            if (gi.getSection() != null) {
                sections.add(gi.getSection());
                if (!showSection.containsKey(gi.getSection())) showSection.put(gi.getSection(), true);
            }
        }
    }
    private boolean hasSections() {
        if (sections.size() == 0) return false;
        if (sections.size() == 1 && sections.first().equals("")) return false;
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBroadcastMessage(Bundle bundle, boolean render) {
        super.onBroadcastMessage(bundle, render);
        long currentTime = System.currentTimeMillis();
        if (gil != null) {
            if (!gil.equals(GeneralItemVisibilityCache.getInstance().getAllVisibleMessages(runId))) {
                loadMessagesFromCache();
            } else {
                System.out.println("not equal");
            }
        }
    }

    private void loadMessagesFromCache() {
        if (!menuHandler.getPropertiesAdapter().isAuthenticated()) {
            this.finish();
        } else {
            if (RunCache.getInstance().getRun(runId) == null) {
                this.finish();
            } else 	{
                gil = GeneralItemVisibilityCache.getInstance().getAllVisibleMessages(runId);

                if (gil != null) {
                    loadSections(gil);
                    if (hasSections()) {
                        loadMessagesWithSections(gil);
                    }   else {
                        loadMessagesNoSections(gil);
                    }

                    renderMessagesList();
                }
            }
        }
    }

    private void loadMessagesNoSections(TreeSet<GeneralItem> gil) {
        gis = new GeneralItem[gil.size()];
        int i = 0;
        for (Iterator<GeneralItem> iterator = gil.iterator(); iterator.hasNext();) {
            gis[i++] = iterator.next();;
        }
    }

    private void loadMessagesWithSections(TreeSet<GeneralItem> gil) {
        ArrayList<GeneralItem> gisList = new ArrayList<GeneralItem>(gil.size()+sections.size());
        gis = new GeneralItem[gil.size()+sections.size()];
        int i = 0;

        for (String section: sections) {
            GeneralItem sectionItem = new GeneralItem();
            sectionItem.setName(section);
            sectionItem.setGameId(getGameId());
            sectionItem.setIconUrl(""+showSection.get(section));
            gisList.add(sectionItem);

            for (Iterator<GeneralItem> iterator = gil.iterator(); iterator.hasNext();) {

                GeneralItem item = iterator.next();
                if (section.equals(item.getSection())&&showSection.get(section)) {
                    gisList.add(item);
                }
            }

        }
        gis = gisList.toArray(new GeneralItem[]{});
    }

    private void renderMessagesList() {
        ArrayList<GenericListRecord> runsList = new ArrayList<GenericListRecord>();
        Long runId = getMenuHandler().getPropertiesAdapter().getCurrentRunId();

        ListView listView = (ListView) findViewById(R.id.listRuns);
        adapter = new GenericMessageListAdapter(this, R.layout.listexcursionscreen, runsList);
        adapter.setOnListItemClickCallback(this);
        listView.setAdapter(adapter);

        for (int j = 0; j < gis.length; j++) {

            MessageListRecord r = new MessageListRecord(gis[j], runId, this);


            adapter.add(r);
        }
    }

    @Override
    public void onListItemClick(View v, int position, GenericListRecord messageListRecord) {
        if (gis[position].getId() != null) {
            GIActivitySelector.startActivity(this, gis[position]);
        } else {
            String section = gis[position].getName();
            showSection.put(section, !showSection.get(section   ));
            loadMessagesFromCache();
        }
    }

    @Override
    public boolean setOnLongClickListener(View v, int position, GenericListRecord messageListRecord) {
        return false;
    }

    public boolean isGenItemActivity() {
        return false;
    }

    public boolean showStatusLed() {
        return true;
    }

}