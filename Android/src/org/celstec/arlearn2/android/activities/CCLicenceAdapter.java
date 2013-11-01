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
 * Contributors: Bernardo Tabuenca
 ******************************************************************************/
package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class CCLicenceAdapter extends ArrayAdapter {

	private static final int RESOURCE = R.layout.license_row;
	private LayoutInflater inflater;

	static class ViewHolder {
		TextView nameTxVw;
	}

	@SuppressWarnings("unchecked")
	public CCLicenceAdapter(Context context, CCLicence[] objects) {
		super(context, RESOURCE, objects);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			// inflate a new view and setup the view holder for future use
			convertView = inflater.inflate(RESOURCE, null);

			holder = new ViewHolder();
			holder.nameTxVw = (TextView) convertView
					.findViewById(R.id.tvLicenseRow);
			convertView.setTag(holder);
		} else {
			// view already defined, retrieve view holder
			holder = (ViewHolder) convertView.getTag();
		}

		CCLicence cat = (CCLicence) getItem(position);
		if (cat == null) {
			Log.e("pete", "Invalid category for position: " + position);
		}
		holder.nameTxVw.setText(cat.getName());
		holder.nameTxVw
				.setCompoundDrawables(cat.getImg(), null, null, null);

		return convertView;
	}
}

class CCLicence {
	private String _name;
	private Drawable _img;
	private String _val;

	public CCLicence(String name, Drawable img, String val) {
		_name = name;
		_img = img;
		_val = val;
	}

	public String getName() {
		return _name;
	}

	public Drawable getImg() {
		return _img;
	}

	public String getVal() {
		return _val;
	}

}
