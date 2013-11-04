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
package org.celstec.arlearn2.android.delegators;

import org.celstec.arlearn2.android.cache.MediaUploadCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.genItemActivities.MultipleChoiceActivity;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.beans.run.Response;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.os.Message;
import android.util.Log;

public class ResponseDelegator {

	private static ResponseDelegator instance;
	public static final String IMAGEURL = "imageUrl";
	public static final String VIDEOURL = "videoUrl";
	public static final String AUDIOURL = "audioUrl";

	private ResponseDelegator() {

	}

	public static ResponseDelegator getInstance() {
		if (instance == null) {
			instance = new ResponseDelegator();
		}
		return instance;
	}

	public void publishMultipleChoiceResponse(Context ctx,GeneralItem generalItem, MultipleChoiceAnswerItem answer) {
		PropertiesAdapter pa = PropertiesAdapter.getInstance(ctx);
		ActionsDelegator.getInstance().publishAction(ctx, "answer_" + answer.getId(), pa.getCurrentRunId(), pa.getFullId(), generalItem.getId(), generalItem.getType());
		try {
			JSONObject responseValueJson = new JSONObject();
			responseValueJson.put("isCorrect", answer.getIsCorrect());
			responseValueJson.put("answer", answer.getAnswer());
			publishResponse(ctx, generalItem, responseValueJson.toString());
		} catch (JSONException e) {
			Log.e("exception", e.getMessage(), e);
		}
	}

    public void publishMultipleChoiceResponse(Context ctx, GeneralItem generalItem, boolean correct) {
        PropertiesAdapter pa = PropertiesAdapter.getInstance(ctx);

        ActionsDelegator.getInstance().publishAction(ctx, "answer_"+(correct?"correct":"wrong") , pa.getCurrentRunId(), pa.getFullId(), generalItem.getId(), generalItem.getType());
    }

	public void publishResponse(Context ctx,GeneralItem generalItem, String responseValue) {
		PropertiesAdapter pa = PropertiesAdapter.getInstance(ctx);
		ActionsDelegator.getInstance().publishAction(ctx, "answer_given", pa.getCurrentRunId(), pa.getFullId(), generalItem.getId(), generalItem.getType());
		Response r = new Response();
		r.setUserEmail(pa.getFullId());
		r.setRunId(pa.getCurrentRunId());
		r.setTimestamp(System.currentTimeMillis());
		r.setGeneralItemId(generalItem.getId());
		r.setResponseValue(responseValue);
		DBAdapter.getAdapter(ctx).getMyResponses().publishResponse(r);
	}
	
	public void publishResponse(Context ctx, Response r) {
		DBAdapter.getAdapter(ctx).getMyResponses().publishResponse(r);
        PropertiesAdapter pa = PropertiesAdapter.getInstance(ctx);
    }

    public void publishResponse(Context ctx, Response r, GeneralItem generalItem) {
        DBAdapter.getAdapter(ctx).getMyResponses().publishResponse(r);
        PropertiesAdapter pa = PropertiesAdapter.getInstance(ctx);
        ActionsDelegator.getInstance().publishAction(ctx, "answer_given", r.getRunId(), pa.getFullId(), generalItem.getId(), generalItem.getType());

    }
	public void synchronizeResponsesWithServer(Context ctx, final long runId) {
		//TODO also sync with server
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = new DBAdapter.DatabaseTask() {

			@Override
			public void execute(DBAdapter db) {
				db.getMyResponses().queryAll(db, runId);
			}
		};
		m.sendToTarget();
		
	}

	public Uri getLocalMediaUri(Response resp, String mediaKind) {
		JSONObject json;
		try {
			json = new JSONObject(resp.getResponseValue());
			return MediaUploadCache.getInstance(resp.getRunId()).getLocalUri(json.getString(mediaKind));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}


}
