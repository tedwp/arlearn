package org.celstec.arlearn2.android.answerQuestion;

import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.client.GenericClient;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.net.Uri;

public abstract class DataCollectorDelegate {
	public static final int PICTURE_RESULT = 1;
	protected NarratorItemActivity ctx;
	protected long runId;
	protected String fullAccount;
	
	protected DataCollectorDelegate(NarratorItemActivity ctx, long runId, String fullAccount) {
		this.ctx =ctx;
		this.runId = runId;
		this.fullAccount = fullAccount;
	}
	
	protected abstract void dcButtonClick();

	public Response createResponse(long currentTime, Uri recordingUri, Uri imageUri, Uri videoUri, String text, int width, int height) {
		Response r = createResponse(currentTime);
		try {
			JSONObject jsonResponse = createJsonResponse(recordingUri, imageUri, videoUri, text);
			jsonResponse.put("width", width);
			jsonResponse.put("height", height);
			r.setResponseValue(jsonResponse.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	private JSONObject createJsonResponse(Uri recordingUri, Uri imageUri, Uri videoUri, String text) throws JSONException {
		JSONObject jsonResponse = new JSONObject();

		if (recordingUri != null) {
			jsonResponse.put("audioUrl", buildRemotePath(recordingUri, runId, fullAccount));
		}
		if (imageUri != null) {
			jsonResponse.put("imageUrl", buildRemotePath(imageUri, runId, fullAccount));
		}
		if (videoUri != null) {
			jsonResponse.put("videoUrl", buildRemotePath(videoUri, runId, fullAccount));
		}
		if (text != null) {
			jsonResponse.put("text", text);
		}

		return jsonResponse;
	}
	
	public String buildRemotePath(Uri uri, long runId, String account) {
		return GenericClient.urlPrefix + "/uploadService/" + runId + "/" + account + "/" + uri.getLastPathSegment();
	}
	
	public Response createResponse(long currentTime) {
		Response r = new Response();
		r.setUserEmail(fullAccount);
		r.setRunId(runId);
		r.setTimestamp(currentTime);
		return r;
	}

}
