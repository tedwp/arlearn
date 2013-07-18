package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.game.VariableDefinition;
import org.celstec.arlearn2.beans.game.VariableEffectDefinition;
import org.celstec.arlearn2.beans.run.VariableInstance;
import org.celstec.arlearn2.jdo.manager.VariableDefinitionManager;
import org.celstec.arlearn2.jdo.manager.VariableEffectDefinitionManager;
import org.celstec.arlearn2.jdo.manager.VariableInstanceManager;

public class VariableDelegator extends GoogleDelegator {

	public VariableDelegator(Service service) {
		super(service);
	}

	public VariableDefinition createVariableDefinition(VariableDefinition variableDefinition) {
        return VariableDefinitionManager.createVariableDefinition(variableDefinition);
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
}