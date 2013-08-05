package org.celstec.arlearn2.android.asynctasks.network;

import android.content.Context;
import android.os.Message;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.broadcast.GenericReceiver;
import org.celstec.arlearn2.android.broadcast.NetworkSwitcher;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.RunDelegator;
import org.celstec.arlearn2.android.delegators.VariableDelegator;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.beans.run.VariableInstance;
import org.celstec.arlearn2.client.RunClient;
import org.celstec.arlearn2.client.VariableClient;
import org.celstec.arlearn2.client.exception.ARLearnException;

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
public class SynchronizeVariableTask extends GenericTask implements NetworkTask {

    private Context ctx;
    private Long runId;
    private Long gameId;
    private String[] names;

    public SynchronizeVariableTask(Long runId, Long gameId, String name) {
        this.runId = runId;
        this.gameId = gameId;
        this.names = new String[]{
                name
        };
    }
    public SynchronizeVariableTask(Long runId, Long gameId, String[] names) {
        this.runId = runId;
        this.gameId = gameId;
        this.names = names;
    }

    public void run(Context ctx) {
        this.ctx = ctx;
        NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
        if (!nwHandler.hasMessages(NetworkTaskHandler.SYNC_VARIABLES)) {
            Message m = Message.obtain(nwHandler);
            m.obj = this;
            m.what = NetworkTaskHandler.SYNC_VARIABLES;
            m.sendToTarget();
        }
    }

    @Override
    public void execute() {
        if (!NetworkSwitcher.isOnline(ctx)) {
            runAfterTasks(ctx);
            return;
        } else {
            try {
                boolean aVariableWasUpdated = false;
                for (String name : names) {
                    VariableInstance varInst = VariableClient.getVariableClient().getVariable(PropertiesAdapter.getInstance(ctx).getAuthToken(), gameId, runId, name);
                    aVariableWasUpdated |= VariableDelegator.getInstance().saveInstance(varInst);
                }
                if (aVariableWasUpdated) {
                    ActivityUpdater.updateActivities(ctx, MapViewActivity.class.getCanonicalName());
                }
            } catch (ARLearnException ae) {
                if (ae.invalidCredentials()) {
                    GenericReceiver.setStatusToLogout(ctx);
                }

            } catch (NullPointerException ne ) {
                ne.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
