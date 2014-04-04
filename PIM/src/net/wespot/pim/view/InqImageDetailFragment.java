/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.wespot.pim.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import net.wespot.pim.R;
import net.wespot.pim.controller.ImageDetailActivity;
import net.wespot.pim.controller.VideoFullScreenView;
import net.wespot.pim.utils.Constants;
import net.wespot.pim.utils.images.ImageFetcher;
import net.wespot.pim.utils.images.ImageWorker;
import net.wespot.pim.utils.images.Utils;
import org.celstec.dao.gen.ResponseLocalObject;

/**
 * This fragment will populate the children of the ViewPager from {@link net.wespot.pim.controller.ImageDetailActivity}.
 */
public class InqImageDetailFragment extends Fragment {
    private static final String IMAGE_DATA_EXTRA = "extra_image_data";
    private static ResponseLocalObject response;
    private String mImageUrl;
    private ImageView mImageView;
    private ImageFetcher mImageFetcher;
    private ImageView mPlayButtonView;
    private String TAG = "InqImageDetailFragment";

    /**
     * Factory method to generate a new instance of the fragment given an image number.
     *
     * @param res The response
     * @return A new instance of InqImageDetailFragment with imageNum extras
     */
    public static InqImageDetailFragment newInstance(ResponseLocalObject res) {
        final InqImageDetailFragment f = new InqImageDetailFragment();

        response = res;

        final Bundle args = new Bundle();
        args.putString(IMAGE_DATA_EXTRA, res.getUriAsString());
        f.setArguments(args);



        return f;
    }

    /**
     * Empty constructor as per the Fragment documentation
     */
    public InqImageDetailFragment() {}

    /**
     * Populate image using a url from extras, use the convenience factory method
     * {@link InqImageDetailFragment#newInstance(org.celstec.dao.gen.ResponseLocalObject)} to create this fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString(IMAGE_DATA_EXTRA) : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate and locate the main ImageView
        final View v = inflater.inflate(R.layout.fragment_detail_image, container, false);
        mImageView = (ImageView) v.findViewById(R.id.imageView);
        mPlayButtonView = (ImageView) v.findViewById(R.id.VideoPreviewPlayButton);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e(TAG, "Current element: "+mImageUrl+" " + response.getUri()+" "+response.getUriAsString()+" "+response.isAudio()+" "+response.getType());

            if(!mImageUrl.contains(".jpg")){
            mPlayButtonView.setVisibility(View.VISIBLE);
            mImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //To change body of implemented methods use File | Settings | File Templates.
                    Log.i(TAG, "Video in fullscreen (URI): " +mImageUrl);

                    Intent intent = new Intent(getActivity(), VideoFullScreenView.class);
                    intent.putExtra("filePath", mImageUrl);
                    getActivity().startActivity(intent);
                }
            });
        } else{
            // Use the parent activity to load the image asynchronously into the ImageView (so a single
            // cache can be used over all pages in the ViewPager
            if (ImageDetailActivity.class.isInstance(getActivity())) {
                mImageFetcher = ((ImageDetailActivity) getActivity()).getImageFetcher();
                mImageFetcher.loadImage(mImageUrl, mImageView);
            }

            // Pass clicks on the ImageView to the parent activity to handle
            if (OnClickListener.class.isInstance(getActivity()) && Utils.hasHoneycomb()) {
                mImageView.setOnClickListener((OnClickListener) getActivity());
            }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImageView != null) {
            // Cancel any pending image work
            ImageWorker.cancelWork(mImageView);
            mImageView.setImageDrawable(null);
            mPlayButtonView.setImageDrawable(null);
        }
    }
}
