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

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import net.wespot.pim.R;
import net.wespot.pim.utils.images.BitmapWorkerTask;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.dao.gen.AccountLocalObject;

/**
 * A fragment that launches other parts of the demo application.
 */
public class PimProfileFragment extends Fragment {

    private static final String TAG = "PimProfileFragment";


    private TextView name;
    private TextView email;
    private TextView web;
    private ImageView picture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.screen_profile, container, false);

        name = (TextView) rootview.findViewById(R.id.profile_name);
        email = (TextView) rootview.findViewById(R.id.profile_email_value);
        web = (TextView) rootview.findViewById(R.id.profile_website_value);
        picture = (ImageView) rootview.findViewById(R.id.imageView);

        getActivity().setTitle(R.string.profile_name);

        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        AccountLocalObject account = INQ.accounts.getLoggedInAccount();

        name.setText(account.getName());
        email.setText(account.getEmail());
        web.setText(account.getFullId());

        if (account.getPicture() != null){
            BitmapWorkerTask task = new BitmapWorkerTask(picture);
            // TODO change to picture
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, account.getPicture());
        }
        else{
            picture.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.foto_perfil_croped));
        }

//        picture.setImageBitmap(account.getPicture());

//        INQ.init(getActivity());
//        INQ.inquiry.syncInquiries();
//        INQ.inquiry.syncHypothesis(151l);
//
//        ARL.init(getActivity());
//        ARL.properties.setAuthToken("ya29.1.AADtN_Wk3DnTkoP7u1l-BxvWjDeqVgQF6HCjj13GYi9xLk-SUXbdVQ4nPn7hiamhwgzskw");
//        ARL.properties.setFullId("2:117769871710404943583");
//        ARL.responses.syncResponses(19806001l);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}