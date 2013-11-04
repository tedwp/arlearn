package org.celstec.arlearn2.android.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.webkit.*;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ActionsDelegator;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.android.delegators.RunDelegator;
import org.celstec.arlearn2.android.oauth.DownloadDetailsTask;
import org.celstec.arlearn2.android.util.MediaFolders;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import java.util.TreeSet;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
public class HtmlListActivity extends GeneralActivity {

    protected WebView webview;
    private long runId = -1;
    private Long gameId = null;

    @Override
    public boolean isGenItemActivity() {
        return false;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gi_detail_narratoritem);
        runId = PropertiesAdapter.getInstance(this).getCurrentRunId();

        RunDelegator.getInstance().loadRun(this, runId);
        webview = (WebView) findViewById(R.id.giNarratorWebView);

        WebSettings ws = webview.getSettings();
        ws.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new Object(){
            public void openGeneralItem(String id){
                System.out.println("item clicked" +id);
                long giId = Long.parseLong(id);
                GIActivitySelector.startActivity(HtmlListActivity.this, GeneralItemsDelegator.getInstance().getGeneralItem(giId));
            }
        }, "arlearn");



    }

    @Override
    protected void onResume() {
        super.onResume();
        gameId = RunCache.getInstance().getGameId(runId);
        if (gameId != null) {
            GeneralItemsDelegator.getInstance().synchronizeGeneralItemsWithServer(this, runId, gameId);
            buildHtml();

            super.onResume();
        } else {
            finish();
        }
    }

    private void buildHtml() {
        String html = GameDelegator.getInstance().getGame(gameId).getConfig().getHtmlMessageList();
        TreeSet<GeneralItem> notVisible = GeneralItemVisibilityCache.getInstance().getAllNotYetVisible(runId);
        html +=" <script type=\"text/javascript\">\n";
        for (GeneralItem generalItem : notVisible) {
            html +="document.getElementById('"+generalItem.getId()+"').style.display = 'none'\n";
        }
         html += "</script>";
        String incommingFile = MediaFolders.getIncommingFilesDir().getAbsolutePath();
        incommingFile = "file:///"+incommingFile+"/"+gameId+"/";
        webview.loadDataWithBaseURL(incommingFile, html, "text/html", "utf-8", null);
//        incommingFile = "file:///"
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

    @Override
    public void onBroadcastMessage(Bundle bundle, boolean render) {
        super.onBroadcastMessage(bundle, render);
        if (render) {
            buildHtml();
        }


    }
}
