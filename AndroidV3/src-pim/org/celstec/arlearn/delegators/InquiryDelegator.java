package org.celstec.arlearn.delegators;

import android.util.Log;
import daoBase.DaoConfiguration;
import de.greenrobot.dao.query.QueryBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.delegators.RunDelegator;
import org.celstec.dao.gen.InquiryLocalObjectDao;
import org.celstec.dao.gen.RunLocalObject;
import org.celstec.events.InquiryEvent;
import org.celstec.arlearn2.client.InquiryClient;
import org.celstec.dao.gen.InquiryLocalObject;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.InputStream;

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
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
public class InquiryDelegator {

    private static InquiryDelegator instance;
    private InquiryLocalObject currentInquiry;

    private InquiryDelegator() {
        ARL.eventBus.register(this);
    }

    public static InquiryDelegator getInstance() {
        if (instance == null) {
            instance = new InquiryDelegator();
        }
        return instance;
    }

    public void syncDataCollectionTasks(){
        ARL.eventBus.post(new SyncDataCollectionTasks());
    }

    public InquiryLocalObject getCurrentInquiry() {
        return currentInquiry;
    }

    public void setCurrentInquiry(InquiryLocalObject currentInquiry) {
        this.currentInquiry = currentInquiry;
    }

    public InquiryLocalObject getInquiryLocalObject(long inquiryId) {
        return INQ.dao.getInquiryLocalObjectDao().load(inquiryId);
    }

    public void syncInquiries() {
        ARL.eventBus.post(new SyncInquiries());
    }

    public void syncHypothesis(long inquiryId) {
        ARL.eventBus.post(new SyncInquiriesHypothesis(inquiryId));
    }

    private void onEventAsync(SyncInquiries sge) {
//        uploadInquiries();
        downloadInquiries();
    }

//    private void uploadInquiries() {
//        InquiryLocalObjectDao dao = DaoConfiguration.getInstance().getInquiryLocalObjectDao();
//        QueryBuilder<InquiryLocalObject> qb = dao.queryBuilder();
//        qb.where(InquiryLocalObjectDao.Properties.IsSynchronized.eq(false));
//        for (InquiryLocalObject inquiry: qb.list()) {
//            try {
//                InquiryClient.getInquiryClient().createInquiry(inquiry);
//                dao.delete(inquiry);
//            } catch (Exception e) {
//                Log.e("ARLearn", e.getMessage(), e);
//            }
//        }
//    }


    private void downloadInquiries() {
        String inquiries = InquiryClient.getInquiryClient().userInquiries();
        if (inquiries == null) return;
        JSONObject json = null;
        try {
            json = new JSONObject(inquiries);
            JSONArray array = json.getJSONArray("result");
            for (int i = 0; i< array.length(); i++) {
                JSONObject inqJsonObject = array.getJSONObject(i);
                long inquiryId =       inqJsonObject.getLong("inquiryId");
                InquiryLocalObject inquiry = DaoConfiguration.getInstance().getInquiryLocalObjectDao().load(inquiryId);
                if (inquiry == null) {
                    inquiry= new InquiryLocalObject();
                }
                inquiry.setId(inquiryId);
                inquiry.setTitle(inqJsonObject.getString("title"));
                inquiry.setDescription(inqJsonObject.getString("description"));
                inquiry.setIsSynchronized(true);
                long runId = InquiryClient.getInquiryClient(). getArlearnRunId(inquiry.getId());
                inquiry.setRunId(runId);
                inquiry.setIcon(downloadImage(inqJsonObject.getString("icon")));


                long rid = DaoConfiguration.getInstance().getInquiryLocalObjectDao().insertOrReplace(inquiry);
                ARL.eventBus.post(new InquiryEvent(inquiry.getId()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private byte[] downloadImage(String imageUrl){
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(imageUrl);
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            int imageLength = (int)(entity.getContentLength());
            InputStream is = entity.getContent();

            byte[] imageBlob = new byte[imageLength];
            int bytesRead = 0;
            while (bytesRead < imageLength) {
                int n = is.read(imageBlob, bytesRead, imageLength - bytesRead);
                if (n <= 0)
                    ; // do some error handling
                bytesRead += n;
            }
            return imageBlob;
        }catch (Exception e) {
            return null;
        }
    }
    private void onEventAsync(SyncDataCollectionTasks dcTask) {
        InquiryLocalObject currentInq = getCurrentInquiry();
        if (currentInq != null) {
            if (currentInq.getRunLocalObject() == null) {
                if (currentInq.getRunId() == 0) return;
                INQ.runs.asyncRun(currentInq.getRunId()); //this is done synchronously
            }
        RunLocalObject run = DaoConfiguration.getInstance().getRunLocalObjectDao().load(currentInq.getRunId());
        if (run != null) {
            if (run.getGameLocalObject() == null) {
                INQ.games.asyncGame(run.getGameId());

            }
            run.refresh();
            INQ.generalItems.syncGeneralItems(run.getGameId());
        }
        }
    }

    private void onEventAsync(SyncInquiriesHypothesis syncInquiriesHypothesis) {
        InquiryClient.Hypothesis hypothesis = InquiryClient.getInquiryClient().getInquiryHypothesis(syncInquiriesHypothesis.inquiryId);
        if (hypothesis != null) {
            InquiryLocalObject inquiry = DaoConfiguration.getInstance().getInquiryLocalObjectDao().load(syncInquiriesHypothesis.inquiryId);
            if (inquiry == null) {
                inquiry= new InquiryLocalObject();
                inquiry.setId(syncInquiriesHypothesis.inquiryId);
            }
            inquiry.setHypothesisTitle(hypothesis.getTitle());
            inquiry.setHypothesisDescription(hypothesis.getDescription());
            DaoConfiguration.getInstance().getInquiryLocalObjectDao().insertOrReplace(inquiry);
            ARL.eventBus.post(new InquiryEvent(inquiry.getId()));

        }
    }

    private class SyncInquiries{}

    private class SyncInquiriesHypothesis {
        private long inquiryId;

        private SyncInquiriesHypothesis(long inquiryId) {
            this.inquiryId = inquiryId;
        }

        public long getInquiryId() {
            return inquiryId;
        }

        public void setInquiryId(long inquiryId) {
            this.inquiryId = inquiryId;
        }
    }

    private class SyncDataCollectionTasks {
    }
}
