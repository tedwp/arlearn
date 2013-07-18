package org.celstec.arlearn2.android.answerQuestion;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.IGeneralActivity;
import org.celstec.arlearn2.android.asynctasks.db.RegisterUploadInDbTask;
import org.celstec.arlearn2.android.asynctasks.network.UploadFileSyncTask;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ResponseDelegator;
import org.celstec.arlearn2.android.menu.MenuHandler;
import org.celstec.arlearn2.beans.run.Response;

import java.io.File;

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
public class RecordAudioActivity extends Activity implements IGeneralActivity {
    private static final String AUDIOSTATUS = "audioStatus";
    private static final String AUDIOFILE = "audioFile";
    private static final String RECORDING = "recording";

    protected MenuHandler menuHandler;
    protected RecordAudioDelegate rad;
    public PropertiesAdapter pa;
    private ImageView publishButton;
    private boolean recordingMedia = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            recordingMedia = savedInstanceState.getBoolean(RECORDING, false);
        }
        setContentView(R.layout.answer_question);
        menuHandler = new MenuHandler(this);
        pa = new PropertiesAdapter(this);

        findViewById(R.id.videoOrPicture).setVisibility(View.INVISIBLE);
        publishButton = (ImageView) findViewById(R.id.publishAnnotation);
        publishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                publish();
            }
        });
        rad = new RecordAudioDelegate(RecordAudioActivity.this);
        if (savedInstanceState != null) {

            rad.setStatus(savedInstanceState.getInt(AUDIOSTATUS));
            rad.setRecordingPath(savedInstanceState.getString(AUDIOFILE));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!recordingMedia) {
            rad.setStatus(RecordAudioDelegate.STOPPED_NO_AUDIO);
            displayPublishButton();
        }
    }

    public void displayPublishButton() {
        if (rad != null && rad.getRecordingPath() == null ) {
            publishButton.setVisibility(View.GONE);
        } else {
            publishButton.setVisibility(View.VISIBLE);
            publishButton.setClickable(true);
        }
    }

    public void publish() {
        rad.publish();
        publishButton.setClickable(false);

        final long currentTime = System.currentTimeMillis();
        final String recordingPath = rad.getRecordingPath();


        Intent resultIntent = new Intent();
        resultIntent.setData(Uri.fromFile(new File(recordingPath)));
        setResult(Activity.RESULT_OK, resultIntent);

        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!recordingMedia) {
            Log.e("RECORDING", "in onPause");
            rad.stop();
            recordingMedia = false;
        }
    }

    @Override
    public void notifyTaskFinished() {
    }

    public MenuHandler getMenuHandler() {
        return menuHandler;
    }

    public boolean isGenItemActivity() {
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(AUDIOSTATUS, rad.getStatus());
        outState.putString(AUDIOFILE, rad.getRecordingPath());
    }

    public RecordAudioDelegate getRecordAudioDelegate() {
        return rad;
    }
}
