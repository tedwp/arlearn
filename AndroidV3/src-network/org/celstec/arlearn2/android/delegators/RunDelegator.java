package org.celstec.arlearn2.android.delegators;

import daoBase.DaoConfiguration;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.events.RunEvent;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunList;

import org.celstec.arlearn2.client.RunClient;
import org.celstec.dao.gen.GameLocalObject;
import org.celstec.dao.gen.GameLocalObjectDao;
import org.celstec.dao.gen.RunLocalObject;

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
public class RunDelegator extends AbstractDelegator{

    private static RunDelegator instance;

    private static long lastSyncDateParticipate = 0l;
    private static long lastSyncDate = 0l;

    private RunDelegator() {
        ARL.eventBus.register(this);
    }

    public static RunDelegator getInstance() {
        if (instance == null) {
            instance = new RunDelegator();
        }
        return instance;
    }

    public void syncRunsParticipate() {
        ARL.eventBus.post(new SyncRunsEventParticipate());
    }

    public void syncRun(long runId) {
        ARL.eventBus.post(new SyncRun(runId));
    }


    private void onEventAsync(SyncRunsEventParticipate sge) {
        String token = returnTokenIfOnline();
        if (token != null) {
                RunList rl =RunClient.getRunClient().getRunsParticipate(token, lastSyncDateParticipate);
                if (rl.getError() == null) {
                    process(rl);
                    lastSyncDateParticipate = rl.getServerTime();
                }

        }
    }

    private void onEventAsync(SyncRun syncRun) {
        asyncRun(syncRun.runId);
    }

    public void asyncRun(long runId) {
        String token = returnTokenIfOnline();
        if (token != null) {
            Run run =RunClient.getRunClient().getRun(runId, token);
            if (run.getError() == null) {
                RunList rl = new RunList();
                rl.addRun(run);
                process(rl);
            }
        }
    }

    private void process(RunList rl) {
        for (Run rBean: rl.getRuns()) {
            RunLocalObject newRun = toDaoLocalObject(rBean);
            DaoConfiguration.getInstance().getRunLocalObjectDao().insertOrReplace(newRun);
            ARL.eventBus.post(new RunEvent(newRun.getId()));
        }
    }

    private RunLocalObject toDaoLocalObject(Run rBean) {
        RunLocalObject runDao = new RunLocalObject();
        runDao.setId(rBean.getRunId());
        runDao.setDeleted(rBean.getDeleted());
        runDao.setTitle(rBean.getTitle());
        runDao.setGameId(rBean.getGameId());
//        GameLocalObject gameLocalObject = DaoConfiguration.getInstance().getGameLocalObjectDao().load(rBean.getGameId());
//        if (gameLocalObject != null)
//            runDao.setGameLocalObject(gameLocalObject);
        return runDao;
    }


    private void sync() {
        PropertiesAdapter pa = PropertiesAdapter.getInstance();
        if (pa != null) {
            String token = pa.getAuthToken();
            if (token != null) {
                RunList rl = RunClient.getRunClient().getRunsParticipate(token);
                for (Run runBean : rl.getRuns()) {
                    RunLocalObject runDao = new RunLocalObject();
                    runDao.setId(runBean.getRunId());
                    runDao.setTitle(runBean.getTitle());
                    runDao.setDeleted(runBean.getDeleted());
                    runDao.setGameId(runBean.getGameId());

                    DaoConfiguration.getInstance().getRunLocalObjectDao().insertOrReplace(runDao);
                }
            }
        }

    }

    private class SyncRunsEventParticipate {

    }

    private class SyncRunsEvent {
    }

    private class SyncRun {
        private  long runId;
        public SyncRun(long runId) {
            this.runId = runId;
        }

        public long getRunId() {
            return runId;
        }

        public void setRunId(long runId) {
            this.runId = runId;
        }
    }
}
