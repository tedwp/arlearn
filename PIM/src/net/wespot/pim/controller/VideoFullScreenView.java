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
 * Contributors: Angel Suarez
 ******************************************************************************/
package net.wespot.pim.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;
import net.wespot.pim.R;

public class VideoFullScreenView extends Activity {

    private VideoView vidDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_fullscreen_view);

        getActionBar().hide();

        vidDisplay = (VideoView) findViewById(R.id.videoView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        String filePath = i.getStringExtra("filePath");

        vidDisplay.setVideoURI(Uri.parse(filePath));
        MediaController mediaController = new MediaController(this);
        vidDisplay.setMediaController(mediaController);
        vidDisplay.requestFocus();
        vidDisplay.seekTo(1);
        vidDisplay.start();

    }

    @Override
    protected void onDestroy() {
        vidDisplay.getHolder().getSurface().release();
        super.onDestroy();
    }
}