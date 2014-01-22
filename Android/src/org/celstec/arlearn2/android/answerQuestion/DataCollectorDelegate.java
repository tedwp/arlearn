package org.celstec.arlearn2.android.answerQuestion;

import android.net.Uri;
import org.celstec.arlearn2.android.asynctasks.db.RegisterUploadInDbTask;
import org.celstec.arlearn2.android.asynctasks.network.UploadFileSyncTask;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ResponseDelegator;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.client.GenericClient;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public abstract class DataCollectorDelegate {

    public static final int PICTURE_RESULT = 1;
	public static final int AUDIO_RESULT = 2;
	public static final int VIDEO_RESULT = 3;
	public static final int TEXT_RESULT = 4;
	
	protected NarratorItemActivity ctx;
	protected long runId;
	protected String fullAccount;
	
	protected DataCollectorDelegate(NarratorItemActivity ctx, long runId, String fullAccount) {
		this.ctx =ctx;
		this.runId = runId;
		this.fullAccount = fullAccount;
	}
	
	protected abstract void dcButtonClick();

	public Response createAudioResponse(Uri recordingUri) {
		Response r = createResponse();
		try {
			JSONObject jsonResponse = createJsonResponse(recordingUri, null, null, null);
			r.setResponseValue(jsonResponse.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	public Response createPictureResponse(Uri pictureUri, int width, int height) {
		Response r = createResponse();
		try {
			JSONObject jsonResponse = createJsonResponse(null, pictureUri, null, null);
			jsonResponse.put("width", width);
			jsonResponse.put("height", height);
			r.setResponseValue(jsonResponse.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	public Response createVideoResponse(Uri videoUri, int width, int height) {
		Response r = createResponse();
		try {
			JSONObject jsonResponse = createJsonResponse(null, null, videoUri, null);
			jsonResponse.put("width", width);
			jsonResponse.put("height", height);
			r.setResponseValue(jsonResponse.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	public Response createTextResponse(String text) {
		Response r = createResponse();
		try {
			JSONObject jsonResponse = createJsonResponse(null, null, null, text);
			r.setTimestamp(System.currentTimeMillis());
            r.setGeneralItemId(ctx.getNarratorBean().getId());
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
		return GenericClient.urlPrefix + "uploadService/" + runId + "/" + account + "/" + uri.getLastPathSegment();
	}
	
	public Response createResponse() {
		Response r = new Response();
		r.setUserEmail(fullAccount);
		r.setRunId(runId);
		return r;
	}

	protected void publishResponseWithFile(Uri localFile, final Response r) {
		final long currentTime = System.currentTimeMillis();
		r.setTimestamp(currentTime);
        r.setGeneralItemId(ctx.getNarratorBean().getId());

        ResponseDelegator.getInstance().publishResponse(ctx, r, ctx.getNarratorBean());
		RegisterUploadInDbTask task = RegisterUploadInDbTask.uploadFile(runId, "audio:" + currentTime, PropertiesAdapter.getInstance(ctx).getFullId(), localFile, getMimeType());
		UploadFileSyncTask fileSyncTask = new UploadFileSyncTask(); 
		
		task.taskToRunAfterExecute(fileSyncTask);
	
//		fileSyncTask.taskToRunAfterExecute(new GenericTask() {
//
//			@Override
//			protected void run(Context ctx) {
//				ResponseDelegator.getInstance().publishResponse(ctx, r);
//			}
//		});
		
		task.run(ctx);
		
	}
	

	protected abstract String getMimeType() ;
}
