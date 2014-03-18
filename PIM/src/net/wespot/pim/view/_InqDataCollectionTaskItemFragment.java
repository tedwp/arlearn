package net.wespot.pim.view;

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
 * Contributors: Angel Suarez
 * ****************************************************************************
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.*;
import daoBase.DaoConfiguration;
import net.wespot.pim.R;
import net.wespot.pim.controller.Adapters.ResponsesLazyListAdapter;
import net.wespot.pim.controller.ImageDetailActivity;
import net.wespot.pim.utils.images.ImageCache;
import net.wespot.pim.utils.images.ImageFetcher;
import net.wespot.pim.utils.layout._ActBar_FragmentActivity;
import org.celstec.dao.gen.InquiryLocalObject;

/**
 * Fragment to display responses from a Data Collection Task (General Item)
 */
public class _InqDataCollectionTaskItemFragment extends _ActBar_FragmentActivity {

    private static final String TAG = "_InqDataCollectionTaskItemFragment";
    private ListView data_collection_tasks_items;
    private InquiryLocalObject inquiry;

    private ResponsesLazyListAdapter datAdapter;

    private TextView current_info;
    private ImageView prev_item;
    private ImageView next_item;

    // Managing data
    private static ImageFetcher mImageFetcher;
    private static final String IMAGE_CACHE_DIR = "images";
    private ViewPager mPager;
    private ImagePagerAdapter mAdapter;
    private long generalItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_data_collection_task_item);


        Bundle extras = getIntent().getExtras();
        if (extras != null){
            Log.e(TAG,extras.getInt("DataCollectionTask")+" testing");

            generalItemId = extras.getLong("DataCollectionTaskGeneralItemId");
        }

        prev_item = (ImageView) findViewById(R.id.prev_button);
        next_item = (ImageView) findViewById(R.id.next_button);
        current_info = (TextView) findViewById(R.id.info_current_element);

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        final int longest = (height > width ? height : width) / 2;

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        mImageFetcher = new ImageFetcher(this, longest);
        mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
        mImageFetcher.setImageFadeIn(false);


        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), DaoConfiguration.getInstance().getGeneralItemLocalObjectDao().load(generalItemId).getResponses().size());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setPageMargin((int) getResources().getDimension(R.dimen.data_collect_pager_image_detail_margin));
        mPager.setOffscreenPageLimit(2);
    }

    public static ImageFetcher getImageFetcher() {
        return mImageFetcher;
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        private final int mSize;


        public ImagePagerAdapter(FragmentManager fm, int size) {
            super(fm);
            mSize = size;
        }

        @Override
        public int getCount() {

            return mSize;
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }
    }
}
