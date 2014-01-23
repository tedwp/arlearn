package org.celstec.arlearn2.android.testAdapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.dataCollection.DataCollectionManager;
import org.celstec.arlearn2.android.dataCollection.PictureManager;
import org.celstec.arlearn2.android.dataCollection.VideoManager;
import org.celstec.arlearn2.android.delegators.ARL;

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
public class DataCollectionTestActivity extends Activity{

    private DataCollectionManager dataCollectorManager;
    private DataCollectionManager videoDataCollectorManager;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_datacollection);

        findViewById(R.id.pictureButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCollectorManager.takeDataSample();
            }
        });

        findViewById(R.id.videoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoDataCollectorManager.takeDataSample();
            }
        });

        findViewById(R.id.sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARL.responses.syncResponses(19806001l);
            }
        });
        Log.e("ARLearn", "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataCollectorManager = new PictureManager(DataCollectionTestActivity.this);
        dataCollectorManager.setRunId(19806001l);
        videoDataCollectorManager = new VideoManager(DataCollectionTestActivity.this);
        videoDataCollectorManager.setRunId(19806001l);
        Log.e("ARLearn", "onResume" + dataCollectorManager);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("test");
        Log.e("ARLearn", "test" + dataCollectorManager);
        videoDataCollectorManager.onActivityResult(requestCode, resultCode, data);
    }

}
