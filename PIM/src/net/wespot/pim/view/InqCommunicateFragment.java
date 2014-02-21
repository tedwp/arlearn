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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import net.wespot.pim.R;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.dao.gen.InquiryLocalObject;

/**
 * A dummy fragment representing a section of the app, but that simply displays dummy text.
 */
public class InqCommunicateFragment extends Fragment {

    private static final String MIME_TYPE = "text/html";
    private static final String ENCONDING = null;
    private InquiryLocalObject inquiry;

    public InqCommunicateFragment() {
//        this.inquiry = API.getInquiryLocalObject();;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater,container,savedInstanceState);

        final View rootView = inflater.inflate(R.layout.fragment_section_communicate, container, false);
        WebView webView = (WebView) rootView.findViewById(R.id.description_communicate);

        TextView title = (TextView) rootView.findViewById(R.id.title_communicate);
        title.setText(R.string.inquiry_title_communicate);

        webView.loadData("Communicate",MIME_TYPE, ENCONDING);
        webView.setBackgroundColor(0x00000000);
//        webView.loadData(INQ.inquiry.getCurrentInquiry().getDescription(),MIME_TYPE, ENCONDING);
        return rootView;
    }
}