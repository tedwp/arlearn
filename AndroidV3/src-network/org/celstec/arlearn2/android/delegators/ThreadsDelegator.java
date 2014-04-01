package org.celstec.arlearn2.android.delegators;

import daoBase.DaoConfiguration;
import org.celstec.arlearn2.android.events.ThreadEvent;
import org.celstec.arlearn2.beans.run.*;
import org.celstec.arlearn2.beans.run.Thread;
import org.celstec.arlearn2.client.ThreadsClient;
import org.celstec.dao.gen.ThreadLocalObject;

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
public class ThreadsDelegator extends AbstractDelegator{

    private static ThreadsDelegator instance;

    private ThreadsDelegator() {
        ARL.eventBus.register(this);
    }

    public static ThreadsDelegator getInstance() {
        if (instance == null) {
            instance = new ThreadsDelegator();
        }
        return instance;
    }

    public void syncThreads(Long runId) {
        ARL.eventBus.post(new SyncThreads(runId));
    }

    private void onEventAsync(SyncThreads st) {
        String token = returnTokenIfOnline();
        if (token != null) {
            ThreadList tl = ThreadsClient.getThreadsClient().getThreads(token, st.getRunId());
            if (tl.getError() ==null) {
                process(tl);
            }
        }

    }

    private void process(ThreadList tl) {
        for (org.celstec.arlearn2.beans.run.Thread thread: tl.getThreads()) {
            ThreadLocalObject existingLocalObject = DaoConfiguration.getInstance().getThreadLocalObject().load(thread.getThreadId());
            ThreadLocalObject newThread = toDaoLocalObject(thread);
            if (existingLocalObject == null || newThread.getLastModificationDate() > existingLocalObject.getLastModificationDate() ) {
                DaoConfiguration.getInstance().getThreadLocalObject().insertOrReplace(newThread);
                ARL.eventBus.post(new ThreadEvent(newThread.getRunId(), newThread.getId()));
            }
        }
    }

    private ThreadLocalObject toDaoLocalObject(Thread tBean) {
        ThreadLocalObject threadObject = new ThreadLocalObject();
        threadObject.setName(tBean.getName());
        threadObject.setId(tBean.getThreadId());
        threadObject.setRunId(tBean.getRunId());
        threadObject.setLastModificationDate(tBean.getLastModificationDate());
        return threadObject;
    }

    private class SyncThreads {
        long runId;

        private SyncThreads(long runId) {
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
