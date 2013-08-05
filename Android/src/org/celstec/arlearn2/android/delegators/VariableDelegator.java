package org.celstec.arlearn2.android.delegators;

import android.content.Context;
import org.celstec.arlearn2.android.asynctasks.network.SynchronizeVariableTask;
import org.celstec.arlearn2.beans.run.VariableInstance;

import java.util.HashMap;

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
public class VariableDelegator {

    private static VariableDelegator instance;
    private static HashMap<String, Integer> variableMap = new HashMap<String, Integer>();

    private VariableDelegator() {

    }

    public static VariableDelegator getInstance() {
        if (instance == null) {
            instance = new VariableDelegator();
        }
        return instance;
    }

    public Integer getVariable(Long runId, String name) {
        String key = getKey(runId, name);
        if (variableMap.containsKey(key))  {
            return variableMap.get(key);
        }
        return null;
    }

    public void syncVariable(Context ctx, Long runId, Long gameId, String name) {
        new SynchronizeVariableTask(runId, gameId, name).run(ctx);
    }

    public void syncVariable(Context ctx, Long runId, Long gameId, String[] names) {
        new SynchronizeVariableTask(runId, gameId, names).run(ctx);
    }

    public boolean varExists(Long runId, String name) {
        return variableMap.containsKey(getKey(runId, name));
    }

    public boolean saveInstance(VariableInstance varInst) {
        if (varInst.getRunId() == null) return false;
        String key = getKey(varInst.getRunId(),varInst.getName());
        int newValue = varInst.getValue().intValue();
        Integer oldValue = variableMap.get(key);
        variableMap.put(key, newValue);
        return oldValue == null || !oldValue.equals(newValue);

    }

    private String getKey(Long runId, String varName) {
        return runId +":"+varName;
    }

    public void saveInstance(long runId, String name, int value) {
        variableMap.put(getKey(runId, name), value);
    }
}
