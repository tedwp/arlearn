package org.celstec.arlearn2.jdo.manager;

import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.VariableEffectInstanceJDO;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;

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
public class VariableEffectInstanceManager {

    private static final String params[] = new String[] { "runId", "account", "teamId"};
    private static final String paramsNames[] = new String[] { "runIdParam", "accountParam", "teamIdParam"};
    private static final String types[] = new String[] { "Long", "String", "String"};

    public static VariableEffectInstanceJDO createVariableEffectInstanceForAccount(String account, int amount, long runId, long variableEffectDefinitionId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        VariableEffectInstanceJDO jdo = new VariableEffectInstanceJDO();
        jdo.setAccount(account);
        jdo.setEffectCount(amount);
        jdo.setVariableEffectDefinitionIdentifier(variableEffectDefinitionId);
        jdo.setRunId(runId);
        jdo.setUniqueIdAccount();
        try {
            pm.makePersistent(jdo);
            return jdo;
        } finally {
            pm.close();
        }
    }

    public static VariableEffectInstanceJDO createVariableEffectInstanceForTeam(String teamId, int amount, long runId, long variableEffectDefinitionId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        VariableEffectInstanceJDO jdo = new VariableEffectInstanceJDO();
        jdo.setTeamId(teamId);
        jdo.setEffectCount(amount);
        jdo.setVariableEffectDefinitionIdentifier(variableEffectDefinitionId);
        jdo.setRunId(runId);
        jdo.setUniqueIdTeam();
        try {
            pm.makePersistent(jdo);
            return jdo;
        } finally {
            pm.close();
        }
    }

    public static VariableEffectInstanceJDO createVariableEffectInstanceForAll(int amount, long runId, long variableEffectDefinitionId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        VariableEffectInstanceJDO jdo = new VariableEffectInstanceJDO();
        jdo.setEffectCount(amount);
        jdo.setVariableEffectDefinitionIdentifier(variableEffectDefinitionId);
        jdo.setRunId(runId);
        jdo.setUniqueIdGlobal();
        try {
            pm.makePersistent(jdo);
            return jdo;
        } finally {
            pm.close();
        }
    }

    public static List<VariableEffectInstanceJDO> getVariableEffectDefinitions(PersistenceManager pm, Long runId, String account, String teamId) {
        Query query = pm.newQuery(VariableEffectInstanceJDO.class);
        Object args[] = { runId, account, teamId};
        if (ManagerUtil.generateFilter(args, params, paramsNames).trim().equals("")) {
                query.setFilter("deleted == null");
            return (List) query.execute();
        }
        query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
        query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
        return ((List<VariableEffectInstanceJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args)));
    }

    public static void delete(Long runId, String account, String teamId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            List<VariableEffectInstanceJDO> deleteList = getVariableEffectDefinitions(pm, runId, account, teamId);
            pm.deletePersistentAll(deleteList);
        } finally {
            pm.close();
        }
    }
}
