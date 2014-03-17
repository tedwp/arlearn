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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import net.wespot.pim.R;
import net.wespot.pim.utils.images.BitmapWorkerTask;
import net.wespot.pim.utils.layout._ActBar_FragmentActivity;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.dao.gen.AccountLocalObject;

/**
 * A fragment that launches other parts of the demo application.
 */
public class PimProfileFragment extends _ActBar_FragmentActivity {

    private static final String TAG = "PimProfileFragment";

    private TextView name;
    private TextView email;
    private TextView web;
    private ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AccountLocalObject account = INQ.accounts.getLoggedInAccount();

        setContentView(R.layout.screen_profile);

        name = (TextView) findViewById(R.id.profile_name);
        email = (TextView) findViewById(R.id.profile_email_value);
        web = (TextView) findViewById(R.id.profile_website_value);
        picture = (ImageView) findViewById(R.id.imageView);

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

        setTitle(R.string.profile_name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}