package org.celstec.arlearn2.delegators;

import java.util.logging.Logger;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.game.VariableDefinition;
import org.celstec.arlearn2.beans.game.VariableEffectDefinition;
import org.celstec.arlearn2.jdo.manager.VariableDefinitionManager;
import org.celstec.arlearn2.jdo.manager.VariableEffectDefinitionManager;

public class VariableDelegator extends GoogleDelegator {
	private static final Logger logger = Logger.getLogger(VariableDelegator.class.getName());

	
	public VariableDelegator(Service service) {
		super(service);
	}

	public VariableDefinition createVariableDefinition(VariableDefinition variableDefinition) {
        return VariableDefinitionManager.createVariableDefinition(variableDefinition);
	}

    public VariableEffectDefinition createVariableEffectDefinition(VariableEffectDefinition variableDef) {

        return VariableEffectDefinitionManager.createVariableDefinition(variableDef);
    }
}
