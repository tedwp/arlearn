package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.game.VariableDefinition;
import org.celstec.arlearn2.beans.game.VariableEffectDefinition;
import org.celstec.arlearn2.beans.run.VariableInstance;
import org.celstec.arlearn2.jdo.manager.VariableDefinitionManager;
import org.celstec.arlearn2.jdo.manager.VariableEffectDefinitionManager;
import org.celstec.arlearn2.jdo.manager.VariableInstanceManager;
import org.celstec.arlearn2.tasks.beans.GenericBean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class VariableDelegator extends GoogleDelegator {

	public VariableDelegator(Service service) {
		super(service);
	}

    public VariableDelegator(GenericBean bean) {
        super(bean);
    }

    public VariableDefinition createVariableDefinition(VariableDefinition variableDefinition) {
        if (variableDefinition.getGameId() == null) {
            variableDefinition.setError("gameId missing");
            return variableDefinition;
        } else {
            return VariableDefinitionManager.createVariableDefinition(variableDefinition);
        }
	}

    public VariableEffectDefinition createVariableEffectDefinition(VariableEffectDefinition variableDef) {

        return VariableEffectDefinitionManager.createVariableDefinition(variableDef);
    }

    public VariableInstance createVariableInstance(VariableInstance variableInstance) {
        if (variableInstance.getGameId() == null) {
            variableInstance.setError("gameId missing");
        } else if (variableInstance.getRunId() == null) {
            variableInstance.setError("runId missing");
        }else if (variableInstance.getName() == null) {
            variableInstance.setError("name missing");
        } else {
            VariableInstanceManager.createVariableInstance(variableInstance);
        }
        return  variableInstance;
    }

    public VariableInstance getVariableInstance(Long gameId, Long runId, String name) {
        VariableInstance returnValue = VariableInstanceManager.getVariableInstance(gameId, runId, name);
        if (returnValue == null) {
            returnValue = new VariableInstance();
            returnValue.setError("no such variable instance");
        }
        return returnValue;
    }

    public List<VariableEffectDefinition> getVariableEffectDefinitions(Long gameId){
        return VariableEffectDefinitionManager.getVariableEffectDefinitions(gameId, null);
    }

    public HashMap<String, VariableDefinition> getVariableDefinitions(Long gameId, int scope) {
        List<VariableDefinition> variableDefinitionList = VariableDefinitionManager.getVariableDefinitions(gameId, null, scope);
        HashMap<String, VariableDefinition> stringVariableDefinitionMap = new HashMap<String, VariableDefinition>();
        for (VariableDefinition variableDefinition : variableDefinitionList) {
            stringVariableDefinitionMap.put(variableDefinition.getName(), variableDefinition);
        }
        return stringVariableDefinitionMap;
    }

    public List<VariableEffectDefinition> getVariableEffectDefinitions(Long gameId, int scope){
        HashMap<String, VariableDefinition> stringVariableDefinitionMap = getVariableDefinitions(gameId, scope);
        List<VariableEffectDefinition> variableEffectDefinitionList = getVariableEffectDefinitions(gameId);
        Iterator<VariableEffectDefinition> iter = variableEffectDefinitionList.iterator();
        while (iter.hasNext()) {
            if (!stringVariableDefinitionMap.containsKey(iter.next().getName())) {
                iter.remove();
            }
        }
        return variableEffectDefinitionList;
    }

//    public List<VariableEffectDefinition> getVariableEffectDefinitionsForTeam(Long gameId){
//        HashMap<String, VariableDefinition> stringVariableDefinitionMap = getVariableDefinitions(gameId, 1);
//        List<VariableEffectDefinition> variableEffectDefinitionList = getVariableEffectDefinitions(gameId);
//        Iterator<VariableEffectDefinition> iter = variableEffectDefinitionList.iterator();
//        while (iter.hasNext()) {
//            if (!stringVariableDefinitionMap.containsKey(iter.next().getName())) {
//                iter.remove();
//            }
//        }
//        return variableEffectDefinitionList;
//    }
}