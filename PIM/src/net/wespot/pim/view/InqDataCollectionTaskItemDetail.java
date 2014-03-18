package net.wespot.pim.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import net.wespot.pim.R;
import net.wespot.pim.controller.ImageDetailActivity;
import net.wespot.pim.utils.images.ImageFetcher;
import net.wespot.pim.utils.images.ImageWorker;
import net.wespot.pim.utils.layout._ActBar_FragmentActivity;

/**
 * ****************************************************************************
 * Copyright (C) 2014 Open Universiteit Nederland
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
 * Contributors: Angel Suarez
 * ****************************************************************************
 */
public class InqDataCollectionTaskItemDetail extends Fragment {

    private static final String IMAGE_DATA_EXTRA = "extra_image_data";
    private static String mImageUrl;
    private ImageView mImageView;
    private ImageView mPlayButtonView;
    private ImageFetcher mImageFetcher;
    private static final String TAG = "ImageDetailFragment";


    /**
     * Factory method to generate a new instance of the fragment given an image number.
     *
     * @param imageUrl The image url to load
     * @return A new instance of ImageDetailFragment with imageNum extras
     */
    public static InqDataCollectionTaskItemDetail newInstance(String imageUrl) {
        final InqDataCollectionTaskItemDetail f = new InqDataCollectionTaskItemDetail();

        final Bundle args = new Bundle();
        args.putString(IMAGE_DATA_EXTRA, imageUrl);
        f.setArguments(args);

        return f;
    }

    /**
     * Empty constructor as per the Fragment documentation
     */
    public InqDataCollectionTaskItemDetail() {}

    /**
     * Populate image using a url from extras, use the convenience factory method
     * {@link InqDataCollectionTaskItemDetail#newInstance(String)} to create this fragment.
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
        final View v = inflater.inflate(R.layout.fragment_data_collection_task_item_detail, container, false);
        mImageView = (ImageView) v.findViewById(R.id.imageView);
        mPlayButtonView = (ImageView) v.findViewById(R.id.VideoPreviewPlayButton);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Log.d(TAG, "Current element: " + mImageUrl);

        // Use the extension to determine whether it is video or not. If it is a video we must configure
        // onclick event to display video in fullscreen.
//        if(AppConstant.FILE_VID.contains(mImageUrl.substring((mImageUrl.lastIndexOf(".") + 1), mImageUrl.length()))){
//            mPlayButtonView.setVisibility(View.VISIBLE);
//            mImageView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    //To change body of implemented methods use File | Settings | File Templates.
//                    Log.i(TAG, "Video in fullscreen (URI): " +mImageUrl);
//
////                    Intent intent = new Intent(getActivity(), VideoFullScreenView.class);
////                    intent.putExtra("filePath", mImageUrl);
////                    getActivity().startActivity(intent);
//                }
//            });
//        }

        // Use the parent activity to load the image asynchronously into the ImageView (so a single
        // cache can be used over all pages in the ViewPager
        if (ImageDetailActivity.class.isInstance(getActivity())) {
            mImageFetcher = ((ImageDetailActivity) getActivity()).getImageFetcher();
            mImageFetcher.loadImage(mImageUrl, mImageView);
        }

        // Pass clicks on the ImageView to the parent activity to handle
        if (View.OnClickListener.class.isInstance(getActivity())) {
            mImageView.setOnClickListener((View.OnClickListener) getActivity());
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
