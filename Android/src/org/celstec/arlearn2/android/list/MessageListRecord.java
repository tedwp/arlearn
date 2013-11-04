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
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.android.list;

import java.text.MessageFormat;

import android.graphics.drawable.Drawable;
import android.util.Log;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.cache.ActionCache;
import org.celstec.arlearn2.android.db.MediaCacheGeneralItems;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageListRecord extends GenericListRecord {

    private boolean read = false;
    private boolean inverted = false;
    private GeneralItem generalItem;
    private Context ctx;

    public MessageListRecord(GeneralItem generalItem, long runId, Context ctx) {
        this.ctx = ctx;
        this.generalItem = generalItem;
        setMessageHeader(generalItem.getName());
        if (generalItem.getId() != null) setRead(ActionCache.getInstance().isRead(runId, generalItem.getId()));
        String description = "";
        if (generalItem.getId() != null) {
            inverted = false;
            double percentage = GeneralItemsDelegator.getInstance().getDownloadPercentage(generalItem);
            double averageStatus = GeneralItemsDelegator.getInstance().getAverageDownloadStatus(generalItem);
            if (percentage < 1 && percentage != -1) {
                description += MessageFormat.format("{0,number,#.##%}", percentage);

                if (averageStatus == MediaCacheGeneralItems.REP_STATUS_TODO) {

                    setError(ctx.getString(R.string.warningDownloadBusy));
                }

                if (averageStatus == MediaCacheGeneralItems.REP_STATUS_FILE_NOT_FOUND) {
                    setError(ctx.getString(R.string.errorDownloadFailed));
                }
            }

            if ("".equals(description)) description = generalItem.getDescription().trim();

            if (description.length() > 100) description = description.subSequence(0, 100) + " [..]";
        } else {
            inverted = true;
        }


        setMessageDetail(description);
        setRightDetail("");
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setDistance(String distance) {
        setRightDetail(distance);
    }

    protected View getView(View v) {
        v = super.getView(v);
        TextView textHeader = (TextView) v.findViewById(R.id.textHeader);
        if (isRead()) {
            textHeader.setTypeface(null, Typeface.NORMAL);
            textHeader.setTextColor(Color.GRAY);
            textHeader.setBackgroundColor(Color.WHITE);
            v.setBackgroundColor(Color.WHITE);
            TextView textDetail = (TextView) v.findViewById(R.id.textDetail);
            TextView textRightDetail = (TextView) v.findViewById(R.id.textRightDetail);
            TextView textError = (TextView) v.findViewById(R.id.textError);
            textDetail.setVisibility(View.VISIBLE);
            textRightDetail.setVisibility(View.VISIBLE);
            textError.setVisibility(View.VISIBLE);
        } else {
            textHeader.setTypeface(null, Typeface.BOLD);
            if (inverted) {
                textHeader.setTextColor(Color.WHITE);
                textHeader.setBackgroundColor(Color.BLACK);
                v.setBackgroundColor(Color.BLACK);
                TextView textDetail = (TextView) v.findViewById(R.id.textDetail);
                TextView textRightDetail = (TextView) v.findViewById(R.id.textRightDetail);
                TextView textError = (TextView) v.findViewById(R.id.textError);
                textDetail.setVisibility(View.GONE);
                textRightDetail.setVisibility(View.GONE);
                textError.setVisibility(View.GONE);
            } else {
                textHeader.setTextColor(Color.BLACK);
                textHeader.setBackgroundColor(Color.WHITE);
                v.setBackgroundColor(Color.WHITE);
                TextView textDetail = (TextView) v.findViewById(R.id.textDetail);
                TextView textRightDetail = (TextView) v.findViewById(R.id.textRightDetail);
                TextView textError = (TextView) v.findViewById(R.id.textError);
                textDetail.setVisibility(View.VISIBLE);
                textRightDetail.setVisibility(View.VISIBLE);
                textError.setVisibility(View.VISIBLE);
            }
        }
        ImageView iv = (ImageView) v.findViewById(R.id.list_icon);
        if (iv != null)
            if ( !inverted) {
                Drawable icon = ListMapItemsActivity.getIconAsDrawable(ctx, generalItem);
                iv.setImageDrawable(icon);
            } else {
                if (Boolean.getBoolean(generalItem.getIconUrl())) {
                    iv.setImageResource(R.drawable.icon_expand);
                } else {
                    iv.setImageResource(R.drawable.icon_collapse);
                }
            }
        return v;
    }
}
